package lrusso96.simplebiblio.core.providers.libgen;

import lrusso96.simplebiblio.core.SimpleBiblio;

import java.net.URI;


public class LibraryGenesisBuilder {

    final SimpleBiblio biblio;
    int maxResultsNumber;
    Sorting mode;
    Field sorting;
    URI mirror;
    URI download_mirror;

    public LibraryGenesisBuilder(SimpleBiblio biblio) {
        this.biblio = biblio;
    }

    public LibraryGenesis build() {
        return new LibraryGenesis(this);
    }

    public LibraryGenesisBuilder setMirror(URI mirror) {
        this.mirror = mirror;
        return this;
    }

    public LibraryGenesisBuilder setDownloadMirror(URI mirror) {
        this.download_mirror = mirror;
        return this;
    }

    public LibraryGenesisBuilder setMaxResultsNumber(int maxResultsNumber) {
        assert maxResultsNumber > 0;
        this.maxResultsNumber = maxResultsNumber;
        return this;
    }

    public LibraryGenesisBuilder setSortingMode(Sorting mode) {
        this.mode = mode;
        return this;
    }

    public LibraryGenesisBuilder setSortingField(Field sorting) {
        this.sorting = sorting;
        return this;
    }
}
