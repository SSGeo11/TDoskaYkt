package com.example.testdoskaykt


import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.testdoskaykt.api.customfeed.CustomFeed


@Composable
fun CustomFeedRowItem(item: CustomFeed, onClick: () -> Unit){
    Card(
        modifier = Modifier
            .clickable { onClick() }
            .size(280.dp, 150.dp)
            .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
    ) {
        Box (modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
        ){
            AsyncImage(model = item.originalUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column (modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomStart)
            ){
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.subtitle,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}
@Composable
fun CustomFeedsRow(mainViewModel: MainViewModel, navController: NavController) {
    val customFeeds by mainViewModel.customFeeds.collectAsState()
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
    ){
        itemsIndexed(customFeeds){_, item ->
            CustomFeedRowItem(
                item = item,
                onClick = { navController.navigate("webview/${Uri.encode(item.url)}") }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(navController: NavController, url: String){
    Log.d("Web", "Start")
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = Color.Black
                ),
                title = { Text(text = "Подборки") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = Color.Black)
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)) {
                val context = LocalContext.current
                AndroidView(
                    factory = { WebView(context).apply {
                        webViewClient = WebViewClient()
                        loadUrl(url)
                    }},
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    )
    Log.d("Web", "Stop")
}
