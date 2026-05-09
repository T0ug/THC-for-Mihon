package eu.kanade.tachiyomi.extension.pt.thehentaicomics

import eu.kanade.tachiyomi.source.model.MangasPage
import eu.kanade.tachiyomi.source.model.SManga
import org.jsoup.Jsoup

class PopularResolver(private val baseUrl: String) {

    private val sitemapUrlParser = SitemapUrlParser()

    fun resolveFromTagSitemap(
        sitemapXml: String,
        page: Int,
        fetchHtml: (String) -> String?,
    ): MangasPage {
        val safePage = page.coerceAtLeast(1)

        val tags = sitemapUrlParser.parse(sitemapXml)
            .sortedByDescending { it.lastModified.orEmpty() }
            .map { it.location }

        if (tags.isEmpty() || safePage > tags.size) {
            return MangasPage(emptyList(), false)
        }

        val targetTagUrl = tags[safePage - 1]
        val tagHtml = runCatching { fetchHtml(targetTagUrl) }.getOrNull()
            ?: return MangasPage(emptyList(), safePage < tags.size)

        val document = Jsoup.parse(tagHtml, targetTagUrl)
        val mangas = document.select(".video-conteudo .thumb-conteudo a").mapNotNull { a ->
            val isAd = a.select(".selo, .thumb-ads").text().contains("ADS", ignoreCase = true)
            if (isAd) return@mapNotNull null

            val href = a.attr("href")
            if (href.startsWith("http") && !href.startsWith(baseUrl)) return@mapNotNull null

            val url = href.removePrefix(baseUrl)
            var title = a.attr("title").trim()
            if (title.isEmpty()) {
                title = a.selectFirst("img")?.attr("alt")?.trim().orEmpty()
            }
            val img = a.selectFirst("img")?.attr("abs:src") // Use abs:src to ensure full URL if necessary, though site seems to use full URLs

            if (url.isEmpty()) return@mapNotNull null

            SManga.create().apply {
                this.url = url
                this.title = title.ifEmpty { "The Hentai Comics" }
                this.thumbnail_url = img
                this.initialized = false
            }
        }

        return MangasPage(mangas, safePage < tags.size)
    }
}
