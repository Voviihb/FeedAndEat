package com.vk_edu.feed_and_eat.common.graphics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.ui.theme.DarkTurquoise
import com.vk_edu.feed_and_eat.ui.theme.ExtraSmallText
import com.vk_edu.feed_and_eat.ui.theme.Gray
import com.vk_edu.feed_and_eat.ui.theme.MediumText
import com.vk_edu.feed_and_eat.ui.theme.SmallText
import com.vk_edu.feed_and_eat.ui.theme.White

@Composable
fun DishCard(link: String,
             ingredients: Int,
             steps: Int, name: String,
             rating: Double, cooked: Int,
             modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(White, White, White, White),
        modifier = modifier.shadow(16.dp, RoundedCornerShape(16.dp)),
        onClick = {}
    ) {
        Column {
            DishImage(link = link, modifier = Modifier.fillMaxWidth())
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.padding(8.dp, 4.dp)
            ) {
                BoldText(
                    text = name,
                    fontSize = MediumText,
                    lineHeight = SmallText,
                    modifier = Modifier.fillMaxWidth()
                )
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LightText(
                        text = stringResource(R.string.small_ingredients) + " $ingredients",
                        fontSize = ExtraSmallText
                    )
                    LightText(
                        text = stringResource(R.string.steps) + " $steps",
                        fontSize = ExtraSmallText
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        RatingBar(rating.toFloat(), 1, modifier = Modifier.height(24.dp))
                        DarkText(
                            text = rating.toString(),
                            fontSize = SmallText,
                            modifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp)
                    ) {
                        SmallIcon(painter = painterResource(R.drawable.delete), color = Gray)
                        DarkText(text = cooked.toString(), fontSize = SmallText)
                    }
                }
                Button(
                    shape = RoundedCornerShape(8.dp, 0.dp, 0.dp, 0.dp),
                    colors = ButtonColors(DarkTurquoise, DarkTurquoise, DarkTurquoise, DarkTurquoise),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(44.dp, 36.dp),
                    onClick = {}
                ) {
                    SmallIcon(painter = painterResource(R.drawable.delete), color = White)
                }
            }
        }
    }
}
