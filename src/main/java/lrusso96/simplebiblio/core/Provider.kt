package lrusso96.simplebiblio.core

import lrusso96.simplebiblio.exceptions.BiblioException
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

abstract class Provider(val name: String, private val maxTries: Int = 3) {

    suspend fun search(query: String) = simpleFun(this::doGetPopular, query)
    suspend fun getRecent() = simpleFun(this::doGetRecent)
    suspend fun getPopular() = simpleFun(this::doGetPopular)

    @Throws(BiblioException::class)
    protected abstract suspend fun doSearch(query: String): List<Ebook>

    @Throws(BiblioException::class)
    protected abstract suspend fun doGetRecent(): List<Ebook>

    @Throws(BiblioException::class)
    protected abstract suspend fun doGetPopular(): List<Ebook>

    private suspend fun simpleFun(method: suspend () -> List<Ebook>, query: String? = null): List<Ebook> {
        var cnt = 0
        while (true) try {
            if (query == null) return method() else return doSearch(query)
        } catch (ex: BiblioException) {
            if (++cnt == maxTries) {
                logger.error { "$name failed to retrieve result for $maxTries time(s)" }
                return ArrayList()
            }
        }
    }

    companion object {
        const val LIBGEN = "Library Genesis"
        const val FEEDBOOKS = "Feedbooks"
        const val STANDARD_EBOOKS = "Standard Ebooks"
    }
}