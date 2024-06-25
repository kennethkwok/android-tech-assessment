package com.pelagohealth.codingchallenge.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.pelagohealth.codingchallenge.feature.fact.FactScreen
import com.pelagohealth.codingchallenge.feature.fact.FactViewModel

object Routes {
    const val NAVIGATION_FACTS = "facts"
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.NAVIGATION_FACTS) {
        composable(Routes.NAVIGATION_FACTS) {
            val viewModel: FactViewModel = hiltViewModel()
            FactScreen(viewModel = viewModel)
        }
    }
}