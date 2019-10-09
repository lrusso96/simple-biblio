package lrusso96.simplebiblio.core;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class SimpleBiblioBuilder {

    private final Set<Provider> providers;
    private Logger logger;

    public SimpleBiblioBuilder() {
        this.providers = new HashSet<>();
    }

    public SimpleBiblioBuilder addProvider(Provider provider) {
        providers.add(provider);
        return this;
    }

    public SimpleBiblioBuilder setLogger(Logger logger){
        this.logger = logger;
        return this;
    }

    public SimpleBiblio build() {
        return new SimpleBiblio(providers, logger);
    }
}
