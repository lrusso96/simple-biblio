package lrusso96.simplebiblio

import kotlinx.coroutines.runBlocking
import lrusso96.simplebiblio.core.Ebook
import lrusso96.simplebiblio.core.providers.feedbooks.Feedbooks
import lrusso96.simplebiblio.core.providers.feedbooks.Language
import org.junit.Assert
import org.junit.Test

class FeedbooksTest {
    @Test
    fun searchTest() {
        val feedbooks = Feedbooks.Builder().build()
        val ret = runBlocking {  feedbooks.search("Carroll") }
        simpleTest(ret)
    }

    @Test
    fun recentTest() {
        val feedbooks = Feedbooks.Builder().build()
        val ret = runBlocking {  feedbooks.getRecent() }
        simpleTest(ret)
    }

    private fun simpleTest(ret: List<Ebook>) {
        Assert.assertTrue(ret.isNotEmpty())
        val book = ret[0]
        Assert.assertNotNull(book.author)
        Assert.assertNotEquals(0, book.id)
        Assert.assertNotNull(book.title)
        Assert.assertTrue(book.getDownloads().isNotEmpty())
        Assert.assertTrue(book.getDownloads()[0].extension!!.isNotBlank())
    }

    @Test
    fun customLanguageTest() {
        val feedbooks = Feedbooks.Builder().addLanguage(Language.ITALIAN).build()
        val ret = runBlocking { feedbooks.search("Dante Alighieri") }
        Assert.assertTrue(ret.isNotEmpty())
        val book = ret[0]
        Assert.assertNotNull(book.author)
        Assert.assertEquals("it", book.language)
    }
}