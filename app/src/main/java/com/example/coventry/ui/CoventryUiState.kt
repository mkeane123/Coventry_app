package com.example.coventry.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.PrimaryKey
import com.example.coventry.R

import com.example.coventry.data.PreviousCall
import com.example.coventry.data.model.PreviousText
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class CoventryUiState  constructor(
    val isLoading: Boolean = true,
    val isFirstLaunch: Boolean = true,
    val hasPermissions: Boolean = false,
    var onCall: Boolean = true,
    var isShowingHomePage: Boolean = false,
    var currentSelectedPastCall: PreviousCall = testPreviousCall,
    var currentSelectedPastText: PreviousText = testPreviousText
    //var currentSelectedCategory: CategoryOfPlace = categoriesOfPlaces[0]
)

@RequiresApi(Build.VERSION_CODES.O)


val testPreviousText: PreviousText = PreviousText(
    id = 1,
    sender = "07865438765",
    body = "This is test text message body",
    timestamp = 12
)


@RequiresApi(Build.VERSION_CODES.O)
val placeholderTime: ZonedDateTime = ZonedDateTime.of(
    LocalDateTime.of(2000, 1, 1, 0, 0),
    ZoneId.of("UTC")
)

@RequiresApi(Build.VERSION_CODES.O)
val testPreviousCall: PreviousCall = PreviousCall(
    threatLevel = 1,
    titleResourceId = R.string.example_call_title1,
    dialogue = "I am scamming you",
    timeOfCall = placeholderTime,
    duration = 3.3423432423,
    callingNumber = "07728334147")

