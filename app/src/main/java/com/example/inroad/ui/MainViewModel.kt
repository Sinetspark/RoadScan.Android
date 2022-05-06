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
import javax.inject.Inject


class MainViewModel : ViewModel() {

    companion object {
        private const val CITY_MASK = "{city}"
    }

    @Inject
    lateinit var interactor: PointInteractor
    //private val _liveData: MutableLiveData<WeatherUiState> = MutableLiveData<WeatherUiState>()
    //val liveData: LiveData<WeatherUiState> = _liveData

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

    fun onScreenClicked(context: Context) {
        getPoints(context)
    }

    private fun getPoints(context: Context) {
        /*interactor.getPoints()
            .map { weather ->
                val title =
                    context.getString(R.string.weather_in_mask).replace(CITY_MASK, weather.city)
                val (weatherTextResId, backgroundColorId) = getWeatherTypeTextAndColorId(weather.weatherType)
                WeatherUiState(
                    title,
                    weather.temperature,
                    weatherTextResId,
                    ContextCompat.getColor(context, backgroundColorId)
                )
            }
            .subscribeOn(Schedulers.io())
            .subscribe(_liveData::postValue)*/
    }
}