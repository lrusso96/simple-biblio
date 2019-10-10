package lrusso96.simplebiblio;

import lrusso96.simplebiblio.core.Ebook;
import lrusso96.simplebiblio.core.SimpleBiblio;
import lrusso96.simplebiblio.core.SimpleBiblioBuilder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleBiblioTest {
    @Test
    public void simpleSearchTest() {
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