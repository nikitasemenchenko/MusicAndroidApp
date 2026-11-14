package ru.magnum.musicapp.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import ru.magnum.musicapp.R

@Composable
fun CoverImage(
    model: String?,
    modifier: Modifier = Modifier
){
    AsyncImage(
        model = model,
        contentDescription = stringResource(R.string.cover_content_description),
        placeholder = painterResource(R.drawable.no_cover_placeholder),
        error = painterResource(R.drawable.no_cover_placeholder),
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}