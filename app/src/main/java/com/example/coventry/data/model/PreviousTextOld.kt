package com.example.coventry.data.model

import androidx.annotation.StringRes
import java.time.ZonedDateTime

data class PreviousTextOld(
    val threatLevel: Int,
    @StringRes val titleResourceId: Int,
    val content: String,
    val timeOfText: ZonedDateTime,
    val callingNumber: String?
)
