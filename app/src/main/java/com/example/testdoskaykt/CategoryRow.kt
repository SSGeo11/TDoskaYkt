package com.example.testdoskaykt

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.testdoskaykt.api.category.Category

@Composable
fun CategoryRow(mainViewModel: MainViewModel, navController: NavController) {
    val categories by mainViewModel.categories.collectAsState()

    LazyRow(){
        item {
            Column (
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
                    .clickable(onClick = { navController.navigate("categories") },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(painter = painterResource(R.drawable.icon_all_categories), contentDescription = null, modifier = Modifier.size(48.dp), colorFilter = ColorFilter.tint(
                    Color.Red))
                Text(
                    text = "Все категории",
                    color = Color.Black,
                    fontSize = 10.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.size(100.dp, 40.dp),
                    style = TextStyle(textAlign = TextAlign.Center)
                )
            }
        }
        itemsIndexed(categories){_, it ->
            CategoryRowItem(item = it) {
                navController.navigate("category/${Uri.encode(Category.toJson(it))}")
            }
        }
    }
}

@Composable
fun CategoryRowItem(item: Category, onClick: () -> Unit){
    val image = LocalContext.current.resources.getIdentifier("icon_${item.id}", "drawable", LocalContext.current.packageName)
    Column (
        modifier = Modifier
            .size(100.dp)
            .padding(8.dp)
            .clickable(onClick = { onClick() },
                indication = null, interactionSource = remember { MutableInteractionSource() }
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(painter = painterResource(id = image), contentDescription = null, modifier = Modifier.size(48.dp), colorFilter = ColorFilter.tint(
            Color.Red))
        Text(
            text = item.name,
            color = Color.Black,
            fontSize = 10.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.size(100.dp, 40.dp),
            style = TextStyle(textAlign = TextAlign.Center)
        )
    }
}

@Composable
fun AllCategoryScreen(navController: NavController, categories: List<Category>){
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                elevation = 2.dp,
                title = { Text(text = "Все категории", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.Red
                        )
                    }
                }
            )
        },
        content = {innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentPadding = innerPadding,

            ){
                itemsIndexed(categories){_, it ->
                    AllCategoryItem(it){
                        navController.navigate("category/${Uri.encode(Category.toJson(it))}")
                    }
                }
            }
        }
    )
}

@Composable
fun AllCategoryItem(category: Category, onClick: () -> Unit){
    val isPriority = remember{ {it: Boolean -> if (it) FontWeight.Bold else FontWeight.Normal} }
    Row (
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick() }
    ){
        Text(
            text = category.name,
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = isPriority(category.isPriority)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = category.count.toString(),
            color = Color.Black,
            fontSize = 14.sp
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryScreen(category: Category, navController: NavController, mainViewModel: MainViewModel) {
    mainViewModel.setCategoryId(category.id.toString())
    val subcategoryId by mainViewModel.subcategoryId.collectAsState()
    val rubricId by mainViewModel.rubricId.collectAsState()

    BackHandler {
        when {
            subcategoryId == null -> {
                mainViewModel.setNull()
                navController.navigateUp()
            }
            rubricId == null -> {
                mainViewModel.setSubcategoryId(null)
            }
            else -> {
                mainViewModel.setRubricId(null)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                elevation = 2.dp,
                title = { Text(text = category.name, color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = {
                        when {
                            subcategoryId == null -> {
                                mainViewModel.setNull()
                                navController.navigateUp()
                            }
                            rubricId == null -> {
                                mainViewModel.setSubcategoryId(null)
                            }
                            else -> {
                                mainViewModel.setRubricId(null)
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.Red
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            val scrollState = rememberScrollState()
            val isRefreshing by mainViewModel.isRefreshing.collectAsState()
            val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = { mainViewModel.refreshFeeds() })

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
                    .padding(innerPadding)
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    SubcategoryRow(mainViewModel, category, subcategoryId, rubricId)
                    FeedColumn(mainViewModel,scrollState ,navController)

                }
                PullRefreshIndicator(refreshing = isRefreshing, state = pullRefreshState, modifier = Modifier.align(
                    Alignment.TopCenter))
            }
        }
    )
}

@Composable
fun SubcategoryRow(mainViewModel: MainViewModel, category: Category, subcategoryId: String?, rubricId: String?){
    val subcategories = category.subcategories
    val subcategory = subcategories.find { it.id.toString() == subcategoryId } ?: subcategories.first()
    val rubric = subcategory.rubrics.find { it.id.toString() == rubricId } ?: subcategory.rubrics.firstOrNull()


    var selectedSubcategory by remember { mutableStateOf(subcategory) }
    var selectedRubric by remember { mutableStateOf(rubric) }
    Log.d("mama", selectedSubcategory.name)
    LazyRow(modifier = Modifier.padding(4.dp)
    ){
        if (subcategoryId == null && rubricId == null){
            item {
               Card(
                    modifier = Modifier
                        .height(30.dp)
                        .padding(horizontal = 4.dp),
                    colors = CardDefaults.cardColors(Color.Red),
                    shape = Shapes().extraSmall
                ){
                    Text(text = category.name, color = Color.Black, fontSize = 14.sp,  modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp))
                }
            }
            itemsIndexed(category.subcategories){_, it ->
                Card(
                    modifier = Modifier
                        .height(30.dp)
                        .padding(horizontal = 4.dp)
                        .clickable {
                            mainViewModel.setSubcategoryId(it.id.toString())
                            selectedSubcategory = it
                        },
                    colors = CardDefaults.cardColors(Color.LightGray),
                    shape = Shapes().extraSmall
                ){
                    Text(text = it.name, color = Color.Black, fontSize = 14.sp,  modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp))
                }
            }
        }else if(subcategoryId != null && rubricId == null){
            item {
                Card(
                    modifier = Modifier
                        .height(30.dp)
                        .padding(horizontal = 4.dp),
                    colors = CardDefaults.cardColors(Color.Red),
                    shape = Shapes().extraSmall
                ){
                    Text(text = selectedSubcategory.name, color = Color.Black, fontSize = 14.sp,  modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp))
                }
            }
            itemsIndexed(selectedSubcategory.rubrics){_, it ->
                Card(
                    modifier = Modifier
                        .height(30.dp)
                        .padding(horizontal = 4.dp)
                        .clickable {
                            mainViewModel.setRubricId(it.id.toString())
                            selectedRubric = it
                        },
                    colors = CardDefaults.cardColors(Color.LightGray),
                    shape = Shapes().extraSmall
                ){
                    Text(text = it.name, color = Color.Black, fontSize = 14.sp,  modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp))
                }
            }
        }else if(subcategoryId != null && rubricId != null){
            item {
                Card(
                    modifier = Modifier
                        .height(30.dp)
                        .padding(horizontal = 4.dp),
                    colors = CardDefaults.cardColors(Color.Red),
                    shape = Shapes().extraSmall
                ){
                    Text(text = selectedSubcategory.name, color = Color.Black, fontSize = 14.sp,  modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp))
                }
            }
            item {
                Card(
                    modifier = Modifier
                        .height(30.dp)
                        .padding(horizontal = 4.dp),
                    colors = CardDefaults.cardColors(Color.Red),
                    shape = Shapes().extraSmall
                ){
                    selectedRubric?.let { Text(text = it.name, color = Color.Black, fontSize = 14.sp,  modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp)) }
                }
            }
            item {
                Card(
                    modifier = Modifier
                        .height(30.dp)
                        .padding(horizontal = 4.dp),
                    colors = CardDefaults.cardColors(Color.LightGray),
                    shape = Shapes().extraSmall
                ){
                    Text(text = "Цена", color = Color.Black, fontSize = 14.sp,  modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp))
                }
            }
        }
    }
}
