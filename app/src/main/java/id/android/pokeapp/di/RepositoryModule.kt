package id.android.pokeapp.di

import dagger.Module
import dagger.Provides
import id.android.pokeapp.data.api.PokeServices
import id.android.pokeapp.repository.PokeRepository
import id.android.pokeapp.repository.PokeRepositoryImpl

@Module
class RepositoryModule {

    @Provides
    fun providePokeListRepor(services: PokeServices): PokeRepository = PokeRepositoryImpl(services)
}