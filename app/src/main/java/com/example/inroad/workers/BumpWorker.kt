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
import com.example.inroad.data.dto.InsertBump
import com.example.inroad.data.dto.InsertBumps
import com.example.inroad.domain.BumpInteractor
import com.example.inroad.managers.BumpManager

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
        bumpManager.bumps
            .blockingSubscribe { location ->
                Log.i("раз", "${location.latitude}, ${location.longitude}")
                val bumps = listOf<InsertBump>(InsertBump(location.latitude, location.longitude, "", ""))
                bumpInteractor.insertBumps(InsertBumps(bumps)).subscribe()
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