package com.vk_edu.feed_and_eat.features.login.pres

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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vk_edu.feed_and_eat.R

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun LoginScreen(
    navigateToHome: () -> Unit,
    navigateToRegister: () -> Unit,
) {
    val viewModel: LoginScreenViewModel = hiltViewModel()
    val loginForm by viewModel.loginFormState
    val errorMsg by viewModel.errorMessage

    val focusRequester = FocusRequester.createRefs().component1()
    val keyboardController = LocalSoftwareKeyboardController.current
    val destination = stringResource(id = R.string.ProfileScreen)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.background_login),
                contentScale = ContentScale.FillBounds
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_feed_and_eat),
                    contentDescription = stringResource(R.string.feed_and_eat_logo),
                    modifier = Modifier
                        .size(width = 250.dp, height = 100.dp)
                        .padding(top = 16.dp)
                )
            }

            Column(
                modifier = Modifier
                    .sizeIn(minWidth = 300.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(id = R.string.log_in),
                    modifier = Modifier
                        .padding(top = 100.dp, bottom = 16.dp)
                        .align(Alignment.Start),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                EmailField(
                    loginForm = loginForm,
                    viewModel = viewModel,
                    focusRequester = focusRequester,
                    errorMsg = errorMsg
                )

                PasswordField(
                    loginForm = loginForm,
                    viewModel = viewModel,
                    focusRequester = focusRequester,
                    errorMsg = errorMsg,
                    keyboardController = keyboardController
                )

                LoginButton(
                    onClickFunc = { viewModel.loginWithEmail(navigateToHome) },
                    loadingState = viewModel.loading
                )

                Text(
                    text = stringResource(R.string.or_login),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(id = R.color.white)
                )

                NoAuthLoginButton(
                    onClickFunc = { viewModel.logout() }, /* TODO replace it after merge! */
                    navigateToHome
                    /* TODO pass nav func to VM after merge with feature_firebaseDatastore */
                )

            }
        }

        val modifier = Modifier
            .align(Alignment.BottomEnd)
            .border(
                1.dp,
                colorResource(id = R.color.white),
                shape = RoundedCornerShape(topStart = 12.dp)
            )
        RegisterButton(modifier = modifier, navigateToRegister)

    }

}

@Composable
private fun EmailField(
    loginForm: LoginForm,
    viewModel: LoginScreenViewModel,
    focusRequester: FocusRequester,
    errorMsg: Exception?
) {
    Box(
        modifier = Modifier
            .width(300.dp)
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                2.dp,
                colorResource(id = R.color.purple_fae),
                shape = RoundedCornerShape(12.dp)
            )

    ) {
        TextField(
            value = loginForm.email,
            onValueChange = {
                viewModel.emailChanged(it)
                viewModel.clearError()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusRequester.requestFocus() }
            ),
            label = { Text(text = stringResource(R.string.enter_e_mail)) },
            leadingIcon = {
                Icon(
                    Icons.Filled.Email,
                    contentDescription = stringResource(R.string.email_logo)
                )
            },
            isError = errorMsg != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                errorContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Gray,
                unfocusedTextColor = Color.Gray,
                disabledTextColor = Color.Gray,
            )
        )
    }
}

@Composable
private fun PasswordField(
    loginForm: LoginForm,
    viewModel: LoginScreenViewModel,
    focusRequester: FocusRequester,
    errorMsg: Exception?,
    keyboardController: SoftwareKeyboardController?,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .width(300.dp)
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                2.dp,
                colorResource(id = R.color.purple_fae),
                shape = RoundedCornerShape(12.dp)
            )
            .background(Color.White)

    ) {
        TextField(
            value = loginForm.password,
            onValueChange = {
                viewModel.passwordChanged(it)
                viewModel.clearError()
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            label = { Text(text = stringResource(R.string.enter_password)) },
            leadingIcon = {
                Icon(
                    Icons.Filled.Lock,
                    contentDescription = stringResource(R.string.lock_logo)
                )
            },
            isError = errorMsg != null,
            supportingText = {
                if (errorMsg != null) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        text = errorMsg.message ?: stringResource(R.string.exception_occured),
                        color = Color.Red
                    )
                    viewModel.passwordChanged("")
                } else {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        text = stringResource(R.string.press_button_login),
                        color = Color.Gray
                    )
                }
            },
            trailingIcon = {
                val image =
                    if (passwordVisible) painterResource(id = R.drawable.hide_password_icon)
                    else painterResource(id = R.drawable.show_password_icon)

                val description =
                    if (passwordVisible) stringResource(R.string.hide_password) else stringResource(
                        R.string.show_password
                    )

                IconButton(
                    onClick = { passwordVisible = !passwordVisible },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(image, description)
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                errorContainerColor = Color.White,
                focusedIndicatorColor = colorResource(id = R.color.purple_fae),
                unfocusedIndicatorColor = colorResource(id = R.color.purple_fae),
                disabledIndicatorColor = colorResource(id = R.color.purple_fae),
                focusedTextColor = Color.Gray,
                unfocusedTextColor = Color.Gray,
                disabledTextColor = Color.Gray,
            )
        )
    }
}

@Composable
private fun LoginButton(
    onClickFunc: () -> Unit,
    loadingState: State<Boolean>
) {
    val loading by loadingState
    Button(
        onClick = onClickFunc,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonColors(
            containerColor = colorResource(id = R.color.purple_fae),
            contentColor = colorResource(id = R.color.white),
            disabledContainerColor = colorResource(id = R.color.purple_fae),
            disabledContentColor = colorResource(id = R.color.white)
        ),
        modifier = Modifier
            .padding(vertical = 16.dp)
            .border(
                1.dp,
                colorResource(id = R.color.white),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .width(100.dp)
                .padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.log_in), fontSize = 24.sp)
            if (loading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun NoAuthLoginButton(
    onClickFunc: () -> Unit,
    navigateToHome: () -> Unit
) {
    Button(
        onClick = {
            onClickFunc()
            navigateToHome()
        },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonColors(
            containerColor = colorResource(id = R.color.purple_fae),
            contentColor = colorResource(id = R.color.white),
            disabledContainerColor = colorResource(id = R.color.purple_fae),
            disabledContentColor = colorResource(id = R.color.white)
        ),
        modifier = Modifier
            .padding(vertical = 16.dp)
            .border(
                1.dp,
                colorResource(id = R.color.white),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Text(text = stringResource(R.string.enter_no_login), fontSize = 24.sp)
    }
}

@Composable
private fun RegisterButton(
    modifier: Modifier,
    navigateToRegister: () -> Unit,
) {
    val destination = stringResource(id = R.string.RegisterScreen)
    Button(
        onClick = navigateToRegister,
        shape = RoundedCornerShape(topStart = 12.dp),
        colors = ButtonColors(
            containerColor = colorResource(id = R.color.purple_fae),
            contentColor = colorResource(id = R.color.white),
            disabledContainerColor = colorResource(id = R.color.purple_fae),
            disabledContentColor = colorResource(id = R.color.white)
        ),
        modifier = modifier
    ) {
        Text(text = stringResource(R.string.sign_up_text), fontSize = 24.sp)
    }
}
