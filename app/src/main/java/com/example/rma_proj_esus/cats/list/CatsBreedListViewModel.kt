package com.example.rma_proj_esus.cats.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rma_proj_esus.cats.list.model.CatBreedListUiModel
import com.example.rma_proj_esus.cats.repository.CatsRepository
import com.example.rma_proj_esus.cats.room_setup.cats.BreedsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.io.IOException
import kotlin.time.Duration.Companion.seconds

class CatsBreedListViewModel(
    private val repository: CatsRepository = CatsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CatBreedListContract.CatsBreedsListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: CatBreedListContract.CatsBreedsListState.() -> CatBreedListContract.CatsBreedsListState) =
        _state.getAndUpdate(reducer)

    private val events = MutableSharedFlow<CatBreedListContract.CatsListUiEvent>()
    fun setEvent(event: CatBreedListContract.CatsListUiEvent) =
        viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        fetchBreeds()
        observeSearchQuery()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            events
                .filterIsInstance<CatBreedListContract.CatsListUiEvent.SearchQueryChanged>()
                .debounce(2.seconds)
                .collect {
                    // Called only when search query was changed
                    // and once 2 seconds have passed after the last char


                    // This is helpful to avoid trigger expensive calls
                    // on every character change
                    //s
                    //si
                    //sib
                    //sibi
                    //sibir
                    //sibirs
                }
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is CatBreedListContract.CatsListUiEvent.SearchQueryChanged -> {
                        setState {
                            copy(
                                isSearchMode = true
                            )
                        }

                        val filteredBreeds = if (it.query.isBlank()) {
                            println("the breeds they are everywhere")

                            state.value.breeds // If the query is empty, return all breeds
                        } else {
                            println("god")

                            state.value.breeds.filter { breed ->
                                breed.name.contains(it.query, ignoreCase = true)
                            }
                        }
                        setState { copy(filteredBreeds = filteredBreeds) }
                    }

                    CatBreedListContract.CatsListUiEvent.ClearSearch -> Unit
                    CatBreedListContract.CatsListUiEvent.CloseSearchMode -> setState {
                        copy(
                            isSearchMode = false
                        )
                    }


                    CatBreedListContract.CatsListUiEvent.Dummy -> Unit
                }
            }
        }
    }

    private fun fetchBreeds() {
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                val breeds: List<CatBreedListUiModel> = withContext(Dispatchers.IO) {
                    repository.fetchBreeds().map { it.asCatBreedListUiModel() }
                }
                setState {
                    copy(breeds = breeds)
                }
            } catch (error: IOException) {
                setState {
                    copy(
                        error = CatBreedListContract.CatsBreedsListState.ListError.ListUpdateFailed(
                            cause = error
                        )
                    )
                }
            } finally {
                setState { copy(fetching = false) }
                setState { copy(filteredBreeds = breeds) }
            }
        }
    }

    private fun BreedsEntity.asCatBreedListUiModel() = CatBreedListUiModel(
        id = this.id,
        name = this.name,
        alternativeName = this.alt_names,
        description = if(this.description.length>250){
            this.description.take(this.description.length/2)+"..."
        }
            else this.description,
        temperament = this.temperament.split(", ").take(3).joinToString (", ") ,
    )
}
