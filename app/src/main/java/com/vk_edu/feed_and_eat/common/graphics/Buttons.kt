package com.vk_edu.feed_and_eat.common.graphics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.ui.theme.MediumText
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R


@Composable
fun RepeatButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Button(
            contentPadding = PaddingValues(36.dp, 16.dp),
            colors = ButtonColors(
                colorResource(R.color.pale_cyan), colorResource(R.color.pale_cyan),
                colorResource(R.color.pale_cyan), colorResource(R.color.pale_cyan)
            ),
            border = BorderStroke(2.dp, colorResource(R.color.dark_cyan)),
            onClick = onClick
        ) {
            Text(
                text = stringResource(R.string.repeat),
                color = colorResource(R.color.dark_cyan),
                fontSize = MediumText
            )
        }
    }
}

@Composable
fun SquareArrowButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        shape = RoundedCornerShape(0.dp, 0.dp, 12.dp, 0.dp),
        colors = ButtonColors(
            colorResource(R.color.white), colorResource(R.color.white),
            colorResource(R.color.white), colorResource(R.color.white)
        ),
        border = BorderStroke(2.dp, colorResource(id = R.color.dark_cyan)),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .size(60.dp)
            .shadow(12.dp, RoundedCornerShape(0.dp, 0.dp, 12.dp, 0.dp)),
        onClick = onClick
    ) {
        LargeIcon(
            painter = painterResource(id = R.drawable.arrowback),
            color = colorResource(id = R.color.medium_cyan)
        )
    }
}

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