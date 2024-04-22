package com.vk_edu.feed_and_eat.features.profile.pres

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.common.graphics.LoadingCircular

@Composable
fun ProfileScreen(
    navigateToRoute: (String) -> Unit,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val profileInfo by viewModel.profileState
    val loading by viewModel.loading
    val selectedThemeOption by viewModel.selectedTheme
    val selectedProfileOption by viewModel.selectedProfileType

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        if (loading) {
            LoadingCircular(padding = PaddingValues(4.dp))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                UserInfoBlock(profileInfo)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AboutMeBlock(profileInfo, viewModel)
                    SaveInfoButton(viewModel = viewModel)
                    LogoutButton(viewModel = viewModel, navigateToRoute = navigateToRoute)
                    SettingsBlock(viewModel, selectedThemeOption, selectedProfileOption)
                }

            }
        }
    }
}

@Composable
private fun UserInfoBlock(profileInfo: Profile) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.user_default_icon),
            contentDescription = stringResource(R.string.user_profile_icon),
            modifier = Modifier.size(100.dp)
        )
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.e_mail),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = profileInfo.email ?: "Error!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Right
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.nickname),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = profileInfo.nickname ?: "Error!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Right
                )
            }

        }
    }
}

@Composable
private fun AboutMeBlock(profileInfo: Profile, viewModel: ProfileScreenViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.about_me),
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(
                    1.dp,
                    colorResource(id = R.color.cyan_fae),
                    shape = RoundedCornerShape(12.dp)
                )
                .background(Color.Transparent)

        ) {
            TextField(
                value = profileInfo.aboutMe,
                onValueChange = { viewModel.aboutMeChanged(it) },
                modifier = Modifier.fillMaxSize(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(id = R.color.light_cyan_fae),
                    focusedContainerColor = colorResource(id = R.color.light_cyan_fae),
                    errorContainerColor = colorResource(id = R.color.light_cyan_fae),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                ),
                label = { Text(stringResource(R.string.enter_information_about_yourself)) },
                textStyle = TextStyle(fontSize = 20.sp)
            )
        }

    }
}

@Composable
private fun SaveInfoButton(viewModel: ProfileScreenViewModel) {
    Button(
        onClick = { viewModel.updateUserProfile() },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonColors(
            containerColor = colorResource(id = R.color.light_cyan_fae),
            contentColor = colorResource(id = R.color.black),
            disabledContainerColor = colorResource(id = R.color.light_cyan_fae),
            disabledContentColor = colorResource(id = R.color.black)
        ),
        modifier = Modifier
            .padding(vertical = 16.dp)
            .border(
                1.dp,
                colorResource(id = R.color.cyan_fae),
                shape = RoundedCornerShape(12.dp)
            )

    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.save_information),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        }

    }
}

@Composable
private fun LogoutButton(viewModel: ProfileScreenViewModel, navigateToRoute: (String) -> Unit) {
    Button(
        onClick = {
            viewModel.logout(navigateToRoute)
        },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonColors(
            containerColor = Color.Red,
            contentColor = colorResource(id = R.color.white),
            disabledContainerColor = Color.Red,
            disabledContentColor = colorResource(id = R.color.white)
        ),
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.logout), fontSize = 24.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun SettingsBlock(
    viewModel: ProfileScreenViewModel,
    selectedThemeOption: ThemeSelection,
    selectedProfileOption: ProfileType
) {
    val themes = mapOf(
        ThemeSelection.LIGHT to stringResource(R.string.light_theme),
        ThemeSelection.DARK to stringResource(R.string.dark_theme),
        ThemeSelection.AS_SYSTEM to stringResource(R.string.as_system_theme)
    )
    val profileTypes = mapOf(
        ProfileType.PUBLIC to stringResource(R.string.public_profile),
        ProfileType.PRIVATE to stringResource(R.string.private_profile)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.settings),
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.theme),
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal
            )
            Column(
                Modifier
                    .selectableGroup()
                    .width(200.dp),
                horizontalAlignment = Alignment.Start
            ) {
                viewModel.themes.forEach { element ->
                    Row(verticalAlignment = Alignment.CenterVertically)
                    {
                        RadioButton(
                            selected = (element == selectedThemeOption),
                            onClick = { viewModel.onThemeOptionSelected(element) },
                            colors = RadioButtonColors(
                                selectedColor = Color.Black,
                                unselectedColor = Color.Black,
                                disabledSelectedColor = Color.Black,
                                disabledUnselectedColor = Color.Black
                            )
                        )
                        Text(text = themes[element].toString(), fontSize = 20.sp)
                    }
                }
            }
        }
        /* TODO change vertical padding*/
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.profile),
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal
            )
            Column(
                Modifier
                    .selectableGroup()
                    .width(200.dp),
                horizontalAlignment = Alignment.Start
            ) {
                viewModel.profileType.forEach { element ->
                    Row(verticalAlignment = Alignment.CenterVertically)
                    {
                        RadioButton(
                            selected = (element == selectedProfileOption),
                            onClick = { viewModel.onProfileOptionSelected(element) },
                            colors = RadioButtonColors(
                                selectedColor = Color.Black,
                                unselectedColor = Color.Black,
                                disabledSelectedColor = Color.Black,
                                disabledUnselectedColor = Color.Black
                            )
                        )
                        Text(text = profileTypes[element].toString(), fontSize = 20.sp)
                    }
                }
            }
        }
    }
}