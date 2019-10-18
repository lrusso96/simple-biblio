package lrusso96.simplebiblio.core.providers.standardebooks;

import lrusso96.simplebiblio.core.Ebook;
import lrusso96.simplebiblio.core.Provider;
import lrusso96.simplebiblio.exceptions.BiblioException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StandardEbooks extends Provider {

    @Override
    public List<Ebook> getRecent() throws BiblioException {
        URI endpoint = URI.create("https://standardebooks.org/rss/new-releases");
        return getRecent(endpoint);
    }

    private List<Ebook> getRecent(URI endpoint) throws BiblioException {
        List<Ebook> ret = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(endpoint.toString()).get();

            Elements entries = doc.getElementsByTag("item");
            for (Element entry : entries)
                ret.add(parseBook(entry));
            return ret;
        } catch (IOException e) {
            throw new BiblioException(e.getMessage());
        }
    }

    private Ebook parseBook(Element entry) {
        Ebook book = new Ebook();
        book.setProvider(this);
        book.setTitle(entry.getElementsByTag("title").text());
        book.setSummary(entry.getElementsByTag("description").text());
        book.setPublished(parseUTC(entry.getElementsByTag("pubDate").text()));
        return book;
    }

    private LocalDate parseUTC(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("E, d MMM yyyy HH:mm:ss Z", Locale.US));
    }
}
