package lrusso96.simple.biblio.providers;

import lrusso96.simple.biblio.Ebook;

import java.util.List;

public class LibraryGenesis extends Provider {

    LibraryGenesis() {
        this.name = "Library Genesis";
    }

    @Override
    public List<Ebook> search(String query) {
        return super.search(query);
    }
}
