package lrusso96.simplebiblio.core.providers.feedbooks

import lrusso96.simplebiblio.core.Ebook
import lrusso96.simplebiblio.core.Provider
import lrusso96.simplebiblio.core.Utils.extractOPDSLinks
import lrusso96.simplebiblio.core.Utils.parseUTC
import lrusso96.simplebiblio.exceptions.BiblioException
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException
import java.net.URI

class Feedbooks private constructor(private val maxResults: Int, private val languages: Set<Language>) : Provider(FEEDBOOKS) {

    @Throws(BiblioException::class)
    override suspend fun doSearch(query: String) = genericSearch(URI.create("$ENDPOINT/search.atom"), query)

    @Throws(BiblioException::class)
    override suspend fun doGetRecent() = genericSearch(URI.create("$ENDPOINT/recent.atom"), null)

    @Throws(BiblioException::class)
    override suspend fun doGetPopular() = genericSearch(URI.create("$ENDPOINT/top.atom"), null)

    @Throws(BiblioException::class)
    private fun genericSearch(endpoint: URI, query: String?): List<Ebook> {
        val ret: MutableList<Ebook> = ArrayList()
        return try {
            var connection = Jsoup.connect("$endpoint")
            if (query != null) connection = connection.data("query", query)

            languages.forEach {
                var totalResults = Int.MAX_VALUE
                var page = 1
                while (ret.size < totalResults) {
                    val doc = connection.data("lang", "$it").data("page", "$page").get()
                    totalResults = doc.getElementsByTag("opensearch:totalResults").text().toInt()
                    val entries = doc.getElementsByTag("entry")
                    entries.forEach { e -> if (ret.size >= maxResults) return ret else ret.add(parseBook(e)) }
                    page++
                }
            }
            ret
        } catch (e: IOException) {
            throw BiblioException(e.message)
        }
    }

    private fun parseBook(entry: Element): Ebook {
        val book = Ebook()
        book.provider = this
        val id = entry.getElementsByTag("id").text()
        book.id = parseID(id)
        book.title = entry.getElementsByTag("title").text()
        book.summary = entry.getElementsByTag("summary").text()
        book.published = parseUTC(entry.getElementsByTag("published").text())
        book.updated = parseUTC(entry.getElementsByTag("updated").text())
        book.language = entry.getElementsByTag("dcterms:language").text()
        book.source = entry.getElementsByTag("dcterms:source").text()
        book.author = entry.getElementsByTag("name").text()
        extractOPDSLinks(book, entry)
        return book
    }

    private fun parseID(string: String): Int {
        val ids = string.split("/").toTypedArray()
        return if (ids.isEmpty()) 0 else ids[ids.size - 1].toInt()
    }

    companion object {
        const val ENDPOINT = "https://feedbooks.com/books"
    }

    data class Builder(
            val maxResults: Int = 50,
            var languages: MutableSet<Language> = HashSet()) {

        fun addLanguage(language: Language) = apply { this.languages.add(language) }

        fun build(): Feedbooks {
            if (languages.isEmpty())
                languages = getDefaultLanguages()
            return Feedbooks(maxResults, languages)
        }

        private fun getDefaultLanguages(): MutableSet<Language> {
            val languages: MutableSet<Language> = HashSet()
            arrayOf(Language.ENGLISH, Language.ITALIAN).forEach { languages.add(it) }
            return languages
        }
    }
}