package lrusso96.simplebiblio.core;

import lrusso96.simplebiblio.exceptions.BiblioException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Provider {
    protected String name;

    public List<Ebook> search(String query) throws BiblioException {
        return new ArrayList<>();
    }

    public List<Ebook> getRecent() throws BiblioException {
        return new ArrayList<>();
    }

    public List<Ebook> getPopular() throws BiblioException {
        return new ArrayList<>();
    }

    public URI loadDownloadURI(Ebook book) throws BiblioException {
        return null;
    }

    public String getName() {
        return name;
    }
}
