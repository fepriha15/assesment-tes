package id.android.pokeapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import id.android.pokeapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.InetSocketAddress
import java.net.Socket


fun hasInternetConnection(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.activeNetworkInfo?.run {
            return when (type) {
                ConnectivityManager.TYPE_WIFI -> isInternetAvailable()
                ConnectivityManager.TYPE_MOBILE -> isInternetAvailable()
                ConnectivityManager.TYPE_ETHERNET -> isInternetAvailable()
                else -> false
            }
        }
    }
    return false
}

fun isInternetAvailable(): Boolean {
    CoroutineScope(IO).launch {
        val hasInet = execute()
        if (hasInet)
            withContext(Main) {
            true
        }
    }
    return false
}

fun execute(): Boolean {
    return try {
        val socket = Socket();
        socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
        socket.close()
        return true
    } catch (e: Exception) {
        Timber.i(e.localizedMessage)
        false
    }
}

fun showErrorMesage(context: Context?, message : String){
    if(!isInternetAvailable())
        Toast.makeText(context, context?.getString(R.string.no_inet_con), Toast.LENGTH_LONG).show()
    else
        Toast.makeText(context, "An error occured: $message", Toast.LENGTH_LONG).show()
}