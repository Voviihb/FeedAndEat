package com.vk_edu.feed_and_eat.common.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> SwipeToDismissItem(item: T, onDismissed: (T) -> Unit, content: @Composable (T) -> Unit) {
    val state = rememberDismissState(
        confirmStateChange = { dismissValue ->
            if (dismissValue == DismissValue.DismissedToStart || dismissValue == DismissValue.DismissedToEnd) {
                onDismissed(item)
                true
            } else {
                false
            }
        }
    )

    SwipeToDismiss(state = state,
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        background = { DeleteBackground(swipeDismissState = state) },
        dismissContent = { content(item) }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeleteBackground(
    swipeDismissState: DismissState
) {
    val color =
        if (swipeDismissState.dismissDirection == DismissDirection.EndToStart
            || swipeDismissState.dismissDirection == DismissDirection.StartToEnd
        ) {
            Color.Red
        } else {
            Color.Transparent
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp)
    ) {
        SmallIcon(
            painter = painterResource(id = R.drawable.baseline_delete_24),
            color = Color.White
        )
    }
}