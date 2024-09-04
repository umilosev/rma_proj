package com.example.rma_proj_esus.cats.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.rma_proj_esus.cats.details.model.CatBreedUiModel
import com.example.rma_proj_esus.cats.details.model.PhotosUiModel
import com.example.rma_proj_esus.core.compose.AppIconButton
import rs.edu.raf.raf_vezbe3compose.core.compose.NoDataContent


fun NavGraphBuilder.catsBreedsDetails(
    route: String,
    navController: NavController,
) = composable(
    route = route,
) { navBackStackEntry ->
    val dataId = navBackStackEntry.arguments?.getString("id")
        ?: throw IllegalArgumentException("id is required.")

    // We have to provide factory class to instantiate our view model
    // since it has a custom property in constructor
    val catsDetailsViewModel = viewModel<CatsDetailsViewModel>(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                // We pass the passwordId which we read from arguments above
                return CatsDetailsViewModel(catID = dataId) as T
            }
        },
    )
    val state = catsDetailsViewModel.state.collectAsState()

    CatsDetailsScreen(
        state = state.value,
        onClose = {
            navController.navigateUp()
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatsDetailsScreen(
    state: CatsDetailsState,
    onClose: () -> Unit,
) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = state.breed?.name ?: "Loading")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                actions = {

                },
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onClose,
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (state.fetching) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (state.error != null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        val errorMessage = when (state.error) {
                            is CatsDetailsState.DetailsError.DataUpdateFailed ->
                                "Failed to load. Error message: ${state.error.cause?.message}."
                        }
                        Text(text = errorMessage)
                    }
                } else if (state.breed != null) {
                    CatsBreedsColumn(
                        data = state.breed,
                        photos = state.photo,
                    )
                } else {
                    NoDataContent(id = state.catID)
                }
            }
        }
    )
}

@Composable
private fun CatsBreedsColumn(
    data: CatBreedUiModel,
    photos: List<PhotosUiModel>,

    ) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        photos.forEach { photo ->
            // Load the image using Coil
            val painter = // You can customize the image loading here
                // For example, you can set crossfade, placeholder, error placeholder, etc.
                rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = photo.url)
                        .apply(block = fun ImageRequest.Builder.() {
                            // You can customize the image loading here
                            // For example, you can set crossfade, placeholder, error placeholder, etc.
                        }).build()
                ).also {
                    // Display the image
                    Image(
                        painter = it,
                        contentDescription = "Breed Image",
                        modifier = Modifier
                            .height(300.dp)
                            .width(300.dp)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Crop
                    )
                }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.headlineSmall,
            text = data.description,
            onTextLayout = {}
        )



        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = "Origin : " + data.origin,
            onTextLayout = {}
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = "Temperament : " + data.temperament,
            onTextLayout = {}
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = "Average age : " + data.life_span,
            onTextLayout = {}
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = "Average weight : " + data.weight,
            onTextLayout = {}
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                text = "Energy level :" + data.energy_level.toString(),
                onTextLayout = {}
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                text = "Affection level :" + data.affection_level.toString(),
                onTextLayout = {}
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                text = "Family friendly :" + data.child_friendly.toString(),
                onTextLayout = {}
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                text = "Grooming :" + data.grooming.toString(),
                onTextLayout = {}
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                text = "Intelligence :" + data.intelligence.toString(),
                onTextLayout = {}
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        val context = LocalContext.current

        Button(
            modifier = Modifier.width(250.dp).height(100.dp)
                .background(Color.White),

            onClick = {
            val url = data.wikipedia_url
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }
        ) {
            Text(text = "Wikipedia")
        }

        Spacer(modifier = Modifier.height(32.dp))

    }

}


/*
@Preview
@Composable
fun PreviewDetailsScreen() {
    Surface {
        CatsDetailsScreen(
            state = CatsDetailsState(
                catID = "124124",
                breed = CatBreedUiModel(
                    id = "1",
                    name = "Maine Coon",
                    life_span = "12-14",
                    alt_names = "Thai",
                    description = "The Maine Coon is one of the largest domesticated cat breeds. It has a distinctive physical appearance and valuable hunting skills.",
                    temperament = "Gentle, Friendly, Intelligent",
                    origin = "United States",
                    weight = Weight(
                        metric = "3-5",
                        imperial = "7-10"
                    ), // kg
                    affection_level = 5,
                    adaptability = 4,
                    child_friendly = 3,
                    health_issues = 2,
                    grooming = 5,
                    energy_level = 1,
                    intelligence = 5,
                    rare = 3,
                    shedding_level = 5,
                    social_needs = 3,
                    stranger_friendly = 1,
                    vocalisation = 5,
                    wikipedia_url = "https:www.google.com",
                    dog_friendly = 3,
                    experimental = 4,
                    indoor = 5,
                    hairless=1,
                    lap=5,
                    rex=3,
                    natural = 1,
                    short_legs = 1,
                    suppressed_tail = 1,
                ),
            ),
            onClose = {},
        )
    }
}*/
