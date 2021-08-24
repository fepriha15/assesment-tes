package id.android.pokeapp.view.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import id.android.pokeapp.databinding.FragmentStatBinding
import id.android.pokeapp.model.Ability
import id.android.pokeapp.model.Pokemon
import id.android.pokeapp.util.showErrorMesage
import id.android.pokeapp.util.toCapitalize
import id.android.pokeapp.view.activity.DetailActivity
import id.android.pokeapp.viewmodel.PokeListViewModel
import id.android.pokeapp.viewmodel.Resource
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class StatFragment : BaseFragment<FragmentStatBinding>() {

    private lateinit var viewModel: PokeListViewModel
    private lateinit var binding: FragmentStatBinding
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentStatBinding
        get() = FragmentStatBinding::inflate

    override fun setupView(binding: FragmentStatBinding) = with(binding) {
        AndroidSupportInjection.inject(this@StatFragment)
        init(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.pokemonAbility.removeObserver(pokemonAbilityObserver)
    }

    private fun init(binding: FragmentStatBinding){
        this.binding = binding
        val parenActivity = activity as DetailActivity
        val pokemon = parenActivity.getPokemon()
        viewModel = (activity as DetailActivity).viewModel
        with(binding){
            tvFirstAb.text = toCapitalize(pokemon?.abilities?.get(0)?.ability?.name)
            tvSecondAb.text = toCapitalize(pokemon?.abilities?.get(1)?.ability?.name)
        }
        viewModel.pokemonAbility.observe(this, pokemonAbilityObserver)
        pokemon?.abilities.let {
            it?.get(0)?.ability?.id?.let { it1 -> viewModel.getAbility(it1) }
            it?.get(1)?.ability?.id?.let { it1 -> viewModel.getAbility(it1) }
        }
    }

    private val pokemonAbilityObserver = Observer<Resource<Ability>> { response ->
        when (response) {
            is Resource.Success -> {
                val data = response.data
                val abilityDesc = response.data?.flavorTextEntries?.find { it.versionGroup.name == "diamond-pearl" && it.language.name == "en" }?.flavorText
                abilityDesc.let {
                    if (data?.name.equals(binding.tvFirstAb.text.toString(), ignoreCase = true))
                        binding.tvFirstAbDes.text = it
                    else
                        binding.tvSecondAbDes.text = it
                }
            }
            is Resource.Error -> {
                response.message?.let { message ->
                    showErrorMesage(context, message)
                }
            }
            is Resource.Loading -> {
            }
        }
    }

}