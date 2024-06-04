package com.example.coventry

//import androidx.compose.foundation.layout.FlowRowScopeInstance.weight
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.coventry.R
import com.example.coventry.ui.CoventryViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.coventry.ui.CategoriesScreen
import com.example.coventry.ui.PlaceScreen
import com.example.coventry.ui.PlacesScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoventryAppBar(
    currentScreen: CoventryScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
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


@Composable
fun CoventryApp(
    viewModel: CoventryViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = CoventryScreen.valueOf(
        backStackEntry?.destination?.route ?: CoventryScreen.Start.name
    )

    Scaffold(
        topBar = {
            CoventryAppBar(
                canNavigateBack = navController.previousBackStackEntry != null, // if we came from somewhere we can go back
                navigateUp = { navController.navigateUp() },
                currentScreen = currentScreen
            )
        }

    ){ innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = CoventryScreen.Start.name,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = CoventryScreen.Start.name) {
                // this will take you to the places in one category
                CategoriesScreen(
                    onNextButtonClicked = {
                        // maybe need to call a view model function
                        navController.navigate(CoventryScreen.Places.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(id = R.dimen.padding_medium))
                )
            }
            composable(route = CoventryScreen.Places.name) {
                val context = LocalContext.current
                PlacesScreen(
                    onNextButtonClicked = {}
                )
            }
            composable(route = CoventryScreen.Place.name) {
                PlaceScreen()
            }

        }
    }
}

//Categories, Places, Place
enum class CoventryScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Categories(title = R.string.choose_category),
    Places(title = R.string.choose_place),
    Place(title = R.string.shown_place)
}



@Preview
@Composable
private fun DisplayCard() {
    //val place: Place = LocalRecommendationsDataProvider.defaultPlace
    //PlaceDetail(place = place)
}