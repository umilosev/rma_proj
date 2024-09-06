package com.example.rma_proj_esus.cats.details.gallery

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.rma_proj_esus.cats.details.model.PhotosUiModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.rma_proj_esus.cats.details.CatsDetailsScreen
import com.example.rma_proj_esus.cats.details.CatsDetailsViewModel
import com.example.rma_proj_esus.cats.list.CatBreedListContract

fun NavGraphBuilder.catBreedGalleryScreen(
    route: String,
    navController: NavController,
) = composable(
    route = route,
) { navBackStackEntry ->
    val dataId = navBackStackEntry.arguments?.getString("id")
        ?: throw IllegalArgumentException("id is required.")

    // We have to provide factory class to instantiate our view model
    // since it has a custom property in constructor
    val catBreedGalleryViewModel = viewModel<CatBreedGalleryViewModel>(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                // We pass the passwordId which we read from arguments above
                return CatBreedGalleryViewModel(catID = dataId) as T
            }
        },
    )
    val state = catBreedGalleryViewModel.state.collectAsState()

    CatBreedGalleryScreen(
        state = state.value,
        photos = state.value.photos,
        navController = navController
    )
}


@Composable
fun CatBreedGalleryScreen(
    state: CatBreedGalleryState,
    photos: List<PhotosUiModel>,
    navController: NavController
) {
    var selectedPhoto by remember { mutableStateOf<PhotosUiModel?>(null) }

    if (selectedPhoto != null) {
        FullScreenPhotoViewer(photo = selectedPhoto!!) {
            selectedPhoto = null // Close fullscreen viewer when clicked
        }
    } else {
        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            items(photos) { photo ->
                GalleryItem(photo) {
                    selectedPhoto = photo // Open photo in fullscreen
                }
            }
        }
    }
}

@Composable
fun FullScreenPhotoViewer(photo: PhotosUiModel, onClose: () -> Unit) {
    val transition = updateTransition(targetState = photo, label = "Photo Transition")

    // Crossfade animation
    val alpha by transition.animateFloat(label = "Alpha Transition") {
        if (it == null) 0f else 1f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { onClose() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(photo.url),
            contentDescription = "Fullscreen Photo",
            modifier = Modifier
                .fillMaxSize()
                .alpha(alpha), // Apply crossfade
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun GalleryItem(photo: PhotosUiModel, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .size(150.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = rememberAsyncImagePainter(photo.url),
            contentDescription = "Gallery Image",
            contentScale = ContentScale.Crop
        )
    }
}

