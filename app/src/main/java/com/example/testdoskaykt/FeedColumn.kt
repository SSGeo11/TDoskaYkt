package com.example.testdoskaykt

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.testdoskaykt.api.feed.Post

@Composable
fun FeedColumn(mainViewModel: MainViewModel, scrollState: ScrollState, navController: NavController){
    val feeds by mainViewModel.feeds.collectAsState()
    val isRefreshing by mainViewModel.isRefreshing.collectAsState()
    val isLoading by mainViewModel.isLoading.collectAsState()
    val styleState by mainViewModel.styleState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(if (styleState == 1) 4.dp else 0.dp)
    ){
        feeds.chunked(if (styleState==1) 2 else 1).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowItems.forEach {item ->
                    val onClick = {navController.navigate("feed/${Uri.encode(Post.toJson(item))}")}
                    when (styleState){
                        1 -> FeedColumnItem1(item = item, onClick = { onClick() })
                        2 -> FeedColumnItem2(item = item, onClick = { onClick() })
                        3 -> FeedColumnItem3(item = item, onClick = { onClick() })
                    }
                }

            }

        }

        if(isLoading || isRefreshing){
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(50.dp),
                color = Color.Red
            )
        }
        LaunchedEffect(scrollState.value) {
            if (scrollState.value == scrollState.maxValue && !isLoading) {
                mainViewModel.fetchMoreFeeds()
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun FeedColumnItem1(item: Post, onClick: () -> Unit) {
    Card(elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clickable { onClick() }
            .width(170.dp)
            .height(270.dp)

    ){
        Column(
            modifier = Modifier
                .background(Color.White)
        ) {
            if (item.picsUrl.isNotEmpty()) {
                AsyncImage(
                    model = item.picsUrl[0].previewUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(170.dp, 150.dp)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.icon_139),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(170.dp, 150.dp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
            ) {
                Text(
                    text = item.title,
                    fontSize = 14.sp,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 6.dp)
                )
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = item.price,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
                Text(
                    text = item.publicDate,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}

@Composable
fun FeedColumnItem2(item: Post, onClick: () -> Unit) {
    Card(elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxSize()
            .clickable { onClick() }

        ) {
            Column( modifier =  Modifier
                .background(Color.White)
            ){
                if (item.picsUrl.isNotEmpty()) {
                    AsyncImage(
                        model = item.picsUrl[0].previewUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.height(150.dp).fillMaxWidth()
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.icon_139),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(170.dp, 150.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 6.dp)
                ) {
                    Text(
                        text = item.title,
                        fontSize = 14.sp,
                        color = Color.Black,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = item.price,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                    Text(
                        text = item.publicDate,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }

    }

}

@Composable
fun FeedColumnItem3(item: Post, onClick: () -> Unit) {
    Card(elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onClick() }

    ){
        Row(modifier = Modifier
            .background(Color.White)
        ) {
            if (item.picsUrl.isNotEmpty()) {
                AsyncImage(
                    model = item.picsUrl[0].previewUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(170.dp, 150.dp)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.icon_139),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(170.dp, 150.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            ) {
                Text(
                    text = item.title,
                    fontSize = 14.sp,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.price,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = item.publicDate,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}

@Composable
fun FeedColumnStyleRow(mainViewModel: MainViewModel = viewModel()){
    val  selectedStyle by mainViewModel.styleState.collectAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Объявления Якутска",
            color = Color.Black,
            fontSize = 16.sp
        )
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            ButtonWithImage(
                selectedStyle = selectedStyle,
                currentStyle = 1,
                onClick = { mainViewModel.setStyle(1) },
                imageResId = R.drawable.feed_column_style_1
            )
            ButtonWithImage(
                selectedStyle = selectedStyle,
                currentStyle = 2,
                onClick = { mainViewModel.setStyle(2) },
                imageResId = R.drawable.feed_column_style_2
            )
            ButtonWithImage(
                selectedStyle = selectedStyle,
                currentStyle = 3,
                onClick = { mainViewModel.setStyle(3) },
                imageResId = R.drawable.feed_column_style_3
            )
        }

    }
}



@Composable
fun ButtonWithImage(selectedStyle: Int, currentStyle: Int, onClick: () -> Unit, imageResId: Int) {
    var isClicked = selectedStyle == currentStyle

    Box(
        modifier = Modifier
            .size(26.dp)
            .background(Color.White)
            .clickable { onClick() }
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        val imageColor = if (isClicked) Color.Red else Color.Gray
        Image(
            painter = rememberAsyncImagePainter(imageResId),
            contentDescription = null,
            colorFilter = ColorFilter.tint(imageColor),
            modifier = Modifier
                .size(24.dp)
        )
    }
}
