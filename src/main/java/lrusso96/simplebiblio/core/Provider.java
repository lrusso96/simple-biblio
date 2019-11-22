package lrusso96.simplebiblio.core;

import lrusso96.simplebiblio.exceptions.BiblioException;
import net.jodah.failsafe.RetryPolicy;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Provider {

    public static final String LIBGEN = "Library Genesis";
    public static final String FEEDBOOKS = "Feedbooks";
    public static final String STANDARD_EBOOKS = "Standard Ebooks";

    protected String name;
    protected RetryPolicy<Object> retryPolicy;

    public Provider(String name, @NotNull RetryPolicy<Object> retryPolicy) {
        this.name = name;
        this.retryPolicy = retryPolicy;
    }

    public static RetryPolicy<Object> getRetryPolicy(SimplePolicy simplePolicy) {
        RetryPolicy<Object> retryPolicy = new RetryPolicy<>()
                .handle(BiblioException.class);
        if (simplePolicy.equals(SimplePolicy.NONE))
            return retryPolicy;
        else
            return retryPolicy
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

    public String getName() {
        return name;
    }
}
