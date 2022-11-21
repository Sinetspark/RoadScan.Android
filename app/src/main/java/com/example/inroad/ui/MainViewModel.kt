package com.example.inroad.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.inroad.di.AppComponent
import com.example.inroad.domain.PingInteractor
import com.example.inroad.domain.PointInteractor
import com.example.inroad.workers.BumpWorker
import io.reactivex.rxjava3.schedulers.Schedulers
import com.example.inroad.domain.entities.Point
import javax.inject.Inject


class MainViewModel @Inject constructor(
    private val pointInteractor: PointInteractor,
    private val pingInteractor: PingInteractor
) : ViewModel() {

    companion object {
        private const val CITY_MASK = "{city}"
    }

    private val _existingPoints: HashSet<String> = HashSet<String>()
    private val _pingData: MutableLiveData<PingState> = MutableLiveData<PingState>()
    private val _mapData: MutableLiveData<MapUiState> = MutableLiveData<MapUiState>()
    private val _connectionData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val mapData: LiveData<MapUiState> = _mapData
    val pingData: LiveData<PingState> = _pingData
    val connectionData: LiveData<Boolean> = _connectionData

    /**
     * Метод вызывается когда activity завершит подготовительные работы. Затем метод идет в интерактор за данными,
     * которые потом преобразуются в данные ui слоя, которые удобно устанавливать для экрана
     */
    fun onInitiallyCreated(component: AppComponent, context: Context) {
        component.inject(this)
    }

    fun onBumpWorkerStart(context: Context) {
        WorkManager.getInstance(context)
            .beginUniqueWork(
                "BumpSender",
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(BumpWorker::class.java)
            )
            .enqueue()
    }

    fun defaultConnection(context: Context) {
        val cm = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val network = cm.activeNetwork;
        _connectionData.postValue(network != null)
    }


    public val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _connectionData.postValue(true);
        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            _connectionData.postValue(false);
        }
    }

    fun getPoints(latitude: Double, longitude: Double, minDistance: Int, maxDistance: Int) {
        pointInteractor.getPoints(latitude, longitude, minDistance, maxDistance)
            .map { points ->
                val result = mutableListOf<Point>()
                for (point in points) {
                    if (!_existingPoints.contains(point.id)) {
                        result.add(point)
                        _existingPoints.add(point.id)
                    }
                }
                MapUiState(result)
            }
            .subscribeOn(Schedulers.io())
            .subscribe(_mapData::postValue)
    }

    fun ping() {
        pingInteractor.getPing()
            .subscribeOn(Schedulers.io())
            .subscribe(
                {_pingData.postValue(PingState(true))},
                {_pingData.postValue(PingState(false))}
            )
    }

    class Factory @Inject constructor(private val viewModel: MainViewModel) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return viewModel as T
        }
    }
}