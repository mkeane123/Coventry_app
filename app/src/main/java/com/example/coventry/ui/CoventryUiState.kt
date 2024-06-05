package com.example.coventry.ui

import com.example.coventry.data.Category
import com.example.coventry.data.categories


data class CoventryUiState(
    var currentSelectedCategory: Category = categories[0]
    //var currentSelectedCategory: CategoryOfPlace = categoriesOfPlaces[0]
)