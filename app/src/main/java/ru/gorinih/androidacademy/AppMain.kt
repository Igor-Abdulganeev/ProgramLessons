package ru.gorinih.androidacademy

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import ru.gorinih.androidacademy.services.MoviesRepoWorker

class AppMain : Application() {

    override fun onCreate() {
        super.onCreate()
        hInstance = this
        createChannel()
        runWorkManager()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.notification_channel_id),
                getString(R.string.name_notification_channel),
                NotificationManager.IMPORTANCE_LOW
            )
            NotificationManagerCompat.from(applicationContext)
                .createNotificationChannel(channel)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun runWorkManager() {
        val worker = MoviesRepoWorker()
        WorkManager.getInstance(applicationContext).cancelAllWorkByTag("ru.gorinih.androidacademy")
        WorkManager.getInstance(applicationContext).enqueue(worker.moviesTaskRequest)
    }

    companion object {
        private lateinit var hInstance: Application

    }
}