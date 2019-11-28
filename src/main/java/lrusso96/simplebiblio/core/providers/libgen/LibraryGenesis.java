package lrusso96.simplebiblio.core.providers.libgen;

import lrusso96.simplebiblio.core.Download;
import lrusso96.simplebiblio.core.Ebook;
import lrusso96.simplebiblio.core.Provider;
import lrusso96.simplebiblio.exceptions.BiblioException;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.threeten.bp.LocalDate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static lrusso96.simplebiblio.core.Utils.extractDownload;


public class LibraryGenesis extends Provider {
    private final static String DEFAULT_COL = "def";
    private final static int DEFAULT_MAX_RESULTS = 25;
    private URI mirror;
    private URI download_mirror;
    private int max_results = DEFAULT_MAX_RESULTS;
    private String sorting_mode = DEFAULT_COL;
    private String sorting_field = DEFAULT_COL;

    LibraryGenesis(LibraryGenesisBuilder builder) {
        super(LIBGEN, builder.biblio);
        try {
            this.mirror = new URI("http://93.174.95.27");
            this.download_mirror = new URI("http://93.174.95.29");
        } catch (URISyntaxException e) {
            log(Level.SEVERE, e.getMessage());
        }
        if (builder.mirror != null)
            this.mirror = builder.mirror;
        if (builder.download_mirror!=null)
            this.download_mirror = builder.download_mirror;

        if (builder.maxResultsNumber > 0)
            this.max_results = builder.maxResultsNumber;

        if (builder.mode != null)
            this.sorting_mode = builder.mode.toString();

        if (builder.sorting != null)
            this.sorting_field = builder.sorting.toString();
    }

    public static List<Download> loadDownloadURIs(Ebook book) throws BiblioException {
        List<Download> ret = new ArrayList<>();
        try {
            String uri = String.format("%s/_ads/%s", book.getDownloadMirror().toString(), book.getMd_hash());
            Document doc = Jsoup.connect(uri).get();
            Elements anchors = doc.getElementsByTag("a");
            for (Element anchor : anchors) {
                if (anchor.text().equalsIgnoreCase("get"))
                    ret.add(extractDownload(book.getDownloadMirror() + anchor.attr("href")));
            }
        } catch (IOException e) {
            throw new BiblioException(e.getMessage());
        }
        return ret;
    }

    @Override
    public List<Ebook> doSearch(String query) throws BiblioException {
        //TODO: do not catch and retry maxTries times!
        if (StringUtils.isEmpty(query) || query.length() < 5)
            throw new BiblioException("Insert at least 5 chars");
        return search(getIds(query));
    }

    @Override
    public List<Ebook> doGetRecent() throws BiblioException {
        return search(getIds(null));
    }

    @Override
    protected List<Ebook> doGetPopular() {
        return new ArrayList<>();
    }

    private List<String> getIds(String query) throws BiblioException {
        int page = 1;
        //reduce number of pages requested
        int results = 25;
        if (max_results > 25)
            results = 50;
        if (max_results > 50)
            results = 100;

        List<String> ids = getIds(query, page, results);
        while (ids.size() < max_results) {
            page++;
            List<String> new_ids = getIds(query, page, results);
            if (new_ids.isEmpty())
                break;
            ids.addAll(new_ids);
        }
        if (max_results < ids.size())
            return ids.stream().limit(max_results).collect(Collectors.toList());
        return ids;
    }

    private Document getRecentDoc(int page) throws IOException {
        return Jsoup.connect(mirror + "/search.php")
                .data("mode", "last")
                .data("page", Integer.toString(page))
                .get();
    }

    private Document getSearchDoc(String query, int page, int results) throws IOException {
        return Jsoup.connect(mirror + "/search.php")
                .data("req", query)
                .data("column", DEFAULT_COL)
                .data("res", Integer.toString(results))
                .data("sort", sorting_field)
                .data("sortmode", sorting_mode)
                .data("page", Integer.toString(page))
                .get();
    }

    private List<String> getIds(String query, int page, int results) throws BiblioException {
        try {
            List<String> list = new ArrayList<>();
            Document doc;
            if (query != null)
                doc = getSearchDoc(query, page, results);
            else
                doc = getRecentDoc(page);
            Elements rows = doc.getElementsByTag("tr");
            for (Element row : rows) {
                String id = row.child(0).text();
                if (StringUtils.isNumeric(id))
                    list.add(id);
            }
            return list;
        } catch (IOException e) {
            throw new BiblioException(e.getMessage());
        }
    }

    private List<Ebook> search(List<String> ids) throws BiblioException {
        if (ids.isEmpty())
            throw new BiblioException("No result: try a new query");
        List<Ebook> list = new ArrayList<>();

        try {
            String body = searchRequest(ids);
            JSONArray response = new JSONArray(body);
            for (int i = 0; i < response.length(); i++) {
                JSONObject bookObject = response.getJSONObject(i);
                Ebook book = parseBook(bookObject);
                book.setId(Integer.parseInt(ids.get(i)));
                list.add(book);
            }
            sortList(list);
            return list;
        } catch (IOException | JSONException | StringIndexOutOfBoundsException e) {
            throw new BiblioException(e.getMessage());
        }
    }

    private void sortList(List<Ebook> list) {
        list.sort((b1, b2) ->
        {
            if (sorting_field.equals(DateFormat.Field.YEAR.toString())) {
                if ("ASC".equals(sorting_mode))
                    return b1.getPublished().compareTo(b2.getPublished());
                return b2.getPublished().compareTo(b1.getPublished());
            } else if (sorting_field.equals(Field.TITLE.toString())) {
                if ("ASC".equals(sorting_mode))
                    return b1.getTitle().compareTo(b2.getTitle());
                return b2.getTitle().compareTo(b1.getTitle());
            } else if (sorting_field.equals(Field.AUTHOR.toString())) {
                if ("ASC".equals(sorting_mode))
                    return b1.getAuthor().compareTo(b2.getAuthor());
                return b2.getAuthor().compareTo(b1.getAuthor());
            } else
                log(Level.INFO, "default sorting applied");
            return b1.getTitle().compareTo(b2.getTitle());
        });
    }

    private String searchRequest(List<String> ids) throws BiblioException, IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        String fields = "Author,Title,MD5,Year,Pages,Language,Filesize,CoverURL";
        RequestBody formBody = new FormBody.Builder()
                .add("ids", encodeIds(ids))
                .add("fields", fields)
                .build();
        Request req = new Request.Builder()
                .url(mirror + "/json.php")
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody)
                .build();
        Response resp = client.newCall(req).execute();
        if (resp.body() == null)
            throw new BiblioException("Invalid response");
        String body = Objects.requireNonNull(resp.body()).string();
        return body.substring(body.indexOf('['), body.lastIndexOf(']') + 1);
    }

    @NotNull
    private String encodeIds(List<String> ids) {
        StringBuilder ids_comma = new StringBuilder();
        for (String id : ids)
            ids_comma.append(",").append(id);
        ids_comma.replace(0, 1, "");
        return ids_comma.toString();
    }

    @NotNull
    private Ebook parseBook(JSONObject object) {
        Ebook book = new Ebook();
        book.setProviderName(this.name);
        book.setAuthor(object.getString(Field.AUTHOR.toString()));
        book.setTitle(object.getString(Field.TITLE.toString()));
        book.setMd_hash(object.getString("md5"));
        String o = object.getString(Field.YEAR + "");
        book.setPublished(parseYear(o));
        o = object.getString("pages");
        if (NumberUtils.isDigits(o))
            book.setPages(Integer.parseInt(o));
        else
            log(Level.WARNING, String.format("error while parsing pages: %s", o));
        book.setLanguage(object.getString("language"));
        o = object.getString("filesize");
        if (NumberUtils.isParsable(o))
            book.setFilesize(Integer.parseInt(o));
        else
            log(Level.WARNING, String.format("error while parsing filesize: %s", o));
        book.setCover(getCoverUri(mirror, object.getString("coverurl")));
        book.setSource(this.name);
        book.setDownloadMirror(this.download_mirror);
        return book;
    }

    @Nullable
    private URI getCoverUri(URI uri, String cover) {
        if (cover.isEmpty()) {
            log(Level.WARNING, "no cover available");
            return null;
        }
        try {
            if (cover.startsWith("http"))
                return new URI(cover);
            return new URI(uri.toString() + "/covers/" + cover);
        } catch (URISyntaxException e) {
            log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    @Nullable
    private LocalDate parseYear(String year) {
        if (NumberUtils.isDigits(year))
            return LocalDate.of(Integer.parseInt(year), 1, 1);
        log(Level.WARNING, String.format("unexpected year format: %s", year));
        return null;
    }
}
