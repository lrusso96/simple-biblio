package lrusso96.simplebiblio.core;

import java.net.URI;

public class Download {
    private URI uri;
    private String extension;

    Download(URI uri, String extension) {
        this.uri = uri;
        this.extension = extension;
    }

    public URI getUri() {
        return uri;
    }

    public String getExtension() {
        return extension;
    }
}
