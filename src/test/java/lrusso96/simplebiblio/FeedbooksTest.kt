package lrusso96.simplebiblio

import kotlinx.coroutines.runBlocking
import lrusso96.simplebiblio.core.Ebook
import lrusso96.simplebiblio.core.providers.feedbooks.Feedbooks
import lrusso96.simplebiblio.core.providers.feedbooks.Language
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FeedbooksTest {
    @Test
    fun searchTest() {
        val feedbooks = Feedbooks.Builder().build()
        val ret = runBlocking { feedbooks.search("Carroll") }
        simpleTest(ret)
    }

    @Test
    fun recentTest() {
        val feedbooks = Feedbooks.Builder().build()
        val ret = runBlocking { feedbooks.getRecent() }
        simpleTest(ret)
    }

    private fun simpleTest(ret: List<Ebook>) {
        assertTrue(ret.isNotEmpty())
        val book = ret[0]
        assertNotNull(book.author)
        assertNotEquals(0, book.id)
        assertNotNull(book.title)
        assertTrue(book.getDownloads().isNotEmpty())
        assertTrue(book.getDownloads()[0].extension!!.isNotBlank())
    }

    @Test
    fun customLanguageTest() {
        val feedbooks = Feedbooks.Builder().addLanguage(Language.ITALIAN).build()
        val ret = runBlocking { feedbooks.search("Dante Alighieri") }
        assertTrue(ret.isNotEmpty())
        val book = ret[0]
        assertNotNull(book.author)
        assertEquals("it", book.language)
    }
}