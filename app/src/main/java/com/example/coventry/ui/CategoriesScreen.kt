package com.example.coventry.ui

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coventry.CoventryScreen
import com.example.coventry.R
import com.example.coventry.data.Category
import com.example.coventry.data.DataSource.categories
import com.example.coventry.ui.utils.ContentType

//import com.example.coventry.data.categories


@Composable
fun CategoriesScreenHome(
    contentType: ContentType,
    onNextButtonClicked: (Category) -> Unit,
    modifier: Modifier = Modifier
) {

    if (contentType == ContentType.LIST_AND_DETAIL) {
        CategoriesScreenGrid(
            onNextButtonClicked = onNextButtonClicked,
            modifier = modifier
        )
    } else {
        CategoriesScreenOneCol(
            onNextButtonClicked = onNextButtonClicked,
            modifier = modifier
        )
    }

}

@Composable
fun CategoriesScreenGrid(
    onNextButtonClicked: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(categories) { category ->
            CategoryItem(
                category = category,
                onClick = {onNextButtonClicked(category)},
                modifier = Modifier.padding(0.dp) // idk where this padding applies to, if anywhere
            )
        }
    }


}
@Composable
fun CategoriesScreenOneCol(
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
            .padding(start = 5.dp, top = 16.dp, end = 5.dp)
            .clickable { onClick.invoke() }
        
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

            }
        }
    }
}

@Preview
@Composable
private fun CardPreview() {
    //val display = categoriesOfPlaces[0]
    //CategoryItem(categoryOfPlace = display)
}
