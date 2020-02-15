package lrusso96.simplebiblio.core.providers.libgen

import lrusso96.simplebiblio.core.Download
import lrusso96.simplebiblio.core.Ebook
import lrusso96.simplebiblio.core.Provider
import lrusso96.simplebiblio.core.Utils.extractDownload
import lrusso96.simplebiblio.exceptions.BiblioException
import mu.KotlinLogging
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.threeten.bp.LocalDate
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.util.concurrent.TimeUnit


private val logger = KotlinLogging.logger {}

class LibraryGenesis private constructor(
        private val mirror: URI,
        private val downloadMirror: URI,
        private val maxResults: Int,
        private val sortingMode: Sorting,
        private val sortingField: Field) : Provider(LIBGEN) {

    @Throws(BiblioException::class)
    override suspend fun doSearch(query: String): List<Ebook> {
        //TODO: do not catch and retry maxTries times!
        if (query.length < 5) throw BiblioException("Insert at least 5 chars")
        return search(getIds(query))
    }

    @Throws(BiblioException::class)
    override suspend fun doGetRecent() = search(getIds(null))

    override suspend fun doGetPopular() = ArrayList<Ebook>()

    @Throws(BiblioException::class)
    private fun getIds(query: String?): List<String> {
        var page = 1
        //reduce number of pages requested
        var results = 25
        if (maxResults > 25) results = 50
        if (maxResults > 50) results = 100
        val ids = getIds(query, page, results)
        while (ids.size < maxResults) {
            page++
            val newIds: List<String> = getIds(query, page, results)
            if (newIds.isEmpty()) break
            ids.addAll(newIds)
        }
        return ids.take(maxResults)
    }

    @Throws(IOException::class)
    private fun getRecentDoc(page: Int): Document {
        return Jsoup.connect("$mirror/search.php")
                .data("mode", "last")
                .data("page", page.toString())
                .get()
    }

    @Throws(IOException::class)
    private fun getSearchDoc(query: String, page: Int, results: Int): Document {
        return Jsoup.connect("$mirror/search.php")
                .data("req", query)
                .data("column", "${Sorting.DEFAULT}")
                .data("res", results.toString())
                .data("sort", "$sortingField")
                .data("sortmode", "$sortingMode")
                .data("page", page.toString())
                .get()
    }

    @Throws(BiblioException::class)
    private fun getIds(query: String?, page: Int, results: Int): MutableList<String> {
        return try {
            val list: MutableList<String> = ArrayList()
            val doc: Document = query?.let { getSearchDoc(it, page, results) } ?: getRecentDoc(page)
            val rows = doc.getElementsByTag("tr")
            for (row in rows) {
                val id = row.child(0).text()
                if (StringUtils.isNumeric(id)) list.add(id)
            }
            list
        } catch (e: IOException) {
            throw BiblioException(e.message)
        }
    }

    @Throws(BiblioException::class)
    private fun search(ids: List<String>): List<Ebook> {
        if (ids.isEmpty()) throw BiblioException("No result: try a new query")
        return try {
            val response = JSONArray(searchRequest(ids))
            val list = ids.mapIndexed { i, it ->
                val book = parseBook(response.getJSONObject(i))
                book.id = it.toInt()
                book
            }.toMutableList()

            //fixme: add sorting by date, consider refactoring
            list.sortedBy {
                when (sortingField) {
                    Field.TITLE -> it.title
                    Field.AUTHOR -> it.author
                    else -> it.title
                }
            }
            if (sortingMode == Sorting.DESCENDING) list.reverse()
            list
        } catch (e: IOException) {
            throw BiblioException(e.message)
        } catch (e: JSONException) {
            throw BiblioException(e.message)
        } catch (e: StringIndexOutOfBoundsException) {
            throw BiblioException(e.message)
        }
    }

    @Throws(BiblioException::class, IOException::class)
    private fun searchRequest(ids: List<String>): String {
        val client = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()
        val fields = "Author,Title,MD5,Year,Pages,Language,Filesize,CoverURL"
        val formBody: RequestBody = FormBody.Builder()
                .add("ids", encodeIds(ids))
                .add("fields", fields)
                .build()
        val req = Request.Builder()
                .url("$mirror/json.php")
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody)
                .build()
        val resp = client.newCall(req).execute()
        val body = resp.body?.string() ?: throw BiblioException("Invalid response")
        return "[${body.substringAfter("[").substringBeforeLast("]")}]"
    }

    private fun encodeIds(ids: List<String>): String {
        val idsComma = StringBuilder()
        for (id in ids) idsComma.append(",").append(id)
        idsComma.replace(0, 1, "")
        return idsComma.toString()
    }

    private fun parseBook(jsonEbook: JSONObject): Ebook {
        val book = Ebook()
        book.provider = this
        book.author = jsonEbook.getString(Field.AUTHOR.toString())
        book.title = jsonEbook.getString(Field.TITLE.toString())
        book.mdHash = jsonEbook.getString("md5")
        var o = jsonEbook.getString(Field.YEAR.toString() + "")
        book.published = parseYear(o)
        o = jsonEbook.getString("pages")
        if (NumberUtils.isDigits(o)) book.pages = o.toInt() else logger.warn { "error while parsing pages: $o" }
        book.language = jsonEbook.getString("language")
        o = jsonEbook.getString("filesize")
        if (NumberUtils.isParsable(o)) book.filesize = o.toInt() else logger.warn { "error while parsing filesize: $o" }
        book.cover = getCoverUri(mirror, jsonEbook.getString("coverurl"))
        book.source = name
        book.downloadMirror = downloadMirror
        return book
    }

    private fun getCoverUri(uri: URI, cover: String): URI? {
        return if (cover.isEmpty()) {
            logger.warn { "no cover available" }
            null
        } else try {
            if (cover.startsWith("http")) URI(cover) else URI("$uri/covers/$cover")
        } catch (e: URISyntaxException) {
            logger.error { e.message }
            null
        }
    }

    private fun parseYear(year: String): LocalDate? {
        if (NumberUtils.isDigits(year)) return LocalDate.of(year.toInt(), 1, 1)
        else logger.warn { "unexpected year format: $year" }
        return null
    }

    companion object {
        private const val DEFAULT_MAX_RESULTS = 25

        @JvmStatic
        @Throws(BiblioException::class)
        fun loadDownloadURIs(book: Ebook): List<Download> {
            return try {
                val uri = String.format("%s/_ads/%s", book.downloadMirror.toString(), book.mdHash)
                val doc = Jsoup.connect(uri).get()
                val anchors = doc.getElementsByTag("a")
                anchors.filter { it.text().toLowerCase() == "get" }
                        .map { extractDownload("${book.downloadMirror}${it.attr("href")}") }
                        .toList()
            } catch (e: IOException) {
                throw BiblioException(e.message)
            }
        }
    }

    data class Builder(
            val mirror: URI = URI("http://93.174.95.27"),
            val downloadMirror: URI = URI("http://93.174.95.29"),
            val maxResults: Int = DEFAULT_MAX_RESULTS,
            val sortingMode: Sorting = Sorting.DEFAULT,
            val sortingField: Field = Field.DEFAULT) {

        fun build() = LibraryGenesis(mirror, downloadMirror, maxResults, sortingMode, sortingField)
    }
}