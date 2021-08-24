package id.android.pokeapp.viewmodel

import id.android.pokeapp.model.NamedApiResource
import id.android.pokeapp.model.Pokemon

sealed class PokeListState {
    abstract val data: List<NamedApiResource>
    abstract val dataForm: Pokemon?
    abstract val loadedAllItems: Boolean
}

data class DefaultState(
    override val data: List<NamedApiResource>,
    override val dataForm: Pokemon?,
    override val loadedAllItems: Boolean,
) : PokeListState()

data class LoadingState(
    override val data: List<NamedApiResource>,
    override val dataForm: Pokemon?,
    override val loadedAllItems: Boolean,
) : PokeListState()

data class ErrorState(
    val errorMessage: String,
    override val data: List<NamedApiResource>,
    override val dataForm: Pokemon?,
    override val loadedAllItems: Boolean,
) : PokeListState()

data class EmptyState(
    override val data: List<NamedApiResource>,
    override val dataForm: Pokemon,
    override val loadedAllItems: Boolean,
) : PokeListState()