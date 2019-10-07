package lrusso96.simplebiblio.core;

import lrusso96.simplebiblio.core.providers.LibraryGenesisBuilder;
import lrusso96.simplebiblio.core.providers.feedbooks.FeedbooksBuilder;
import lrusso96.simplebiblio.exceptions.BiblioException;

import java.util.*;

public class SimpleBiblio {

    private final Set<Provider> providers;

    SimpleBiblio(Set<Provider> providers){
        if(providers == null || providers.isEmpty())
            this.providers = setDefaultProviders();
        else
            this.providers = providers;
    }

    private Set<Provider> setDefaultProviders(){
        Set<Provider> providers = new HashSet<>();
        providers.add(new LibraryGenesisBuilder().build());
        providers.add(new FeedbooksBuilder().build());
        return providers;
    }

    public List<Ebook> searchAll(String query) throws BiblioException {
        List<Ebook> ebooks = new ArrayList<>();
        //todo: support for parallel search
        for(Provider provider : providers)
            Optional.ofNullable(provider.search(query)).ifPresent(ebooks::addAll);
        return ebooks;
    }
}
