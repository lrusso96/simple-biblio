package lrusso96.simplebiblio.core

import org.jsoup.nodes.Element
import org.threeten.bp.*
import java.net.URI
import kotlin.math.ln
import kotlin.math.pow

object Utils {

    @JvmStatic
    fun bytesToReadableSize(bytes: Int): String {
        val unit = 1000
        if (bytes < unit) return "$bytes B"
        val exp = (ln(bytes.toDouble()) / ln(unit.toDouble())).toInt()
        val pre = "kMGTPE"[exp - 1]
        return String.format("%.1f %sB", bytes / unit.toDouble().pow(exp.toDouble()), pre)
    }

    @JvmStatic
    fun parseUTC(date: String): LocalDate = LocalDateTime.ofInstant(Instant.parse(date), ZoneId.of(ZoneOffset.UTC.id)).toLocalDate()

    @JvmStatic
    fun extractDownload(dwn: String): Download {
        val uri = URI.create(dwn)
        val index = dwn.lastIndexOf('.')
        var extension = ""
        if (index > 0) extension = dwn.substring(index + 1)
        val download = Download()
        download.extension = extension
        download.uri = uri
        return download
    }

    @JvmStatic
    fun extractOPDSLinks(ebook: Ebook, element: Element) {
        val coverKey = "http://opds-spec.org/image"
        val downloadKey = "http://opds-spec.org/acquisition"
        val links = element.getElementsByTag("link")
        for (link in links) {
            val rel = link.attr("rel")
            if (rel == coverKey)
                ebook.cover = URI.create(link.attr("href"))
            else if (rel.startsWith(downloadKey)) {
                val download = link.attr("href")
                ebook.addDownload(extractDownload(download))
            }
        }
    }
}