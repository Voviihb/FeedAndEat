package com.vk_edu.feed_and_eat.common.graphics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R

@Composable
fun OutlinedThemeButton(
    text: String,
    fontSize: TextUnit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        shape = RoundedCornerShape(8.dp),
        colors = ButtonColors(
            colorResource(R.color.white_cyan), colorResource(R.color.white_cyan),
            colorResource(R.color.white_cyan), colorResource(R.color.white_cyan)
        ),
        border = BorderStroke(1.dp, colorResource(R.color.medium_cyan)),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier,
        onClick = onClick
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                color = colorResource(R.color.dark_cyan),
                fontSize = fontSize,
                textAlign = TextAlign.Center
            )
        }
    }
}