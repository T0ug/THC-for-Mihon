package eu.kanade.tachiyomi.extension.pt.thehentaicomics

import eu.kanade.tachiyomi.source.model.SChapter
import eu.kanade.tachiyomi.source.model.SManga
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class ContentPageParser(
    private val baseUrl: String,
) {

    fun parseDetails(html: String, pageUrl: String): SManga {
        val document = Jsoup.parse(html, pageUrl)
        val title = document.extractTitle(pageUrl)

        return SManga.create().apply {
            url = pageUrl.removePrefix(baseUrl)
            this.title = title
            thumbnail_url = document.selectFirst("meta[property=og:image]")?.attr("abs:content")
                ?: document.selectFirst("img.wp-post-image")?.attr("abs:src")
            initialized = true
        }
    }

    fun parseChapterList(pageUrl: String): List<SChapter> = listOf(parseChapter(pageUrl, "Capitulo unico"))

    private fun parseChapter(pageUrl: String, chapterName: String): SChapter = SChapter.create().apply {
        url = pageUrl.removePrefix(baseUrl)
        name = chapterName
        chapter_number = 1f
    }

    private fun Document.extractTitle(pageUrl: String): String {
        val pageTitle = selectFirst("h1")?.text()
        val metaTitle = selectFirst("meta[property=og:title]")?.attr("content")

        return (pageTitle ?: metaTitle ?: pageUrl.trimEnd('/').substringAfterLast('/').replace('-', ' '))
            .removeSuffix(" - The Hentai Comics")
            .trim()
            .ifEmpty { "The Hentai Comics" }
    }
}
