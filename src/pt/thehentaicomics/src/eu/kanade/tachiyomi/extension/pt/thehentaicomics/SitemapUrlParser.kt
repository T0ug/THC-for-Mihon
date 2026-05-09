package eu.kanade.tachiyomi.extension.pt.thehentaicomics

import org.jsoup.Jsoup

class SitemapUrlParser {

    fun parse(xml: String): List<SitemapUrlEntry> {
        val document = Jsoup.parse(xml)

        return document.select("url").mapNotNull { element ->
            val location = element.selectFirst("loc")?.text()?.takeIf(String::isNotEmpty)
                ?: return@mapNotNull null
            val lastModified = element.selectFirst("lastmod")?.text()

            SitemapUrlEntry(
                location = location,
                lastModified = lastModified,
            )
        }
    }
}

class SitemapUrlEntry(
    val location: String,
    val lastModified: String?,
)
