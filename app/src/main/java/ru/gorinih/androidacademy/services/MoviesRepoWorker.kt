package ru.gorinih.androidacademy.services

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import java.util.concurrent.TimeUnit

class MoviesRepoWorker {

    private val TAG = "MoviesRepoWorker"
    private val networkType = NetworkType.CONNECTED
    private val constraint = Constraints.Builder()
        .setRequiredNetworkType(networkType)
        .build()

    val moviesTaskRequest =
        PeriodicWorkRequest.Builder(MoviesWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraint)
            .setInitialDelay(10, TimeUnit.SECONDS)
            .addTag(TAG)
            .build()


}