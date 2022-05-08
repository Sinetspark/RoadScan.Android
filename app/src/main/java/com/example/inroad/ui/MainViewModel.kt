package com.example.inroad.ui

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.inroad.di.AppComponent
import com.example.inroad.domain.PointInteractor
import com.example.inroad.workers.TestWorker
import io.reactivex.rxjava3.schedulers.Schedulers
import com.example.inroad.domain.entities.Point
import javax.inject.Inject


class MainViewModel : ViewModel() {

    companion object {
        private const val CITY_MASK = "{city}"
    }

    @Inject
    lateinit var pointInteractor: PointInteractor
    private val _existingPoints: HashSet<String> = HashSet<String>()
    private val _liveData: MutableLiveData<MapUiState> = MutableLiveData<MapUiState>()
    val liveData: LiveData<MapUiState> = _liveData

    /**
     * Метод вызывается когда activity завершит подготовительные работы. Затем метод идет в интерактор за данными,
     * которые потом преобразуются в данные ui слоя, которые удобно устанавливать для экрана
     */
    fun onInitiallyCreated(component: AppComponent, context: Context) {
        component.inject(this)
        getPoints(context)
    }

    fun onServiceButtonClicked(context: Context) {
        WorkManager.getInstance(context)
            .beginUniqueWork(
                "SuperDuperLocationSender",
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(TestWorker::class.java)
            )
            .enqueue()
    }

    private fun getPoints(context: Context) {
        pointInteractor.getPoints()
            .map { points ->
                val result = listOf<Point>()
                for (point in points) {
                    if (!_existingPoints.contains(point.id)) {
                        result.plus(point)
                        _existingPoints.add(point.id)
                    }
                }
                MapUiState(result)
            }
            .subscribeOn(Schedulers.io())
            .subscribe(_liveData::postValue)
    }
}