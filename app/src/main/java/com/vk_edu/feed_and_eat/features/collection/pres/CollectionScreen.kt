package com.vk_edu.feed_and_eat.features.collection.pres

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.collection.data.models.Compilation
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar
import java.lang.Integer.min


@Composable
fun SingleCollection(collection: Compilation){
    val pictureList = MutableList(4){R.drawable.noimage}
    for (i in 0..min(3, collection.recipeList.size - 1)){
        val pic = collection.recipeList[i].picture
        pictureList[i] = pic
    }

    Card(modifier = Modifier
        .padding(10.dp)
        .shadow(10.dp, RoundedCornerShape(20.dp))
    ){
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .height(130.dp)
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.mediumcyan))
            ) {
                items(4){i ->
                    Image(
                        painter = painterResource(id = pictureList[i]),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(65.dp)
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(colorResource(id = R.color.background))
                    .height(60.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = collection.name,
                    fontSize = 22.sp,
                    fontStyle = FontStyle.Normal
                )
                Text(
                    text = stringResource(id = R.string.recipes) + ": " + collection.recipeList.size,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp)
                )
            }
        }
    }
}

@Composable
fun CollectionScreen(
    navigateToRoute : (String) -> Unit
) {
    val viewModel : CollectionScreenViewModel = hiltViewModel()
    viewModel.getCollectionViewModel()
    val collectionList = viewModel.mutableCollection.value.compilations
    Log.d("TAG", collectionList.toString())

    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, BottomScreen.CollectionScreen.route) },
    ) {padding ->
        Spacer(modifier = Modifier.height(20.dp))
        LazyVerticalGrid(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalArrangement = Arrangement.Top,
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .background(colorResource(id = R.color.dimcyan))
                .padding(padding)
        ){
            items(collectionList.size){i ->
                SingleCollection(collection = collectionList[i])
            }
        }
    }
}