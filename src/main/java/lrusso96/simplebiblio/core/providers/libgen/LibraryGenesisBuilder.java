package lrusso96.simplebiblio.core.providers.libgen;

import java.net.URI;

public class LibraryGenesisBuilder {

    private int maxResultsNumber;
    private Sorting mode;
    private Field sorting;
    private URI mirror;

    public LibraryGenesis build(){
        return new LibraryGenesis(mirror, maxResultsNumber, mode, sorting);
    }

    public LibraryGenesisBuilder setMirror(URI mirror) {
        this.mirror = mirror;
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
