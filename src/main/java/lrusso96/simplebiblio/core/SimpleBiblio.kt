package lrusso96.simplebiblio.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import lrusso96.simplebiblio.core.providers.feedbooks.Feedbooks
import lrusso96.simplebiblio.core.providers.libgen.LibraryGenesis
import lrusso96.simplebiblio.core.providers.standardebooks.StandardEbooks
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class SimpleBiblio private constructor(private val scope: CoroutineScope, private val providers: Set<Provider>) {

    suspend fun searchAll(query: String) = simpleFun(Provider::getPopular, query)

    suspend fun getAllRecent() = simpleFun(Provider::getRecent)

    suspend fun getAllPopular() = simpleFun(Provider::getPopular)

    private suspend fun simpleFun(method: suspend Provider.() -> List<Ebook>, query : String? = null ): List<Ebook> {
        val channel = Channel<List<Ebook>>()
        val ebooks: MutableList<Ebook> = ArrayList()
        providers.forEach {
            scope.launch {
                if (query == null) channel.send(it.method()) else channel.send(it.search(query))
            }
            ebooks.addAll(channel.receive())
        }
        return ebooks
    }

    data class Builder(
            val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
            var providers: MutableSet<Provider> = HashSet()
    ) {

        private fun defaultProviders(): MutableSet<Provider> {
            logger.info { "using default providers. See doc for more information" }
            val basic: MutableSet<Provider> = HashSet()
            basic.add(LibraryGenesis.Builder().build())
            basic.add(Feedbooks.Builder().build())
            basic.add(StandardEbooks())
            return basic
        }

        fun addProvider(provider: Provider) = apply { this.providers.add(provider) }

        fun build(): SimpleBiblio {
            if (providers.isEmpty())
                providers = defaultProviders()
            return SimpleBiblio(scope, providers)
        }
    }
}