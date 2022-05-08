package com.example.inroad.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.inroad.managers.LocationManager
import com.example.inroad.domain.PointInteractor

class TestWorker (
    appContext: Context,
    workerParams: WorkerParameters,
    private val locationManager: LocationManager
    ) : Worker(appContext, workerParams) {

    private val notificationManager
        get() = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        private val interactor = PointInteractor(appContext)

    override fun doWork(): Result {
        // Mark the Worker as important
        setForegroundAsync(createForegroundInfo())
        //блокирующая задача
        //testWork()
        updateLocation()
        return Result.success()
    }

   /* private fun testWork() {
        runBlocking {
            repeat(1000) {
                if (!isStopped) {
                    interactor.postLocation(Random.nextDouble(), Random.nextDouble())
                        .subscribe()
                            Log.i("раз", "два")
                    delay(1000)
                }
            }
        }
    }*/

    private fun updateLocation() {
        locationManager.locations
            .blockingSubscribe { location ->
                Log.i("раз", "${location.latitude}, ${location.longitude}")
            }
    }

    @NonNull
    private fun createForegroundInfo(): ForegroundInfo {
        // Build a notification using bytesRead and contentLength
        val context = applicationContext
        val id = "123"
        val title = "Foreground Title"
        val cancel = "Cancel Button"
        // This PendingIntent can be used to cancel the worker
        val intent = WorkManager.getInstance(context)
            .createCancelPendingIntent(getId())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
        val notification: Notification = NotificationCompat.Builder(context, id)
            .setContentTitle(title)
            .setChannelId("Super puper unique id")
            .setTicker(title)
            .setSmallIcon(android.R.drawable.ic_menu_call)
            .setOngoing(true) // Add the cancel action to the notification which can
            // be used to cancel the worker
            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()
        return ForegroundInfo(1234, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val name = "Channel Name"
        val descriptionText = "Description of channel"
        val importance = NotificationManager.IMPORTANCE_MIN
        val mChannel = NotificationChannel("Super puper unique id", name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        notificationManager.createNotificationChannel(mChannel)
    }
}