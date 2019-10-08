package lrusso96.simplebiblio.core.providers.libgen;

public enum Sorting {
    DESCENDING("DESC"),
    ASCENDING("ASC");

    private final String text;

    Sorting(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}