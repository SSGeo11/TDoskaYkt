package com.example.testdoskaykt

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.testdoskaykt.api.feed.Post

@Composable
fun FeedDetail(item: Post, mainViewModel: MainViewModel, navController: NavController){
    val recommendedFeeds by mainViewModel.recommendedFeeds.collectAsState()
    var selectedImageUrl by remember { mutableStateOf(item.picsUrl.firstOrNull()?.normalUrl ?: "") }
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ){
        if (item.picsUrl.isNotEmpty()) {
            AsyncImage(
                model = selectedImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop)
                if (item.picsUrl.size > 1){
                    LazyRow( modifier = Modifier.fillMaxWidth() ) {
                        itemsIndexed(item.picsUrl){_, it ->
                            DetailPhotoItem(url = it.thumbUrl){
                                selectedImageUrl = it.normalUrl
                                Log.d("detail", selectedImageUrl)
                            }
                        }
                    }
                }
        }else{
            Image(
                painter = painterResource(R.drawable.icon_139),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop
            )
        }

        Column(Modifier.padding(5.dp)){
            Text(
                text = item.titleView,
                fontSize = 20.sp,
                color = Color.Black
            )
            Text(
                text = item.price,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)
            )

            if (item.options.isNotEmpty()){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ){
                    item.options.forEach { it ->
                        Row(modifier = Modifier.padding(top = 5.dp)) {
                            Text(
                                text = it.title,
                                color = Color.Gray,
                                fontSize = 16.sp,
                                modifier = Modifier.width(150.dp)
                            )
                            Text(
                                text = it.value,
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            Text(
                text = item.text,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )
            FeedDetailInfo("Опубликован", item.publicDate)
            FeedDetailInfo("В избранном", item.favorites.toString())
            FeedDetailInfo("Просмотры", item.views.toString())
            Row(modifier = Modifier.padding(top = 5.dp)) {
                Button(onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(Color.White),
                    border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .weight(1f),
                    shape = Shapes().extraSmall
                )
                {
                    Icon(imageVector = Icons.Filled.Share, contentDescription = "Поделиться", tint = Color.Red)
                    Text(text = "Поделиться", fontSize = 16.sp,
                        color = Color.Black, modifier = Modifier.padding(horizontal = 4.dp),
                        fontWeight = FontWeight.Normal
                    )
                }

                Button(onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(Color.White),
                    border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)),
                    modifier = Modifier.padding(horizontal = 5.dp),
                    shape = Shapes().extraSmall
                )
                {
                    Icon(imageVector = Icons.Filled.FavoriteBorder, contentDescription = "В избранное", tint = Color.Red)
                }
            }
        }
        Button(onClick = { /*TODO*/ },
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Пожаловаться", modifier = Modifier.align(Alignment.CenterVertically),
                color = Color.Black, fontWeight = FontWeight.Normal)
        }
        Text(text = "Похожие объявления", color = Color.Black)
        mainViewModel.fetchRecommendedFeeds(item.categoryId.toString(), item.subcategoryId.toString(), item.rubricId.toString())
        val filteredFeeds = recommendedFeeds.filter { it.id != item.id }
        val displayFeeds = if (filteredFeeds.size > 5) filteredFeeds.subList(0, 6) else filteredFeeds

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(4.dp)
        ) {
            displayFeeds.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowItems.forEach { item ->
                        val onClick = { navController.navigate("feed/${Uri.encode(Post.toJson(item))}") }
                        FeedColumnItem1(item = item, onClick = { onClick() })
                    }
                }
            }
        }
    }
}


@Composable
fun DetailPhotoItem(url: String, onClick: () -> Unit){
    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = Modifier
            .size(120.dp, 60.dp)
            .padding(4.dp)
            .clickable { onClick() },
        contentScale = ContentScale.Crop
    )
}

@Composable
fun FeedDetailInfo(infoText: String, text: String){
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(imageVector = Icons.Filled.Info, contentDescription = null, tint = Color.Gray.copy(0.5f), modifier = Modifier.padding(end = 5.dp))
        Text(text = infoText, color = Color.Gray, modifier = Modifier.width(150.dp), fontSize = 14.sp)
        Text(text = text, color = Color.Black, fontSize = 14.sp)
    }
}


@Composable
fun FeedDetailScreen(navController: NavController, item: Post, mainViewModel: MainViewModel){
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                elevation = 2.dp,
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = Color.Red)
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.FavoriteBorder, contentDescription = "В избранное", tint = Color.Red)
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Share, contentDescription = "Поделиться", tint = Color.Red)
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White))
            {
                FeedDetail(item, mainViewModel, navController)
            }
        }
    )
}