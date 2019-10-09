package lrusso96.simplebiblio;

import lrusso96.simplebiblio.core.Ebook;
import lrusso96.simplebiblio.core.providers.libgen.LibraryGenesis;
import lrusso96.simplebiblio.core.providers.libgen.LibraryGenesisBuilder;
import lrusso96.simplebiblio.exceptions.BiblioException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LibgenTest {
    @Test
    public void simpleSearch() throws BiblioException {
        LibraryGenesis libgen = new LibraryGenesisBuilder().build();
        List<Ebook> ret = libgen.search("Carroll");
        assertNotEquals(0, ret.size());
        Ebook book = ret.get(0);
        assertNotNull(book.getAuthor());
        assertNotEquals(0, book.getId());
        assertNotNull(book.getTitle());
        libgen.loadDownloadURI(book);
        assertNotNull(book.getDownload());
        assertNotNull(book.getDownload());
    }
}