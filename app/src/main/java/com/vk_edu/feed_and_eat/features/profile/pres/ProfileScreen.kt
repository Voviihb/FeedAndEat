package com.vk_edu.feed_and_eat.features.profile.pres

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R

@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val profileInfo by viewModel.profileState
    val settingsChoice by viewModel.settingsState
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
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
                SaveInfoButton()
                LogoutButton()
                SettingsBlock()
            }

        }
    }
}

@Composable
private fun UserInfoBlock(profileInfo: Profile) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.user_default_icon),
            contentDescription = "User profile icon",
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
                Text(text = "E-Mail:", fontSize = 24.sp, fontWeight = FontWeight.Normal)
                Text(
                    text = profileInfo.email,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Right
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Никнейм:",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = profileInfo.nickname,
                    fontSize = 24.sp,
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
        Text(text = "Обо мне:", fontSize = 24.sp, fontWeight = FontWeight.Normal)
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
                label = { Text("Введите желаемую информацию о себе") },
                textStyle = TextStyle(fontSize = 20.sp)
            )
        }

    }
}

@Composable
private fun SaveInfoButton() {
    Button(
        onClick = { /*TODO*/ },
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
            Text("Сохранить информацию", fontSize = 24.sp, fontWeight = FontWeight.Medium)
        }

    }
}

@Composable
private fun LogoutButton() {
    Button(
        onClick = { /*TODO*/ },
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
            Text("Выйти из аккаунта", fontSize = 24.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun SettingsBlock() {
    val themes = listOf("Светлая", "Темная", "Как в системе")
    val (selectedThemeOption, onThemeOptionSelected) = remember { mutableStateOf(themes[0]) }
    val profileType = listOf("Открытый", "Закрытый")
    val (selectedProfileOption, onProfileOptionSelected) = remember { mutableStateOf(profileType[0]) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Настройки:",
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Тема:", fontSize = 24.sp, fontWeight = FontWeight.Normal)
            Column(
                Modifier
                    .selectableGroup()
                    .width(200.dp),
                horizontalAlignment = Alignment.Start
            ) {
                themes.forEach { element ->
                    Row(verticalAlignment = Alignment.CenterVertically)
                    {
                        RadioButton(
                            selected = (element == selectedThemeOption),
                            onClick = { onThemeOptionSelected(element) },
                            colors = RadioButtonColors(
                                selectedColor = Color.Black,
                                unselectedColor = Color.Black,
                                disabledSelectedColor = Color.Black,
                                disabledUnselectedColor = Color.Black
                            )
                        )
                        Text(text = element, fontSize = 20.sp)
                    }
                }
            }
        }
        /* TODO change vertical padding*/
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Профиль:", fontSize = 24.sp, fontWeight = FontWeight.Normal)
            Column(
                Modifier
                    .selectableGroup()
                    .width(200.dp),
                horizontalAlignment = Alignment.Start
            ) {
                profileType.forEach { element ->
                    Row(verticalAlignment = Alignment.CenterVertically)
                    {
                        RadioButton(
                            selected = (element == selectedProfileOption),
                            onClick = { onProfileOptionSelected(element) },
                            colors = RadioButtonColors(
                                selectedColor = Color.Black,
                                unselectedColor = Color.Black,
                                disabledSelectedColor = Color.Black,
                                disabledUnselectedColor = Color.Black
                            )
                        )
                        Text(text = element, fontSize = 20.sp)
                    }
                }
            }
        }
    }
}