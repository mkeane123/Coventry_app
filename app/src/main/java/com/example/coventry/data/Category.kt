package com.example.coventry.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.coventry.R


data class Category(
    val id: Int,
    @StringRes val titleResourceId: Int,
    @DrawableRes val imageResourceId: Int,
    val listOfPlaces: List<Place>
)



