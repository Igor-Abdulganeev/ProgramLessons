package ru.gorinih.androidacademy.services

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi
import ru.gorinih.androidacademy.data.db.MoviesDatabase
import ru.gorinih.androidacademy.data.models.Movies
import ru.gorinih.androidacademy.data.network.MoviesApi
import ru.gorinih.androidacademy.data.repository.MoviesRepository
import ru.gorinih.androidacademy.presentation.ui.movies.paging.MoviesRemoteMediator

class MoviesWorker(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    @ExperimentalSerializationApi
    @InternalCoroutinesApi
    override fun doWork(): Result {
        val scope = CoroutineScope(Dispatchers.IO)
        return try {
            Log.d(TAG, "Сервис запущен")
            val movieApi = MoviesApi.newInstance()
            val movieDatabase = MoviesDatabase.newInstance(context)
            val movieMediator = MoviesRemoteMediator(movieApi, movieDatabase)
            Log.d(TAG, "Созданы объекты")
            val key = movieDatabase.remoteKeysDao.getMaxNextKey() ?: 1
            Log.d(TAG, "Ключ получен = $key")
            scope.launch {
                val resultMovies = movieMediator.getData(currentKey = key)
                Log.d(TAG, "Данные получены")
                movieMediator.insertData(
                    currentKey = key,
                    endOfPaginationReached = resultMovies.isEmpty(),
                    result = resultMovies
                )
                Log.d(TAG, "Данные закэшированы")
            }
            Log.d(TAG, "Закончили работу")
            Result.success()
        } catch (ex: Throwable) {
            Log.d(TAG, "У нас возникла ошибка - $ex")
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "MoviesWorker"
    }
}