package lrusso96.simplebiblio

import kotlinx.coroutines.runBlocking
import lrusso96.simplebiblio.core.Ebook
import lrusso96.simplebiblio.core.SimpleBiblio
import org.junit.Assert
import org.junit.Test

class SimpleBiblioTest {
    @Test
    fun simpleSearchTest() = runBlocking {  simpleTest(setUp().searchAll("Carroll")) }

    @Test
    fun getRecentTest() = runBlocking {  simpleTest(setUp().getAllRecent()) }

    @Test
    fun getPopularTest() = runBlocking { simpleTest(setUp().getAllPopular()) }

    private fun simpleTest(ret: List<Ebook>) {
        Assert.assertTrue(ret.isNotEmpty())
        val book = ret[0]
        Assert.assertNotNull(book.author)
        if (book.id == 0) println(book.title)
        Assert.assertNotEquals(0, book.id)
        Assert.assertNotNull(book.title)
        Assert.assertTrue(book.getDownloads().isNotEmpty())
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