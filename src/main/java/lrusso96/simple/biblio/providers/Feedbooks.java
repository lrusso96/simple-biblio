package lrusso96.simple.biblio.providers;

import lrusso96.simple.biblio.Ebook;

import java.util.List;

public class Feedbooks extends Provider {

    Feedbooks() {
        this.name = "providers.Feedbooks";
    }

    @Override
    public List<Ebook> search(String query) {
        return super.search(query);
    }
}