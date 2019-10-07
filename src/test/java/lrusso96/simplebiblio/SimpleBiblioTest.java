package lrusso96.simplebiblio;

import lrusso96.simplebiblio.core.Ebook;
import lrusso96.simplebiblio.core.SimpleBiblio;
import lrusso96.simplebiblio.core.SimpleBiblioBuilder;
import lrusso96.simplebiblio.exceptions.BiblioException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SimpleBiblioTest {
    @Test
    public void simpleSearch() throws BiblioException {
        SimpleBiblio biblio = new SimpleBiblioBuilder().build();
        List<Ebook> ret = biblio.searchAll("Carroll");
        assertNotEquals(0, ret.size());
        Ebook book = ret.get(0);
        assertNotNull(book.getAuthor());
        assertNotEquals(0, book.getId());
        assertNotNull(book.getTitle());
        assertNotNull(book.getDownload());
    }
}