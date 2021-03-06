package lrusso96.simplebiblio

import kotlinx.coroutines.runBlocking
import lrusso96.simplebiblio.core.Ebook
import lrusso96.simplebiblio.core.Utils.bytesToReadableSize
import lrusso96.simplebiblio.core.providers.libgen.Field
import lrusso96.simplebiblio.core.providers.libgen.LibraryGenesis
import lrusso96.simplebiblio.core.providers.libgen.Sorting
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.net.URI

class LibgenTest {
    @Test
    fun simpleSearchTest() {
        val libgen = LibraryGenesis.Builder().build()
        val ret = runBlocking { libgen.search("Carroll") }
        simpleTest(ret)
    }

    @Test
    fun recentTest() {
        val libgen = LibraryGenesis.Builder().build()
        val ret = runBlocking { libgen.getRecent() }
        simpleTest(ret)
    }

    private fun simpleTest(ret: List<Ebook>) {
        assertTrue(ret.isNotEmpty())
        val book = ret[0]
        assertNotNull(book.author)
        assertNotEquals(0, book.id)
        assertNotNull(book.title)
        Thread.sleep(2000)
        assertTrue(book.getDownloads().isNotEmpty())
        assertTrue(book.getDownloads()[0].extension!!.isNotBlank())
        println(String.format("filesize: %s", bytesToReadableSize(book.filesize)))
    }

    @Test
    fun customOptionsTest() {
        val libgen = LibraryGenesis.Builder(
                mirror = URI("http://93.174.95.27"),
                maxResults = 10,
                sortingField = Field.TITLE,
                sortingMode = Sorting.ASCENDING)
                .build()
        val ret = runBlocking { libgen.search("Dante Alighieri") }
        assertTrue(ret.size > 1)
        assertTrue(ret.size <= 10)
        val b1 = ret[0]
        val b2 = ret[1]
        assertTrue(b1.title!! <= b2.title!!)
    }
}