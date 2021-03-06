package id.android.pokeapp.repository

import id.android.pokeapp.model.*
import io.reactivex.Single

interface PokeRepository {

    fun getPokemonList(offset: Int, limit: Int): NamedApiResourceList

    fun getPokemonSingle(id: Int): Single<Pokemon>

    fun getPokemon(id: Int): Pokemon

    fun getPokemonListDetail(offset: Int, limit: Int): Single<List<Pokemon>>

    fun getSpecies(id: Int): Single<PokemonSpecies>

    fun getPokemonFormList(offset: Int, limit: Int): Single<List<NamedApiResource>>

    fun getPokemonForm(id: Int): Single<PokemonForm>

    fun getStat(id: Int): Single<Stat>

    fun getAbility(id: Int): Single<Ability>
}