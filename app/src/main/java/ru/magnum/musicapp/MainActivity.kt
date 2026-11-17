package ru.magnum.musicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import ru.magnum.musicapp.data.repository.UserPreferencesRepository
import ru.magnum.musicapp.domain.model.AppLanguage
import ru.magnum.musicapp.domain.model.AppTheme
import ru.magnum.musicapp.presentation.navigation.AppNavigation
import ru.magnum.musicapp.ui.theme.MusicAppTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val selectedTheme by preferencesRepository.selectedTheme.collectAsStateWithLifecycle(
                initialValue = AppTheme.SYSTEM
            )

            val useDarkTheme = when (selectedTheme) {
                AppTheme.DARK -> true
                AppTheme.LIGHT -> false
                AppTheme.SYSTEM -> isSystemInDarkTheme()
            }

            MusicAppTheme(darkTheme = useDarkTheme) {
                AppNavigation()
            }
        }
    }
}