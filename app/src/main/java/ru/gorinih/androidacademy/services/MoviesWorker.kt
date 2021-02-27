package ru.gorinih.androidacademy.services

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.R
import ru.gorinih.androidacademy.data.db.MoviesDatabase
import ru.gorinih.androidacademy.data.models.Movies
import ru.gorinih.androidacademy.data.network.MoviesApi
import ru.gorinih.androidacademy.presentation.MainActivity
import ru.gorinih.androidacademy.presentation.ui.movies.paging.MoviesRemoteMediator

class MoviesWorker(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    @ExperimentalSerializationApi
    @InternalCoroutinesApi
    override fun doWork(): Result {
        return try {
            val movieNotification = loadLastNotification()
            movieNotification?.let { showNotification(it) }
            Result.success()
        } catch (ex: Throwable) {
            Log.d(TAG, "Error in $TAG = $ex")
            Result.failure()
        }
    }

    @InternalCoroutinesApi
    private fun showNotification(movieNotification: Movies.Movie) {
        val notificationBuilder = buildNotification(movieNotification)
        val notificationManager = NotificationManagerCompat.from(context.applicationContext)
        val urlImage =
            if (movieNotification.detailPoster != "null") movieNotification.detailPoster
            else if (movieNotification.poster != "null") movieNotification.poster
            else "null"
        if (urlImage.substring(urlImage.length - 4, urlImage.length) != "null")
            Glide.with(context)
                .asBitmap()
                .load(urlImage)
                .placeholder(R.drawable.ic_no_photo)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d(TAG, "notification error glide")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        notificationManager.notify(
                            context.getString(R.string.notification_channel_id),
                            1,
                            notificationBuilder.setStyle(
                                NotificationCompat.BigPictureStyle().bigPicture(resource)
                            ).build()
                        )
                        return true
                    }
                })
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        Log.d(TAG, "notification completed")
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        else {
            notificationManager.notify(
                context.getString(R.string.notification_channel_id),
                1,
                notificationBuilder
                    .setStyle(
                        NotificationCompat.BigTextStyle().bigText(movieNotification.description)
                    )
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            context.resources,
                            R.drawable.location
                        )
                    )
                    .build()
            )
        }
    }

    @ExperimentalSerializationApi
    @InternalCoroutinesApi
    private fun loadLastNotification(): Movies.Movie? {
        var movie: Movies.Movie?
        runBlocking(Dispatchers.IO) {
            movie = getMovie()
        }
        return movie
    }

    @InternalCoroutinesApi
    private fun buildNotification(movie: Movies.Movie): NotificationCompat.Builder {
        val time = System.currentTimeMillis()
        val genres = movie.listOfGenre.map {
            it.nameGenre
        }.sorted()
            .joinToString()
        val intent = Intent(context, MainActivity::class.java)
            .setAction(Intent.ACTION_VIEW)
            .putExtra(context.getString(R.string.name_intent), movie.id)
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        return NotificationCompat.Builder(
            context.applicationContext,
            context.getString(R.string.notification_channel_id)
        )
            .setContentTitle(movie.nameMovie)
            .setContentText(genres)
            .setSmallIcon(R.drawable.location)
            .setWhen(time)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }

    @InternalCoroutinesApi
    @ExperimentalSerializationApi
    private suspend fun getMovie(): Movies.Movie? {
        val movieApi = MoviesApi.newInstance()
        val movieDatabase = MoviesDatabase.newInstance(context)
        val movieMediator = MoviesRemoteMediator(movieApi, movieDatabase)
        val key = movieDatabase.remoteKeysDao.getMaxNextKey() ?: 0
        if (key != 0) {
            val resultMovies =
                movieMediator.getData(currentKey = key)
            movieMediator.insertData(
                currentKey = key,
                endOfPaginationReached = resultMovies.isEmpty(),
                result = resultMovies
            )
            return resultMovies.maxOfWith(
                compareBy { it?.rating }
            ) { it }
        }
        return null
    }

    companion object {
        private const val TAG = "MoviesWorker"
    }
}