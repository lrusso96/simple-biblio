package lrusso96.simplebiblio.core.providers.standardebooks

import lrusso96.simplebiblio.core.Ebook
import lrusso96.simplebiblio.core.Provider
import lrusso96.simplebiblio.core.Utils.extractOPDSLinks
import lrusso96.simplebiblio.core.Utils.parseUTC
import lrusso96.simplebiblio.exceptions.BiblioException
import okhttp3.internal.toImmutableList
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException

class StandardEbooks : Provider(STANDARD_EBOOKS) {

    override suspend fun doSearch(query: String) = ArrayList<Ebook>()

    @Throws(BiblioException::class)
    override suspend fun doGetRecent() = loadRecent(recentIds())

    override suspend fun doGetPopular() = ArrayList<Ebook>()

    @Throws(BiblioException::class)
    private fun recentIds(): List<String> {
        return try {
            val endpoint = "https://standardebooks.org/rss/new-releases"
            val doc = Jsoup.connect(endpoint).get()
            val entries = doc.getElementsByTag("item")
            entries.map { it.getElementsByTag("link").text() }
                    .toImmutableList()
        } catch (e: IOException) {
            throw BiblioException(e.message)
        }
    }

    @Throws(BiblioException::class)
    private fun loadRecent(ids: List<String>): List<Ebook> {
        return try {
            val endpoint = "https://standardebooks.org/opds/all"
            val doc = Jsoup.connect(endpoint).get()
            val recent = doc.getElementsByTag("entry")
                    .toList()
                    .filter { ids.contains(it.getElementsByTag("id").text()) }
                    .map { parseBook(it) }
                    .toImmutableList()
            recent.forEachIndexed { i, it -> it.id = i }
            recent
        } catch (e: IOException) {
            throw BiblioException(e.message)
        }
    }

    //TODO: parse language field
    private fun parseBook(entry: Element): Ebook {
        val book = Ebook()
        book.provider = this
        book.author = entry.getElementsByTag("author").first().getElementsByTag("name").text()
        book.title = entry.getElementsByTag("title").text()
        book.summary = entry.getElementsByTag("summary").text()
        book.published = parseUTC(entry.getElementsByTag("published").text())
        book.updated = parseUTC(entry.getElementsByTag("updated").text())
        extractOPDSLinks(book, entry)
        return book
    }
}