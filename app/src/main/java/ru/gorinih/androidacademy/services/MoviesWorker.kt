package ru.gorinih.androidacademy.services

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalSerializationApi
    @InternalCoroutinesApi
    override fun doWork(): Result {
        return try {
            val scope = CoroutineScope(Dispatchers.IO)
            val movieApi = MoviesApi.newInstance()
            val movieDatabase = MoviesDatabase.newInstance(context)
            val movieMediator = MoviesRemoteMediator(movieApi, movieDatabase)
            val key = movieDatabase.remoteKeysDao.getMaxNextKey() ?: 1
            val notificationManager = NotificationManagerCompat.from(context.applicationContext)

            Log.d(TAG, "Получен ключ = $key")

            scope.launch {

                val movieNotify = getMovie(key, movieMediator)
                Log.d(TAG, "Получен фильм ${movieNotify.id} - ${movieNotify.nameMovie}")

                val showNotify = buildNotification(movieNotify)

                val urlImage =
                    if (movieNotify.detailPoster != "null") movieNotify.detailPoster
                    else if (movieNotify.poster != "null") movieNotify.poster
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
                                    "MovieView",
                                    1,
                                    showNotify.setStyle(
                                        NotificationCompat.BigPictureStyle().bigPicture(resource)
                                    ).build()
                                )
                                scope.cancel()
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
                        "MovieView",
                        1,
                        showNotify
                            .setStyle(
                                NotificationCompat.BigTextStyle().bigText(movieNotify.description)
                            )
                            .build()
                    )
                    scope.cancel()
                }
            }
            Result.success()
        } catch (ex: Throwable) {
            Log.d(TAG, "Error in $TAG = $ex")
            Result.failure()
        }
    }

    private fun buildNotification(movie: Movies.Movie): NotificationCompat.Builder {
        val time = System.currentTimeMillis()
        val genres = movie.listOfGenre.map {
            it.nameGenre
        }.sorted()
            .joinToString()
        val intent = Intent(context, MainActivity::class.java)
            .setAction(Intent.ACTION_VIEW)
            .putExtra("idMovie", movie.id)
            .putExtra("startNotify", true)
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        return NotificationCompat.Builder(
            context.applicationContext, "MovieView"
        )
            .setContentTitle(movie.nameMovie)
            .setContentText(genres)
            .setSmallIcon(R.drawable.location)
            .setWhen(time)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }

    private suspend fun getMovie(key: Int, movieMediator: MoviesRemoteMediator): Movies.Movie {
        val resultMovies = withContext(Dispatchers.IO) {
            movieMediator.getData(currentKey = key)
        }
        withContext(Dispatchers.IO) {
            movieMediator.insertData(
                currentKey = key,
                endOfPaginationReached = resultMovies.isEmpty(),
                result = resultMovies
            )
        }
        return resultMovies.maxOfWith(
            compareBy { it.rating }
        ) { it }
    }

    companion object {
        private const val TAG = "MoviesWorker"
    }
}