package com.example.lin_li_tranimg.presentation.compose

import android.app.Activity
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.view.WindowCompat
import com.example.lin_li_tranimg.R
import com.example.lin_li_tranimg.presentation.LoginDialogType
import com.example.lin_li_tranimg.presentation.LoginEvent
import com.example.lin_li_tranimg.presentation.viewmodel.LoginViewModel
import com.example.lin_li_tranimg.ui.theme.ButtonStyles
import com.example.lin_li_tranimg.ui.theme.ButtonTypography
import com.example.lin_li_tranimg.ui.theme.DialogBackgroundColor
import com.example.lin_li_tranimg.ui.theme.LoginStatusBarColor
import com.example.lin_li_tranimg.ui.theme.Yellow
import com.example.lin_li_tranimg.util.EmailVisualTransformation
import org.koin.androidx.compose.getViewModel

/**
 * 登入頁面的 Composable 函數。
 * 提供登入界面和相關的用戶互動功能。
 *
 * @param onRegister 當用戶點擊註冊時的回調。
 * @param onLogin 當用戶成功登入時的回調。
 * @param onForgetPassword 當用戶點擊忘記密碼時的回調。
 * @param onGuest 當用戶選擇訪客登入時的回調。
 * @param modifier Composable 的修飾符。
 */
@Composable
fun LoginScreen(
    onRegister: () -> Unit,
    onLogin: () -> Unit,
    onForgetPassword: () -> Unit,
    onGuest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: LoginViewModel = getViewModel()
    val loginScreenState = viewModel.loginScreenState.value
    LoginScreenTheme {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = stringResource(R.string.bg_login_content),
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
                AccountField(
                    account = loginScreenState.accountText,
                    onAccountChange = { viewModel.onEvent(LoginEvent.AccountTextEntered(it)) },
                    onIconEyeClick = { viewModel.onEvent(LoginEvent.IconEyeClicked) },
                    isAccountVisible = loginScreenState.isEyeOpened
                )
                PasswordField(
                    password = loginScreenState.passwordText,
                    onPasswordChange = { viewModel.onEvent(LoginEvent.PasswordTextEntered(it)) }
                )
                AboveLoginButtonRow(
                    onForgetPassword = onForgetPassword,
                    onRegister = onRegister,
                    onGuest = onGuest,
                    isSwitchBarChecked = loginScreenState.isRememberSwitchBarOn,
                    onSwitchBarChecked = { viewModel.onEvent(LoginEvent.RememberBarSwitched) }

                )
                LoginButton(
                    loginScreenState.isLoginButtonEnabled,
                    onClick = { viewModel.onEvent(LoginEvent.LoginButtonClicked) })
                // 根據登入成功與否顯示登入狀態的對話框
                when (loginScreenState.loginDialogType) {
                    LoginDialogType.Loading -> LoginLoadingDialog {}
                    LoginDialogType.Success -> {
                        LoginSuccessDialog {}
                        viewModel.resetDialogTypeToNone()
                        onLogin()
                    }

                    is LoginDialogType.Error -> {
                        LoginFailedDialog(
                            errorMessage = loginScreenState.loginDialogType.message,
                            onDismiss = { viewModel.resetDialogTypeToNone() }
                        )
                    }

                    LoginDialogType.None -> {}
                }
            }
        }
    }
}

@Composable
private fun AutoLoginSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountField(
    account: String,
    onAccountChange: (String) -> Unit,
    onIconEyeClick: () -> Unit,
    isAccountVisible: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = account,
        onValueChange = onAccountChange,
        label = {
            Text(
                text = stringResource(R.string.account_label),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.icon_login_person),
                contentDescription = stringResource(R.string.icon_account_content),
                modifier.size(24.dp)
            )
        },
        trailingIcon = {
            IconButton(onClick = onIconEyeClick) {
                Icon(
                    painter = painterResource(
                        id = if (isAccountVisible) R.drawable.icon_open_eye else R.drawable.icon_close_eye
                    ),
                    contentDescription = if (isAccountVisible) stringResource(R.string.icon_eye_opened_content) else stringResource(R.string.icon_eye_closeed_content),
                    modifier.size(24.dp)
                )
            }
        },
        visualTransformation = if (isAccountVisible) VisualTransformation.None else EmailVisualTransformation(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedLabelColor = Color.Gray,
            textColor = Color.Black
        ),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = {
            Text(
                text = stringResource(R.string.password_label),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.icon_password),
                contentDescription = stringResource(R.string.icon_password_content),
                modifier.size(24.dp)
            )
        },
        visualTransformation = PasswordVisualTransformation(),
        textStyle = TextStyle(fontSize = 24.sp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedLabelColor = Color.Gray,
            textColor = Color.Black
        ),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp)
            .background(Color.White)
    )
}

@Composable
private fun LoginButton(
    isButtonEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = isButtonEnabled,
        colors = ButtonStyles.defaultButtonColors(),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.login_by_guest),
            fontSize = ButtonTypography.fontSize
        )
    }
}

@Composable
private fun AboveLoginButtonRow(
    onForgetPassword: () -> Unit,
    onRegister: () -> Unit,
    onGuest: () -> Unit,
    isSwitchBarChecked: Boolean,
    onSwitchBarChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            modifier = modifier.clickable {
                onForgetPassword()
            },
            text = stringResource(R.string.forget_password),
            color = Color.White,
        )
        Spacer(modifier = modifier.width(10.dp))
        Text(
            modifier = modifier.clickable {
                onRegister()
            },
            text = stringResource(R.string.register),
            color = Color.White
        )
        Spacer(modifier = modifier.width(10.dp))
        Text(
            modifier = modifier.clickable {
                onGuest()
            },
            text = stringResource(R.string.login_by_guest),
            color = Color.White
        )
        Spacer(modifier.weight(1f))
        Text(
            text = stringResource(R.string.remember_password),
            color = Color.White
        )
        AutoLoginSwitch(
            checked = isSwitchBarChecked,
            onCheckedChange = onSwitchBarChecked
        )
    }
}

@Composable
private fun LoginLoadingDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = modifier
                .size(299.dp, 144.dp)
                .background(DialogBackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = modifier
                    .width(64.dp),
                color = Yellow
            )
            LoginTextBody(stringResource(R.string.loading))
        }
    }
}

@Composable
private fun LoginSuccessDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = modifier
                .size(299.dp, 144.dp)
                .background(DialogBackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginTextHeader(stringResource(R.string.login_successful))
            Icon(
                painter = painterResource(id = R.drawable.icon_login_success),
                contentDescription = stringResource(R.string.login_successful),
                modifier.size(64.dp),
                tint = Yellow
            )
        }
    }
}

@Composable
private fun LoginFailedDialog(
    modifier: Modifier = Modifier,
    errorMessage: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = modifier
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
            LoginTextHeader(stringResource(R.string.login_failed))
            Icon(
                painter = painterResource(id = R.drawable.icon_login_fail),
                contentDescription = stringResource(R.string.login_failed),
                modifier.size(64.dp),
                tint = Yellow
            )
            LoginTextBody(string = errorMessage)
        }
    }
}

@Composable
private fun LoginTextHeader(string: String) {
    Text(
        color = Color.White,
        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
        text = string
    )
}

@Composable
private fun LoginTextBody(string: String) {
    Text(
        color = Color.White,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        text = string
    )
}
@Composable
fun LoginScreenTheme(content: @Composable () -> Unit) {
    val currentView = LocalView.current

    MaterialTheme(
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = {
            SideEffect {
                val window = (currentView.context as Activity).window
                window.statusBarColor = LoginStatusBarColor.toArgb()
                WindowCompat.getInsetsController(window, currentView).isAppearanceLightStatusBars = false
            }
            content()
        }
    )
}
