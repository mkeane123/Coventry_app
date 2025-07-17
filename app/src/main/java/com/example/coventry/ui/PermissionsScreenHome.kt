package com.example.coventry.ui

import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coventry.data.Category

import com.example.coventry.ui.utils.ContentType

//import com.example.coventry.data.categories


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PermissionsScreenHomeComposable(
    contentType: ContentType,
    onNextButtonClicked: (Category) -> Unit, // Need to change onNextButtonClicked
    modifier: Modifier = Modifier,
    viewModel: CoventryViewModel
) {
    // handle permisisons
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    //Log.d("Permission check", "hasPermissions: ${uiState.hasPermissions}")

    //val hasPermission by viewModel.hasPermissions.collectAsState()

    //val isFirstLaunch = viewModel.isFirstLaunch.collectAsState().value

    val permissions = buildList {
        add(android.Manifest.permission.RECORD_AUDIO)
        add(android.Manifest.permission.RECEIVE_SMS)
        add(android.Manifest.permission.READ_SMS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }.toTypedArray()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()

    ) { result ->
        val allGranted = result.values.all { it }
        viewModel.updatePermissionsGranted(true)
        if (allGranted) {
            viewModel.updateFirstLaunchDone()
        }
    }

    LaunchedEffect(Unit) {
        val allGranted = permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        Log.d("Permission check (inside)", "Has permissions: $allGranted")
        viewModel.updatePermissionsGranted(true)

        if (!allGranted && uiState.isFirstLaunch){
            permissionLauncher.launch(permissions)
        } else if (allGranted) {
            viewModel.updateFirstLaunchDone()
        }

    }

    if (uiState.hasPermissions){
        TestDisplay("Microphone permission granted!")
    }else{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("This app needs microphone access to work properly.")
            Spacer(modifier = Modifier.size(24.dp))
            Button(onClick = {
                permissionLauncher.launch(permissions)
                Log.d("Button Pressed", "Has permissions: ${uiState.hasPermissions}")
            }) {
                Text(text = "Grant Microphone Permission")
            }
        }
    }



}

@Composable
fun TestDisplay(
    text: String
) {
    Text(text = text)

}




@Preview
@Composable
private fun CardPreview() {
    //val display = categoriesOfPlaces[0]
    //CategoryItem(categoryOfPlace = display)
}
