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

    void setMirror(URI mirror) {
        this.mirror = mirror;
    }

    void setMaxResultsNumber(int maxResultsNumber) {
        assert maxResultsNumber > 0;
        this.maxResultsNumber = maxResultsNumber;
    }

    void setSortingMode(Sorting mode) {
        this.mode = mode;
    }

    void setSortingField(Field sorting) {
        this.sorting = sorting;
    }
}
