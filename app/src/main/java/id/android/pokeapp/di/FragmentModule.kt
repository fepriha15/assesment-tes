package id.android.pokeapp.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import id.android.pokeapp.view.fragment.StatFragment
import id.android.pokeapp.view.fragment.EvolutionFragment

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeMainFragment(): EvolutionFragment

    @ContributesAndroidInjector
    internal abstract fun contributeDetailFragment(): StatFragment
}
