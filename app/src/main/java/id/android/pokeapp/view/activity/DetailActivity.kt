package id.android.pokeapp.view.activity

import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import id.android.pokeapp.databinding.ActivityDetailBinding
import id.android.pokeapp.di.GlideApp
import id.android.pokeapp.model.Pokemon
import id.android.pokeapp.model.PokemonSpecies
import id.android.pokeapp.util.showErrorMesage
import id.android.pokeapp.view.adapter.DetailViewPager
import id.android.pokeapp.viewmodel.PokeListViewModel
import id.android.pokeapp.viewmodel.Resource
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class DetailActivity : BaseActivity<ActivityDetailBinding>() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: PokeListViewModel
    private var pokemon : Pokemon? = null

    companion object {
        const val DETAIL = "DETAIL"
    }

    override val bindingInflater: (LayoutInflater) -> ActivityDetailBinding
        get() = ActivityDetailBinding::inflate

    override fun setupView(binding: ActivityDetailBinding) {
        AndroidInjection.inject(this)
        init()
    }

    fun getPokemon(): Pokemon?{
        return pokemon
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.pokemonSpecies.removeObserver(pokeSpeciesObserve)
        viewModel.pokemonDetail.removeObserver(pokeDetailObserve)
    }

    private fun init() {
        pokemon = intent.getSerializableExtra(DETAIL) as Pokemon
        viewModel = ViewModelProvider(this, viewModelFactory).get(PokeListViewModel::class.java)
        observerViewModel()
        setupPager()
//        viewModel.getPokemon(pokeId)
        pokemonInit()
        viewModel.getPokemonSpecies(pokemon?.id ?: 0)
    }

    private fun pokemonInit(){
        val pokeImage = pokemon?.sprites?.other?.officialArtwork?.frontDefault
        GlideApp.with(this).load(pokeImage)
            .centerCrop().into(binding.imgDetailPoke)
        binding.tvDtlPokeName.text = pokemon?.name?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    private fun observerViewModel() {
        viewModel.pokemonSpecies.observe(this, pokeSpeciesObserve)
        viewModel.pokemonDetail.observe(this, pokeDetailObserve)
    }

    private fun setupPager() {
        val viewPagerAdapter = DetailViewPager(this, supportFragmentManager)
        binding.viewPager.adapter = viewPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    private val pokeDetailObserve = Observer<Resource<Pokemon>> { response ->
        when (response) {
            is Resource.Success -> {
                val data = response.data
                pokemon = data
            }
            is Resource.Error -> {
                response.message?.let { message ->
                    showErrorMesage(this, message)
                }
            }
            is Resource.Loading -> {
            }
        }
    }

    private val pokeSpeciesObserve = Observer<Resource<PokemonSpecies>> { response ->
        when (response) {
            is Resource.Success -> {
                val description = response.data?.flavorTextEntries?.find { it.version.name == "alpha-sapphire" && it.language.name == "en" }?.flavorText
                binding.tvDtlDes.text = description?.replace("\n", "")
            }
            is Resource.Error -> {
                response.message?.let { message ->
                    showErrorMesage(this, message)
                }
            }
            is Resource.Loading -> {
            }
        }
    }
}