package com.example.coventry

//import androidx.compose.foundation.layout.FlowRowScopeInstance.weight
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.coventry.R
import com.example.coventry.ui.CoventryViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.coventry.data.Category
import com.example.coventry.ui.PlacesHome
import com.example.coventry.ui.PlacesScreen
import com.example.coventry.ui.utils.ContentType
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.example.coventry.data.Place
import com.example.coventry.data.PreviousCall
import com.example.coventry.ui.HomeScreen
import com.example.coventry.ui.HowToUseAppHomeScreen
import com.example.coventry.ui.IndividualPreviousCallHome
import com.example.coventry.ui.IndividualPreviousTextHome
import com.example.coventry.ui.OnBoardingScreenHome
import com.example.coventry.ui.OnLiveCallHome
import com.example.coventry.ui.PastCallsHome
import com.example.coventry.ui.PastTextsHome
//import com.example.coventry.ui.CategoriesScreenHome

import com.example.coventry.ui.PermissionsScreenHomeComposable
import com.example.coventry.ui.SettingsHomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoventryAppBar(
    currentScreen: CoventryScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(stringResource(currentScreen.title))
                },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CoventryApp(
    windowSize: WindowWidthSizeClass,
    viewModel: CoventryViewModel,
    navController: NavHostController = rememberNavController()
) {

    val contentType: ContentType
    when(windowSize){
        WindowWidthSizeClass.Compact -> {
            contentType = ContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Medium -> {
            contentType = ContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Expanded -> {
            contentType = ContentType.LIST_AND_DETAIL
        }
        else -> {
            contentType = ContentType.LIST_ONLY
        }

    }

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = CoventryScreen.valueOf(
        backStackEntry?.destination?.route ?: CoventryScreen.Start.name
    )

    // check if first launch and permissions and load screen accordingly
    val isFirstLaunch by viewModel.isFirstLaunch.collectAsState()
    //val isFirstLaunch = viewModel.isFirstLaunch.collectAsState()
    val hasPermissions by viewModel.hasPermissions.collectAsState()

    val uiState by viewModel.uiState.collectAsState()

    val onCall = uiState.onCall

    //var startDestination: String? = null

    /*
    when (isFirstLaunch.value) {
        null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        true -> {
            startDestination = CoventryScreen.OnBoardingScreen.name
        }
        false -> {
            if (hasPermissions) startDestination = CoventryScreen.HomeScreen.name else CoventryScreen.Start.name

        }
    }

     */


    val startDestination = if (!isFirstLaunch && hasPermissions) {
            CoventryScreen.OnBoardingScreen.name

    } else {
        CoventryScreen.Start.name // take users to permissions screen
    }



    Scaffold(
        topBar = {
            CoventryAppBar(
                //currentSelectedCat = viewModel.currentSelCat,
                canNavigateBack = navController.previousBackStackEntry != null, // if we came from somewhere we can go back
                navigateUp = { navController.navigateUp() },
                currentScreen = currentScreen
            )
        }

    ){ innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        if (startDestination != null) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(route = CoventryScreen.Start.name) {
                    // this will take you to the categories screen which is the start
                    PermissionsScreenHomeComposable(
                        contentType = contentType,
                        onNextButtonClicked = {

                            viewModel.updatePermissionsGranted(true)
                            viewModel.updateFirstLaunchDone()
                            navController.navigate(CoventryScreen.Places.name) {
                                popUpTo(CoventryScreen.Start.name) { inclusive = true}
                            }
                            viewModel.setIsShowingHomePage(true)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(id = R.dimen.padding_medium)),
                        viewModel = viewModel
                    )

                }
                composable(route = CoventryScreen.Places.name) {
                    val context = LocalContext.current
                    PlacesHome(
                        contentType=contentType,
                        uiState = uiState,
                        onNextButtonClicked = {

                            viewModel.setIsShowingHomePage(!uiState.isShowingHomePage)
                        },
                        onDetailScreenBackPressed = {viewModel.setIsShowingHomePage(!uiState.isShowingHomePage)}// maybe make isShowing home page true, this might be best moved elsewhere though
                    )
                }
                composable(route = CoventryScreen.OnLiveCall.name) {
                    val context = LocalContext.current
                    OnLiveCallHome(
                        onReportButtonClicked = {viewModel.reportCall()},
                        onBlockButtonClicked = {viewModel.blockCaller()},
                        onEndCallButtonClicked = {viewModel.endCall()},
                        viewModel = viewModel,
                        navController = navController
                    )
                }
                composable(route = CoventryScreen.HomeScreen.name) {
                    val context = LocalContext.current
                    HomeScreen(
                        navController = navController,
                        viewModel = viewModel
                    )
                }

                composable(route = CoventryScreen.PreviousTextsScreen.name) {
                    PastTextsHome(
                        contentType = ContentType.LIST_ONLY,
                        onIndividualCardPressed = {},
                        viewModel = viewModel,
                        navController = navController
                    )

                }

                composable(route = CoventryScreen.PreviousCallsScreen.name) {
                    PastCallsHome(
                        contentType = ContentType.LIST_ONLY,
                        onIndividualCardPressed = {},
                        viewModel = viewModel,
                        navController = navController
                    )

                }

                composable(route = CoventryScreen.SettingsScreen.name) {
                    SettingsHomeScreen(
                        onNextButtonClicked = {},
                        viewModel = viewModel,
                        navController = navController
                    )

                }

                composable(route = CoventryScreen.HowToUseAppScreen.name) {
                    HowToUseAppHomeScreen(
                        viewModel = viewModel,
                        navController = navController
                    )

                }

                composable(route = CoventryScreen.IndividualPastCallScreen.name) {
                    IndividualPreviousCallHome(
                        onReportButtonClicked = {viewModel.reportCall()},
                        onBlockButtonClicked = {viewModel.blockCaller()},
                        viewModel = viewModel,
                        navController = navController,
                        previousCall = uiState.currentSelectedPastCall
                    )

                }

                composable(route = CoventryScreen.IndividualPastTextScreen.name) {
                    IndividualPreviousTextHome(
                        onReportButtonClicked = {viewModel.reportCall()},
                        onBlockButtonClicked = {viewModel.blockCaller()},
                        viewModel = viewModel,
                        navController = navController,
                        previousText = uiState.currentSelectedPastText
                    )

                }

                composable(route = CoventryScreen.OnBoardingScreen.name) {
                    OnBoardingScreenHome(
                        navController = navController,
                        viewModel = viewModel,
                        onGetStarted = {
                            viewModel.setFirstLaunchDone()
                            viewModel.updateFirstLaunchDone()
                            navController.navigate(CoventryScreen.HomeScreen.name) {
                                popUpTo(0) {inclusive = true}
                                launchSingleTop = true
                            }
                        }
                    )

                }
            }
        }
    }
}

enum class CoventryScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Categories(title = R.string.categories),
    Places(title = R.string.choose_place),
    OnLiveCall(title = R.string.on_live_call_home),
    HomeScreen(title= R.string.home_screen),
    PreviousCallsScreen(title = R.string.previous_calls_screen),
    PreviousTextsScreen(title = R.string.previous_texts_screen),
    SettingsScreen(title = R.string.settings),
    HowToUseAppScreen(title = R.string.how_to_use_app),
    IndividualPastCallScreen(title = R.string.individualPastCallScreen),
    IndividualPastTextScreen(title = R.string.individualPastTextScreen),
    OnBoardingScreen(title = R.string.on_boarding_screen)
}

@Preview
@Composable
private fun DisplayCard() {
}