package id.android.pokeapp.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import id.android.pokeapp.view.activity.DetailActivity
import id.android.pokeapp.view.activity.MainActivity
import id.android.pokeapp.view.activity.SplashScreenActivity

@Module(includes = [FragmentModule::class])
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashScreenActivity

    @ContributesAndroidInjector
    abstract fun contributeDetailActivity(): DetailActivity
}
