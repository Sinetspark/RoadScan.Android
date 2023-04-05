package com.example.inroad.fragments

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.inroad.data.dto.PointStatus
import com.example.inroad.di.AppComponent
import com.example.inroad.domain.PointInteractor
import com.example.inroad.domain.entities.Point
import com.example.inroad.ui.MainViewModel
import com.example.inroad.ui.PingState
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MapViewModel @Inject constructor(
    private val pointInteractor: PointInteractor
) : ViewModel() {

    private val _mapData: MutableLiveData<MapState> = MutableLiveData<MapState>()
    val mapData: LiveData<MapState> = _mapData
    private val _existingPoints: HashSet<String> = HashSet<String>()

    /**
     * Метод вызывается когда activity завершит подготовительные работы. Затем метод идет в интерактор за данными,
     * которые потом преобразуются в данные ui слоя, которые удобно устанавливать для экрана
     */
    fun onInitiallyCreated(component: AppComponent, context: Context) {
        //component.inject(this)
    }

    fun getPoints(latitude: Double, longitude: Double, minDistance: Int, maxDistance: Int, status: PointStatus) {
        pointInteractor.getPoints(latitude, longitude, minDistance, maxDistance, 1)
            .map { points ->
                val result = mutableListOf<Point>()
                for (point in points) {
                    if (!_existingPoints.contains(point.id)) {
                        result.add(point)
                        _existingPoints.add(point.id)
                    }
                }
                MapState(result)
            }
            .subscribeOn(Schedulers.io())
            .subscribe(_mapData::postValue)
    }

    class Factory @Inject constructor(private val viewModel: MapViewModel) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return viewModel as T
        }
    }
}