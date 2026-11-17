package ru.magnum.musicapp.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.magnum.musicapp.R
import ru.magnum.musicapp.domain.model.AppLanguage
import ru.magnum.musicapp.domain.model.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val selectedTheme by viewModel.selectedTheme.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(R.string.settings_title))
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            //Тема
            Text(
                text = stringResource(R.string.settings_theme_label),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            OptionItem(
                title = stringResource(R.string.settings_theme_system),
                selected = selectedTheme == AppTheme.SYSTEM,
                onClick = { viewModel.setTheme(AppTheme.SYSTEM)}
            )
            OptionItem(
                title = stringResource(R.string.settings_theme_light),
                selected = selectedTheme == AppTheme.LIGHT,
                onClick = { viewModel.setTheme(AppTheme.LIGHT)}
            )
            OptionItem(
                title = stringResource(R.string.settings_theme_dark),
                selected = selectedTheme == AppTheme.DARK,
                onClick = { viewModel.setTheme(AppTheme.DARK)}
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            //Язык
            Text(
                text = stringResource(R.string.settings_language_label),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            OptionItem(
                title = stringResource(R.string.settings_language_system),
                selected = selectedLanguage == AppLanguage.SYSTEM,
                onClick = { viewModel.setLanguage(AppLanguage.SYSTEM)}
            )
            OptionItem(
                title = stringResource(R.string.settings_language_ru),
                selected = selectedLanguage == AppLanguage.RUSSIAN,
                onClick = { viewModel.setLanguage(AppLanguage.RUSSIAN)}
            )
            OptionItem(
                title = stringResource(R.string.settings_language_en),
                selected = selectedLanguage == AppLanguage.ENGLISH,
                onClick = { viewModel.setLanguage(AppLanguage.ENGLISH)}
            )
        }
    }
}

@Composable
private fun OptionItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}