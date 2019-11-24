package lrusso96.simplebiblio;

import lrusso96.simplebiblio.core.Ebook;
import lrusso96.simplebiblio.core.SimpleBiblio;
import lrusso96.simplebiblio.core.SimpleBiblioBuilder;
import org.junit.Test;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleBiblioTest {

    public SimpleBiblio setUp() {
        Logger logger = Logger.getLogger("simple-biblio-test");
        logger.setLevel(Level.WARNING);
        return new SimpleBiblioBuilder().setLogger(logger).build();
    }

    @Test
    public void simpleSearchTest() {
        List<Ebook> ret = setUp().searchAll("Carroll");
        simpleTest(ret);
    }

    @Test
    public void getRecentTest() {
        List<Ebook> ret = setUp().getAllRecent();
        simpleTest(ret);
    }

    @Test
    public void getPopularTest() {
        List<Ebook> ret = setUp().getAllPopular();
        simpleTest(ret);
    }

    private void simpleTest(List<Ebook> ret) {
        assertNotEquals(0, ret.size());
        Ebook book = ret.get(0);
        assertNotNull(book.getAuthor());
        if (book.getId() == 0)
            System.out.println(book.getTitle());
        assertNotEquals(0, book.getId());
        assertNotNull(book.getTitle());
        assertNotEquals(0, book.getDownloads().size());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}