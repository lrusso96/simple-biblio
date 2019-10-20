package lrusso96.simplebiblio.core;

import lrusso96.simplebiblio.exceptions.BiblioException;
import net.jodah.failsafe.RetryPolicy;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Provider {
    protected String name;
    protected RetryPolicy<Object> retryPolicy;

    public Provider(String name, RetryPolicy<Object> retryPolicy) {
        this.name = name;
        if (retryPolicy != null)
            this.retryPolicy = retryPolicy;
        else
            this.retryPolicy = new RetryPolicy<>()
                    .handle(BiblioException.class)
                    .withDelay(Duration.ofSeconds(1))
                    .withMaxRetries(3);
    }

    public List<Ebook> search(String query) throws BiblioException {
        return new ArrayList<>();
    }

    public List<Ebook> getRecent() throws BiblioException {
        return new ArrayList<>();
    }

    public List<Ebook> getPopular() throws BiblioException {
        return new ArrayList<>();
    }

    public List<Download> loadDownloadURIs(Ebook book) throws BiblioException {
        return new ArrayList<>();
    }

    public String getName() {
        return name;
    }
}
