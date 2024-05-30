package com.example.testdoskaykt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.testdoskaykt.api.category.Category
import com.example.testdoskaykt.api.feed.Post

@Composable
fun NavigationHost(navController: NavHostController = rememberNavController(), mainViewModel: MainViewModel){
    NavHost(navController, startDestination = "main" ){
        composable("main"){ MainScreen(navController)}
        composable(
            "webview/{url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            WebViewScreen(navController, url)
        }
        composable("feed/{item}",
            arguments = listOf(navArgument("item") {type = NavType.StringType})
        ){backStackEntry ->
            val json = backStackEntry.arguments?.getString("item") ?: ""
            val post = Post.fromJson(json)
            FeedDetailScreen(navController, post, mainViewModel)
        }
        composable("categories") {
            val categories by mainViewModel.categories.collectAsState()
            AllCategoryScreen(navController, categories)
        }
        composable("category/{item}",
            arguments = listOf(navArgument("item") {type = NavType.StringType})
        ){backStackEntry ->
            val json = backStackEntry.arguments?.getString("item") ?: ""
            val category = Category.fromJson(json)
            CategoryScreen(category, navController, mainViewModel)
        }
    }

}
