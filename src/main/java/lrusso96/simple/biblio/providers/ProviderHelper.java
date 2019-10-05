package lrusso96.simple.biblio.providers;

import java.util.ArrayList;
import java.util.List;

public class ProviderHelper {

    public static List<Provider> initProviders(){
        List<Provider> providers = new ArrayList<>();
        providers.add(new LibraryGenesis());
        providers.add(new Feedbooks());
        return providers;
    }
}
