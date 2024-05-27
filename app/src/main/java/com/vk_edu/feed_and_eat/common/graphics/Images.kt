package com.vk_edu.feed_and_eat.common.graphics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vk_edu.feed_and_eat.R

@Composable
fun DishImage(link: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest
            .Builder(context = LocalContext.current)
            .data(link)
            .crossfade(true)
            .build(),
        error = painterResource(R.drawable.broken_image),
        placeholder = painterResource(R.drawable.loading_image),
        contentDescription = stringResource(R.string.imageDescription),
        contentScale = ContentScale.Crop,
        modifier = modifier.aspectRatio(4f / 3f)
    )
}

@Composable
fun ProfileImage(link: Any?, onClick: () -> Unit, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest
            .Builder(context = LocalContext.current)
            .data(link)
            .crossfade(true)
            .build(),
        error = painterResource(R.drawable.broken_image),
        placeholder = painterResource(R.drawable.loading_image),
        contentDescription = stringResource(R.string.user_profile_icon),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(100.dp)
            .clickable(onClick = onClick)
    )
}

@Composable
fun DefaultProfileImage(onClick: () -> Unit, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest
            .Builder(context = LocalContext.current)
            .data(R.drawable.user_default_icon)
            .crossfade(true)
            .build(),
        error = painterResource(R.drawable.broken_image),
        placeholder = painterResource(R.drawable.user_default_icon),
        contentDescription = stringResource(R.string.user_profile_icon),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(100.dp)
            .clickable(onClick = onClick)
    )
}