package com.example.coventry.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.coventry.R

/**
 * Data model for place
 */
data class Place(
    val id: Int,
    @StringRes val titleResourceId: Int,
    @StringRes val openingTimeResourceId: Int?,
    @DrawableRes val imageResourceId: Int,
    @StringRes val placeDetails: Int
)

