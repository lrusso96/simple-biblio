package lrusso96.simplebiblio;

import lrusso96.simplebiblio.core.Ebook;
import lrusso96.simplebiblio.core.providers.standardebooks.StandardEbooks;
import lrusso96.simplebiblio.exceptions.BiblioException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class StandardEbooksTest {

    @Test
    public void recentTest() throws BiblioException {
        StandardEbooks standard = new StandardEbooks();
        List<Ebook> ret = standard.getRecent();
        simpleTest(ret);
    }

    private void simpleTest(List<Ebook> ret) {
        assertNotEquals(0, ret.size());
        Ebook book = ret.get(0);
        assertNotNull(book.getTitle());
        assertNotNull(book.getAuthor());
        assertNotNull(book.getSummary());
        assertNotNull(book.getPublished());
        assertNotEquals(0, book.getDownload().size());
    }
}