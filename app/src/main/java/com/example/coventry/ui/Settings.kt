package com.example.coventry.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coventry.data.model.PreviousCall
import androidx.activity.result.contract.ActivityResultContracts

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsHomeScreen(
    onNextButtonClicked: (PreviousCall) -> Unit,
    navController: NavController,
    viewModel: CoventryViewModel
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
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

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // Permissions card
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable {
                        if (!hasPermissions) {
                            permissionLauncher.launch(requiredPermissions.toTypedArray())
                        } else {
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.packageName, null)
                            )
                            context.startActivity(intent)
                        }
                    }
            ) {
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
                }
            }

            // Delete calls card
            DeleteCallsCard(
                viewModel = viewModel,
                context = context,
                snackbarHostState = snackbarHostState
            )

            // Delete texts card with confirmation + snackbar
            DeleteTextsCard(
                viewModel = viewModel,
                context = context,
                snackbarHostState = snackbarHostState
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeleteTextsCard(
    viewModel: CoventryViewModel,
    context: Context,
    snackbarHostState: SnackbarHostState
) {
    var showDialogue by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }

    if (showDialogue) {
        AlertDialog(
            onDismissRequest = { showDialogue = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete all saved texts from the app? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearTextsDB(context)
                    showDialogue = false
                    showSnackbar = true
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialogue = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showSnackbar) {
        LaunchedEffect(Unit) {
            snackbarHostState.showSnackbar("Texts deleted")
            showSnackbar = false
        }
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { showDialogue = true }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Delete saved texts from app",
                fontSize = 18.sp
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeleteCallsCard(
    viewModel: CoventryViewModel,
    context: Context,
    snackbarHostState: SnackbarHostState
) {
    var showDialogue by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }

    if (showDialogue) {
        AlertDialog(
            onDismissRequest = { showDialogue = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete all saved calls from the app? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearCallsDB(context)
                    showDialogue = false
                    showSnackbar = true
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialogue = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showSnackbar) {
        LaunchedEffect(Unit) {
            snackbarHostState.showSnackbar("Calls deleted")
            showSnackbar = false
        }
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { showDialogue = true }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Delete saved Calls from app",
                fontSize = 18.sp
            )
        }
    }
}
