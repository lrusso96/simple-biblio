package lrusso96.simplebiblio.core;

import lrusso96.simplebiblio.core.providers.feedbooks.FeedbooksBuilder;
import lrusso96.simplebiblio.core.providers.libgen.LibraryGenesisBuilder;
import lrusso96.simplebiblio.core.providers.standardebooks.StandardEbooks;
import lrusso96.simplebiblio.exceptions.BiblioException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleBiblio {

    private final Set<Provider> providers;
    private final Logger logger;

    SimpleBiblio(Set<Provider> providers, Logger logger) {
        if (providers == null || providers.isEmpty())
            this.providers = setDefaultProviders();
        else
            this.providers = providers;
        this.logger = logger;
    }

    private Set<Provider> setDefaultProviders() {
        Set<Provider> basic = new HashSet<>();
        basic.add(new LibraryGenesisBuilder().build());
        basic.add(new FeedbooksBuilder().build());
        basic.add(new StandardEbooks());
        return basic;
    }

    public List<Ebook> searchAll(String query) {
        List<Ebook> ebooks = new ArrayList<>();
        //todo: support for parallel search
        for (Provider provider : providers) {
            try {
                ebooks.addAll(provider.search(query));
            } catch (BiblioException e) {
                log(Level.SEVERE, e.getMessage());
            }
        }
        return ebooks;
    }

    private void log(Level level, String str) {
        if (logger != null) logger.log(level, str);
    }
}
