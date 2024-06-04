package com.example.coventry.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.coventry.R


data class Category(
    val id: Int,
    @StringRes val titleResourceId: Int,
    @DrawableRes val imageResourceId: Int,
)


val categories =  listOf(
    Category(id = 1, titleResourceId = R.string.coffee_shops, imageResourceId = R.drawable.coffee_cropped),
    Category(id = 2, titleResourceId = R.string.parks, imageResourceId = R.drawable.park_cropped),
    Category(id = 3, titleResourceId = R.string.bars, imageResourceId = R.drawable.bar_cropped),
    Category(id = 4, titleResourceId = R.string.restaurants, imageResourceId = R.drawable.restaurant_cropped),
    Category(id = 5, titleResourceId = R.string.museums, imageResourceId = R.drawable.museum_cropped),
    Category(id = 6, titleResourceId = R.string.sports, imageResourceId = R.drawable.sports_cropped),
)
