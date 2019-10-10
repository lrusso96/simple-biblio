package lrusso96.simplebiblio;

import lrusso96.simplebiblio.core.Ebook;
import lrusso96.simplebiblio.core.providers.libgen.Field;
import lrusso96.simplebiblio.core.providers.libgen.LibraryGenesis;
import lrusso96.simplebiblio.core.providers.libgen.LibraryGenesisBuilder;
import lrusso96.simplebiblio.core.providers.libgen.Sorting;
import lrusso96.simplebiblio.exceptions.BiblioException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LibgenTest {
    @Test
    public void simpleSearchTest() throws BiblioException, InterruptedException {
        LibraryGenesis libgen = new LibraryGenesisBuilder().build();
        List<Ebook> ret = libgen.search("Carroll");
        simpleTest(libgen, ret);
    }

    @Test
    public void recentTest() throws BiblioException, InterruptedException {
        LibraryGenesis libgen = new LibraryGenesisBuilder().build();
        List<Ebook> ret = libgen.getRecent();
        simpleTest(libgen, ret);
    }

    private void simpleTest(LibraryGenesis libgen, List<Ebook> ret) throws BiblioException, InterruptedException {
        assertNotEquals(0, ret.size());
        Ebook book = ret.get(0);
        assertNotNull(book.getAuthor());
        assertNotEquals(0, book.getId());
        assertNotNull(book.getTitle());
        libgen.loadDownloadURI(book);
        Thread.sleep(2000);
        assertNotNull(book.getDownload());
    }

    @Test
    public void customSortingTest() throws BiblioException {
        LibraryGenesis libgen = new LibraryGenesisBuilder()
                .setSortingField(Field.TITLE)
                .setSortingMode(Sorting.ASCENDING)
                .build();
        List<Ebook> ret = libgen.search("Dante Alighieri");
        assertTrue(ret.size() > 1);
        Ebook b1 = ret.get(0);
        Ebook b2 = ret.get(1);
        assertTrue(b1.getTitle().compareTo(b2.getTitle()) <= 0);
    }
}