package com.example.coventry.ui

import com.example.coventry.data.Category
import com.example.coventry.data.DataSource.categories
import com.example.coventry.data.Place
import com.example.coventry.data.defaultPlace


data class CoventryUiState(
    var currentSelectedCategory: Category = categories[0],
    var currentSelectedPlace: Place = defaultPlace,
    var isShowingHomePage: Boolean = true
    //var currentSelectedCategory: CategoryOfPlace = categoriesOfPlaces[0]
)