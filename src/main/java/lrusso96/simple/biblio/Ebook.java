package lrusso96.simple.biblio;

import java.net.URI;
import java.time.LocalDate;

public class Ebook {
    private int id;
    private String author;
    private String title;
    private String summary;
    private LocalDate published;
    private LocalDate updated;
    private String language;
    private int pages;
    private int filesize;   //bytes
    private String extension;
    private URI cover;
    private URI download;
    private String source;  //not always a URI!
    private String hash;
}
