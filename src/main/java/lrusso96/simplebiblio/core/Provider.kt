package lrusso96.simplebiblio.core

import lrusso96.simplebiblio.exceptions.BiblioException
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

abstract class Provider(val name: String, private val maxTries: Int = 3) {

    suspend fun search(query: String): List<Ebook> {
        var cnt = 0
        while (true) try {
            return doSearch(query)
        } catch (ex: BiblioException) {
            if (++cnt == maxTries) {
                logger.error { "$name failed to retrieve result for $maxTries time(s)" }
                return ArrayList()
            }
        }
    }

    @Throws(BiblioException::class)
    protected abstract suspend fun doSearch(query: String): List<Ebook>

    suspend fun getRecent(): List<Ebook> {
        var cnt = 0
        while (true) try {
            return doGetRecent()
        } catch (ex: BiblioException) {
            if (++cnt == maxTries) {
                logger.error { "$name failed to retrieve result for $maxTries time(s)" }
                return ArrayList()
            }
        }
    }

    @Throws(BiblioException::class)
    protected abstract suspend fun doGetRecent(): List<Ebook>

    suspend fun getPopular(): List<Ebook> {
        var cnt = 0
        while (true) try {
            return doGetPopular()
        } catch (ex: BiblioException) {
            if (++cnt == maxTries) {
                logger.error { "$name failed to retrieve result for $maxTries time(s)" }
                return ArrayList()
            }
        }
    }

    @Throws(BiblioException::class)
    protected abstract suspend fun doGetPopular(): List<Ebook>

    companion object {
        const val LIBGEN = "Library Genesis"
        const val FEEDBOOKS = "Feedbooks"
        const val STANDARD_EBOOKS = "Standard Ebooks"
    }
}