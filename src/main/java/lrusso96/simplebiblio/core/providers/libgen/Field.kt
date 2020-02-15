package lrusso96.simplebiblio.core.providers.libgen

enum class Field(private val text: String) {
    DEFAULT("def"), AUTHOR("author"), TITLE("title"), YEAR("year");

    override fun toString() = text
}