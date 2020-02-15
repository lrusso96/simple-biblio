package lrusso96.simplebiblio.core

import lrusso96.simplebiblio.core.providers.libgen.LibraryGenesis.Companion.loadDownloadURIs
import lrusso96.simplebiblio.exceptions.BiblioException
import mu.KotlinLogging
import org.threeten.bp.LocalDate
import java.net.URI
import java.util.*

private val logger = KotlinLogging.logger {}

class Ebook {
    var id = 0
    var author: String? = null
    var title: String? = null
    var summary: String? = null
    var published: LocalDate? = null
    var updated: LocalDate? = null
    var language: String? = null
    var pages = 0
    var filesize = 0
    private var downloads: MutableList<Download> = ArrayList()
    var cover: URI? = null
    var source: String? = null
    var mdHash: String? = null
    var provider: Provider? = null
    var downloadMirror: URI? = null

    fun getDownloads(): List<Download> {
        try {
            if (downloads.isEmpty() && provider?.name == Provider.LIBGEN)
                downloads = loadDownloadURIs(this) as MutableList<Download>
        } catch (e: BiblioException) {
            logger.error { e.message }
        }
        return downloads
    }

    fun addDownload(dwn: Download) {
        downloads.add(dwn)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Ebook) return false
        return id == other.id && title == other.title
    }

    override fun hashCode() = Objects.hash(id, title)
}