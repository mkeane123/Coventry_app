package com.example.coventry.ui

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.coventry.R

import com.example.coventry.data.PreviousCall
import com.example.coventry.data.PreviousText
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class CoventryUiState  constructor(
    var onCall: Boolean = true,
    var isShowingHomePage: Boolean = true,
    var currentSelectedPastCall: PreviousCall = testPreviousCall,
    var currentSelectedPastText: PreviousText = testPreviousText
    //var currentSelectedCategory: CategoryOfPlace = categoriesOfPlaces[0]
)

@RequiresApi(Build.VERSION_CODES.O)
val testPreviousText: PreviousText = PreviousText(
    threatLevel = 1,
    titleResourceId = R.string.example_call_title1,
    content = "Give me your bank account details",
    callingNumber = "07728334147",
    timeOfText = ZonedDateTime.now()
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

