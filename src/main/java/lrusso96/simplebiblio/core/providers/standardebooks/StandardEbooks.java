package lrusso96.simplebiblio.core.providers.standardebooks;

import lrusso96.simplebiblio.core.Ebook;
import lrusso96.simplebiblio.core.SimplePolicy;
import lrusso96.simplebiblio.core.Provider;
import lrusso96.simplebiblio.exceptions.BiblioException;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static lrusso96.simplebiblio.core.Utils.extractOPDSLinks;
import static lrusso96.simplebiblio.core.Utils.parseUTC;

public class StandardEbooks extends Provider {

    public StandardEbooks(@NotNull RetryPolicy<Object> retryPolicy) {
        super(STANDARD_EBOOKS, retryPolicy);
    }

    public StandardEbooks(SimplePolicy simplePolicy) {
        this(getRetryPolicy(simplePolicy));
    }

    @Override
    public List<Ebook> getRecent() throws BiblioException {
        return Failsafe.with(retryPolicy).get(() -> loadRecent(getRecentIds()));
    }

    private List<String> getRecentIds() throws BiblioException {
        List<String> ret = new ArrayList<>();
        try {
            String endpoint = "https://standardebooks.org/rss/new-releases";
            Document doc = Jsoup.connect(endpoint).get();
            Elements entries = doc.getElementsByTag("item");
            for (Element entry : entries)
                ret.add(entry.getElementsByTag("link").text());
            return ret;
        } catch (IOException e) {
            throw new BiblioException(e.getMessage());
        }
    }

    private List<Ebook> loadRecent(List<String> ids) throws BiblioException {
        List<Ebook> ret = new ArrayList<>();
        try {
            String endpoint = "https://standardebooks.org/opds/all";
            Document doc = Jsoup.connect(endpoint).get();
            Elements entries = doc.getElementsByTag("entry");
            List<Element> recent = entries.stream()
                    .filter(x -> ids.contains(x.getElementsByTag("id").text()))
                    .collect(Collectors.toList());
            for (int i = 0; i < recent.size(); i++) {
                Ebook ebook = parseBook(entries.get(i));
                ebook.setId(i);
                ret.add(ebook);
            }
            return ret;
        } catch (IOException e) {
            throw new BiblioException(e.getMessage());
        }
    }

    //TODO: parse language field
    private Ebook parseBook(Element entry) {
        Ebook book = new Ebook();
        book.setProviderName(this.name);
        book.setAuthor(entry.getElementsByTag("author").first().getElementsByTag("name").text());
        book.setTitle(entry.getElementsByTag("title").text());
        book.setSummary(entry.getElementsByTag("summary").text());
        book.setPublished(parseUTC(entry.getElementsByTag("published").text()));
        book.setUpdated(parseUTC(entry.getElementsByTag("updated").text()));
        extractOPDSLinks(book, entry);
        return book;
    }
}
