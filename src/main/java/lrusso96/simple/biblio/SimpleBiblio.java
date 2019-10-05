package lrusso96.simple.biblio;

import lrusso96.simple.biblio.providers.Provider;
import lrusso96.simple.biblio.providers.ProviderHelper;

import java.util.ArrayList;
import java.util.List;

public class SimpleBiblio {

    private static List<Provider> providers;

    public SimpleBiblio(){
        providers = ProviderHelper.initProviders();
    }

    public static List<Ebook> search(String query){
        List<Ebook> ebooks = new ArrayList<>();
        //todo: support for parallel search
        for(Provider provider : providers)
            ebooks.addAll(provider.search(query));
        return ebooks;
    }

    public static List<Provider> getSupportedProviders(){
        return providers;
    }
}
