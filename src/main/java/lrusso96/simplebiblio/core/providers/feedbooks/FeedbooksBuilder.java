package lrusso96.simplebiblio.core.providers.feedbooks;

import lrusso96.simplebiblio.core.SimpleBiblio;

import java.util.HashSet;
import java.util.Set;

public class FeedbooksBuilder {

    final Set<String> languages = new HashSet<>();
    final SimpleBiblio biblio;

    public FeedbooksBuilder(SimpleBiblio biblio) {
        this.biblio = biblio;
    }

    public FeedbooksBuilder addLanguage(Language language) {
        languages.add(language.toString());
        return this;
    }

    public Feedbooks build() {
        return new Feedbooks(this);
    }
}
