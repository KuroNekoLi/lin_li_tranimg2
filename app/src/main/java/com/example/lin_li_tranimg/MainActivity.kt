package com.example.lin_li_tranimg

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lin_li_tranimg.MainActivity.Companion.TAG
import com.example.lin_li_tranimg.domain.LoginRepositoryImpl
import com.example.lin_li_tranimg.ui.theme.ButtonStyles
import com.example.lin_li_tranimg.ui.theme.Lin_li_tranimgTheme
import com.example.lin_li_tranimg.util.EmailVisualTransformation

class MainActivity : ComponentActivity() {
    private val loginRepository = LoginRepositoryImpl()
    companion object {
        const val TAG = "LinLi"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Lin_li_tranimgTheme {
                val viewModel: LoginViewModel  = viewModel(factory = LoginViewModelFactory(LocalContext.current,loginRepository))
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyPage(
                        Modifier,
                        viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun MyPage(modifier: Modifier = Modifier, viewModel: LoginViewModel) {

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 設定背景圖片
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "背景圖片",
            contentScale = ContentScale.Crop, // 根據需要調整，這會確保圖片填滿容器
            modifier = Modifier.matchParentSize() // 圖片將填滿Box容器
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(top = 150.dp)
        ) {
            AccountField(viewModel)
            PasswordField(viewModel)
            LoginButton(viewModel,onClick = {
                //判斷是否記住密碼
                val switchBarState = viewModel.rememberPasswordSwitchState.value
                viewModel.rememberPassWord(switchBarState)
            })
            LinkRow(viewModel)
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
        label = { Text("CMoney帳號 (手機號碼或email)") },
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
    Log.i(TAG, "text: ${password.text}")
    OutlinedTextField(
        value = password.text,
        onValueChange = { viewModel.onTextPasswordChange(TextFieldValue(it)) },
        label = { Text("密碼") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp)
            .background(Color.White)
    )
}

@Composable
fun LoginButton(viewModel: LoginViewModel,onClick: () -> Unit) {
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
fun LinkRow(viewModel: LoginViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text("忘记密码", color = Color.White)
        Spacer(modifier = Modifier.width(10.dp))
        Text("注册", color = Color.White)
        Spacer(modifier = Modifier.width(10.dp))
        Text("访客登录", color = Color.White)
        Spacer(Modifier.weight(1f)) // 这会把剩下的空间占满，把后面的控件推到右边
        Text("记住密码", color = Color.White)
        RememberPasswordSwitch(viewModel) // 这个控件会靠右对齐
    }
}

@Composable
fun LoggingLoadingDialog(){
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator()
    }
}
@Preview(showBackground = true)
@Composable
fun LoggingLoadingDialogPreview(){
    LoggingSuccessDialog()
}
@Composable
fun LoggingSuccessDialog() {
    Box(
        contentAlignment = Alignment.Center, 
        modifier = Modifier
            .size(299.dp, 144.dp)
            .background(MaterialTheme.colorScheme.onBackground)
    ) {
       LoginTextHeader(string = "登入成功")
    }
}

@Composable
fun LoggingFailedDialog() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        LoginTextHeader(string = "登入失敗")
    }
}

@Composable
fun LoginTextHeader(string: String){
    Text(
        color = Color.White,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        text = string)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Lin_li_tranimgTheme {
//        MyPage()
    }
}

