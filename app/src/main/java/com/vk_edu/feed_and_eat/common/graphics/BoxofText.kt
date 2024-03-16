package com.vk_edu.feed_and_eat.common.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.features.recipe.pres.LightBlue
import com.vk_edu.feed_and_eat.features.recipe.pres.LightWhite
import com.vk_edu.feed_and_eat.features.recipe.pres.Turquoise

@Composable
fun TextBox(text : String){
    Text(
        text = text,
        modifier = Modifier
            .padding(5.dp)
            .width(80.dp)
            .background(LightWhite, shape = RoundedCornerShape(5.dp))
            .clip(RoundedCornerShape(5.dp)),
    )
}

@Composable
fun BoxofText(bigText : List<String>){
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(70.dp),
        contentPadding = PaddingValues(5.dp),
        modifier = Modifier
            .padding(10.dp)
            .height(200.dp)
            .fillMaxWidth()
            .background(LightBlue)
            .border(2.dp, Turquoise, RectangleShape)
            .clip(RoundedCornerShape(10.dp))
    ){
        for (item in bigText){
            item{
                TextBox(
                    text = item
                )
            }

        }
    }
}