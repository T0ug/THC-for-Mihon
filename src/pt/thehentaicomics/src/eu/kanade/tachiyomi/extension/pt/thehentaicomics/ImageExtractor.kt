package eu.kanade.tachiyomi.extension.pt.thehentaicomics

import eu.kanade.tachiyomi.source.model.Page
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.text.Normalizer

class ImageExtractor(
    private val baseUrl: String,
) {

    fun extractPages(html: String, pageUrl: String): List<Page> {
        val document = Jsoup.parse(html, pageUrl)
        val slug = pageUrl.trimEnd('/').substringAfterLast('/').lowercase()
        val title = document.extractTitle(pageUrl)
        val imageKeys = buildImageKeys(slug, title, document)

        val candidates = document.select(".entry-content img, article img, img.alignnone, img.size-full")
            .mapIndexedNotNull { index, image -> image.toCandidate(index, imageKeys) }
            .distinctBy { it.url }

        val primaryImages = candidates
            .filter { it.score >= PRIMARY_SCORE }
            .sortedBy { it.index }

        val fallbackImages = candidates
            .filter { it.isReadableSize }
            .sortedBy { it.index }

        return primaryImages.ifEmpty { fallbackImages }
            .mapIndexed { index, candidate -> Page(index, imageUrl = candidate.url) }
    }

    private fun Element.toCandidate(index: Int, imageKeys: Set<String>): ImageCandidate? {
        val imageUrl = attr("abs:src").takeIf(String::isNotEmpty) ?: return null
        if (isObviousNonReadingImage(imageUrl)) return null

        val width = attr("width").toIntOrNull() ?: 0
        val height = attr("height").toIntOrNull() ?: 0
        val className = className().lowercase()
        val normalizedUrl = normalizeForMatch(imageUrl)
        val keyMatch = imageKeys.any { key -> key.length >= MIN_KEY_LENGTH && key in normalizedUrl }
        val readableSize = isReadableSize(width, height, className)

        if (!readableSize && !keyMatch) return null

        return ImageCandidate(
            index = index,
            url = imageUrl,
            score = scoreImage(className, keyMatch, readableSize),
            isReadableSize = readableSize,
        )
    }

    private fun scoreImage(className: String, keyMatch: Boolean, readableSize: Boolean): Int {
        var score = 0
        if (keyMatch) score += 3
        if ("size-full" in className) score += 2
        if ("alignnone" in className) score += 1
        if ("wp-image-" in className) score += 1
        if (readableSize) score += 1
        return score
    }

    private fun isReadableSize(width: Int, height: Int, className: String): Boolean {
        if ("size-full" in className && width == 0 && height == 0) return true
        return width >= MIN_IMAGE_SIDE && height >= MIN_IMAGE_SIDE
    }

    private fun isObviousNonReadingImage(imageUrl: String): Boolean {
        val normalized = imageUrl.removePrefix(baseUrl).lowercase()

        return normalized.endsWith(".gif") ||
            "logo" in normalized ||
            "avatar" in normalized ||
            "banner" in normalized ||
            "-150x150." in normalized ||
            "-300x" in normalized ||
            "thumbnail" in normalized
    }

    private fun buildImageKeys(slug: String, title: String, document: Document): Set<String> {
        val ogImage = document.selectFirst("meta[property=og:image]")?.attr("abs:content").orEmpty()
        val ogImageName = ogImage.substringAfterLast('/').substringBeforeLast('.')
        val ogImageKey = ogImageName.replace(Regex("""-\d+x\d+|-e\d+"""), "")

        return sequenceOf(slug, title, ogImageKey)
            .flatMap { value ->
                val normalized = normalizeForMatch(value)
                sequenceOf(normalized) + normalized.split('-').asSequence()
            }
            .filter { it.length >= MIN_KEY_LENGTH }
            .toSet()
    }

    private fun Document.extractTitle(pageUrl: String): String = (
        selectFirst("h1")?.text()
            ?: selectFirst("meta[property=og:title]")?.attr("content")
            ?: pageUrl.trimEnd('/').substringAfterLast('/').replace('-', ' ')
        )
        .removeSuffix(" - The Hentai Comics")

    private fun normalizeForMatch(value: String): String {
        val withoutAccents = Normalizer.normalize(value, Normalizer.Form.NFD)
            .replace(Regex("""\p{Mn}+"""), "")

        return withoutAccents
            .lowercase()
            .replace(Regex("""[^a-z0-9]+"""), "-")
            .trim('-')
    }

    private data class ImageCandidate(
        val index: Int,
        val url: String,
        val score: Int,
        val isReadableSize: Boolean,
    )

    private companion object {
        const val MIN_IMAGE_SIDE = 500
        const val MIN_KEY_LENGTH = 4
        const val PRIMARY_SCORE = 4
    }
}
