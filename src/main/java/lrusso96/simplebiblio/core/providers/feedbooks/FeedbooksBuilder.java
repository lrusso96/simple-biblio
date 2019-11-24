package lrusso96.simplebiblio.core.providers.feedbooks;

import lrusso96.simplebiblio.core.SimplePolicy;
import net.jodah.failsafe.RetryPolicy;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static lrusso96.simplebiblio.core.Provider.getRetryPolicy;

public class FeedbooksBuilder {

    final Set<String> languages = new HashSet<>();
    RetryPolicy<Object> retryPolicy;
    Logger logger;

    public FeedbooksBuilder addLanguage(Language language) {
        languages.add(language.toString());
        return this;
    }

    public FeedbooksBuilder setRetryPolicy(RetryPolicy<Object> policy) {
        this.retryPolicy = policy;
        return this;
    }

    public FeedbooksBuilder setRetryPolicy(SimplePolicy simplePolicy) {
        this.retryPolicy = getRetryPolicy(simplePolicy);
        return this;
    }

    public FeedbooksBuilder setLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public Feedbooks build() {
        if (retryPolicy == null)
            retryPolicy = getRetryPolicy(SimplePolicy.DEFAULT);
        return new Feedbooks(this);
    }
}
