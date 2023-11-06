package com.example.lin_li_tranimg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lin_li_tranimg.ui.theme.Lin_li_tranimgTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lin_li_tranimgTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyPage()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPage(modifier: Modifier = Modifier,viewModel: LoginViewModel = viewModel()) {
    val account by viewModel.accountState.collectAsState()
    val password by viewModel.passwordState.collectAsState()
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

            OutlinedTextField(
                value = account,
                onValueChange = viewModel::onTextAccountChange,
                label = { Text("CMoney帳號 (手機號碼或email)") },
                leadingIcon = {
                    // 添加在最左邊的圖示
                    Icon(
                        painter = painterResource(id = R.drawable.icon_login_person),
                        contentDescription = "帳號圖示",
                        Modifier.size(24.dp,24.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp)
                    .background(Color.White)
            )
            OutlinedTextField(
                value = password,
                onValueChange = viewModel::onTextPasswordChange,
                label = { Text("密碼") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp)
                    .background(Color.White)
            )
            // 登入按鈕
            Button(
                onClick = { /* 处理点击事件 */ },


                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("登入")
            }
            // 切換開關
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("忘記密碼", color = Color.White)
                Spacer(modifier = Modifier.width(5.dp))
                Text("註冊", color = Color.White)
                Spacer(modifier = Modifier.width(5.dp))
                Text("訪客登入", color = Color.White)
                Spacer(Modifier.weight(1f))
                Text("記住密碼", color = Color.White)
                Switch(checked = false, onCheckedChange = {})
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Lin_li_tranimgTheme {
        MyPage()
    }
}

