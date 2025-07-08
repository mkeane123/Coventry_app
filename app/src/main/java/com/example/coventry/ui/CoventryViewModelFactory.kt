package com.example.coventry.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coventry.data.DataStoreManager
import com.example.coventry.ui.CoventryViewModel

class CoventryViewModelFactory(
    private val dataStoreManager: DataStoreManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoventryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CoventryViewModel(dataStoreManager) as T
        }
        throw IllegalArgumentException("Unkown ViewModel class")
    }
}