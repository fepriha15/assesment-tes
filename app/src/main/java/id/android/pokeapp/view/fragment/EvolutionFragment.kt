package id.android.pokeapp.view.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import id.android.pokeapp.databinding.FragmentEvolutionBinding

class EvolutionFragment : BaseFragment<FragmentEvolutionBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentEvolutionBinding
        get() = FragmentEvolutionBinding::inflate

    override fun setupView(binding: FragmentEvolutionBinding) {
        AndroidSupportInjection.inject(this)

    }

}