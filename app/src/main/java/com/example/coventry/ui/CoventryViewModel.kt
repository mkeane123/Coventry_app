package com.example.coventry.ui

import androidx.lifecycle.ViewModel
import com.example.coventry.R
import com.example.coventry.data.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CoventryViewModel : ViewModel() {

    // App UI state
    private val _uiState = MutableStateFlow(CoventryUiState())
    val uiState: StateFlow<CoventryUiState> = _uiState.asStateFlow()

    fun setCurrentSelectedCategory(chosenCat: Category) {
        _uiState.update { currentState ->
            currentState.copy(
                currentSelectedCategory = chosenCat
            )
        }
    }

    init {
        // nothing in here yet as noting in app
    }



}