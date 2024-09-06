package com.example.rma_proj_esus.cats.details.gallery

import com.example.rma_proj_esus.cats.details.model.CatBreedUiModel
import com.example.rma_proj_esus.cats.details.model.PhotosUiModel

data class CatBreedGalleryState(
    val catID: String,
    val fetching: Boolean = false,
    val photos : List<PhotosUiModel> = emptyList(),
    val error: DetailsError? = null,
) {
    sealed class DetailsError {
        data class DataUpdateFailed(val cause: Throwable? = null) : DetailsError()
    }
}