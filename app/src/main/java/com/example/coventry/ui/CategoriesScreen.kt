package com.example.coventry.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coventry.R
import com.example.coventry.data.Category
import com.example.coventry.data.categories

@Composable
fun CategoriesScreen(
    // might need to pass the categories in here
    onNextButtonClicked: (Category) -> Unit,
    modifier: Modifier = Modifier
) {



    Scaffold(

    ) { it ->
        LazyColumn(contentPadding = it) {
            items(categories) { it ->
                CategoryItem(
                    category = it,
                    onClick = {onNextButtonClicked(it)},
                    modifier = Modifier.padding(0.dp) // idk where this padding applies to, if anywhere
                )
            }
        }


    }
    
}



@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
    ) {
    Card(
        modifier = Modifier
            //.fillMaxSize()
            //.size(100.dp)
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
                    painter = painterResource(id = category.imageResourceId),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )

                Text(
                    text = stringResource(id = category.titleResourceId),
                    fontSize = 32.sp,
                    modifier = Modifier.padding(start = 20.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                CategoryItemButton(
                    onClick = onClick
                )
            }
        }
        
    }

}

@Composable
fun CategoryItemButton(
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

@Composable
fun CategoryItemButtonB(
    onClick: (Category) -> Unit,
    modifier: Modifier = Modifier
){
    Button(onClick = { /*TODO*/ }) {

    }

}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarApp() { // Might get rid of this and instead have a home page and then you go to the categories page
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(64.dp)
            ) {
                Image(
                    modifier = Modifier
                        //.size(dimensionResource(id = R.dimen.image_size))
                        //.padding(dimensionResource(id = R.dimen.padding_small)),
                        .padding(16.dp)
                        .size(100.dp),
                    painter = painterResource(R.drawable.lady_godiva),
                    contentDescription = null
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.displayLarge
                )

            }
        },
    )
}

@Preview
@Composable
private fun CardPreview() {
    //val display = categoriesOfPlaces[0]
    //CategoryItem(categoryOfPlace = display)
}
