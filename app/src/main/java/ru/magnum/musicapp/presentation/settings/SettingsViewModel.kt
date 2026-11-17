package ru.magnum.musicapp.presentation.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.magnum.musicapp.data.repository.UserPreferencesRepository
import ru.magnum.musicapp.domain.model.AppLanguage
import ru.magnum.musicapp.domain.model.AppTheme
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: UserPreferencesRepository
): ViewModel() {
    val selectedTheme = preferencesRepository.selectedTheme.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppTheme.SYSTEM
    )

    val selectedLanguage = preferencesRepository.selectedLanguage.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppLanguage.SYSTEM
    )

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            preferencesRepository.setTheme(theme)
        }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch {
            preferencesRepository.setLanguage(language)
            val targetTags = language.code
            val currentTags = AppCompatDelegate.getApplicationLocales().toLanguageTags()
            if (currentTags != targetTags) {
                val locale = if (targetTags.isEmpty()) {
                    LocaleListCompat.getEmptyLocaleList()
                } else {
                    LocaleListCompat.forLanguageTags(targetTags)
                }
                AppCompatDelegate.setApplicationLocales(locale)
            }
        }
    }

}