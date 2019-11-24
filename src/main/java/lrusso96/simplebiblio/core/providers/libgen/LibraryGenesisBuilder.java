package lrusso96.simplebiblio.core.providers.libgen;

import lrusso96.simplebiblio.core.SimplePolicy;
import net.jodah.failsafe.RetryPolicy;

import java.net.URI;
import java.util.logging.Logger;

import static lrusso96.simplebiblio.core.Provider.getRetryPolicy;

public class LibraryGenesisBuilder {

    private int maxResultsNumber;
    private Sorting mode;
    private Field sorting;
    private URI mirror;
    private RetryPolicy<Object> retryPolicy;
    private Logger logger;

    public LibraryGenesis build() {
        if (retryPolicy == null)
            retryPolicy = getRetryPolicy(SimplePolicy.DEFAULT);
        return new LibraryGenesis(mirror, maxResultsNumber, mode, sorting, retryPolicy, logger);
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

    public LibraryGenesisBuilder setRetryPolicy(RetryPolicy<Object> policy) {
        this.retryPolicy = policy;
        return this;
    }

    public LibraryGenesisBuilder setRetryPolicy(SimplePolicy simplePolicy) {
        return setRetryPolicy(getRetryPolicy(simplePolicy));
    }

    public LibraryGenesisBuilder setLogger(Logger logger) {
        this.logger = logger;
        return this;
    }
}
