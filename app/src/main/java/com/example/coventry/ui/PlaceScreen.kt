package com.example.coventry.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.coventry.data.Place

@Composable
fun PlaceScreen(
    selectedPlace: Place,
    modifier: Modifier = Modifier
) {
    Text(text = "Here we will show one place")
}