package com.vk_edu.feed_and_eat.common.graphics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R

@Composable
fun LoadingCircular(modifier: Modifier = Modifier){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = colorResource(id = R.color.medium_cyan),
            strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth,
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
        )
    }
}