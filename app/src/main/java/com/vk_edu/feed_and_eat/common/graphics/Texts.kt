package com.vk_edu.feed_and_eat.common.graphics

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.vk_edu.feed_and_eat.R


@Composable
fun LightText(
    text: String,
    fontSize: TextUnit,
    textAlign: TextAlign = TextAlign.Left,
    maxLines: Int = 2,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = fontSize,
        color = colorResource(R.color.gray),
        overflow = TextOverflow.Ellipsis,
        maxLines = maxLines,
        textAlign = textAlign,
        modifier = modifier
    )
}

@Composable
fun DarkText(text: String, fontSize: TextUnit, maxLines: Int = 2, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = fontSize,
        color = colorResource(R.color.black),
        overflow = TextOverflow.Ellipsis,
        maxLines = maxLines,
        modifier = modifier
    )
}

@Composable
fun BoldText(
    text: String,
    fontSize: TextUnit,
    lineHeight: TextUnit = TextUnit.Unspecified,
    fixLinesNumber: Boolean = false,
    maxLines: Int = 2,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        color = colorResource(R.color.black),
        overflow = TextOverflow.Ellipsis,
        minLines = if (fixLinesNumber) 2 else 1,
        maxLines = maxLines,
        lineHeight = lineHeight,
        modifier = modifier
    )
}
