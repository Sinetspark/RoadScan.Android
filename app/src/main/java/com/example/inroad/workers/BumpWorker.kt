package com.example.inroad.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.inroad.data.dto.InsertBump
import com.example.inroad.data.dto.InsertBumps
import com.example.inroad.domain.BumpInteractor
import com.example.inroad.domain.entities.Point
import com.example.inroad.managers.BumpManager
import com.example.inroad.R

class BumpWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val bumpManager: BumpManager
    ) : Worker(appContext, workerParams) {

    private val notificationManager
        get() = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        private val bumpInteractor = BumpInteractor()

    override fun doWork(): Result {
        // Mark the Worker as important
        setForegroundAsync(createForegroundInfo())
        //блокирующая задача
        insertBumps()
        return Result.success()
    }

    private fun insertBumps() {
        val bumpLocations = arrayListOf<InsertBump>()
        bumpManager.bumps
            .blockingSubscribe { location ->
                Log.i("bump", "${location.latitude}, ${location.longitude}")
                bumpLocations.add(InsertBump(location.latitude,
                    location.longitude, "", ""))
                if (bumpLocations.count() >= 10) {
                    bumpInteractor.insertBumps(InsertBumps(bumpLocations))
                        .subscribe()
                    bumpLocations.clear()
                    Log.i("insert bumps", "insert new > 10 bumps")
                }
            }
    }

    @NonNull
    private fun createForegroundInfo(): ForegroundInfo {
        // Build a notification using bytesRead and contentLength
        val context = applicationContext
        val id = "bumpId"
        val title = "Выявление неровностей"
        val cancel = "Отмена"
        // This PendingIntent can be used to cancel the worker
        val intent = WorkManager.getInstance(context)
            .createCancelPendingIntent(getId())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
        val notification: Notification = NotificationCompat.Builder(context, id)
            .setContentTitle(title)
            .setChannelId("Bump unique id")
            .setTicker(title)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setOngoing(true) // Add the cancel action to the notification which can
            // be used to cancel the worker
            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()
        return ForegroundInfo(1234, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val name = "Bump"
        val descriptionText = "Insert bumps"
        val importance = NotificationManager.IMPORTANCE_MIN
        val mChannel = NotificationChannel("Bump unique id", name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        notificationManager.createNotificationChannel(mChannel)
    }
}