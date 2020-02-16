package lrusso96.simplebiblio

import kotlinx.coroutines.runBlocking
import lrusso96.simplebiblio.core.Ebook
import lrusso96.simplebiblio.core.providers.standardebooks.StandardEbooks
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class StandardEbooksTest {

    @Test
    fun recentTest() = runBlocking { simpleTest(StandardEbooks().getRecent()) }

    private fun simpleTest(ret: List<Ebook>) {
        assertTrue(ret.isNotEmpty())
        val book = ret[0]
        assertNotNull(book.title)
        assertNotNull(book.author)
        assertNotNull(book.summary)
        assertNotNull(book.published)
        assertTrue(book.getDownloads().isNotEmpty())
    }
}