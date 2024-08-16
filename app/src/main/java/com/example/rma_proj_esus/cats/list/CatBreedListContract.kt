package com.example.rma_proj_esus.cats.list

import com.example.rma_proj_esus.cats.list.model.CatBreedListUiModel

interface CatBreedListContract {

    data class CatsBreedsListState(
        val fetching: Boolean = false,
        val breeds: List<CatBreedListUiModel> = emptyList(),
        val filteredBreeds:List<CatBreedListUiModel> = emptyList(),
        val isSearchMode: Boolean = false,
        val error: ListError? = null
    ) {
        sealed class ListError {
            data class ListUpdateFailed(val cause: Throwable? = null) : ListError()
        }
    }
    sealed class CatsListUiEvent {
        data class SearchQueryChanged(val query: String) : CatsListUiEvent()
        data object ClearSearch : CatsListUiEvent()
        data object CloseSearchMode : CatsListUiEvent()
        data object Dummy : CatsListUiEvent()
    }
}