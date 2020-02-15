package lrusso96.simplebiblio.core.providers.feedbooks

enum class Language(private val code: String) {
    ENGLISH("en"), ITALIAN("it"), SPANISH("es"), FRENCH("fr"), DEUTSCH("de");

    override fun toString(): String {
        return code
    }

}