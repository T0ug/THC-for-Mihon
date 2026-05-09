package eu.kanade.tachiyomi.extension.pt.thehentaicomics

import eu.kanade.tachiyomi.source.model.MangasPage
import eu.kanade.tachiyomi.source.model.SManga

class LatestResolver(
    private val baseUrl: String,
    private val pageSize: Int = 5,
) {

    private val sitemapIndexParser = SitemapIndexParser()
    private val sitemapUrlParser = SitemapUrlParser()
    private val urlClassifier = UrlClassifier(baseUrl)

    fun resolveFromIndexXml(
        indexXml: String,
        page: Int,
        fetchSitemapXml: (String) -> String?,
        fetchMangaDetails: (String) -> SManga? = { null },
    ): MangasPage {
        val candidates = mutableListOf<SitemapUrlEntry>()
        val seenUrls = mutableSetOf<String>()
        val safePage = page.coerceAtLeast(1)
        val offset = (safePage - 1) * pageSize
        val requiredCandidates = offset + pageSize + 1

        for (sitemap in prioritizedSitemaps(indexXml)) {
            val sitemapXml = runCatching { fetchSitemapXml(sitemap.location) }.getOrNull() ?: continue

            for (entry in sitemapUrlParser.parse(sitemapXml)) {
                if (!urlClassifier.isReadableCandidate(entry.location)) continue

                val normalizedUrl = urlClassifier.normalize(entry.location)
                if (seenUrls.add(normalizedUrl)) {
                    candidates += SitemapUrlEntry(
                        location = normalizedUrl,
                        lastModified = entry.lastModified,
                    )
                }
            }

            if (candidates.size >= requiredCandidates) break
        }

        val latestEntries = candidates
            .asSequence()
            .sortedWith(
                compareByDescending<SitemapUrlEntry> { it.lastModified.orEmpty() }
                    .thenBy { it.location },
            )
            .take(requiredCandidates)
            .toList()

        val pageEntries = latestEntries
            .drop(offset)
            .take(pageSize)

        val mangas = pageEntries.map { entry ->
            runCatching { fetchMangaDetails(entry.location) }.getOrNull() ?: entry.toManga()
        }

        return MangasPage(mangas, latestEntries.size > offset + pageSize)
    }

    private fun prioritizedSitemaps(indexXml: String): List<SitemapIndexEntry> = sitemapIndexParser.parse(indexXml)
        .sortedByDescending { it.lastModified.orEmpty() }

    private fun SitemapUrlEntry.toManga(): SManga = SManga.create().apply {
        url = location.removePrefix(baseUrl)
        title = location
            .trimEnd('/')
            .substringAfterLast('/')
            .replace('-', ' ')
            .ifEmpty { "The Hentai Comics" }
            .replaceFirstChar { it.titlecase() }
        initialized = false
    }
}
