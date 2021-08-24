package id.android.pokeapp.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import id.android.pokeapp.MainApp
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, NetworkModule::class, RepositoryModule::class])

interface AppComponent : AndroidInjector<MainApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(mainApp: MainApp): Builder

        fun build(): AppComponent
    }

    override fun inject(instance: MainApp)
}