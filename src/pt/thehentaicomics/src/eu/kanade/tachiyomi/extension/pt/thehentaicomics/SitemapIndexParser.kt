package eu.kanade.tachiyomi.extension.pt.thehentaicomics

import org.jsoup.Jsoup

class SitemapIndexParser {

    fun parse(xml: String): List<SitemapIndexEntry> {
        val document = Jsoup.parse(xml)

        return document.select("sitemap").mapNotNull { element ->
            val location = element.selectFirst("loc")?.text()?.takeIf(String::isNotEmpty)
                ?: return@mapNotNull null
            val lastModified = element.selectFirst("lastmod")?.text()

            SitemapIndexEntry(
                location = location,
                lastModified = lastModified,
            )
        }
    }

    companion object {
        const val SITEMAP_INDEX_URL = "https://thehentaicomics.com/sitemap_index.xml"
    }
}

class SitemapIndexEntry(
    val location: String,
    val lastModified: String?,
)
