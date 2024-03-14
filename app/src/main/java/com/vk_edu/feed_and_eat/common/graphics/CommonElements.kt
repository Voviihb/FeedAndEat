package com.vk_edu.feed_and_eat.common.graphics

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R


val plug: () -> Unit = {Log.d("PLUG", "plug pressed")}
val turquoise = Color(red = 0x00, blue = 0xB6, green = 0xBB)

@Composable
fun BackSquareButton(
    modifier: Modifier = Modifier,
    func: () -> Unit = plug,
){
    Button(
        onClick = func,
        modifier = modifier
            .height(60.dp)
            .width(60.dp)
            .background(Color.White, shape = RoundedCornerShape(5.dp))
            .clip(shape = RoundedCornerShape(5.dp))
            .rightBorder(3.dp, turquoise)
            .bottomBorder(3.dp, turquoise),
        contentPadding = PaddingValues(0.dp),
        shape = RectangleShape,
        colors = ButtonDefaults.outlinedButtonColors(Color.Transparent)
    ){
        Icon(
            painter = painterResource(id = R.drawable.arrowback),
            contentDescription = null,
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
    func: () -> Unit = plug,
){
    Button(
        onClick = func,
        modifier = modifier
            .height(60.dp)
            .width(60.dp)
            .background(Color.White, shape = RoundedCornerShape(5.dp))
            .clip(shape = RoundedCornerShape(5.dp))
            .leftBorder(3.dp, turquoise)
            .bottomBorder(3.dp, turquoise)
            .topBorder(3.dp, turquoise)
            .bottomBorder(3.dp, turquoise),
        contentPadding = PaddingValues(0.dp),
        shape = RectangleShape,
        colors = ButtonDefaults.outlinedButtonColors(Color.Transparent)
    ){
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = turquoise,
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
        )
    }
}

@Composable
fun ComposeButton(text : String, fn:() -> Unit) {
    Button(
        onClick = fn,
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(CornerSize(15.dp)))
            .border(2.dp, Color.Black, RoundedCornerShape(CornerSize(30.dp))),
        contentPadding = PaddingValues(0.dp)
    ) {
        Surface(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(CornerSize(15.dp)))
                .padding(16.dp)

        ) {
            // Your button text
            Text(
                text = text,
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}