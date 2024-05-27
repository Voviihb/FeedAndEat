package com.vk_edu.feed_and_eat.features.collection.pres

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.BoldText
import com.vk_edu.feed_and_eat.common.graphics.DishImage
import com.vk_edu.feed_and_eat.common.graphics.LargeIcon
import com.vk_edu.feed_and_eat.common.graphics.LoadingCircular
import com.vk_edu.feed_and_eat.common.graphics.RepeatButton
import com.vk_edu.feed_and_eat.features.collection.domain.models.CollectionDataModel
import com.vk_edu.feed_and_eat.ui.theme.LargeText


@Composable
fun AllCollectionsScreen(
    navigateToCollection: (String) -> Unit,
    viewModel: AllCollectionsScreenViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .background(colorResource(R.color.pale_cyan))
    ) {
        if (viewModel.loading.value)
            LoadingCircular()
        else if (viewModel.errorMessage.value != null)
            RepeatButton(onClick = {
                viewModel.clearError()
                viewModel.loadAllUserCollections()
            })
        else
            key(viewModel.collectionsData.value) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    viewModel.collectionsData.value.forEach {compilation ->
                        item {
                            CollectionCard(
                                compilation = compilation,
                                navigateToCollection = navigateToCollection,
                            )
                        }
                    }
                    item{
                        CollectionCard(
                            viewModel = viewModel,
                        )
                    }
                }
            }
        }
    }


@Composable
fun CollectionCard(
    compilation: CollectionDataModel,
    navigateToCollection: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            colorResource(R.color.white), colorResource(R.color.white),
            colorResource(R.color.white), colorResource(R.color.white)
        ),
        modifier = modifier.shadow(12.dp, RoundedCornerShape(16.dp)),
        onClick = {
            navigateToCollection(CollectionRoutes.Collection.route + "/${compilation.id}")
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DishImage(link = compilation.picture ?: "")
            BoldText(
                text = compilation.name,
                fontSize = LargeText,
                fixLinesNumber = true,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun CollectionCard(
    viewModel: AllCollectionsScreenViewModel,
    modifier: Modifier = Modifier
){

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            colorResource(R.color.white), colorResource(R.color.white),
            colorResource(R.color.white), colorResource(R.color.white)
        ),
        modifier = modifier.shadow(12.dp, RoundedCornerShape(16.dp)),
        onClick = {
            viewModel.openWindowDialog()
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LargeIcon(
                painter = painterResource(id = R.drawable.plus), 
                color = colorResource(id = R.color.dark_cyan),
                modifier = Modifier
                    .aspectRatio(4f / 3f)
                    .padding(12.dp)
            )
            BoldText(
                text = stringResource(id = R.string.new_collection),
                fontSize = LargeText,
                fixLinesNumber = true,
                modifier = Modifier.padding(8.dp)
            )
        }
        WindowDialog(viewModel = viewModel)
    }
}

@Composable
fun WindowDialog(
    viewModel: AllCollectionsScreenViewModel,
){
    val openDialog by viewModel.activeWindowDialog

    var collectionName by rememberSaveable { mutableStateOf("") }

    if (openDialog) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ){
            AlertDialog(
                onDismissRequest = { viewModel.openWindowDialog() },
                title = { Text(stringResource(id = R.string.input_collection_name)) },
                text = {
                    TextField(
                        value = collectionName,
                        onValueChange = {
                            collectionName = it
                        },
                        modifier = Modifier
                            .border(2.dp, colorResource(id = R.color.dark_cyan), RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                    )
                },
                confirmButton = {
                    Button(
                        contentPadding = PaddingValues(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            colorResource(id = R.color.pale_cyan),
                            colorResource(id = R.color.medium_cyan)
                        ),
                        border = BorderStroke(2.dp, colorResource(id = R.color.medium_cyan)),
                        onClick = {
                            viewModel.openWindowDialog()
                            viewModel.createNewUserCollection(collectionName)
                        },
                        modifier = Modifier,
                    ) {
                        Text(
                            stringResource(id = R.string.confirm),
                            fontSize = LargeText,
                            modifier = Modifier.padding(4.dp)
                            )
                    }
                },
                modifier = Modifier
                    .border(
                        2.dp,
                        colorResource(id = R.color.dark_cyan),
                        RoundedCornerShape(24.dp)
                    )
                    .clip(RoundedCornerShape(24.dp))
            )
        }
    }
}
