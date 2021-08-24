package id.android.pokeapp

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import id.android.pokeapp.di.DaggerAppComponent
import timber.log.Timber

class MainApp : DaggerApplication() {

    private val applicationInjector = DaggerAppComponent.builder().application(this).build()

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = applicationInjector

    override fun onCreate() {
        super.onCreate()
        if (id.android.pokeapp.BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}