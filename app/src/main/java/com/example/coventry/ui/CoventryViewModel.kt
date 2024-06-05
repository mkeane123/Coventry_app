package com.example.coventry.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.coventry.R
import com.example.coventry.data.Category
import com.example.coventry.data.Place
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

    fun setCurrentSelectedPlcace(chosenPlace: Place) {
        _uiState.update { currentState ->
            currentState.copy(
                currentSelectedPlace = chosenPlace
            )
        }

    }

    fun setIsShowingHomePage(isShowing: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isShowingHomePage = isShowing
            )
        }

    }

    private fun resetApp() {
        _uiState.value = CoventryUiState()
    }

    init {
        //resetApp()
    }



}