package lrusso96.simplebiblio;

import lrusso96.simplebiblio.core.Ebook;
import lrusso96.simplebiblio.core.providers.libgen.Field;
import lrusso96.simplebiblio.core.providers.libgen.LibraryGenesis;
import lrusso96.simplebiblio.core.providers.libgen.LibraryGenesisBuilder;
import lrusso96.simplebiblio.core.providers.libgen.Sorting;
import lrusso96.simplebiblio.exceptions.BiblioException;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static lrusso96.simplebiblio.core.Utils.bytesToReadableSize;
import static org.junit.Assert.*;

public class LibgenTest {

    @Test
    public void simpleSearchTest() throws BiblioException, InterruptedException {
        LibraryGenesis libgen = new LibraryGenesisBuilder().build();
        List<Ebook> ret = libgen.search("Carroll");
        simpleTest(ret);
    }

    @Test
    public void recentTest() throws BiblioException, InterruptedException {
        LibraryGenesis libgen = new LibraryGenesisBuilder().build();
        List<Ebook> ret = libgen.getRecent();
        simpleTest(ret);
    }

    private void simpleTest(List<Ebook> ret) throws InterruptedException {
        assertNotEquals(0, ret.size());
        Ebook book = ret.get(0);
        assertNotNull(book.getAuthor());
        assertNotEquals(0, book.getId());
        assertNotNull(book.getTitle());
        Thread.sleep(2000);
        assertNotEquals(0, book.getDownloads().size());
        assertNotEquals(0, book.getDownloads().get(0).getExtension().length());
        System.out.println(String.format("filesize: %s", bytesToReadableSize(book.getFilesize())));
    }

    @Test
    public void customOptionsTest() throws BiblioException, URISyntaxException {
        LibraryGenesis libgen = new LibraryGenesisBuilder()
                .setMirror(new URI("http://93.174.95.27"))
                .setMaxResultsNumber(10)
                .setSortingField(Field.TITLE)
                .setSortingMode(Sorting.ASCENDING)
                .build();
        List<Ebook> ret = libgen.search("Dante Alighieri");
        assertTrue(ret.size() > 1);
        assertTrue(ret.size() <= 10);
        Ebook b1 = ret.get(0);
        Ebook b2 = ret.get(1);
        assertTrue(b1.getTitle().compareTo(b2.getTitle()) <= 0);
    }
}