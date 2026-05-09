package eu.kanade.tachiyomi.extension.pt.thehentaicomics

class UrlClassifier(
    private val baseUrl: String,
) {

    fun isReadableCandidate(url: String): Boolean {
        if (!isSameHost(url)) return false

        val path = normalize(url).removePrefix(baseUrl).lowercase()

        return path.isNotEmpty() &&
            path != "/" &&
            !path.endsWith(".xml") &&
            !path.startsWith("/tag/") &&
            !path.startsWith("/category/") &&
            !path.startsWith("/page/") &&
            !path.startsWith("/wp-") &&
            !path.contains("attachment_id=") &&
            !path.contains("/attachment-")
    }

    fun normalize(url: String): String = if (url.startsWith(insecureBaseUrl)) {
        baseUrl + url.removePrefix(insecureBaseUrl)
    } else {
        url
    }

    private fun isSameHost(url: String): Boolean = url.startsWith(baseUrl) || url.startsWith(insecureBaseUrl)

    private val insecureBaseUrl = baseUrl.replace("https://", "http://")
}
