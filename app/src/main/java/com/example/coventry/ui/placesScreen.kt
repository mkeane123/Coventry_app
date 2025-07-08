package com.example.coventry.ui

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coventry.R
import com.example.coventry.data.Category

import com.example.coventry.data.Place
import com.example.coventry.ui.utils.ContentType

//import com.example.coventry.data.categories


@Composable
fun PlacesHome(
    contentType: ContentType,
    uiState: CoventryUiState,
    onNextButtonClicked: (Place) -> Unit,
    onDetailScreenBackPressed: () -> Unit,
){
    /*

    if (contentType == ContentType.LIST_AND_DETAIL) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
            //modifier = Modifier.weight(1f)
        ) {

            LazyColumn {
                items(uiState.currentSelectedCategory.listOfPlaces) { place ->
                    PlaceListItem(
                        place = place,
                        selected = false,
                        onCardClick = {onNextButtonClicked(place)}
                    )
                }
            }

            Column {
                //show selected place
                ShowPlace(
                    contentType = contentType,
                    isOnlyPlace = false,
                    coventryUiState = uiState,
                    place = uiState.currentSelectedPlace,
                    onBackPressed = onDetailScreenBackPressed,
                    modifier = Modifier.fillMaxSize().
                    verticalScroll(rememberScrollState())
                )
            }
        }
    } else { // show list only view (default composable
        if (uiState.isShowingHomePage){
            PlacesScreen(
                coventryUiState = uiState,
                selectedCategory = uiState.currentSelectedCategory,
                onNextButtonClicked = {onNextButtonClicked(it)} ,
                changeIsShowing = onDetailScreenBackPressed,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }

        else {
            ShowPlace(
                contentType = contentType,
                isOnlyPlace = true,
                coventryUiState = uiState,
                place = uiState.currentSelectedPlace,
                onBackPressed = onDetailScreenBackPressed,
                modifier = Modifier.fillMaxSize()
            )

        }
    }

     */
}


@Composable
fun PlaceListItem(
    place: Place,
    selected: Boolean,
    onCardClick: () -> Unit
){
    Card(modifier = Modifier
        .width(750.dp)
        .clickable { onCardClick.invoke() }
        .padding(
            top = dimensionResource(
                id = R.dimen.padding_medium
            ),
            start = dimensionResource(id = R.dimen.padding_small)
        )
    ) {
        Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
            Image(
                painter = painterResource(id = place.imageResourceId),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )

            Text(
                text = stringResource(id = place.titleResourceId),
                fontSize = 32.sp,
                modifier = Modifier.padding(start = 20.dp)
            )
        }

    }
}


@Composable
fun PlacesScreen(
    coventryUiState: CoventryUiState,
    selectedCategory: Category,
    onNextButtonClicked:(Place) -> Unit,
    changeIsShowing: () -> Unit,
    modifier: Modifier = Modifier
){
    Scaffold(

    ) { it ->
        LazyColumn(contentPadding = it) {
            items(selectedCategory.listOfPlaces) { it ->
                PlacesItem(
                    uiState = coventryUiState,
                    place = it,
                    onClick = {onNextButtonClicked(it)},
                    modifier = Modifier.size(100.dp) // idk where this padding applies to, if anywhere
                )
            }
        }
    }
}

/*
@Composable
fun ShowPlace(
    contentType: ContentType,
    isOnlyPlace:Boolean,
    coventryUiState: CoventryUiState,
    place: Place,
    onBackPressed : () -> Unit,
    modifier: Modifier

){
    if (isOnlyPlace){
        Button(
            onClick = onBackPressed
        ) {
            Text(text = "Back to places")
        }
    }
    if (coventryUiState.currentSelectedPlace !in coventryUiState.currentSelectedCategory.listOfPlaces){
        val newPlace = coventryUiState.currentSelectedCategory.listOfPlaces[0]
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Image(
                painter = painterResource(id = newPlace.imageResourceId),
                contentDescription = stringResource(id = place.titleResourceId),
                modifier = Modifier.size(500.dp)
            )

            Text(
                text = stringResource(id = newPlace.titleResourceId),
                fontSize = 32.sp,
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_small))
            )

            if (newPlace.openingTimeResourceId!=null) {
                Text(
                    text = stringResource(id = newPlace.openingTimeResourceId),
                    fontSize = 32.sp
                )
            }
            else{Text(text = "")}

            Text(
                text = stringResource(id = newPlace.placeDetails),
                fontSize = 32.sp
            )
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Image(
                painter = painterResource(id = place.imageResourceId),
                contentDescription = stringResource(id = place.titleResourceId),
                modifier = Modifier.size(500.dp)
            )

            Text(
                text = stringResource(id = place.titleResourceId),
                fontSize = 32.sp
            )

            if (place.openingTimeResourceId!=null) {
                Text(
                    text = stringResource(id = place.openingTimeResourceId),
                    fontSize = 32.sp
                )
            }
            else{Text(text = "")}

            Text(
                text = stringResource(id = place.placeDetails),
                fontSize = 32.sp
            )
        }
    }

}


 */
@Composable
fun PlacesItem(
    uiState: CoventryUiState,
    place: Place,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .padding(start = 5.dp, top = 16.dp, end = 5.dp)
            .clickable { onClick.invoke() }
            //.size(100.dp)

    ) {
        Column(
            //modifier =Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = place.imageResourceId),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )

                Text(
                    text = stringResource(id = place.titleResourceId),
                    fontSize = 32.sp,
                    modifier = Modifier.padding(start = 20.dp)
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }

    }

}
