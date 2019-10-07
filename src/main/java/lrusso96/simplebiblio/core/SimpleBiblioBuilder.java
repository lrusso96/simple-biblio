package lrusso96.simplebiblio.core;

import java.util.HashSet;
import java.util.Set;

public class SimpleBiblioBuilder {

    private final Set<Provider> providers;

    public SimpleBiblioBuilder() {
        this.providers = new HashSet<>();
    }

    public SimpleBiblioBuilder addProvider(Provider provider){
        providers.add(provider);
        return this;
    }

    public SimpleBiblio build(){
        return new SimpleBiblio(providers);
    }
}
