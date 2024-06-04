package com.example.coventry.ui

import androidx.lifecycle.ViewModel
import com.example.coventry.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CoventryViewModel : ViewModel() {

    // App UI state
    private val _uiState = MutableStateFlow(CoventryUiState())
    val uiState: StateFlow<CoventryUiState> = _uiState.asStateFlow()


    init {
        // nothing in here yet as noting in app
    }



}