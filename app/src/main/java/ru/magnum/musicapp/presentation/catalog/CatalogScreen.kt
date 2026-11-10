package ru.magnum.musicapp.presentation.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.magnum.musicapp.data.exoplayer.PlayerConnectionStatus

@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel = hiltViewModel(),
    toPlayerScreen: () -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val playerConnectionStatus by viewModel.playerConnectionStatus.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (playerConnectionStatus == PlayerConnectionStatus.CONNECTING) {
            Text(
                "Подключение к плееру…",
                modifier = Modifier.padding(16.dp)
            )
        }
        if (playerConnectionStatus == PlayerConnectionStatus.ERROR) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Не удалось подключиться к плееру",
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = { viewModel.retryPlayerConnection() }
                ) {
                    Text("Повторить")
                }
            }
        }
        if (uiState.isSyncing && uiState.tracks.isNotEmpty()) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        Box(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()) {
            when {
                uiState.tracks.isNotEmpty() ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        itemsIndexed(
                            uiState.tracks,
                            key = { _, track ->
                                track.id
                            }
                        ) { index, track ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable {
                                        viewModel.play(
                                            uiState.tracks,
                                            index
                                        )
                                        toPlayerScreen()
                                    }
                            ) {
                                Text(
                                    text = track.title,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }

                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.errorMessage ?: "Произошла ошибка",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Button(
                            onClick = { viewModel.getTracks() },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Повторить")
                        }
                    }
                }

                else -> {
                    Text(
                        "Каталог пуст",
                        modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}