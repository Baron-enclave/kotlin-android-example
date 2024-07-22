package com.example.kotlinandroidexample.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlinandroidexample.views.ui.theme.KotlinAndroidExampleTheme

class LoginComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinAndroidExampleTheme {
                LoginScreen()
            }
        }
    }
}

@Composable
fun LoginScreen() {
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var email by rememberSaveable {
        mutableStateOf("")
    }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
                .fillMaxSize()
        ) {
            EmailField(email) {
                email = it
            }
            PasswordField(password) {
                password = it
            }
            ElevatedButton(
                onClick = {},
            ) {
                Text(text = "Login")
            }
        }

    }
}

@Composable
fun EmailField(email: String, onEmailChange: (String) -> Unit) {
    TextField(
        value = email,
        label = {
            Text(text = "Email")
        },
        placeholder = {
            Text(text = "Enter your email")
        },
        onValueChange = onEmailChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            autoCorrect = true
        ), modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
    )
}

@Composable
fun PasswordField(password: String, onPasswordChange: (String) -> Unit) {
    TextField(
        value = password,
        label = {
            Text("Password")
        },
        placeholder = {
            Text(text = "Enter your password")
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            autoCorrect = true,
        ),
        visualTransformation = PasswordVisualTransformation(),
        onValueChange = onPasswordChange,
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    KotlinAndroidExampleTheme {
        LoginScreen()
    }
}