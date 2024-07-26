@file:OptIn(ExperimentalLayoutApi::class)

package com.example.kotlinandroidexample.compose

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

const val TAG = "ADD_LIST"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddListingScreen(navController: NavHostController? = null) {
    Log.d(TAG, "AddListingScreen")
    var businessType by remember {
        mutableStateOf("")
    }
    var address by remember {
        mutableStateOf("")
    }
    var describeState by remember {
        mutableStateOf("")
    }
    val businessTypeStateData =
        navController?.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("businessType")
    val addressStateData =
        navController?.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("address")

    businessTypeStateData?.value?.let {
        businessType = it
    }

    addressStateData?.value?.let {
        address = it
    }




    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Log.d(TAG, "topBar: ")
            Surface(shadowElevation = 2.dp) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = ("Add Listing"),
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.W500
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = { /*TODO*/ },
                        ) {
                            Text(text = "SUBMIT")
                        }
                    }
                )
            }
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    vertical = 32.dp,
                    horizontal = 16.dp
                )
            ) {
                item {
                    GroupContent(title = "Basic Information", subTitle = "Required*") {
                        Log.d(TAG, "AddListingScreen: basic info")
                        MyTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = "",
                            onValueChange = {},
                            label = {
                                Text(text = "Business Name*")
                            },
                            onClearText = {}
                        )
                        MyTextFieldSelectable(value = address, label = {
                            Text(text = "Address*")
                        }, onClick = {
                            navController?.navigate("addressScreen")
                        })
                        MyTextFieldSelectable(value = businessType, label = {
                            Text(text = "Business Type*")
                        }, onClick = {
                            navController?.navigate("businessTypeSelectionScreen")
                        })
                        MyTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 100.dp),
                            value = describeState,
                            onValueChange = { describeState = it },
                            label = {
                                Text(text = "Describe this place*")
                            }, supportingText = {
                                Row {
                                    Text(
                                        text = "Minimum of 25 characters",
                                        modifier = Modifier.weight(1f),
                                        maxLines = 1,
                                        softWrap = true
                                    )
                                    Text(text = "${describeState.length}/65000")
                                }
                            },
                            onClearText = { describeState = "" }

                        )
                    }
                    GroupContent(title = "Price Range*") {
                        Log.d(TAG, "Price Range: ")
                        val options = mutableListOf("$", "$$", "$$$")
                        var selectedIndex by remember {
                            mutableIntStateOf(0)
                        }
                        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                            options.forEachIndexed { index, opt ->
                                SegmentedButton(
                                    selected = selectedIndex == index,
                                    onClick = { selectedIndex = index },
                                    shape = SegmentedButtonDefaults.itemShape(
                                        index = index,
                                        count = options.size,
                                        baseShape = SegmentedButtonDefaults.baseShape.copy(CornerSize(8.dp))
                                    ),
                                    icon = {},
                                    colors = SegmentedButtonDefaults.colors(
                                        activeContainerColor = MaterialTheme.colorScheme.primary,
                                        activeContentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
//                    border = BorderStroke(0.dp, color = Color.Transparent)
                                ) {
                                    Text(text = opt)
                                }
                            }
                        }
                    }
                    GroupContent(title = "What food do they serve?") {
                        Log.d(TAG, "What food do they serve: ")
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Surface(
                                border = BorderStroke(1.dp, Color.Gray),
                                shape = RoundedCornerShape(12.dp),
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = "Select all that apply*")
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        maxItemsInEachRow = 3
                                    ) {
                                        InputChip(
                                            selected = false,
                                            onClick = { /*TODO*/ },
                                            label = { Text("Dairy") })
                                        InputChip(
                                            selected = false,
                                            onClick = { /*TODO*/ },
                                            label = { Text("Egg") })
                                        InputChip(
                                            selected = false,
                                            onClick = { /*TODO*/ },
                                            label = { Text("Honey") })
                                        InputChip(
                                            selected = false,
                                            onClick = { /*TODO*/ },
                                            label = { Text("Meat / Fish") })
                                    }
                                    HorizontalDivider()
                                    InputChip(
                                        selected = false,
                                        onClick = { /*TODO*/ },
                                        label = { Text("None of the above") })
                                }
                            }
                            MyTextField(
                                modifier = Modifier.defaultMinSize(minHeight = 150.dp),
                                value = "",
                                onValueChange = {},
                                label = { Text(text = "Name 3+ vegan dishes on the menu*") })
                        }
                    }
                    GroupContent(
                        title = "Additional Information",
                        subTitle = "Optional - but tremendously helpful!",
                    ) {
                        SubGroup("Food Tags") {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                maxItemsInEachRow = 3,
                            ) {
                                InputChip(
                                    selected = false,
                                    onClick = { /*TODO*/ },
                                    label = { Text("American") })
                                InputChip(
                                    selected = false,
                                    onClick = { /*TODO*/ },
                                    label = { Text("European") })
                                InputChip(
                                    selected = false,
                                    onClick = { /*TODO*/ },
                                    label = { Text("Pizza") })
                                InputChip(
                                    selected = false,
                                    onClick = { /*TODO*/ },
                                    label = { Text("Gluten-Free") })
                                InputChip(
                                    selected = false,
                                    onClick = { /*TODO*/ },
                                    label = { Text("Breakfast") })
                                InputChip(
                                    selected = false,
                                    onClick = { /*TODO*/ },
                                    label = { Text("Catering") })
                                InputChip(
                                    selected = false,
                                    onClick = { /*TODO*/ },
                                    label = { Text("Fast Food") })
                                InputChip(
                                    selected = false,
                                    onClick = { /*TODO*/ },
                                    label = { Text("More") },
                                    trailingIcon = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null) },
                                    colors = InputChipDefaults.inputChipColors(
                                        trailingIconColor = MaterialTheme.colorScheme.primary,
                                        labelColor = MaterialTheme.colorScheme.primary,
                                    ),
                                    border = InputChipDefaults.inputChipBorder(
                                        enabled = true,
                                        selected = false,
                                        borderColor = MaterialTheme.colorScheme.primary
                                    )
                                )

                            }
                        }
                        SubGroup("Opening Hours") {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                MyTextFieldSelectable(
                                    value = "",
                                    onValueChange = {},
                                    label = { Text(text = "Select Hours") },
                                    onClick = {},
                                )
                                MyTextField(
                                    value = "",
                                    onValueChange = {},
                                    label = { Text(text = "Anything to note about these hours?") })

                            }
                        }
                        SubGroup("Payment Options") {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                maxItemsInEachRow = 3,
                            ) {
                                InputChip(
                                    selected = false,
                                    onClick = { /*TODO*/ },
                                    label = { Text("Cash only") })
                                InputChip(
                                    selected = false,
                                    onClick = { /*TODO*/ },
                                    label = { Text("accepts credit cards") })
                            }
                        }
                        SubGroup("Amenities") {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                maxItemsInEachRow = 3,
                            ) {
                                InputChip(
                                    selected = false,
                                    onClick = { /*TODO*/ },
                                    label = { Text("Outdoor seating") })
                                InputChip(
                                    selected = false,
                                    onClick = { /*TODO*/ },
                                    label = { Text("Reservation available") })
                                InputChip(
                                    selected = false,
                                    onClick = { /*TODO*/ },
                                    label = { Text("Wheelchair accessible") })
                                InputChip(
                                    selected = false,
                                    onClick = { /*TODO*/ },
                                    label = { Text("Wi-Fi") })
                            }
                        }
                        SubGroup("Contact") {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                MyTextField(
                                    value = "",
                                    onValueChange = {},
                                    label = { Text(text = "Phone Number") })
                                MyTextField(
                                    value = "",
                                    onValueChange = {},
                                    label = { Text(text = "Website URL") })
                                MyTextField(
                                    value = "",
                                    onValueChange = {},
                                    label = { Text(text = "Instagram URL") })
                                MyTextField(
                                    value = "",
                                    onValueChange = {},
                                    label = { Text(text = "Facebook URL") })
                                MyTextField(
                                    value = "",
                                    onValueChange = {},
                                    label = { Text(text = "Business Email") })
                                MyTextField(
                                    value = "",
                                    onValueChange = {},
                                    label = { Text(text = "Owner/manager name") })
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = false, onCheckedChange = {})
                                    Text(
                                        text = "I am the owner/manager",
                                        style = TextStyle(fontWeight = FontWeight.W500)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: (@Composable () -> Unit)? = null,
    supportingText: (@Composable () -> Unit)? = null,
    onClearText: (() -> Unit)? = null
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = label,
        supportingText = supportingText,
        trailingIcon = {
            if (value.isNotEmpty())
                IconButton(
                    onClick = {
                        onClearText?.invoke()
                    }, colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.Clear, null)
                }
        }
    )
}

@Composable
fun MyTextFieldSelectable(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: ((String) -> Unit)? = null,
    label: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                true, onClick = onClick,
            ),
        value = value,
        onValueChange = {
            onValueChange?.invoke(it)
        },
        label = label,
        readOnly = true,
        enabled = false,
        trailingIcon = {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
        },
        colors = TextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun GroupContent(
    title: String,
    subTitle: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(text = title, fontWeight = FontWeight.Bold)
        if (subTitle != null) Text(
            text = subTitle,
            modifier = Modifier.padding(top = 8.dp),
            color = Color.Gray,
            fontWeight = FontWeight.W500
        )
        Column(
            Modifier.padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = content,
        )
    }
}
@Composable
fun SubGroup(title: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = title,
            style = TextStyle(
                fontWeight = FontWeight.W500,
            ),
        )
        Surface(modifier = Modifier.padding(vertical = 16.dp), content = content)
    }
}

@Preview
@Composable
fun AddListingPreview() {
    AddListingScreen()
}