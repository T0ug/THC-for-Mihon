package eu.kanade.tachiyomi.extension.pt.thehentaicomics

import eu.kanade.tachiyomi.source.model.MangasPage
import eu.kanade.tachiyomi.source.model.SManga
import org.jsoup.Jsoup

class SearchResolver(private val baseUrl: String) {

    fun parseSearchResults(html: String, requestUrl: String): MangasPage {
        val document = Jsoup.parse(html, requestUrl)

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
            val img = a.selectFirst("img")?.attr("abs:src")

            if (url.isEmpty()) return@mapNotNull null

            SManga.create().apply {
                this.url = url
                this.title = title.ifEmpty { "The Hentai Comics" }
                this.thumbnail_url = img
                this.initialized = false
            }
        }

        val hasNextPage = document.select("a.next, .nav-links a.next, .pagination a.next").isNotEmpty()

        return MangasPage(mangas, hasNextPage)
    }
}
