package lrusso96.simplebiblio.core.providers.feedbooks;

import lrusso96.simplebiblio.core.Ebook;
import lrusso96.simplebiblio.core.Provider;
import lrusso96.simplebiblio.exceptions.BiblioException;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import static lrusso96.simplebiblio.core.Utils.extractOPDSLinks;
import static lrusso96.simplebiblio.core.Utils.parseUTC;

public class Feedbooks extends Provider {

    private Set<String> languages;

    Feedbooks(Set<String> languages, RetryPolicy<Object> retryPolicy) {
        super("Feedbooks", retryPolicy);
        if (languages == null || languages.isEmpty())
            setDefaultLanguages();
        else
            this.languages = languages;
    }

    private void setDefaultLanguages() {
        String[] default_lang = {"en", "it"};
        languages = new HashSet<>(Arrays.asList(default_lang));
    }

    @Override
    public List<Ebook> search(String query) {
        URI endpoint = URI.create("https://feedbooks.com/books/search.atom");
        return Failsafe.with(retryPolicy).get(() -> _search(endpoint, query));
    }

    @Override
    public List<Ebook> getRecent() {
        URI endpoint = URI.create("https://feedbooks.com/books/recent.atom");
        return Failsafe.with(retryPolicy).get(() -> _search(endpoint, null));
    }

    @Override
    public List<Ebook> getPopular() {
        URI endpoint = URI.create("https://feedbooks.com/books/top.atom");
        return Failsafe.with(retryPolicy).get(() -> _search(endpoint, null));
    }

    private List<Ebook> _search(URI endpoint, String query) throws BiblioException {
        List<Ebook> ret = new ArrayList<>();
        try {
            Connection connection = Jsoup.connect(endpoint.toString());
            if (query != null)
                connection = connection.data("query", query);
            //todo: use a maxResults variable
            int cnt = 50;
            for (String language : languages) {
                int totalResults = Integer.MAX_VALUE;
                for (int page = 1; ret.size() < totalResults; page++) {
                    Document doc = connection.data("lang", language).data("page", Integer.toString(page)).get();
                    totalResults = NumberUtils.toInt(doc.getElementsByTag("opensearch:totalResults").text());

                    Elements entries = doc.getElementsByTag("entry");
                    for (Element entry : entries) {
                        if (cnt-- == 0)
                            return ret;

                        ret.add(parseBook(entry));
                    }
                }
            }
            return ret;
        } catch (IOException e) {
            throw new BiblioException(e.getMessage());
        }
    }

    private Ebook parseBook(Element entry) {
        Ebook book = new Ebook();
        book.setProvider(this);
        String id = entry.getElementsByTag("id").text();
        book.setId(parseID(id));
        book.setTitle(entry.getElementsByTag("title").text());
        book.setSummary(entry.getElementsByTag("summary").text());
        book.setPublished(parseUTC(entry.getElementsByTag("published").text()));
        book.setUpdated(parseUTC(entry.getElementsByTag("updated").text()));
        book.setLanguage(entry.getElementsByTag("dcterms:language").text());
        book.setSource(entry.getElementsByTag("dcterms:source").text());
        book.setAuthor(entry.getElementsByTag("name").text());
        extractOPDSLinks(book, entry);
        return book;
    }

    private int parseID(String string) {
        String[] ids = string.split("/");
        if (ids.length == 0)
            return 0;
        return NumberUtils.toInt(ids[ids.length - 1], 0);
    }
}