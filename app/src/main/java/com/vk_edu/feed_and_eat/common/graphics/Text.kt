package com.vk_edu.feed_and_eat.common.graphics

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.vk_edu.feed_and_eat.ui.theme.Black
import com.vk_edu.feed_and_eat.ui.theme.ExtraLargeText
import com.vk_edu.feed_and_eat.ui.theme.Gray
import com.vk_edu.feed_and_eat.ui.theme.SmallText


@Composable
fun LightText(text: String, fontSize: TextUnit, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = fontSize,
        color = Gray,
        overflow = TextOverflow.Ellipsis,
        maxLines = 2,
        modifier = modifier
    )
}

@Composable
fun DarkText(text: String, fontSize: TextUnit, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = fontSize,
        color = Black,
        overflow = TextOverflow.Ellipsis,
        maxLines = 2,
        modifier = modifier
    )
}

@Composable
fun BoldText(text: String, fontSize: TextUnit, lineHeight: TextUnit = TextUnit.Unspecified,
             modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        color = Black,
        overflow = TextOverflow.Ellipsis,
        maxLines = 2,
        lineHeight = lineHeight,
        modifier = modifier
    )
}

@Preview(showSystemUi = true)
@Composable
fun previewText() {
    Column {
        BoldText("Hello,vdfbdfbrgfnrgtnythjyhumytmytytnygtnytnytgnytnythmhmygh World!", ExtraLargeText)
        LightText("Hello, World!", SmallText)
        DarkText("Hello, World!", SmallText)
    }
}