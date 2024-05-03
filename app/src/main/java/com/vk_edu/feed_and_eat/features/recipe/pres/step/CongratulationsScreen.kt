package com.vk_edu.feed_and_eat.features.recipe.pres.step

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen

@Composable
fun CongratulationScreen(
    name : String,
    navigateBack: () -> Unit,
    navigateToRoute: (String) -> Unit
){
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white_cyan))
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp)
                .width(400.dp)
                .background(
                    colorResource(id = R.color.white_cyan),
                    RoundedCornerShape(24.dp)
                )
                .clip(RoundedCornerShape(24.dp))
                .border(
                    4.dp,
                    colorResource(id = R.color.cyan_fae),
                    RoundedCornerShape(24.dp)
                )
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = stringResource(R.string.congratulations),
                fontSize = 24.sp,
                color = colorResource(id = R.color.gray),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = name,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.red),
                modifier = Modifier
                    .background(colorResource(id = R.color.yellow), RoundedCornerShape(4.dp))
                    .clip(RoundedCornerShape(4.dp))
                    .border(
                        2.dp,
                        colorResource(id = R.color.red),
                        RoundedCornerShape(4.dp)
                    )
            )
            Spacer(modifier = Modifier.height(60.dp))
        }
        Button(
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                colorResource(id = R.color.white_cyan),
                contentColor = colorResource(R.color.black)
            ),
            border = BorderStroke(
                2.dp,
                colorResource(id = R.color.dark_cyan),
            ),
            onClick = { navigateToRoute(BottomScreen.HomeScreen.route) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .width(112.dp)
        ) {
            Text(
                text = stringResource(id = R.string.to_main),
                overflow = TextOverflow.Visible
            )
        }
    }
}