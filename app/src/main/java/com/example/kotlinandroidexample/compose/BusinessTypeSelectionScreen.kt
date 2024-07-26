package com.example.kotlinandroidexample.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessTypeSelectionScreen(navHostController: NavHostController? = null) {
    val businessTypes = listOf(
        "Restaurant",
        "Health Stores",
        "Veg-Shop",
        "Bakery",
    )
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Business Type")
                },
                colors = TopAppBarDefaults.topAppBarColors(),
                navigationIcon = {
                    IconButton(onClick = {
                        navHostController!!.previousBackStackEntry?.savedStateHandle?.set(
                            "businessType",
                            "Business Type"
                        )
                        navHostController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        },
        content = {
            Surface(modifier = Modifier.padding(it)) {
                LazyVerticalGrid(columns = GridCells.Fixed(count = 2)) {
                    items(
                        count = businessTypes.size,
                        itemContent = { index ->
                            TextButton(onClick = {
                                navHostController!!.previousBackStackEntry?.savedStateHandle?.set(
                                    "businessType",
                                    businessTypes[index]
                                )
                                navHostController.popBackStack()

                            }) {
                                Text(text = businessTypes[index])
                            }
                        }
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun BusinessTypeSelectionScreenPreview() {
    BusinessTypeSelectionScreen()
}