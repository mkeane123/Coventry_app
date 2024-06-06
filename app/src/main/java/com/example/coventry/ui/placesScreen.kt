package com.example.coventry.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coventry.R
import com.example.coventry.data.Category
import com.example.coventry.data.DataSource.categories
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
    if (contentType == ContentType.LIST_AND_DETAIL) {
        // show list and detail view, two separate screens (need to make new composable)
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize()
        ) {
            PlacesScreen(
                coventryUiState = uiState,
                selectedCategory = uiState.currentSelectedCategory,
                onNextButtonClicked = {onNextButtonClicked(it)} ,
                changeIsShowing = onDetailScreenBackPressed,
                modifier = Modifier
                    //.fillMaxSize()
                    .size(100.dp)
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            )
            ShowPlace(
                coventryUiState = uiState,
                place = uiState.currentSelectedPlace,
                onBackPressed = onDetailScreenBackPressed
            )
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
                coventryUiState = uiState,
                place = uiState.currentSelectedPlace,
                onBackPressed = onDetailScreenBackPressed
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
                    modifier = Modifier.padding(0.dp) // idk where this padding applies to, if anywhere
                )
            }
        }


    }

}

@Composable
fun ShowPlace(
    coventryUiState: CoventryUiState,
    place: Place,
    onBackPressed : () -> Unit

){  Button(
    onClick = onBackPressed
) {
    Text(text = "Back to places")
}
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
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

    ) {
        Column(
            modifier =Modifier.fillMaxSize()
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

                PlaceItemButton(
                    uiState = uiState,
                    onClick = onClick
                )
            }
        }

    }

}

@Composable
fun PlaceItemButton(
    uiState: CoventryUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary
        )

    }

}