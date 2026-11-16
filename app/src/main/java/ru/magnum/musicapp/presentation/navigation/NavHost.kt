package ru.magnum.musicapp.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.magnum.musicapp.R
import ru.magnum.musicapp.presentation.catalog.CatalogScreen
import ru.magnum.musicapp.presentation.components.MiniPlayer
import ru.magnum.musicapp.presentation.utils.toStringRes
import ru.magnum.musicapp.presentation.player.PlayerScreen
import ru.magnum.musicapp.presentation.player.PlayerViewModel
import ru.magnum.musicapp.presentation.trackInfo.TrackInfoScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showMiniPlayer = currentDestination?.hierarchy?.any {
        it.hasRoute<CatalogScreen>()
    } == true

    val playerViewModel: PlayerViewModel = hiltViewModel()
    val playerUiState by playerViewModel.uiState.collectAsStateWithLifecycle()
    val playbackState by playerViewModel.playbackState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val error = playbackState.error
    val message = error?.let { stringResource(it.toStringRes()) }
    val retryLabel = stringResource(R.string.retry)

    LaunchedEffect(error) {
        error ?: return@LaunchedEffect
        val result = snackbarHostState.showSnackbar(
            message = message!!,
            actionLabel = retryLabel,
            withDismissAction = true,
            duration = SnackbarDuration.Long
        )
        if (result == SnackbarResult.ActionPerformed) {
            playerViewModel.togglePlayback()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showMiniPlayer) {
                playerUiState.currentTrack?.let { track ->
                    MiniPlayer(
                        track = track,
                        onOpenPlayer = {
                            navController.navigate(PlayerScreen)
                        },
                        onTogglePlayback = playerViewModel::togglePlayback,
                        onSkipToPrevious = playerViewModel::seekToPreviousTrack,
                        onSkipToNext = playerViewModel::seekToNextTrack,
                        playbackState = playbackState,
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CatalogScreen,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<CatalogScreen> {
                CatalogScreen()
            }

            composable<PlayerScreen> {
                PlayerScreen(
                    viewModel = playerViewModel,
                    onCollapse = { navController.popBackStack() },
                    onOpenTrackInfo = { trackId ->
                        navController.navigate(TrackInfoScreen(trackId))
                    }
                )
            }

            composable<TrackInfoScreen> {
                TrackInfoScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}