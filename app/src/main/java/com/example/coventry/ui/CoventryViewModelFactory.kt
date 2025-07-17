package com.example.coventry.ui

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coventry.data.DataStoreManager
import com.example.coventry.data.repository.PreviousTextRepository


class CoventryViewModelFactory(
    private val context: Context,
    private val dataStoreManager: DataStoreManager
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = PreviousTextRepository(context.applicationContext)
        if (modelClass.isAssignableFrom(CoventryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CoventryViewModel(dataStoreManager, repository) as T
        }
        throw IllegalArgumentException("Unkown ViewModel class")
    }
}