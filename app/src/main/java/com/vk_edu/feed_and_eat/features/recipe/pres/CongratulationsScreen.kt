package com.vk_edu.feed_and_eat.features.recipe.pres

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk_edu.feed_and_eat.R

@Composable
fun CongratulationScreen(){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(400.dp)
            .width(400.dp)
            .background(colorResource(id = R.color.white), shape = RoundedCornerShape(200.dp))
    ){
        Text(
            "Congratulations! You have cooked a recipe!",
            fontSize = 24.sp,
            color = colorResource(id = R.color.gray)
        )
    }
}