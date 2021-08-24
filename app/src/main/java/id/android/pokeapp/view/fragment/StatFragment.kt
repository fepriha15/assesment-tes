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
import id.android.pokeapp.view.activity.DetailActivity
import id.android.pokeapp.viewmodel.PokeListViewModel
import id.android.pokeapp.viewmodel.Resource
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
            tvFirstAb.text = pokemon?.abilities?.get(0)?.ability?.name
            tvSecondAb.text = pokemon?.abilities?.get(1)?.ability?.name
        }
        observerViewModel()
    }

    private fun observerViewModel() {
        viewModel.pokemonAbility.observe(this, pokemonAbilityObserver)
    }

    private val pokemonAbilityObserver = Observer<Resource<Ability>> { response ->
        when (response) {
            is Resource.Success -> {
                val data = response.data

                val abilityDesc = response.data?.flavorTextEntries?.find { it.versionGroup.name == "diamond-pearl" && it.language.name == "end" }?.flavorText
                abilityDesc.let {
                    if (data?.name == binding.tvFirstAb.text)
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