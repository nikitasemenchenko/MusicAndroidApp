package ru.magnum.musicapp.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun RunningText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle,
    color: Color,
    textAlign: TextAlign? = null
) {
    var isOverflowing by remember(text) { mutableStateOf(false) }
    var isScrolling by remember(text) { mutableStateOf(false) }
    val scrollState = remember(text) { ScrollState(0) }
    val density = LocalDensity.current

    LaunchedEffect(text, isOverflowing, scrollState, density) {
        if (!isOverflowing) return@LaunchedEffect

        while (true) {
            delay(1_000L.milliseconds)
            isScrolling = true
            try {
                val maxScroll = snapshotFlow { scrollState.maxValue }
                    .first { it > 0 }
                val distanceDp = with(density) { maxScroll.toDp().value }
                val durationMillis = (distanceDp / 30f * 1_000L)
                    .toInt()
                    .coerceAtLeast(1)
                scrollState.animateScrollTo(
                    value = maxScroll,
                    animationSpec = tween(
                        durationMillis = durationMillis,
                        easing = LinearEasing
                    )
                )
                delay(1_000L.milliseconds)
                scrollState.animateScrollTo(
                    value = 0,
                    animationSpec = tween(
                        durationMillis = durationMillis,
                        easing = LinearEasing
                    )
                )
            } finally {
                withContext(NonCancellable) {
                    scrollState.scrollTo(0)
                }
                isScrolling = false
            }
        }
    }

    Box(modifier = modifier) {
        Text(
            text = text,
            modifier = if (isScrolling) {
                Modifier
                    .alpha(0f)
                    .clearAndSetSemantics {}
            } else {
                Modifier
            },
            style = style,
            color = color,
            textAlign = textAlign,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { layoutResult ->
                isOverflowing = layoutResult.hasVisualOverflow
            }
        )
        if (isScrolling) {
            Text(
                text = text,
                modifier = Modifier.horizontalScroll(scrollState),
                style = style,
                color = color,
                textAlign = textAlign,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip
            )
        }
    }
}