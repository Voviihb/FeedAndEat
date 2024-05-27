package com.vk_edu.feed_and_eat.common.graphics

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R

@Composable
fun OutlinedTextInput(
    text: String,
    fontSize: TextUnit,
    placeholderText: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = text,
        textStyle = TextStyle(fontSize = fontSize, color = colorResource(R.color.black)),
        placeholder = { LightText(text = placeholderText, fontSize = fontSize) },
        shape = RoundedCornerShape(8.dp),
        singleLine = singleLine,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = colorResource(R.color.white_cyan),
            focusedContainerColor = colorResource(R.color.white_cyan),
            errorContainerColor = colorResource(R.color.white_cyan),
            focusedIndicatorColor = colorResource(R.color.medium_cyan),
            unfocusedIndicatorColor = colorResource(R.color.medium_cyan),
            disabledIndicatorColor = colorResource(R.color.medium_cyan),
            errorIndicatorColor = colorResource(R.color.medium_cyan),
            focusedTextColor = colorResource(R.color.black),
            unfocusedTextColor = colorResource(R.color.black),
            disabledTextColor = colorResource(R.color.black),
            cursorColor = colorResource(R.color.black),
            errorCursorColor = colorResource(R.color.black)
        ),
        modifier = modifier,
        onValueChange = onValueChange
    )
}