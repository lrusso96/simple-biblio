package lrusso96.simplebiblio.core;

import lrusso96.simplebiblio.exceptions.BiblioException;

import java.util.List;
import java.util.logging.Level;

public abstract class Provider {

    public static final String LIBGEN = "Library Genesis";
    public static final String FEEDBOOKS = "Feedbooks";
    public static final String STANDARD_EBOOKS = "Standard Ebooks";

    protected String name;
    private SimpleBiblio biblio;

    public Provider(String name, SimpleBiblio biblio) {
        this.name = name;
        this.biblio = biblio;
    }

    public List<Ebook> search(String query) throws BiblioException {
        int cnt = 0;
        while (true)
            try {
                return doSearch(query);
            } catch (BiblioException ex) {
                if (++cnt == biblio.getMaxTries())
                    throw ex;
            }
    }

    protected abstract List<Ebook> doSearch(String query) throws BiblioException;

    public List<Ebook> getRecent() throws BiblioException {
        int cnt = 0;
        while (true)
            try {
                return doGetRecent();
            } catch (BiblioException ex) {
                if (++cnt == biblio.getMaxTries())
                    throw ex;
            }
    }

    protected abstract List<Ebook> doGetRecent() throws BiblioException;

    public List<Ebook> getPopular() throws BiblioException {
        int cnt = 0;
        while (true)
            try {
                return doGetPopular();
            } catch (BiblioException ex) {
                if (++cnt == biblio.getMaxTries())
                    throw ex;
            }
    }

    protected abstract List<Ebook> doGetPopular() throws BiblioException;

    public String getName() {
        return name;
    }

    protected void log(Level level, String str) {
        if (biblio.logger != null)
            biblio.logger.log(level, str);
    }
}
