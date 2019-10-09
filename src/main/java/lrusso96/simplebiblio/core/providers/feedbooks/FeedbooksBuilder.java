package lrusso96.simplebiblio.core.providers.feedbooks;

import java.util.HashSet;
import java.util.Set;

public class FeedbooksBuilder {

    private final Set<String> languages = new HashSet<>();

    public FeedbooksBuilder addLanguage(Language language) {
        languages.add(language.toString());
        return this;
    }

    public Feedbooks build(){
        return new Feedbooks(languages);
    }
}
