package com.example.kotlinandroidexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.kotlinandroidexample.compose.AddListingScreen
import com.example.kotlinandroidexample.compose.AddressScreen
import com.example.kotlinandroidexample.compose.BusinessTypeSelectionScreen
import com.example.kotlinandroidexample.compose.ui.theme.KotlinAndroidExampleTheme

class HCComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainApp()
        }
    }
}


@Composable
fun MainApp() {
    val navController = rememberNavController()
    KotlinAndroidExampleTheme(dynamicColor = false) {
        NavHost(navController = navController, startDestination = "HC") {
            navigation(route = "HC", startDestination = "addListingScreen") {
                composable("addListingScreen") {
                    AddListingScreen(navController)
                }
                composable("businessTypeSelectionScreen") {
                    BusinessTypeSelectionScreen(navController)
                }
                composable("addressScreen") {
                    AddressScreen(navHostController = navController)
                }
            }

        }
    }
}