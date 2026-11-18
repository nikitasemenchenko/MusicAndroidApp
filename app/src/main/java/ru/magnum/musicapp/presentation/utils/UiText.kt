package ru.magnum.musicapp.presentation.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    // для обычных строк (например, текст ошибки с бэкенда)
    data class DynamicString(val value: String) : UiText

    //Для строк из ресурсов strings.xml
    class StringResource(
        @StringRes val resId: Int,
        val args: Array<Any> = emptyArray()
    ) : UiText

    // Метод для получения строки
    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
        }
    }
}