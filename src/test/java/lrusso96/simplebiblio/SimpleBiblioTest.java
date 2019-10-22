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
    public void simpleSearchTest() throws InterruptedException {
        SimpleBiblio biblio = new SimpleBiblioBuilder().build();
        List<Ebook> ret = biblio.searchAll("Carroll");
        simpleTest(ret);
    }

    @Test
    public void getRecentTest() {
        SimpleBiblio biblio = new SimpleBiblioBuilder().build();
        List<Ebook> ret = biblio.getAllRecent();
        simpleTest(ret);
    }

    @Test
    public void getPopularTest() {
        SimpleBiblio biblio = new SimpleBiblioBuilder().build();
        List<Ebook> ret = biblio.getAllPopular();
        simpleTest(ret);
    }

    private void simpleTest(List<Ebook> ret) {
        assertNotEquals(0, ret.size());
        Ebook book = ret.get(0);
        assertNotNull(book.getAuthor());
        assertNotEquals(0, book.getId());
        assertNotNull(book.getTitle());
        assertNotNull(book.getDownload());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}