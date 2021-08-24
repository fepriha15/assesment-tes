package id.android.pokeapp.di

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import id.android.pokeapp.BuildConfig.BASE_URL
import id.android.pokeapp.data.api.PokeServices
import id.android.pokeapp.model.ApiResource
import id.android.pokeapp.model.NamedApiResource
import id.android.pokeapp.util.ApiResourceAdapter
import id.android.pokeapp.util.NamedApiResourceAdapter
import id.android.pokeapp.util.hasInternetConnection
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {
    private val cacheSize = (5 * 1024 * 1024).toLong()

    @Provides
    fun provideAPI(retrofit: Retrofit): PokeServices = retrofit.create(PokeServices::class.java)

    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).
        addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
        addConverterFactory(GsonConverterFactory.create(GsonBuilder().apply {
            setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            registerTypeAdapter(
                TypeToken.get(ApiResource::class.java).type,
                ApiResourceAdapter()
            )
            registerTypeAdapter(
                TypeToken.get(NamedApiResource::class.java).type,
                NamedApiResourceAdapter()
            )
        }.create())).
        client(okHttpClient).build()


    @Provides
    fun providesOkHttpClient(context: Context): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val myCache = Cache(context.cacheDir, cacheSize)
        return OkHttpClient.Builder().addInterceptor(logging).addNetworkInterceptor(logging).addInterceptor { chain ->
                var request = chain.request()
                request = if (hasInternetConnection(context)) request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                else request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                chain.proceed(request)
            }.cache(myCache).build()
    }
}