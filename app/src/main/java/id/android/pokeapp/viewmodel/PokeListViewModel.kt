package id.android.pokeapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.android.pokeapp.MainApp
import id.android.pokeapp.R
import id.android.pokeapp.di.SCHEDULER_MAIN_THREAD
import id.android.pokeapp.model.Ability
import id.android.pokeapp.model.NamedApiResource
import id.android.pokeapp.model.Pokemon
import id.android.pokeapp.model.PokemonSpecies
import id.android.pokeapp.repository.PokeRepository
import id.android.pokeapp.util.hasInternetConnection
import id.android.pokeapp.util.isInternetAvailable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named


class PokeListViewModel @Inject constructor(private val app: Application, private val repo: PokeRepository,
                                            @Named(SCHEDULER_MAIN_THREAD) val mainScheduler: Scheduler) : AndroidViewModel(app) {

    val pokeList : MutableLiveData<Resource<List<NamedApiResource>>> = MutableLiveData()
    val pokemonDetail : MutableLiveData<Resource<Pokemon>> = MutableLiveData()
    val pokemonAbility : MutableLiveData<Resource<Ability>> = MutableLiveData()
    val pokemonSpecies : MutableLiveData<Resource<PokemonSpecies>> = MutableLiveData()
    private val disposables = CompositeDisposable()

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    fun getPokemonFormList(offset: Int, limit: Int) {
        pokeList.postValue(Resource.Loading())
        try {
            if(hasInternetConnection(getApplication<MainApp>())) {
                disposables.add(repo.getPokemonFormList(offset, limit).
                subscribeOn(Schedulers.io()).
                observeOn(mainScheduler).
                subscribe({ value -> pokeList.postValue(Resource.Success(value)) },
                          { error ->  pokeList.postValue(Resource.Error(error.localizedMessage ?: "")) }))
            } else {
                pokeList.postValue(Resource.Error(app.getString(R.string.no_inet_con)))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> pokeList.postValue(Resource.Error("Network Failure"))
                else -> pokeList.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun getPokemon(id: Int) {
        pokemonDetail.postValue(Resource.Loading())
        try {
            if(hasInternetConnection(getApplication<MainApp>())) {
                disposables.add(repo.getPokemon(id).
                subscribeOn(Schedulers.io()).
                observeOn(mainScheduler).
                subscribe({ value -> pokemonDetail.postValue(Resource.Success(value)) },
                          { error -> pokemonDetail.postValue(Resource.Error(error.localizedMessage ?: "")) }))
            } else {
                pokemonDetail.postValue(Resource.Error(app.getString(R.string.no_inet_con)))
            }
            } catch(t: Throwable) {
                when(t) {
                    is IOException -> pokemonDetail.postValue(Resource.Error("Network Failure"))
                    else -> pokemonDetail.postValue(Resource.Error("Conversion Error"))
                }
            }
    }

    fun getPokemonSpecies(id: Int) {
        pokemonSpecies.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(getApplication<MainApp>())) {
                disposables.add(repo.getSpecies(id).
                subscribeOn(Schedulers.io()).
                observeOn(mainScheduler).
                subscribe({ value -> pokemonSpecies.postValue(Resource.Success(value)) },
                          { error -> pokemonSpecies.postValue(Resource.Error(error.localizedMessage ?: "")) }))
            } else {
                pokemonSpecies.postValue(Resource.Error(app.getString(R.string.no_inet_con)))
            }
    } catch(t: Throwable) {
        when(t) {
            is IOException -> pokemonSpecies.postValue(Resource.Error("Network Failure"))
            else -> pokemonSpecies.postValue(Resource.Error("Conversion Error"))
        }
    }
    }

    fun getAbility(id: Int) {
        pokemonAbility.postValue(Resource.Loading())
        try {
            if(hasInternetConnection(getApplication<MainApp>())) {
                disposables.add(repo.getAbility(id).
                subscribeOn(Schedulers.io()).
                observeOn(mainScheduler).
                subscribe({ value -> pokemonAbility.postValue(Resource.Success(value)) },
                          { error -> pokemonAbility.postValue(Resource.Error(error.localizedMessage ?: "")) }))
            } else {
                pokemonAbility.postValue(Resource.Error(app.getString(R.string.no_inet_con)))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> pokemonAbility.postValue(Resource.Error("Network Failure"))
                else -> pokemonAbility.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
}
