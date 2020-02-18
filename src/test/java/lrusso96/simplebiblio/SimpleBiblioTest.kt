package lrusso96.simplebiblio

import kotlinx.coroutines.runBlocking
import lrusso96.simplebiblio.core.Ebook
import lrusso96.simplebiblio.core.SimpleBiblio
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SimpleBiblioTest {
    @Test
    fun simpleSearchTest() = runBlocking { simpleTest(setUp().searchAll("Carroll")) }

    @Test
    fun getRecentTest() = runBlocking { simpleTest(setUp().getAllRecent()) }

    @Test
    fun getPopularTest() = runBlocking { simpleTest(setUp().getAllPopular()) }

    private fun simpleTest(ret: List<Ebook>) {
        assertTrue(ret.isNotEmpty())
        val book = ret[0]
        assertNotNull(book.author)
        assertNotNull(book.title)
        assertTrue(book.getDownloads().isNotEmpty())
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    companion object {
        fun setUp() = SimpleBiblio.Builder().build()
    }
}