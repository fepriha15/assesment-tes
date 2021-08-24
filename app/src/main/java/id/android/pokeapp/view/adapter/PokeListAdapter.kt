package id.android.pokeapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import id.android.pokeapp.databinding.PokeListAdapterBinding
import id.android.pokeapp.di.GlideApp
import id.android.pokeapp.model.Pokemon
import timber.log.Timber
import java.lang.StringBuilder
import java.util.*


class PokeListAdapter(private val context: Context) : RecyclerView.Adapter<PokeListAdapter.PokeListViewHolder>() {
    lateinit var itemListener: PokeItemListener
    private var pokes: MutableList<Pokemon?> = mutableListOf()

    interface PokeItemListener {
        fun onPokeClick(pokeList: Pokemon?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeListViewHolder {
        val view = PokeListAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokeListViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokeListViewHolder, position: Int) {
        pokes[position].let { holder.bindItem(it) }
    }

    override fun getItemCount(): Int = pokes.size

    fun add(poke: Pokemon?) {
        if (!pokes.contains(poke)) {
            pokes.add(poke)
            pokes.sortBy { it?.id }
            notifyDataSetChanged()
        }
    }

    fun addAll(pokeList: MutableList<Pokemon?>) {
        pokes.clear()
        pokes = pokeList
        pokes.sortBy { it?.id }
        notifyDataSetChanged()
    }

    inner class PokeListViewHolder(private val binding: PokeListAdapterBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(poke: Pokemon?) {
            with(binding) {
                tvPokeName.text = poke?.name?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                tvPokeId.text = StringBuilder("#").append(poke?.id.toString().padStart(3, '0'))
                val pokeImage = poke?.sprites?.other?.officialArtwork?.frontDefault
                GlideApp.with(context).load(pokeImage).into(imgPoke)
            }
            itemView.setOnClickListener {
                itemListener.onPokeClick(poke)
            }
        }
    }

}