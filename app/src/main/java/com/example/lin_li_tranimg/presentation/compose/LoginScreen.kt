package com.example.lin_li_tranimg.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.lin_li_tranimg.R
import com.example.lin_li_tranimg.presentation.LoginEvent
import com.example.lin_li_tranimg.presentation.UIEvent
import com.example.lin_li_tranimg.presentation.activity.MainActivity.Companion.FORGET_PASSWORD_SCREEN
import com.example.lin_li_tranimg.presentation.activity.MainActivity.Companion.GUEST_SCREEN
import com.example.lin_li_tranimg.presentation.activity.MainActivity.Companion.REGISTER_SCREEN
import com.example.lin_li_tranimg.presentation.activity.MainActivity.Companion.STOCK_SCREEN
import com.example.lin_li_tranimg.presentation.viewmodel.LoginViewModel
import com.example.lin_li_tranimg.ui.theme.ButtonStyles
import com.example.lin_li_tranimg.ui.theme.DialogBackgroundColor
import com.example.lin_li_tranimg.ui.theme.Yellow
import com.example.lin_li_tranimg.util.EmailVisualTransformation

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel
) {
    var currentDialog by remember { mutableStateOf<@Composable (() -> Unit)?>(null) }

    LaunchedEffect(key1 = true) {
        viewModel.uiEventFlow.collect { event ->
            when (event) {
                is UIEvent.NavigateToHomeScreen -> {
                    navController.navigate(STOCK_SCREEN)
                }

                is UIEvent.NavigateToRegisterScreen -> {
                    navController.navigate(REGISTER_SCREEN)
                }

                is UIEvent.NavigateToForgetPasswordScreen -> {
                    navController.navigate(FORGET_PASSWORD_SCREEN)
                }

                is UIEvent.NavigateToGuestScreen -> {
                    navController.navigate(GUEST_SCREEN)
                }

                is UIEvent.ShowErrorDialog -> {
                    val errorText = viewModel.loginScreenState.value.errorText
                    currentDialog = {
                        LoginFailedDialog(errorText) {
                            currentDialog = null
                        }
                    }
                }

                is UIEvent.ShowSuccessDialog -> {
                    currentDialog = { LoginSuccessDialog {} }
                }

                is UIEvent.ShowLoadingDialog -> {
                    currentDialog = { LoginLoadingDialog {} }
                }
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "背景圖片",
            contentScale = ContentScale.Crop,
            modifier = modifier.matchParentSize()
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(top = 150.dp)
        ) {
            AccountField(viewModel)
            PasswordField(viewModel)
            AboveLoginButtonRow(viewModel, navController)
            LoginButton(viewModel, onClick = {
                viewModel.onEvent(LoginEvent.LoginButtonClicked)
            })
            // 顯示登入狀態的對話框
            currentDialog?.invoke()
        }
    }

}

@Composable
fun RememberPasswordSwitch(viewModel: LoginViewModel) {
    val isRememberSwitchBarOn = viewModel.loginScreenState.value.isRememberSwitchBarOn
    Switch(
        checked = isRememberSwitchBarOn,
        onCheckedChange = { viewModel.onEvent(LoginEvent.RememberBarSwitched) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountField(viewModel: LoginViewModel) {
    val account = viewModel.loginScreenState.value.accountText
    val isPasswordVisible = viewModel.loginScreenState.value.isEyeOpened

    OutlinedTextField(
        value = account,
        onValueChange = { newText ->
            viewModel.onEvent(LoginEvent.AccountTextEntered(newText))
        },
        label = {
            Text(
                text = "CMoney帳號 (手機號碼或email)",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.icon_login_person),
                contentDescription = "帳號圖示",
                Modifier.size(24.dp)
            )
        },
        trailingIcon = {
            IconButton(onClick = { viewModel.onEvent(LoginEvent.IconEyeClicked) }) {
                Icon(
                    painter = painterResource(
                        id = if (isPasswordVisible) R.drawable.icon_open_eye else R.drawable.icon_close_eye
                    ),
                    contentDescription = if (isPasswordVisible) "隱藏帳號" else "顯示帳號",
                    Modifier.size(24.dp)
                )
            }
        },
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else EmailVisualTransformation(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedLabelColor = Color.Gray,
            textColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(viewModel: LoginViewModel) {
    val password = viewModel.loginScreenState.value.passwordText
    OutlinedTextField(
        value = password,
        onValueChange = { newText ->
            viewModel.onEvent(LoginEvent.PasswordTextEntered(newText))
        },
        label = {
            Text(
                text = "密碼",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.icon_password),
                contentDescription = "帳號圖示",
                Modifier.size(24.dp)
            )
        },
        visualTransformation = PasswordVisualTransformation(),
        textStyle = TextStyle(fontSize = 24.sp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedLabelColor = Color.Gray,
            textColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp)
            .background(Color.White)
    )
}

@Composable
fun LoginButton(viewModel: LoginViewModel, onClick: () -> Unit) {
    val isButtonEnabled = viewModel.loginScreenState.value.isLoginButtonEnabled
    Button(
        onClick = onClick,
        enabled = isButtonEnabled,
        colors = ButtonStyles.defaultButtonColors(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("登入")
    }
}

@Composable
fun AboveLoginButtonRow(
    viewModel: LoginViewModel,
    navController: NavController
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier.clickable {
                navController.navigate(FORGET_PASSWORD_SCREEN)
            },
            text = "忘記密碼",
            color = Color.White,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            modifier = Modifier.clickable {
                navController.navigate(REGISTER_SCREEN)
            },
            text = "註冊",
            color = Color.White
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            modifier = Modifier.clickable {
                navController.navigate(GUEST_SCREEN)
            },
            text = "訪客登入",
            color = Color.White
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = "記住密碼",
            color = Color.White
        )
        RememberPasswordSwitch(viewModel)
    }
}

@Composable
fun LoginLoadingDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .size(299.dp, 144.dp)
                .background(DialogBackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(64.dp),
                color = Yellow
            )
            LoginTextBody(string = "登入中，請您稍候...")
        }
    }
}

@Composable
fun LoginSuccessDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .size(299.dp, 144.dp)
                .background(DialogBackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginTextHeader(string = "登入成功")
            Icon(
                painter = painterResource(id = R.drawable.icon_login_success),
                contentDescription = "登入成功",
                Modifier.size(64.dp),
                tint = Yellow
            )
        }
    }
}

@Composable
fun LoginFailedDialog(errorMessage: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .size(299.dp, 178.dp)
                .background(DialogBackgroundColor)
                .clickable(
                    indication = null,
                    interactionSource = remember {
                        MutableInteractionSource()
                    }) {

                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginTextHeader(string = "登入失敗")
            Icon(
                painter = painterResource(id = R.drawable.icon_login_fail),
                contentDescription = "登入失敗",
                Modifier.size(64.dp),
                tint = Yellow
            )
            LoginTextBody(string = errorMessage)
        }
    }
}

@Composable
fun LoginTextHeader(string: String) {
    Text(
        color = Color.White,
        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
        text = string
    )
}

@Composable
fun LoginTextBody(string: String) {
    Text(
        color = Color.White,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        text = string
    )
}

