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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.lin_li_tranimg.R
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
    // 用于控制对话框显示的状态
    var showDialog by remember { mutableStateOf(false) }
    var dialogContent by remember { mutableStateOf<@Composable () -> Unit>({}) }

    // LaunchedEffect用于监听一次性事件
    LaunchedEffect(Unit) {
        viewModel.loginEvent.collect { event ->
            showDialog = true
            dialogContent = when (event) {
                LoginViewModel.LoginEvent.ShowLoading -> {
                    { LoginLoadingDialog() }
                }

                LoginViewModel.LoginEvent.ShowSuccess -> {
                    {
                        LoginSuccessDialog()
                        navController.navigate(STOCK_SCREEN)
                    }
                }

                LoginViewModel.LoginEvent.ShowFailure -> {
                    {
                        LoginFailedDialog(onDismiss = { showDialog = false })
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 設定背景圖片
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "背景圖片",
            contentScale = ContentScale.Crop, // 根據需要調整，這會確保圖片填滿容器
            modifier = modifier.matchParentSize() // 圖片將填滿Box容器
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
                //判斷是否記住密碼
                val switchBarState = viewModel.rememberPasswordSwitchState.value
                val account = viewModel.accountState.value.text
                val password = viewModel.passwordState.value.text
                viewModel.rememberPassWord(switchBarState)
                //調用login方法
                viewModel.login(account, password)
            })
        }
        // 根据状态显示对话框
        if (showDialog) {
            dialogContent()
        }
    }

}

@Composable
fun RememberPasswordSwitch(viewModel: LoginViewModel) {
    val switchState = viewModel.rememberPasswordSwitchState.collectAsState()
    Switch(
        checked = switchState.value,
        onCheckedChange = viewModel::onRememberPasswordSwitchChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountField(viewModel: LoginViewModel) {
    val account by viewModel.accountState.collectAsState()
    val isPasswordVisible by viewModel.isAccountVisible.collectAsState()

    OutlinedTextField(
        value = account,
        onValueChange = viewModel::onTextAccountChange,
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
            IconButton(onClick = { viewModel.onTogglePasswordVisibility() }) {
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
//            focusedLabelColor = Color.Black, // 当TextField被选中时Label的颜色
            unfocusedLabelColor = Color.Gray, // 当TextField未被选中时Label的颜色
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
    val password by viewModel.passwordState.collectAsState()
    OutlinedTextField(
        value = password.text,
        onValueChange = { viewModel.onTextPasswordChange(TextFieldValue(it)) },
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
//            focusedLabelColor = Color.Black, // 当TextField被选中时Label的颜色
            unfocusedLabelColor = Color.Gray, // 当TextField未被选中时Label的颜色
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
    // 获取登录按钮是否应该被启用的状态
    val isButtonEnabled by viewModel.isLoginButtonEnabled.collectAsState()
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
        Spacer(Modifier.weight(1f)) // 这会把剩下的空间占满，把后面的控件推到右边
        Text(
            text = "記住密碼",
            color = Color.White
        )
        RememberPasswordSwitch(viewModel) // 这个控件会靠右对齐
    }
}

@Composable
fun LoginLoadingDialog() {
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

@Composable
fun LoginSuccessDialog() {
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

@Composable
fun LoginFailedDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .size(299.dp, 178.dp)
                .background(DialogBackgroundColor)
                .clickable(
                    indication = null,
                    interactionSource = remember {
                        MutableInteractionSource()
                    }) { /* 使得点击对话框内部不会关闭对话框 */ },
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
            LoginTextBody(string = "登入失敗的錯誤訊息")
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

@Preview(showBackground = true)
@Composable
fun LoginLoadingDialogPreview() {
    LoginLoadingDialog()
}
