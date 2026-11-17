package ru.magnum.musicapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.magnum.musicapp.domain.model.AppLanguage
import ru.magnum.musicapp.domain.model.AppTheme
import javax.inject.Inject
import javax.inject.Singleton


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val themeKey = stringPreferencesKey("appTheme")
    private val languageKey = stringPreferencesKey("appLanguage")

    val selectedTheme: Flow<AppTheme> = context.dataStore.data.map { preferences ->
        val name = preferences[themeKey] ?: AppTheme.SYSTEM.name
        runCatching {
            AppTheme.valueOf(name)
        }.getOrDefault(AppTheme.SYSTEM)
    }

    val selectedLanguage: Flow<AppLanguage> = context.dataStore.data.map { preferences ->
        val code = preferences[languageKey] ?: AppLanguage.SYSTEM.code
        AppLanguage.entries.find { it.code == code } ?: AppLanguage.SYSTEM
    }

    suspend fun setTheme(theme: AppTheme) {
        context.dataStore.edit { preferences ->
            preferences[themeKey] = theme.name
        }
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { preferences ->
            preferences[languageKey] = language.code
        }
    }
}