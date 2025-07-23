package com.example.coventry.data.model

import androidx.annotation.StringRes
import java.time.ZonedDateTime


data class PreviousCall(
    val threatLevel: Int,
    @StringRes val titleResourceId: Int,
    val dialogue: String,
    val timeOfCall: ZonedDateTime,
    val duration: Double,
    val callingNumber: String
)