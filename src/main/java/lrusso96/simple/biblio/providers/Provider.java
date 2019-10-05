package lrusso96.simple.biblio.providers;

import lrusso96.simple.biblio.Ebook;

import java.util.List;

public abstract class Provider {
    protected String name;

    public List<Ebook> search(String query){
        return null;
    }

}
