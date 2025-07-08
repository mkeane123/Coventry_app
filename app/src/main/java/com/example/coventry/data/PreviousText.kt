package com.example.coventry.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.time.ZonedDateTime

data class PreviousText(
    val threatLevel: Int,
    @StringRes val titleResourceId: Int,
    val content: String,
    val timeOfText: ZonedDateTime,
    val callingNumber: String?
)

