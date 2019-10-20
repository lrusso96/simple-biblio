package lrusso96.simplebiblio.core.providers.feedbooks;

import net.jodah.failsafe.RetryPolicy;

import java.util.HashSet;
import java.util.Set;

public class FeedbooksBuilder {

    private final Set<String> languages = new HashSet<>();
    private RetryPolicy<Object> retryPolicy;

    public FeedbooksBuilder addLanguage(Language language) {
        languages.add(language.toString());
        return this;
    }

    public FeedbooksBuilder setRetryPolicy(RetryPolicy<Object> policy) {
        this.retryPolicy = policy;
        return this;
    }


    public Feedbooks build() {
        return new Feedbooks(languages, retryPolicy);
    }
}
