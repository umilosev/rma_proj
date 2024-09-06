package com.example.rma_proj_esus.cats.details.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rma_proj_esus.cats.details.CatsDetailsState
import com.example.rma_proj_esus.cats.details.model.PhotosUiModel
import com.example.rma_proj_esus.cats.list.CatBreedListContract
import com.example.rma_proj_esus.cats.repository.CatsRepository
import com.example.rma_proj_esus.cats.room_setup.photos.PhotosEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class CatBreedGalleryViewModel(
    private val catID: String,
    private val repository: CatsRepository = CatsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CatBreedGalleryState(catID = catID))
    val state = _state.asStateFlow()
    private fun setState(reducer: CatBreedGalleryState.() -> CatBreedGalleryState) =
        _state.getAndUpdate(reducer)


    init {
        fetchBreedPhotos()
    }

    private fun fetchBreedPhotos() {
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                val photos = withContext(Dispatchers.IO) {
                    repository.fetchBreedPhotos(catID = catID)
                }.map { it.asPhotosUiModel() }
                setState { copy(photos=photos) }
            } catch (error: IOException) {
                setState {
                    copy(error = CatBreedGalleryState.DetailsError.DataUpdateFailed(cause = error))
                }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }

    private fun PhotosEntity.asPhotosUiModel() = PhotosUiModel(
        id= this.id.toString(),
        url=this.url,
        width=1,
        height=1,
    )
}

