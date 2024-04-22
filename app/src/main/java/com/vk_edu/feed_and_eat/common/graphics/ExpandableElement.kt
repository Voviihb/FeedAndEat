package com.vk_edu.feed_and_eat.common.graphics

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R

@Composable
fun ExpandableInfo(width : Int, surface : @Composable () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val xOffset by animateDpAsState(targetValue = if (expanded) (0).dp else (width - 60).dp,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    Surface(
        color = colorResource(R.color.transparent),
        modifier = Modifier
            .width(width.dp)
            .fillMaxHeight()
            .background(colorResource(R.color.transparent))
            .offset(x = xOffset)
    ) {
        Row {
            Box(
                modifier = Modifier
                    .background(colorResource(R.color.transparent))
            ){
                InfoSquareButton { expanded = !expanded }
            }
            surface()
        }

    }
}