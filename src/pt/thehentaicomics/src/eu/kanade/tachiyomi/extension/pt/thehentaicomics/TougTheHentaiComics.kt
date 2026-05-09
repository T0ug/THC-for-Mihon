package eu.kanade.tachiyomi.extension.pt.thehentaicomics

import eu.kanade.tachiyomi.network.GET
import eu.kanade.tachiyomi.source.model.FilterList
import eu.kanade.tachiyomi.source.model.MangasPage
import eu.kanade.tachiyomi.source.model.Page
import eu.kanade.tachiyomi.source.model.SChapter
import eu.kanade.tachiyomi.source.model.SManga
import eu.kanade.tachiyomi.source.online.HttpSource
import okhttp3.Request
import okhttp3.Response

class TougTheHentaiComics : HttpSource() {

    override val name = "Toug - The Hentai Comics"

    override val baseUrl = "https://thehentaicomics.com"

    override val lang = "pt-BR"

    override val supportsLatest = true

    private val latestResolver = LatestResolver(baseUrl)
    private val popularResolver = PopularResolver(baseUrl)
    private val contentPageParser = ContentPageParser(baseUrl)
    private val imageExtractor = ImageExtractor(baseUrl)

    override fun popularMangaRequest(page: Int): Request = GET("$baseUrl/post_tag-sitemap.xml?page=$page", headers)

    override fun popularMangaParse(response: Response): MangasPage {
        response.use {
            return popularResolver.resolveFromTagSitemap(
                sitemapXml = it.body.string(),
                page = it.request.url.queryParameter("page")?.toIntOrNull() ?: 1,
                fetchHtml = ::fetchBody,
            )
        }
    }

    override fun latestUpdatesRequest(page: Int): Request = GET("${SitemapIndexParser.SITEMAP_INDEX_URL}?page=$page", headers)

    override fun latestUpdatesParse(response: Response): MangasPage {
        response.use {
            return latestResolver.resolveFromIndexXml(
                indexXml = it.body.string(),
                page = it.request.url.queryParameter("page")?.toIntOrNull() ?: 1,
                fetchSitemapXml = ::fetchBody,
                fetchMangaDetails = ::fetchMangaDetails,
            )
        }
    }

    override fun searchMangaRequest(page: Int, query: String, filters: FilterList): Request = GET(baseUrl, headers)

    override fun searchMangaParse(response: Response): MangasPage = MangasPage(emptyList(), false)

    override fun mangaDetailsParse(response: Response): SManga {
        response.use {
            return contentPageParser.parseDetails(it.body.string(), it.request.url.toString())
        }
    }

    override fun chapterListParse(response: Response): List<SChapter> {
        response.use {
            return contentPageParser.parseChapterList(it.request.url.toString())
        }
    }

    override fun pageListParse(response: Response): List<Page> {
        response.use {
            return imageExtractor.extractPages(it.body.string(), it.request.url.toString())
        }
    }

    override fun imageUrlParse(response: Response): String = throw UnsupportedOperationException()

    override fun getFilterList(): FilterList = FilterList()

    private fun fetchBody(url: String): String? = client.newCall(GET(url, headers)).execute().use { response ->
        if (!response.isSuccessful) return@use null
        response.body.string()
    }

    private fun fetchMangaDetails(url: String): SManga? {
        val html = fetchBody(url) ?: return null
        return contentPageParser.parseDetails(html, url)
    }
}
