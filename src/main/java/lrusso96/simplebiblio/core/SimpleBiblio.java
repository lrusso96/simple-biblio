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

    final Logger logger;
    private final Set<Provider> providers;

    SimpleBiblio(Set<Provider> providers, Logger logger) {
        this.logger = logger;
        if (providers == null || providers.isEmpty())
            this.providers = setDefaultProviders();
        else
            this.providers = providers;
    }

    private Set<Provider> setDefaultProviders() {
        Set<Provider> basic = new HashSet<>();
        basic.add(new LibraryGenesisBuilder(this).build());
        basic.add(new FeedbooksBuilder(this).build());
        basic.add(new StandardEbooks(this));
        return basic;
    }

    public List<Ebook> searchAll(String query) {
        List<Ebook> ebooks = new ArrayList<>();
        //todo: support for parallel search
        for (Provider provider : providers) {
            try {
                ebooks.addAll(provider.search(query));
            } catch (BiblioException e) {
                log(e.getMessage());
            }
        }
        return ebooks;
    }

    public List<Ebook> getAllRecent() {
        List<Ebook> ebooks = new ArrayList<>();
        //todo: support for parallel search
        for (Provider provider : providers) {
            try {
                ebooks.addAll(provider.getRecent());
            } catch (BiblioException e) {
                log(e.getMessage());
            }
        }
        return ebooks;
    }

    public List<Ebook> getAllPopular() {
        List<Ebook> ebooks = new ArrayList<>();
        //todo: support for parallel search
        for (Provider provider : providers) {
            try {
                ebooks.addAll(provider.getPopular());
            } catch (BiblioException e) {
                log(e.getMessage());
            }
        }
        return ebooks;
    }

    private void log(String str) {
        if (logger != null) logger.log(Level.SEVERE, str);
    }

    public int getMaxTries() {
        return 3;
    }
}
