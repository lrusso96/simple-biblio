package lrusso96.simplebiblio.core;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.time.*;

public class Utils {

    private Utils() {
    }

    public static String bytesToReadableSize(int bytes) {
        int unit = 1000;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = Character.toString(("kMGTPE").charAt(exp - 1));
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static LocalDate parseUTC(String date) {
        Instant instant = Instant.parse(date);
        return LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId())).toLocalDate();
    }

    public static Download extractDownload(String dwn) {
        URI uri = URI.create(dwn);
        int i = dwn.lastIndexOf('.');
        String extension = "";
        if (i > 0)
            extension = dwn.substring(i + 1);
        Download download = new Download();
        download.setExtension(extension);
        download.setUri(uri);
        return download;
    }

    public static void extractOPDSLinks(Ebook ebook, Element element) {
        String coverKey = "http://opds-spec.org/image";
        String downloadKey = "http://opds-spec.org/acquisition";
        Elements links = element.getElementsByTag("link");
        for (Element link : links) {
            String rel = link.attr("rel");
            if (rel.equals(coverKey))
                ebook.setCover(URI.create(link.attr("href")));
            else if (rel.startsWith(downloadKey)) {
                String download = link.attr("href");
                ebook.setDownload(extractDownload(download));
            }
        }
    }
}