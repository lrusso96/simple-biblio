package lrusso96.simplebiblio.core.providers;

import lrusso96.simplebiblio.core.Ebook;
import lrusso96.simplebiblio.core.Provider;
import lrusso96.simplebiblio.exceptions.BiblioException;

import java.util.List;

public class LibraryGenesis extends Provider {

    LibraryGenesis() {
        this.name = "Library Genesis";
    }

    @Override
    public List<Ebook> search(String query) throws BiblioException {
        return super.search(query);
    }

    @Override
    public List<Ebook> getRecent() throws BiblioException {
        return super.getRecent();
    }

    @Override
    public List<Ebook> getPopular() throws BiblioException {
        return super.getPopular();
    }
}
