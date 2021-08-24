package id.android.pokeapp.data.api

import id.android.pokeapp.model.*
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeServices {
    @GET("pokemon/")
    fun getPokemonList(@Query("offset") offset: Int, @Query("limit") limit: Int): NamedApiResourceList

    @GET("pokemon-form/")
    fun getPokemonFormList(@Query("offset") offset: Int, @Query("limit") limit: Int): Single<NamedApiResourceList>

    @GET("pokemon-form/{id}/")
    fun getPokemonForm(@Path("id") id: Int): Single<PokemonForm>

    @GET("pokemon/{id}/")
    fun getPokemon(@Path("id") id: Int): Single<Pokemon>

    @GET("ability/{id}/")
    fun getAbility(@Path("id") id: Int): Single<Ability>

    @GET("pokemon-species/{id}/")
    fun getPokemonSpecies(@Path("id") id: Int): Single<PokemonSpecies>

}