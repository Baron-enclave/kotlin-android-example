package com.example.kotlinandroidexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.kotlinandroidexample.compose.ui.theme.Black0F
import com.example.kotlinandroidexample.compose.ui.theme.KotlinAndroidExampleTheme

class YtbHomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinAndroidExampleTheme(dynamicColor = false) {
                HomeContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent() {
    val videoCategories = mutableListOf(
        VideoCategory("Tất cả"),
        VideoCategory("Trực tiếp"),
        VideoCategory("Trò chơi"),
        VideoCategory("Tin tức"),
        VideoCategory("Hoạt hình"),
        VideoCategory("Âm nhạc")
    )
    val videos = mutableListOf(
        YtbVideo(
            thumbnail = "https://i.ytimg.com/vi/eWyu9pwu__g/mqdefault.jpg",
            channelTitle = "Fitwaffle Kitchen",
            title = "Dulce De Leche Ice Cream! Recipe tutorial #Shorts"
        ),
        YtbVideo(
            thumbnail = "https://i.ytimg.com/vi/eWyu9pwu__g/mqdefault.jpg",
            channelTitle = "Fitwaffle Kitchen",
            title = "Dulce De Leche Ice Cream! Recipe tutorial #Shorts"
        ),
        YtbVideo(
            thumbnail = "https://i.ytimg.com/vi/eWyu9pwu__g/mqdefault.jpg",
            channelTitle = "Fitwaffle Kitchen",
            title = "Dulce De Leche Ice Cream! Recipe tutorial #Shorts"
        ),
        YtbVideo(
            thumbnail = "https://i.ytimg.com/vi/eWyu9pwu__g/mqdefault.jpg",
            channelTitle = "Fitwaffle Kitchen",
            title = "Dulce De Leche Ice Cream! Recipe tutorial #Shorts"
        ),
    )
    var videoCategorySelected by remember {
        mutableStateOf<VideoCategory?>(videoCategories.first())
    }
    var navItemSelected by remember {
        mutableStateOf(NavItems.HOME)
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                    title = {
                        Image(
                            painterResource(
                                R.drawable.youtube_logo,
                            ),
                            null,
                            modifier = Modifier.size(100.dp, 40.dp)
                        )
                    },
                    actions = {
                        Row {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(painterResource(R.drawable.cast), null)
                            }
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(painterResource(R.drawable.notifications_outline), null)
                            }
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(Icons.Default.Search, null)
                            }
                        }
                    }
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        count = videoCategories.size,

                        itemContent = { index ->
                            VideoCategoryChip(
                                videoCategory =
                                videoCategories[index],
                                selected = videoCategories[index] == videoCategorySelected,
                                onClick = { currentCategory ->
                                    videoCategorySelected = currentCategory
                                }
                            )
                        }
                    )
                }
            }
        },
        content = { innerPadding ->
            LazyColumn(
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                items(

                    count = videos.size,
                    itemContent = { index ->
                        VideoItem(videos[index])
                    }

                )
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .height(
                        WindowInsets.safeContent.only(WindowInsetsSides.Bottom).asPaddingValues()
                            .calculateBottomPadding() + 48.dp
                    ),
                containerColor = Color.White
            ) {
                for (item in NavItems.entries) {
                    when (item) {
                        NavItems.HOME -> {
                            NavBarItem(
                                selected = navItemSelected == item,
                                label = item.label,
                                unselectedIcon = {
                                    Icon(Icons.Outlined.Home, null)
                                },
                                selectedIcon = {
                                    Icon(Icons.Default.Home, null)
                                },
                                onClick = {
                                    navItemSelected = item
                                }
                            )
                        }

                        NavItems.SHORTS -> {
                            NavBarItem(
                                selected = navItemSelected == item,
                                label = item.label,
                                unselectedIcon = {
                                    Icon(Icons.Outlined.PlayArrow, null)
                                },
                                selectedIcon = {
                                    Icon(Icons.Default.PlayArrow, null)
                                },
                                onClick = {
                                    navItemSelected = item
                                }
                            )
                        }

                        NavItems.SHORTS_CREATION -> {
                            NavBarItem(
                                selected = navItemSelected == item,
                                label = item.label,
                                unselectedIcon = {
                                    Icon(
                                        Icons.Outlined.Add,
                                        null,
                                        Modifier
                                            .clip(CircleShape)
                                            .border(BorderStroke(1.dp, Color.Black), CircleShape)
                                            .size(32.dp)
                                    )
                                },

                                onClick = {
                                    navItemSelected = item
                                }
                            )
                        }

                        NavItems.SUBSCRIBED_CHANNEL -> {
                            NavBarItem(
                                selected = navItemSelected == item,
                                label = item.label,
                                unselectedIcon = {
                                    Icon(Icons.Outlined.FavoriteBorder, null)
                                },
                                selectedIcon = {
                                    Icon(Icons.Default.Favorite, null)
                                },
                                onClick = {
                                    navItemSelected = item
                                }
                            )
                        }

                        NavItems.PROFILE -> {
                            NavBarItem(
                                selected = navItemSelected == item,
                                label = item.label,
                                unselectedIcon = {
                                    Icon(Icons.Outlined.AccountCircle, null)
                                },
                                selectedIcon = {
                                    Icon(Icons.Default.AccountCircle, null)
                                },
                                onClick = {
                                    navItemSelected = item
                                }
                            )
                        }
                    }
                }

            }

        }
    )
}

@Composable
fun RowScope.NavBarItem(
    selected: Boolean,
    label: String? = null,
    selectedIcon: (@Composable () -> Unit)? = null,
    unselectedIcon: @Composable () -> Unit,
    onClick: (() -> Unit)
) {
    @Composable
    fun Item() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (selectedIcon == null || !selected)
                unselectedIcon.invoke()
            else {
                selectedIcon.invoke()
            }
            if (label != null)
                Text(text = label, fontSize = 10.sp, maxLines = 1)
        }
    }
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = { Item() },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = Color.White,
            selectedIconColor = Black0F,
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoCategoryChip(
    selected: Boolean,
    videoCategory: VideoCategory,
    onClick: ((videoCategory: VideoCategory) -> Unit)? = null
) {
    FilterChip(
        selected = selected,
        onClick = {
            onClick?.invoke(videoCategory)
        },
        label = {
            Text(text = videoCategory.name)
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.Black.copy(
                alpha = 0.05f
            )
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = false,
            borderColor = Color.Transparent
        ),
    )
}

@Composable
fun VideoItem(video: YtbVideo) {
    Column {
        Image(
            rememberAsyncImagePainter(model = video.thumbnail),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Cyan)
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .fillMaxSize()
        ) {
            AsyncImage(
                model = video.channelAvatar,
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(CircleShape)
                    .background(color = Color.Yellow)
                    .size(32.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = video.title,
                    maxLines = 2,
                    fontWeight = FontWeight.W500,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                Text(
                    text = "hello",
                    maxLines = 1,

                    )
            }
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = { /*TODO*/ },
            ) {
                Icon(Icons.Default.MoreVert, "", modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    KotlinAndroidExampleTheme(dynamicColor = false) {
        HomeContent()
    }
}

data class YtbVideo(
    val thumbnail: String,
    val title: String,
    val channelAvatar: String? = "https://yt3.ggpht.com/xxhb3lwPqHXiDp4RK5ALaPqOdD8SPJmJB0qiZJ0kizLaZIw8t7V8D9nX_6_n8iktSvPrsuPWdg=s68-c-k-c0x00ffffff-no-rj",
    val views: Int? = null,
    val channelTitle: String
)

data class VideoCategory(val name: String)

enum class NavItems(
    val label: String?,
//    val unselectedIcon: @Composable () -> Unit,
//    val selectedIcon: (@Composable () -> Unit)?
) {
    HOME("Trang chủ"),
    SHORTS("Shorts"),
    SHORTS_CREATION(null),
    SUBSCRIBED_CHANNEL("Kênh đăng ký"),
    PROFILE("Bạn")
}