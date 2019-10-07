package lrusso96.simplebiblio;

import lrusso96.simplebiblio.core.Ebook;
import lrusso96.simplebiblio.core.providers.feedbooks.Feedbooks;
import lrusso96.simplebiblio.core.providers.feedbooks.FeedbooksBuilder;
import lrusso96.simplebiblio.core.providers.feedbooks.Language;
import lrusso96.simplebiblio.exceptions.BiblioException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FeedbooksTest {
    @Test
    public void simpleSearch() throws BiblioException {
        Feedbooks feedbooks = new FeedbooksBuilder().build();
        List<Ebook> ret = feedbooks.search("Carroll");
        assertNotEquals(0, ret.size());
        Ebook book = ret.get(0);
        assertNotNull(book.getAuthor());
        assertNotEquals(0, book.getId());
        assertNotNull(book.getTitle());
        assertNotNull(book.getDownload());
    }

    @Test
    public void customLanguage() throws BiblioException {
        Feedbooks feedbooks = new FeedbooksBuilder().addLanguage(Language.ITALIAN).build();
        List<Ebook> ret = feedbooks.search("Dante Alighieri");
        assertNotEquals(0, ret.size());
        Ebook book = ret.get(0);
        assertNotNull(book.getAuthor());
        assertEquals("it", book.getLanguage());
    }
}