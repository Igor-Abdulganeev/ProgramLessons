package ru.gorinih.androidacademy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
class MovieViewModelFactory @Inject constructor(
    private val viewModels: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var viewModel: Provider<out ViewModel>? = viewModels[modelClass]

        if (viewModel == null) {
            for ((key, value) in viewModels) {
                if (modelClass.isAssignableFrom(key)) {
                    viewModel = value
                }
            }
        }
        if (viewModel == null)
            throw IllegalArgumentException("Unknown ViewModel $modelClass")
        return viewModel.get() as T
    }
}