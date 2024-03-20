package com.vk_edu.feed_and_eat.common.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R


@Composable
fun SquareArrowButton(
    modifier: Modifier = Modifier,
    onclick: () -> Unit = {},
){
    val turquoise = Color(red = 0x00, green = 0xB6, blue = 0xBB)
    Button(
        onClick = onclick,
        contentPadding = PaddingValues(0.dp),
        shape = RectangleShape,
        colors = ButtonDefaults.outlinedButtonColors(Color.Transparent),
        modifier = modifier
            .height(60.dp)
            .width(60.dp)
            .border(2.dp,
                color = turquoise,
                shape = RoundedCornerShape(5.dp))
            .background(Color.White, shape = RoundedCornerShape(5.dp))
            .clip(shape = RoundedCornerShape(5.dp))
    ){
        Icon(
            painter = painterResource(id = R.drawable.arrowback),
            contentDescription = stringResource(id = R.string.back),
            tint = turquoise,
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
        )
    }
}

@Composable
fun InfoSquareButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
){
    val turquoise = Color(red = 0x00, green = 0xB6, blue = 0xBB)
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(0.dp),
        shape = RectangleShape,
        colors = ButtonDefaults.outlinedButtonColors(Color.Transparent),
        modifier = modifier
            .height(60.dp)
            .width(60.dp)
            .background(Color.White, shape = RoundedCornerShape(5.dp))
            .clip(shape = RoundedCornerShape(5.dp))
            .border(2.dp,
                color = turquoise,
                shape = RoundedCornerShape(5.dp)),
    ){
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = stringResource(id = R.string.info),
            tint = turquoise,
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
        )
    }
}