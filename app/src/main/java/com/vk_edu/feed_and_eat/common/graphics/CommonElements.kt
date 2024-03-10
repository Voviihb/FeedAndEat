package com.vk_edu.feed_and_eat.common.graphics

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R


val plug: () -> Unit = {Log.d("PLUG", "plug pressed")}

@Preview
@Composable
fun ReturnBackButton(
    modifier: Modifier = Modifier,
    func: () -> Unit = plug
) {
    Button(
        onClick = func,
        modifier = modifier
            .width(60.dp)
            .height(60.dp),
        shape = RoundedCornerShape(30.dp),
        contentPadding = PaddingValues(0.dp),
        ) {
            Image(  painter = painterResource(id = R.drawable.back),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
        }
    }

@Composable
fun infoSquareButton(
    modifier: Modifier = Modifier,
    func: () -> Unit = plug
){
    Button(
        onClick = func,
        modifier = modifier
            .width(63.dp)
            .height(88.dp)
            .background(Color.Transparent),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.outlinedButtonColors(Color.Transparent)
    ){
        Image(  painter = painterResource(id = R.drawable.info),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
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