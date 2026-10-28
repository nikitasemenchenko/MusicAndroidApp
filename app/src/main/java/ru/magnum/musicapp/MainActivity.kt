package ru.magnum.musicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import ru.magnum.musicapp.presentation.navigation.AppNavigation
import ru.magnum.musicapp.ui.theme.MusicAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicAppTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .statusBarsPadding()
                ) {
                    AppNavigation()
                }
            }
        }
    }
}