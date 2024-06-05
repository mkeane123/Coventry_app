package com.example.coventry.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coventry.data.Category

@Composable
fun PlacesScreen(
    selectedCategory: Category,
    onNextButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    Text(
        text = selectedCategory.id.toString(),
        fontSize = 60.sp
    )

}