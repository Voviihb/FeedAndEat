package com.vk_edu.feed_and_eat.common.graphics

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.ui.theme.MediumIconSize
import com.vk_edu.feed_and_eat.ui.theme.SmallIconSize

@Composable
fun SmallIcon(painter: Painter, color: Color, modifier: Modifier = Modifier) {
    Icon(
        painter = painter,
        tint = color,
        contentDescription = stringResource(R.string.icon),
        modifier = modifier.size(SmallIconSize)
    )
}

@Composable
fun MediumIcon(painter: Painter, color: Color, modifier: Modifier = Modifier) {
    Icon(
        painter = painter,
        tint = color,
        contentDescription = stringResource(R.string.icon),
        modifier = modifier.size(MediumIconSize)
    )
}
