package lrusso96.simplebiblio.core.providers.libgen;

import lrusso96.simplebiblio.core.Ebook;
import lrusso96.simplebiblio.core.Provider;
import lrusso96.simplebiblio.exceptions.BiblioException;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static lrusso96.simplebiblio.core.Utils.parseYear;

public class LibraryGenesis extends Provider {

    private URI mirror;

    private final static String DEFAULT_COL = "def";
    private final static int DEFAULT_MAX_RESULTS = 25;

    private int maxResultsNumber = DEFAULT_MAX_RESULTS;
    private String sorting_mode = Sorting.DESCENDING.toString();
    private String sorting_field = Field.YEAR.toString();


    LibraryGenesis(URI mirror, int maxResultsNumber, Sorting mode, Field sorting) {
        this.name = "Library Genesis";
        if(mirror != null)
            this.mirror = mirror;
        else
        try {
            this.mirror = new URI("http://93.174.95.27");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if(maxResultsNumber > 0)
            this.maxResultsNumber = maxResultsNumber;
        if(mode != null)
            this.sorting_mode = mode.toString();
        if(sorting != null)
            this.sorting_field = sorting.toString();
    }

    @Override
    public List<Ebook> search(String query) throws BiblioException {
        List<String> ids = getIds(query, DEFAULT_COL);
        return search(ids);
    }

    @Override
    public List<Ebook> getRecent() throws BiblioException {
        return super.getRecent();
    }

    @Override
    public List<Ebook> getPopular() throws BiblioException {
        return super.getPopular();
    }

    private List<String> getIds(String stuff, String column) throws BiblioException {
        int page = 1;
        //reduce number of pages requested
        int results = 25;
        if (maxResultsNumber > 25)
            results = 50;
        if (maxResultsNumber > 50)
            results = 100;

        List<String> ids = getIds(stuff, column, page, results);
        while (ids.size() < maxResultsNumber) {
            page++;
            List<String> new_ids = getIds(stuff, column, page, results);
            if (new_ids.isEmpty())
                break;
            ids.addAll(new_ids);
        }
        if (maxResultsNumber < ids.size())
            return ids.stream().limit(maxResultsNumber).collect(Collectors.toList());
        return ids;
    }

    private List<String> getIds(String stuff, String column, int page, int results) throws BiblioException {
        try {
            List<String> list = new ArrayList<>();
            Document doc = Jsoup.connect(mirror + "/search.php")
                    .data("req", stuff)
                    .data("column", column)
                    .data("res", Integer.toString(results))
                    .data("sort", sorting_field)
                    .data("sortmode", sorting_mode)
                    .data("page", Integer.toString(page))
                    .get();
            Elements rows = doc.getElementsByTag("tr");
            for (Element row : rows) {
                String id = row.child(0).text();
                if (StringUtils.isNumeric(id))
                    list.add(id);
            }
            return list;
        } catch (IOException e) {
            throw new BiblioException("");
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
            list.sort((b1, b2) ->
            {
                if (sorting_field.equals(DateFormat.Field.YEAR + "")) {
                    if ("ASC".equals(sorting_mode))
                        return b1.getPublished().compareTo(b2.getPublished());
                    return b2.getPublished().compareTo(b1.getPublished());
                }
                if (sorting_field.equals(Field.TITLE + "")) {
                    if ("ASC".equals(sorting_mode))
                        return b1.getTitle().compareTo(b2.getTitle());
                    return b2.getTitle().compareTo(b1.getTitle());
                }
                //fixme: dead code (never reached)
                return b1.getTitle().compareTo(b2.getTitle());
            });
            return list;
        } catch (IOException | JSONException | StringIndexOutOfBoundsException e) {
            throw new BiblioException("");
        }
    }

    private String searchRequest(List<String> ids) throws BiblioException, IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        String fields = "Author,Title,MD5,Year,Pages,Language,Filesize,Extension,CoverURL";
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

    private String encodeIds(List<String> ids) {
        StringBuilder ids_comma = new StringBuilder();
        for (String id : ids)
            ids_comma.append(",").append(id);
        ids_comma.replace(0, 1, "");
        return ids_comma.toString();
    }

    private Ebook parseBook(JSONObject object) {
        Ebook book = new Ebook();
        book.setAuthor(object.getString(Field.AUTHOR + ""));
        book.setTitle(object.getString(Field.TITLE + ""));
        book.setMd_hash(object.getString("md5"));
        String o = object.getString(Field.YEAR + "");
        if (NumberUtils.isParsable(o))
            book.setPublished(parseYear(o));
        o = object.getString("pages");
        if (NumberUtils.isParsable(o))
            book.setPages(Integer.parseInt(o));
        book.setLanguage(object.getString("language"));
        o = object.getString("filesize");
        if (NumberUtils.isParsable(o))
            book.setFilesize(Integer.parseInt(o));
        book.setExtension(object.getString("extension"));
        book.setCover(getCoverUri(mirror, object.getString("coverurl")));
        return book;
    }

    public void loadDownloadURI(Ebook book) throws BiblioException {
        if (book.getDownload() != null)
            return;
        try {
            Document doc = Jsoup.connect("http://93.174.95.29/_ads/" + book.getMd_hash()).get();
            Elements anchors = doc.getElementsByTag("a");
            for (Element anchor : anchors) {
                if (anchor.text().equalsIgnoreCase("get")) {
                    book.setDownload(new URI(anchor.attr("href")));
                    return;
                }
            }
        } catch (IOException e) {
            throw new BiblioException("");
        } catch (URISyntaxException e) {
        }
        throw new BiblioException("no download uri available");
    }

    private URI getCoverUri(URI uri, String cover) {
        if (cover.isEmpty())
            return null;
        try {
            if (cover.startsWith("http"))
                return new URI(cover);
            return new URI(uri.toString() + "/covers/" + cover);
        } catch (URISyntaxException e) {
            return null;
        }
    }
}