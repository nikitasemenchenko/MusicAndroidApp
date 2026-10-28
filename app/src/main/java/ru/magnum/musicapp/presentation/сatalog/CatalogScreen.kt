package ru.magnum.musicapp.presentation.сatalog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel = hiltViewModel()
){
    val uiState by viewModel.state.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
    ) {
        items(uiState.tracks, key = {it.id}) { track ->
            Text(
                text = track.title,
                modifier = Modifier.padding(8.dp)
            )
        }

    }
}