package com.example.rma_proj_esus.cats.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rma_proj_esus.cats.api.photos.model.PhotosApiModel
import com.example.rma_proj_esus.cats.details.model.CatBreedUiModel
import com.example.rma_proj_esus.cats.details.model.PhotosUiModel
import com.example.rma_proj_esus.cats.list.CatBreedListContract
import com.example.rma_proj_esus.cats.repository.CatsRepository
import com.example.rma_proj_esus.cats.room_setup.cats.BreedsEntity
import com.example.rma_proj_esus.cats.room_setup.photos.PhotosEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class CatsDetailsViewModel(
    private val catID: String,
    private val repository: CatsRepository = CatsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CatsDetailsState(catID = catID))
    val state = _state.asStateFlow()
    private fun setState(reducer: CatsDetailsState.() -> CatsDetailsState) =
        _state.getAndUpdate(reducer)


    private val events = MutableSharedFlow<CatBreedListContract.CatsListUiEvent>()

    init {
        fetchBreedDetails()
    }

    private fun fetchBreedDetails() {
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                val breed = withContext(Dispatchers.IO) {
                    repository.fetchBreedDetails(catID = catID)
                }.asCatBreedUiModel()
                setState { copy(breed=breed) }
            } catch (error: IOException) {
                setState {
                    copy(error = CatsDetailsState.DetailsError.DataUpdateFailed(cause = error))
                }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }

    private fun BreedsEntity.asCatBreedUiModel() = CatBreedUiModel(
        id = this.id,
        name = this.name,
        alt_names = this.alt_names,
        description = this.description.subSequence(0, minOf(251, this.description.length)).toString(),
        temperament = this.temperament
            .split(",")
            .take(3)
            .joinToString(","),
        adaptability = this.adaptability,
        affection_level = this.affection_level,
        child_friendly = this.child_friendly,
        dog_friendly = this.dog_friendly,
        energy_level = this.energy_level,
        experimental = this.experimental,
        grooming = this.grooming,
        hairless = this.hairless,
        health_issues = this.health_issues,
        indoor = this.indoor,
        intelligence = this.intelligence,
        lap = this.lap,
        life_span = this.life_span,
        natural = this.natural,
        rare=this.rare,
        rex=this.rex,
        shedding_level = this.shedding_level,
        short_legs = this.short_legs,
        social_needs = this.social_needs,
        stranger_friendly = this.stranger_friendly,
        suppressed_tail = this.suppressed_tail,
        vocalisation = this.vocalisation,
        weight = this.weight,
        wikipedia_url = this.wikipedia_url,
    )

}
