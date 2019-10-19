package lrusso96.simplebiblio.core;

import lrusso96.simplebiblio.exceptions.BiblioException;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private List<Download> download = new ArrayList<>();
    private URI cover;
    private String source;  //not always a URI!
    private String md_hash;
    private Provider provider;

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

    public List<Download> getDownload() {
        try {
            if (download.isEmpty() && provider != null)
                this.download.addAll(provider.loadDownloadURIs(this));
        } catch (BiblioException e) {
            // log error
        }
        return download;
    }

    public void addDownload(Download dwn) {
        download.add(dwn);
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

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }
}
