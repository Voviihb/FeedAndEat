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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard
import com.vk_edu.feed_and_eat.features.navigation.pres.Screen
import com.vk_edu.feed_and_eat.ui.theme.ExtraSmallText
import com.vk_edu.feed_and_eat.ui.theme.MediumText
import com.vk_edu.feed_and_eat.ui.theme.SmallIconSize
import com.vk_edu.feed_and_eat.ui.theme.SmallText

@Composable
fun DishCard(
    recipeCard: RecipeCard,
    inFavourites: Boolean,
    favouritesCollectionId: String?,
    addToFavourites: ((String, RecipeCard) -> Unit),
    removeFromFavourites: ((String, RecipeCard) -> Unit),
    navigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier,
    largeCard: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            colorResource(R.color.white), colorResource(R.color.white),
            colorResource(R.color.white), colorResource(R.color.white)
        ),
        modifier = modifier.shadow(12.dp, RoundedCornerShape(16.dp)),
        onClick = { navigateToRoute(Screen.RecipeScreen.route + "/${recipeCard.recipeId}") }
    ) {
        Column {
            DishImage(link = recipeCard.image, modifier = Modifier.fillMaxWidth())
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 4.dp)
            ) {
                BoldText(
                    text = recipeCard.name,
                    fontSize = MediumText,
                    lineHeight = SmallText,
                    fixLinesNumber = !largeCard,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LightText(
                        text = (if (largeCard) stringResource(R.string.ingredients) else stringResource(R.string.small_ingredients)) +
                                " ${ if (recipeCard.ingredients > 0) recipeCard.ingredients else "-" }",
                        fontSize = ExtraSmallText
                    )
                    LightText(
                        text = stringResource(R.string.steps) + " ${recipeCard.steps}",
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
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(8.dp, 2.dp, 0.dp, 0.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        RatingBar(
                            recipeCard.rating.toFloat(),
                            stars = 1,
                            modifier = Modifier.height(SmallIconSize)
                        )
                        DarkText(
                            text = recipeCard.rating.toString(),
                            fontSize = SmallText,
                            modifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        SmallIcon(
                            painter = painterResource(R.drawable.cooked_icon),
                            color = colorResource(R.color.gray)
                        )
                        DarkText(
                            text = recipeCard.cooked.toString(),
                            fontSize = SmallText,
                            modifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp)
                        )
                    }
                }
                Button(
                    shape = RoundedCornerShape(8.dp, 0.dp, 0.dp, 0.dp),
                    colors = ButtonColors(
                        colorResource(R.color.medium_cyan), colorResource(R.color.medium_cyan),
                        colorResource(R.color.medium_cyan), colorResource(R.color.medium_cyan)
                    ),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(if (largeCard) 60.dp else 44.dp, 36.dp),
                    onClick = {
                        if (favouritesCollectionId != null) {
                            if (inFavourites)
                                removeFromFavourites(favouritesCollectionId, recipeCard)
                            else
                                addToFavourites(favouritesCollectionId, recipeCard)
                        }
                    }
                ) {
                    SmallIcon(
                        painter = if (inFavourites) painterResource(R.drawable.shaded_like_icon)
                        else painterResource(R.drawable.like_icon),
                        color = colorResource(R.color.white)
                    )
                }
            }
        }
    }
}
