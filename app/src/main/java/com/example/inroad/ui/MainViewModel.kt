package com.example.inroad.ui

import android.content.Context
import android.util.Log
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
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject


class MainViewModel @Inject constructor(
    private val pointInteractor: PointInteractor,
    private val pingInteractor: PingInteractor
) : ViewModel() {

    companion object {
        private const val CITY_MASK = "{city}"
    }

    private val _existingPoints: HashSet<String> = HashSet<String>()
    private val _liveData: MutableLiveData<MapUiState> = MutableLiveData<MapUiState>()
    val liveData: LiveData<MapUiState> = _liveData

    /**
     * Метод вызывается когда activity завершит подготовительные работы. Затем метод идет в интерактор за данными,
     * которые потом преобразуются в данные ui слоя, которые удобно устанавливать для экрана
     */
    fun onInitiallyCreated(component: AppComponent, context: Context) {
        component.inject(this)
    }

    fun onServiceButtonClicked(context: Context) {
        WorkManager.getInstance(context)
            .beginUniqueWork(
                "SuperDuperLocationSender",
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(BumpWorker::class.java)
            )
            .enqueue()
    }

    fun getPoints(context: Context, latitude: Double, longitude: Double, minDistance: Int, maxDistance: Int) {
        try {
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
                .subscribe(_liveData::postValue)
        } catch (e: Exception) {
            Log.i("tagerror", "error")
        }
    }

    fun ping(): Observable<Void> {
        return pingInteractor.getPing()
    }

    class Factory @Inject constructor(private val viewModel: MainViewModel) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return viewModel as T
        }
    }
}