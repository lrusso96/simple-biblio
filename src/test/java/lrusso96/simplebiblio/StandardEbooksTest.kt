package lrusso96.simplebiblio

import kotlinx.coroutines.runBlocking
import lrusso96.simplebiblio.core.Ebook
import lrusso96.simplebiblio.core.providers.standardebooks.StandardEbooks
import org.junit.Assert
import org.junit.Test

class StandardEbooksTest {

    @Test
    fun recentTest() = runBlocking { simpleTest(StandardEbooks().getRecent()) }

    private fun simpleTest(ret: List<Ebook>) {
        Assert.assertTrue(ret.isNotEmpty())
        val book = ret[0]
        Assert.assertNotNull(book.title)
        Assert.assertNotNull(book.author)
        Assert.assertNotNull(book.summary)
        Assert.assertNotNull(book.published)
        Assert.assertTrue(book.getDownloads().isNotEmpty())
    }
}