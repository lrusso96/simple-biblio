package lrusso96.simplebiblio

import lrusso96.simplebiblio.core.Ebook
import lrusso96.simplebiblio.core.providers.feedbooks.Feedbooks
import lrusso96.simplebiblio.core.providers.standardebooks.StandardEbooks
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EbookTest {
    @Test
    fun equalsTest() {
        val provider1 = Feedbooks.Builder().build()
        val provider2 = Feedbooks.Builder().build()
        val provider3 = StandardEbooks()
        val id = 1
        val ebook1 = Ebook()
        ebook1.id = id
        ebook1.provider = provider1
        val ebook2 = Ebook()
        ebook2.id = id
        ebook2.provider = provider2
        assertTrue(ebook1 == ebook2)

        ebook2.id = id + 1
        assertFalse(ebook1 == ebook2)

        ebook2.id = id
        ebook2.provider = provider3
        assertFalse(ebook1 == ebook2)
    }
}
