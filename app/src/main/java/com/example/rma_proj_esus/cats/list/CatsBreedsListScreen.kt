package com.example.rma_proj_esus.cats.list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.rma_proj_esus.cats.list.model.CatBreedListUiModel


@ExperimentalMaterial3Api
fun NavGraphBuilder.catBreedsListScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val catsBreedsListViewModel = viewModel<CatsBreedListViewModel>()
    val state by catsBreedsListViewModel.state.collectAsState()
    Log.d("State", "State je : $state")
    CatsBreedsListScreen(
        state = state,
        onItemClick = {
            navController.navigate(route = "cats/breed/${it.id}")
            catsBreedsListViewModel.setEvent(CatBreedListContract.CatsListUiEvent.CloseSearchMode)
        },
        eventPublisher = {
            catsBreedsListViewModel.setEvent(it)
        },
    )
}


@ExperimentalMaterial3Api
@Composable
fun CatsBreedsListScreen(
    state: CatBreedListContract.CatsBreedsListState,
    eventPublisher: (uiEvent: CatBreedListContract.CatsListUiEvent) -> Unit,
    onItemClick: (CatBreedListUiModel) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "la lista de gatos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            )
        },

        content = {

            CatsBreedsList(
                paddingValues = it,
                items = if (state.isSearchMode) state.filteredBreeds else state.breeds,
                onItemClick = onItemClick,
                state = state,
            )

            if (state.filteredBreeds.isEmpty()) {
                when (state.fetching) {
                    true -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    false -> {
                        if (state.error != null) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                val errorMessage = when (state.error) {
                                    is CatBreedListContract.CatsBreedsListState.ListError.ListUpdateFailed ->
                                        "Failed to load. Error message: ${state.error.cause?.message}."
                                }
                                Text(text = errorMessage)
                            }
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(text = "No cats.")
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun CatsBreedsList(
    items: List<CatBreedListUiModel>,
    state: CatBreedListContract.CatsBreedsListState,
    paddingValues: PaddingValues,
    onItemClick: (CatBreedListUiModel) -> Unit
) {
    // LazyColumn should be used for infinite lists which we will
    // learn soon. In the meantime we can use Column with verticalScroll
    // modifier so it can be scrollable.
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        val catsBreedsListViewModel = viewModel<CatsBreedListViewModel>()

        var searchText by remember { mutableStateOf("") }

        TextField(
            value = searchText,
            onValueChange = { newText ->
                println("is this being called: $newText - $searchText")
                searchText = newText
                catsBreedsListViewModel.setEvent(
                    CatBreedListContract.CatsListUiEvent.SearchQueryChanged(
                        searchText
                    )
                )
            },
            label = { Text("Buscar gatos por raza") },
            modifier = Modifier
                .width(360.dp)
                .height(75.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        items.forEach {
            Column {
                key(it.alternativeName) {
                    CatBreedsListItem(
                        data = it,
                    ) {
                        onItemClick(it)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

    }
}


@Composable
private fun CatBreedsListItem(
    data: CatBreedListUiModel,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
            },
    ) {
        Text(
            modifier = Modifier.padding(all = 16.dp),
            text = data.name
        )
        Text(
            modifier = Modifier.padding(all = 16.dp),
            text = data.description
        )
        Row(
            modifier = Modifier.padding(16.dp)
        ){
            Card(
                modifier=Modifier.padding(16.dp).fillMaxWidth().background(Color.Black)
            )
            {
                Text(
                modifier = Modifier.padding(all = 16.dp),
                text = data.temperament
                )
            }
        }


        if(data.alternativeName.isNotBlank())Text(
            modifier = Modifier.padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .weight(weight = 1f),
            text = data.alternativeName
        )

        Row {
            Text(
                modifier = Modifier.padding(all = 16.dp),
                text = "Aprende más sobre esta gatita aquí"
            )
            Icon(
                modifier = Modifier.padding(end = 16.dp),
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
            )
        }

    }
}


/*@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewPasswordListScreen() {
    Rma_ProjTheme {
        CatsBreedsListScreen(
            state = CatBreedListContract.CatsBreedsListState(breeds = SampleData),
            onItemClick = {},
            eventPublisher = {},
        )
    }
}*/
