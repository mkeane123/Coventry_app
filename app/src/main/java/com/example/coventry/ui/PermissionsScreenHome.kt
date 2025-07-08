package com.example.coventry.ui

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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


@Composable
fun PermissionsScreenHomeComposable(
    contentType: ContentType,
    onNextButtonClicked: (Category) -> Unit, // Need to change onNextButtonClicked
    modifier: Modifier = Modifier,
    viewModel: CoventryViewModel
) {
    // handle permisisons
    val context = LocalContext.current
    val hasPermission by viewModel.hasPermissions.collectAsState()

    val permissions = arrayOf(
        android.Manifest.permission.RECORD_AUDIO,
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()

    ) { result ->
        val allGranted = result.values.all { it }
        viewModel.updatePermissionsGranted(allGranted)
        if (allGranted) {
            viewModel.updateFirstLaunchDone()
        }
    }

    LaunchedEffect(Unit) {
        val allGranted = permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        if (allGranted) {
            viewModel.updatePermissionsGranted(true)
            viewModel.updateFirstLaunchDone()
        } else {
            permissionLauncher.launch(permissions)
        }
    }
/* // In here is where actually get the permissions
    if (viewModel.hasPermissions.collectAsState().value) {
        // You might pre-fill with a default category or let the user select
        onNextButtonClicked(Category("Default", "default")) // Replace this appropriately
    } else {
        // Display UI explaining the need for permissions
        Text(
            "Permissions are required to continue.",
            modifier = modifier.padding(16.dp)
        )
    }

 */

    if (hasPermission) { // if this is true
        TestDisplay("Have permissions")
    } else {
        Column(
            modifier = modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("This app needs microphone access to work")
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
