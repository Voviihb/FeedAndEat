package com.vk_edu.feed_and_eat.features.profile.pres

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.BoldText
import com.vk_edu.feed_and_eat.common.graphics.DarkText
import com.vk_edu.feed_and_eat.common.graphics.LightText
import com.vk_edu.feed_and_eat.common.graphics.LoadingCircular
import com.vk_edu.feed_and_eat.features.navigation.models.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.pres.GlobalNavigationBar
import com.vk_edu.feed_and_eat.ui.theme.LargeText
import com.vk_edu.feed_and_eat.ui.theme.MediumText

@Composable
fun ProfileScreen(
    navigateToRoute: (String) -> Unit,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    Scaffold(
        bottomBar = { GlobalNavigationBar(navigateToRoute, BottomScreen.ProfileScreen.route) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
        ) {
            LaunchedEffect(Unit) {
                viewModel.loadProfileInfo()
            }
            if (viewModel.loading.value) {
                LoadingCircular()
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(12.dp),
                ) {
                    UserInfoBlock(viewModel.profileState.value)
                    AboutMeBlock(viewModel.profileState.value, viewModel)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        SaveInfoButton(viewModel = viewModel)
                        LogoutButton(viewModel = viewModel, navigateToRoute = navigateToRoute)
                    }
                }
            }
        }
    }
}

@Composable
private fun UserInfoBlock(profileInfo: Profile, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.user_default_icon),
            contentDescription = stringResource(R.string.user_profile_icon),
            modifier = Modifier.size(100.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LightText(text = stringResource(R.string.e_mail), fontSize = LargeText)
                DarkText(
                    text = profileInfo.email ?: stringResource(id = R.string.anonymous_user),
                    fontSize = LargeText
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LightText(text = stringResource(R.string.nickname), fontSize = LargeText)
                DarkText(
                    text = profileInfo.nickname ?: stringResource(id = R.string.anonymous_user),
                    fontSize = LargeText
                )
            }
        }
    }
}

@Composable
private fun AboutMeBlock(
    profileInfo: Profile,
    viewModel: ProfileScreenViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        LightText(text = stringResource(R.string.about_me), fontSize = LargeText)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, colorResource(id = R.color.dark_cyan), RoundedCornerShape(12.dp))
        ) {
            TextField(
                value = profileInfo.aboutMe,
                modifier = Modifier.fillMaxSize(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(id = R.color.white_cyan),
                    focusedContainerColor = colorResource(id = R.color.white_cyan),
                    errorContainerColor = colorResource(id = R.color.white_cyan),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                ),
                placeholder = { LightText(
                    text = stringResource(R.string.enter_information_about_yourself),
                    fontSize = MediumText
                ) },
                textStyle = TextStyle(fontSize = MediumText),
                onValueChange = { viewModel.aboutMeChanged(it) }
            )
        }
    }
}

@Composable
private fun SaveInfoButton(viewModel: ProfileScreenViewModel, modifier: Modifier = Modifier) {
    Button(
        shape = RoundedCornerShape(12.dp),
        colors = ButtonColors(
            containerColor = colorResource(id = R.color.white_cyan),
            contentColor = colorResource(id = R.color.black),
            disabledContainerColor = colorResource(id = R.color.white_cyan),
            disabledContentColor = colorResource(id = R.color.black)
        ),
        border = BorderStroke(1.dp, colorResource(id = R.color.dark_cyan)),
        modifier = modifier.shadow(12.dp, RoundedCornerShape(12.dp)),
        onClick = { viewModel.updateUserProfile() }
    ) {
        BoldText(
            text = stringResource(R.string.save_information),
            fontSize = LargeText,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

@Composable
private fun LogoutButton(
    viewModel: ProfileScreenViewModel,
    navigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        shape = RoundedCornerShape(12.dp),
        colors = ButtonColors(
            containerColor = colorResource(id = R.color.red),
            contentColor = colorResource(id = R.color.white),
            disabledContainerColor = colorResource(id = R.color.red),
            disabledContentColor = colorResource(id = R.color.white)
        ),
        modifier = modifier.shadow(12.dp, RoundedCornerShape(12.dp)),
        onClick = {
            viewModel.logout(navigateToRoute)
        }
    ) {
        Text(
            text = stringResource(R.string.logout),
            fontSize = LargeText,
            color = colorResource(R.color.white),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}
