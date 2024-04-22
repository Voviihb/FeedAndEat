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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R

@Composable
fun TextBox(text : String){
    val lightWhite =  colorResource(id = R.color.white)
    Text(
        text = text,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(5.dp)
            .width(80.dp)
            .background(lightWhite, shape = RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp)),
    )
}

@Composable
fun BoxWithCards(bigText : List<String?>){
    val turquoise = colorResource(id = R.color.turqoise)
    val lightBlue = colorResource(id = R.color.pale_cyan)
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(70.dp),
        contentPadding = PaddingValues(5.dp),
        modifier = Modifier
            .padding(10.dp)
            .height(200.dp)
            .fillMaxWidth()
            .background(lightBlue, RoundedCornerShape(12.dp))
            .border(2.dp, turquoise, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
    ){
        for (item in bigText){
            item{
                if (item != null){
                    TextBox(
                        text = item,
                    )
                }

            }

        }
    }
}