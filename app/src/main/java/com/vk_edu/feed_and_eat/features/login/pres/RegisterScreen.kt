package com.vk_edu.feed_and_eat.features.login.pres

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
fun RegisterScreen(
    context: Context,
    navigateToHome : () -> Unit,
    navigateToLogin : () -> Unit,
) {
    val viewModel: RegisterScreenViewModel = hiltViewModel()
    val registerForm by viewModel.registerFormState
    val errorMsg by viewModel.errorMessage
    val signUpState by viewModel.signUpState

    val focusRequester = FocusRequester.createRefs().component1()
    val keyboardController = LocalSoftwareKeyboardController.current
    val passwordVisible = rememberSaveable { mutableStateOf(false) }

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
                    text = stringResource(R.string.sign_up),
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
                    registerForm = registerForm,
                    viewModel = viewModel,
                    errorMsg = errorMsg
                )


                UsernameField(
                    registerForm = registerForm,
                    viewModel = viewModel,
                    errorMsg = errorMsg
                )


                PasswordField(
                    registerForm = registerForm,
                    viewModel = viewModel,
                    passwordVisible = passwordVisible,
                    errorMsg = errorMsg
                )


                PasswordControlField(
                    registerForm = registerForm,
                    viewModel = viewModel,
                    passwordVisible = passwordVisible,
                    keyboardController = keyboardController,
                    focusRequester = focusRequester,
                    errorMsg = errorMsg
                )


                SignUpButton(viewModel = viewModel)

            }
        }

        val modifier = Modifier
            .align(Alignment.BottomEnd)
            .border(
                1.dp,
                colorResource(id = R.color.white),
                shape = RoundedCornerShape(topStart = 12.dp)
            )
        LoginButton(modifier = modifier, navigateToLogin)
    }

    LaunchedEffect(Unit) {
        if (viewModel.isUserAuthenticated) {
            Toast.makeText(context, context.getString(R.string.authenticated), Toast.LENGTH_SHORT)
                .show()
            navigateToHome()
        }
    }
}


@Composable
private fun EmailField(
    registerForm: RegisterForm,
    viewModel: RegisterScreenViewModel,
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
            value = registerForm.email,
            onValueChange = {
                viewModel.emailChanged(it)
                viewModel.clearError()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            label = { Text(text = stringResource(R.string.enter_e_mail)) },
            leadingIcon = {
                Icon(
                    Icons.Filled.Email,
                    contentDescription = stringResource(R.string.email_logo)
                )
            },
            singleLine = true,
            isError = errorMsg != null,
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
private fun UsernameField(
    registerForm: RegisterForm,
    viewModel: RegisterScreenViewModel,
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
            value = registerForm.login,
            onValueChange = {
                viewModel.loginChanged(it)
                viewModel.clearError()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            label = { Text(stringResource(R.string.enter_nickname)) },
            leadingIcon = {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = stringResource(R.string.person_logo)
                )
            },
            singleLine = true,
            isError = errorMsg != null,
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
    registerForm: RegisterForm,
    viewModel: RegisterScreenViewModel,
    passwordVisible: MutableState<Boolean>,
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
            value = registerForm.password,
            onValueChange = {
                viewModel.password1Changed(it)
                viewModel.clearError()
            },
            visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            label = { Text(stringResource(R.string.enter_password)) },
            leadingIcon = {
                Icon(
                    Icons.Filled.Lock,
                    contentDescription = stringResource(R.string.lock_logo)
                )
            },
            trailingIcon = {
                val image =
                    if (passwordVisible.value) painterResource(id = R.drawable.hide_password_icon)
                    else painterResource(id = R.drawable.show_password_icon)

                val description =
                    if (passwordVisible.value) stringResource(R.string.hide_password) else stringResource(
                        R.string.show_password
                    )

                IconButton(
                    onClick = { passwordVisible.value = !passwordVisible.value },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(image, description)
                }
            },
            singleLine = true,
            isError = errorMsg != null,
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
private fun PasswordControlField(
    registerForm: RegisterForm,
    viewModel: RegisterScreenViewModel,
    passwordVisible: MutableState<Boolean>,
    keyboardController: SoftwareKeyboardController?,
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
            .background(Color.White)

    ) {
        TextField(
            value = registerForm.passwordControl,
            onValueChange = {
                viewModel.password2Changed(it)
                viewModel.clearError()
            },
            visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            label = { Text(stringResource(R.string.repeat_password)) },
            leadingIcon = {
                Icon(
                    Icons.Filled.Lock,
                    contentDescription = stringResource(R.string.lock_logo)
                )
            },
            trailingIcon = {
                val image =
                    if (passwordVisible.value) painterResource(id = R.drawable.hide_password_icon)
                    else painterResource(id = R.drawable.show_password_icon)

                val description =
                    if (passwordVisible.value) stringResource(R.string.hide_password) else stringResource(
                        R.string.show_password
                    )

                IconButton(
                    onClick = { passwordVisible.value = !passwordVisible.value },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(image, description)
                }
            },

            supportingText = {
                if (errorMsg != null) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        text = errorMsg.message ?: if (errorMsg is PasswordDiffersException) {
                            stringResource(R.string.password_differs)
                        } else {
                            stringResource(R.string.exception_occured)
                        },
                        color = Color.Red
                    )
                    viewModel.password1Changed("")
                    viewModel.password2Changed("")
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
            singleLine = true,
            isError = errorMsg != null,
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
private fun SignUpButton(viewModel: RegisterScreenViewModel) {
    val loading by viewModel.loading
    Button(
        onClick = {
            viewModel.registerUserWithEmail()
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
        Column(
            modifier = Modifier
                .width(200.dp)
                .padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.sign_up), fontSize = 24.sp)
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
private fun LoginButton(
    modifier: Modifier,
    navigateToLogin : () -> Unit,
) {
    val destination = stringResource(id = R.string.LoginScreen)
    Button(
        onClick = navigateToLogin,
        shape = RoundedCornerShape(topStart = 12.dp),
        colors = ButtonColors(
            containerColor = colorResource(id = R.color.purple_fae),
            contentColor = colorResource(id = R.color.white),
            disabledContainerColor = colorResource(id = R.color.purple_fae),
            disabledContentColor = colorResource(id = R.color.white)
        ),
        modifier = modifier
    ) {
        Text(text = stringResource(R.string.log_in), fontSize = 24.sp)
    }
}