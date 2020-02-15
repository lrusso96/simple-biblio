package lrusso96.simplebiblio.core.providers.libgen

enum class Sorting(private val text: String) {
    DEFAULT("def"), DESCENDING("DESC"), ASCENDING("ASC");

    override fun toString() = text
}