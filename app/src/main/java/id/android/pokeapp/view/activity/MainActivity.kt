package id.android.pokeapp.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.android.AndroidInjection
import id.android.pokeapp.R
import id.android.pokeapp.databinding.ActivityMainBinding
import id.android.pokeapp.model.NamedApiResource
import id.android.pokeapp.model.Pokemon
import id.android.pokeapp.util.showErrorMesage
import id.android.pokeapp.view.adapter.PokeListAdapter
import id.android.pokeapp.viewmodel.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class MainActivity : BaseActivity<ActivityMainBinding>(), SwipeRefreshLayout.OnRefreshListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: PokeListViewModel
    private lateinit var pokeListAdapter : PokeListAdapter

    private var locationPermissionGranted: Boolean = false
    private var offset: Int = 0
    private var limit: Int = 20

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun setupView(binding: ActivityMainBinding) {
        AndroidInjection.inject(this)
        init()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionGranted = false
        when (requestCode) {
            1 -> { // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
        }
    }

    private val itemListener = object : PokeListAdapter.PokeItemListener {
        override fun onPokeClick(pokeList: Pokemon?) { //
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.DETAIL, pokeList)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.pokemonListDetail.removeObserver(pokeDetailObsever)
    }

    override fun onRefresh() {
        viewModel.getPokemonListDetail(offset, limit)
    }

    private fun init(){
        viewModel = ViewModelProvider(this, viewModelFactory).get(PokeListViewModel::class.java)
        binding.loading.setOnRefreshListener(this)

        //toolbar
        val toolbarTitle = findViewById<View>(R.id.toolbar_title) as TextView
        toolbarTitle.text = this.getString(R.string.poke_app)

        //recyclerView adapter
        pokeListAdapter = PokeListAdapter(this)
        pokeListAdapter.itemListener = itemListener
        viewModel.pokemonListDetail.observe(this, pokeDetailObsever)
        setupRv(binding)
        getPermission()
        viewModel.getPokemonListDetail(offset, limit)
    }

    private fun setupRv(mainBinding: ActivityMainBinding) {
        mainBinding.rvPokeList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = pokeListAdapter
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private val pokeDetailObsever = Observer<Resource<List<Pokemon>>> { response ->
        when(response) {
            is Resource.Success -> {
                val listPokemon: MutableList<Pokemon?> = mutableListOf()
                binding.loading.isRefreshing = false
                response.data?.let { listPokemon.addAll(it) }
                listPokemon.sortBy { it?.id }
                pokeListAdapter.addAll(listPokemon)
            }
            is Resource.Error -> {
                response.message?.let { message ->
                    binding.loading.isRefreshing = false
                    showErrorMesage(this, message)
                }
            }
            is Resource.Loading -> {
                binding.loading.isRefreshing = true

            }
        }
    }

    private fun getPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_NETWORK_STATE), 1)
        }
    }
}