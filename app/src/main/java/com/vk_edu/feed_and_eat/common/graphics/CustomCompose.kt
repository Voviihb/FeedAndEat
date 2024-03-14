package com.vk_edu.feed_and_eat.common.graphics

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

fun Modifier.bottomBorder(strokeWidth: Dp, color: Color) = composed(
//  This method must be used AFTER compose.background
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx/2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width , y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

fun Modifier.rightBorder(strokeWidth: Dp, color: Color) = composed(
//  This method must be used AFTER compose.background
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val height = size.height
            val width = size.width - strokeWidthPx/2

            drawLine(
                color = color,
                start = Offset(x = width, y = 2f),
                end = Offset(x = width , y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

fun Modifier.leftBorder(strokeWidth: Dp, color: Color) = composed(
//  This method must be used AFTER compose.background
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val height = size.height
            val width = size.width - strokeWidthPx/2

            drawLine(
                color = color,
                start = Offset(x = 3f, y = 0f),
                end = Offset(x = 3f , y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

fun Modifier.topBorder(strokeWidth: Dp, color: Color) = composed(
//  This method must be used AFTER compose.background
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx/2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = 2f),
                end = Offset(x = width , y = 2f),
                strokeWidth = strokeWidthPx
            )
        }
    }
)