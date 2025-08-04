package com.example.coventry.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coventry.data.model.PreviousCall
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.runtime.LaunchedEffect

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsHomeScreen(
    onNextButtonClicked: (PreviousCall) -> Unit,
    navController: NavController,
    //modifier: Modifier,
    viewModel: CoventryViewModel
) {
    val context = LocalContext.current

    LaunchedEffect(Unit){
        viewModel.checkInitialPermissions(context)
    }

    val requiredPermissions = listOf(
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.RECEIVE_SMS
    )

    val hasPermissions by viewModel.hasPermissions.collectAsState()

    var permissionsChecked by remember { mutableStateOf(hasPermissions) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val allGranted = result.values.all { it }
        viewModel.updatePermissionStatus(allGranted)
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){

        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clickable {
                    if (!hasPermissions) {
                        permissionLauncher.launch(requiredPermissions.toTypedArray())
                    } else {
                        // open app settings to revoke permissions
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                        context.startActivity(intent)
                    }
                }
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = if (hasPermissions) "Permissions granted, tap to remove" else "Tap to grant permissions",
                    fontSize = 18.sp
                )
                /*
                Switch(
                    checked = permissionsChecked,
                    onCheckedChange = {isChecked ->
                        permissionsChecked = isChecked

                        if (isChecked) {
                            permissionLauncher.launch(requiredPermissions.toTypedArray())
                        } else {
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.packageName, null)
                            )
                            context.startActivity(intent)
                        }


                    }
                )
                */
            }
        }

    }

}