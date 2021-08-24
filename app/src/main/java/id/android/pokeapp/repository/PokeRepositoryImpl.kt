package id.android.pokeapp.repository

import id.android.pokeapp.data.api.PokeServices
import id.android.pokeapp.model.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*

class PokeRepositoryImpl(private val services: PokeServices) : PokeRepository {

    override fun getPokemonList(offset: Int, limit: Int): NamedApiResourceList = services.getPokemonList(offset, limit)

    override fun getPokemonFormList(offset: Int, limit: Int): Single<List<NamedApiResource>> {
        return services.getPokemonFormList(offset, limit).
        flattenAsObservable { it.results }.map {
            val event = it
            event
        }.toList()
    }

    override fun getPokemonForm(id: Int): Single<PokemonForm> = services.getPokemonForm(id)

    override fun getPokemonListDetail(offset: Int, limit: Int): Single<List<Pokemon>> {
        return services.getPokemonFormList(offset,limit).
        flattenAsObservable { it.results }.
        map { services.getPokemonSingle(it.id) }.
        map { it.blockingGet()}.
        toList()
    }

    override fun getPokemonSingle(id: Int): Single<Pokemon> = services.getPokemonSingle(id)
    override fun getPokemon(id: Int): Pokemon = services.getPokemon(id)
    override fun getSpecies(id: Int): Single<PokemonSpecies> = services.getPokemonSpecies(id)
    override fun getAbility(id: Int): Single<Ability> = services.getAbility(id)

    override fun getStat(id: Int): Single<Stat> {
        TODO("Not yet implemented")
    }

}