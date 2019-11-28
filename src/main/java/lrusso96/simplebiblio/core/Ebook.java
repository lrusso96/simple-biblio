package lrusso96.simplebiblio.core;

import lrusso96.simplebiblio.core.providers.libgen.LibraryGenesis;
import lrusso96.simplebiblio.exceptions.BiblioException;
import org.threeten.bp.LocalDate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static lrusso96.simplebiblio.core.Provider.LIBGEN;

public class Ebook {
    private int id;
    private String author;
    private String title;
    private String summary;
    private LocalDate published;
    private LocalDate updated;
    private String language;
    private int pages;
    private int filesize; //bytes
    private List<Download> downloads = new ArrayList<>();
    private URI cover;
    private String source;  //not always a URI!
    private String md_hash;
    //todo: fix ugly semantics
    private String provider_name;
    private URI download_mirror;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public LocalDate getPublished() {
        return published;
    }

    public void setPublished(LocalDate published) {
        this.published = published;
    }

    public LocalDate getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDate updated) {
        this.updated = updated;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public URI getCover() {
        return cover;
    }

    public void setCover(URI cover) {
        this.cover = cover;
    }

    public List<Download> getDownloads() {
        try {
            if (downloads.isEmpty() && provider_name.equals(LIBGEN))
                this.downloads = LibraryGenesis.loadDownloadURIs(this);
        } catch (BiblioException e) {
            // log error
        }
        return downloads;
    }

    public void addDownload(Download dwn) {
        downloads.add(dwn);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMd_hash() {
        return md_hash;
    }

    public void setMd_hash(String md_hash) {
        this.md_hash = md_hash;
    }

    public String getProviderName() {
        return provider_name;
    }

    public void setProviderName(String provider_name) {
        this.provider_name = provider_name;
    }

    public URI getDownloadMirror() {
        return download_mirror;
    }

    public void setDownloadMirror(URI download_mirror) {
        this.download_mirror = download_mirror;
    }
}
