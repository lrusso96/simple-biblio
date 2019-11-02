package lrusso96.simplebiblio.core;

import java.net.URI;

public class Download {
    private URI uri;
    private String extension;

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public URI getUri() {
        return uri;
    }

    public String getExtension() {
        return extension;
    }
}
