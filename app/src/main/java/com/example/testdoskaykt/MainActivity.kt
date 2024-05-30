package com.example.testdoskaykt

//import androidx.compose.material.BottomAppBar

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.testdoskaykt.ui.theme.TestDoskaYktTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestDoskaYktTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White,
                ) {
                   val mainViewModel: MainViewModel = viewModel()
                   NavigationHost(mainViewModel = mainViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, mainViewModel: MainViewModel = viewModel()){
    val query = remember { mutableStateOf("") }
    val isActive = remember { mutableStateOf(false) }

    val isRefreshing by mainViewModel.isRefreshing.collectAsState()
    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = { mainViewModel.refreshFeeds() })

    val scrollState = rememberScrollState()
    var isTopBarVisible by remember { mutableStateOf(true) }
    var isBottomBarVisible by remember { mutableStateOf(true) }
    val prevScroll = remember { mutableIntStateOf(0) }
    Log.d("Scroll", "${scrollState.maxValue}, ${scrollState.value}")


    LaunchedEffect(scrollState.value) {
        val currentScroll = scrollState.value

        if (currentScroll > prevScroll.value && isTopBarVisible && isBottomBarVisible ) {
            // Scroll down
            isTopBarVisible = false
            isBottomBarVisible = false
            Log.d("Eas", "Down")
        } else if (currentScroll < prevScroll.value && !isTopBarVisible && !isBottomBarVisible ) {
            // Scroll up
            isTopBarVisible = true
            isBottomBarVisible = true

            Log.d("Eas", "Up")
        }
        prevScroll.value = currentScroll
    }

    Scaffold(
        containerColor = Color.White,
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            AnimatedVisibility(
                visible = isTopBarVisible,
                enter = fadeIn(initialAlpha = 0.4f) + slideInVertically(),
                exit = fadeOut(targetAlpha = 0.4f) + slideOutVertically()
            ) {
                SearchBar(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .border(1.dp, Color.LightGray, Shapes().extraSmall)
                    ,
                    shape = Shapes().extraSmall,
                    colors = SearchBarDefaults.colors(
                        containerColor = Color.White,
                        dividerColor = Color.LightGray,
                        inputFieldColors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    ),
                    query = query.value,
                    placeholder = {
                        Text(
                            text = "Поиск в Якутске",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.height(30.dp)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    onQueryChange = {
                        query.value = it
                    },
                    onSearch = {

                    },
                    active = isActive.value ,
                    onActiveChange ={
                        isActive.value = it

                    }
                ) {}
            }
        },
        bottomBar = {AnimatedVisibility(
            visible = isBottomBarVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ){
            BottomAppBar(
                modifier = Modifier.border(1.dp, Color.LightGray),
                containerColor = Color.White,
                tonalElevation = 2.dp,
            ) {
                Row (horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.weight(1f)){
                    BottomBarItem(image = Icons.Filled.Home, text = "Главная"){}
                    BottomBarItem(image = Icons.Filled.Star, text = "Избранное"){}
                    BottomBarItem(image = Icons.Filled.Add, text = "Добавить"){}
                    BottomBarItem(image = Icons.Filled.Notifications, text = "Уведомления"){}
                    BottomBarItem(image = Icons.Filled.Person, text = "Профиль"){}
                }

            }
        }

        }
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .padding(it)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                CustomFeedsRow(mainViewModel,navController)
                CategoryRow(mainViewModel, navController)
                FeedColumnStyleRow()
                Spacer(modifier = Modifier
                    .height(8.dp)
                    .background(Color.White))
                FeedColumn(mainViewModel, scrollState, navController)
            }
            PullRefreshIndicator(refreshing = isRefreshing, state = pullRefreshState, modifier = Modifier.align(Alignment.TopCenter))
        }
    }
}

@Composable
fun BottomBarItem(image: ImageVector, text: String, onCLick: () -> Unit){
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Image(imageVector = image, contentDescription = null, modifier = Modifier.clickable { onCLick() })
        Text(text = text, fontSize = 9.sp, color = Color.LightGray)
    }
}