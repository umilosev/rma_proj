package com.example.rma_proj_esus.cats.details

import com.example.rma_proj_esus.cats.details.model.CatBreedUiModel
import com.example.rma_proj_esus.cats.details.model.PhotosUiModel


data class CatsDetailsState(
    val catID: String,
    val fetching: Boolean = false,
    val breed: CatBreedUiModel? = null,
    val photo: List<PhotosUiModel> = emptyList(),
    val error: DetailsError? = null,
) {
    sealed class DetailsError {
        data class DataUpdateFailed(val cause: Throwable? = null) : DetailsError()
    }
}
