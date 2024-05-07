package com.vk_edu.feed_and_eat.common.graphics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R


@Composable
fun SquareArrowButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        shape = RoundedCornerShape(0.dp, 0.dp, 8.dp, 0.dp),
        colors = ButtonColors(
            colorResource(R.color.white), colorResource(R.color.white),
            colorResource(R.color.white), colorResource(R.color.white)
        ),
        border = BorderStroke(2.dp, colorResource(id = R.color.dark_cyan)),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .size(60.dp)
            .shadow(12.dp, RoundedCornerShape(0.dp, 0.dp, 8.dp, 0.dp)),
        onClick = onClick
    ) {
        LargeIcon(
            painter = painterResource(id = R.drawable.arrowback),
            color = colorResource(id = R.color.medium_cyan)
        )
    }
}


@Composable
fun InfoSquareButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(0.dp),
        shape = RectangleShape,
        colors = ButtonDefaults.outlinedButtonColors(colorResource(R.color.transparent)),
        modifier = modifier
            .height(60.dp)
            .width(60.dp)
            .background(colorResource(R.color.white), shape = RoundedCornerShape(4.dp))
            .clip(shape = RoundedCornerShape(4.dp))
            .border(
                2.dp,
                color = colorResource(id = R.color.dark_cyan),
                shape = RoundedCornerShape(4.dp)
            ),
    ){
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = stringResource(id = R.string.info),
            tint = colorResource(id = R.color.dark_cyan),
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
        )
    }
}