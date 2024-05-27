package com.vk_edu.feed_and_eat.features.recipe.pres.preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.DishImage
import com.vk_edu.feed_and_eat.common.graphics.MediumIcon
import com.vk_edu.feed_and_eat.common.graphics.SmallIcon
import com.vk_edu.feed_and_eat.ui.theme.MediumText

@Composable
fun BottomBar(
    navigateToStep: (Int) -> Unit,
    viewModel: RecipesScreenViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier.padding(start = 12.dp)
        ){
            DropDownContainer(
                viewModel = viewModel,
                modifier = Modifier
                    .background(colorResource(id = R.color.light_cyan), RoundedCornerShape(4.dp))
                    .border(2.dp, colorResource(id = R.color.dark_cyan), RoundedCornerShape(4.dp))
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.medium_cyan))
                .padding(4.dp)
        ) {
            Button(
                onClick = { viewModel.expand() },
                colors = ButtonDefaults.buttonColors(
                    colorResource(id = R.color.medium_cyan),
                    colorResource(id = R.color.white)
                )
            ) {
                MediumIcon(
                    painter = painterResource(id = R.drawable.add_collection),
                    color = colorResource(id = R.color.white),
                )
            }
            Button(
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    colorResource(id = R.color.medium_cyan),
                    contentColor = colorResource(R.color.white)
                ),
                border = BorderStroke(
                    2.dp,
                    color = colorResource(R.color.white_cyan),
                ),
                onClick = { navigateToStep(0) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
            ){
                Text(
                    text = stringResource(id = R.string.start_cooking),
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Visible,
                    fontSize = MediumText,
                    color =  colorResource(R.color.white),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
            }
            Button(
                onClick = {
                    if (viewModel.recipe.value.id in viewModel.favouriteRecipeIds.value){
                            viewModel.removeRecipeFromUserCollection(
                                collectionId = viewModel.favouritesCollectionId.value ?: "",
                                id = viewModel.recipe.value.id ?: ""
                            )
                        } else {
                        viewModel.addRecipeToUserCollection(
                            collectionId = viewModel.favouritesCollectionId.value ?: "",
                            id = viewModel.recipe.value.id ?: "",
                            image = viewModel.recipe.value.image ?: ""
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    colorResource(id = R.color.medium_cyan),
                    colorResource(id = R.color.white)
                )
            ) {
                MediumIcon(
                    painter =
                    if (viewModel.inFavourite())
                        painterResource(id = R.drawable.shaded_like_icon)
                    else painterResource(id = R.drawable.like_icon),
                    color = colorResource(id = R.color.white),
                )
            }
        }
    }
}

@Composable
fun DropDownContainer(
    viewModel: RecipesScreenViewModel,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = viewModel.collectionButtonExpanded.value,
        onDismissRequest = { viewModel.expand() },
        modifier = modifier
    ) {
        if (viewModel.collectionsList?.value != null){
            viewModel.collectionsList.value.forEach { collection ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clickable { }
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                colorResource(id = R.color.white),
                                RoundedCornerShape(8.dp)
                            )
                            .border(
                                2.dp,
                                colorResource(id = R.color.dark_cyan),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(start = 12.dp, end = 4.dp)
                            .clip(RoundedCornerShape(8.dp))

                    ) {
                        if (collection.picture != null){
                            collection.picture?.let {
                                DishImage(
                                    link = it,
                                    modifier = Modifier
                                        .height(48.dp)
                                )
                            }
                        } else {
                            MediumIcon(
                                painter = painterResource(id = R.drawable.noimage), 
                                color = colorResource(id = R.color.black),
                                modifier = Modifier
                                    .height(48.dp)
                                    .aspectRatio(4f / 3f)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            collection.name,
                            fontSize = MediumText
                        )
                        AddButtonShapePlus(
                            onClick = {},
                            size = 32
                        )
                        }
                    }
                }
            }
        }
    }

@Composable
fun AddButtonShapePlus(
    onClick: () -> Unit,
    size : Int,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .padding(4.dp)
            .border(2.dp, colorResource(id = R.color.dark_cyan), CircleShape)
            .clip(CircleShape)
            .background(colorResource(id = R.color.light_cyan))
    ){
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                colorResource(id = R.color.medium_cyan),
                colorResource(id = R.color.black)
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .border(2.dp, colorResource(id = R.color.dark_cyan), CircleShape)
                .clip(CircleShape)
                .size(size.dp)
                .background(colorResource(id = R.color.light_cyan))
        ) {
            SmallIcon(
                painter = painterResource(id = R.drawable.plus),
                color = colorResource(id = R.color.white),
            )
        }
    }
}