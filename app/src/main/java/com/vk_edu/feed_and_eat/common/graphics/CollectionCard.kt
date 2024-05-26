package com.vk_edu.feed_and_eat.common.graphics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.collection.domain.models.Compilation
import com.vk_edu.feed_and_eat.features.navigation.pres.Screen

@Composable
fun CollectionCard(
    compilation: Compilation,
    navigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier,
){
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            colorResource(R.color.white), colorResource(R.color.white),
            colorResource(R.color.white), colorResource(R.color.white)
        ),
        modifier = modifier
            .shadow(12.dp, RoundedCornerShape(16.dp))
            .padding(bottom = 8.dp)
        ,
        onClick = {
            navigateToRoute(Screen.CollectionScreen.route + "/${compilation.id}")
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.padding(bottom = 8.dp)
        ){
            DishImage(link = compilation.picture ?: "")
            Text(
                text = compilation.name,
                fontSize = 20.sp,
                color = colorResource(id = R.color.black)
            )
        }

    }
}