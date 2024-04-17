package com.vk_edu.feed_and_eat.common.graphics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun LoadingCircular(padding : PaddingValues){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        CircularProgressIndicator(
            color = colorResource(id = R.color.mediumcyan),
            strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth,
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
        )
    }
}