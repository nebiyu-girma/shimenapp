package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.ShimenaViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
  data object Home : Screen("home", "The Journey", Icons.Default.LinearScale)
  data object Gallery : Screen("gallery", "The 10 Hands", Icons.Default.Groups)
  data object Archive : Screen("archive", "Living Archive", Icons.Default.Bookmark)
  data object About : Screen("about", "Our Mission", Icons.Default.Eco)
  data object Stage : Screen("stage/{stageId}", "Stage", Icons.Default.Handyman) {
    fun createRoute(stageId: Int) = "stage/$stageId"
  }
}

class MainActivity : ComponentActivity() {
  private val viewModel: ShimenaViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      MyApplicationTheme {
        ShimenaApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun ShimenaApp(viewModel: ShimenaViewModel) {
  val navController = rememberNavController()
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  val showBottomBar = currentRoute in listOf(
    Screen.Home.route,
    Screen.Gallery.route,
    Screen.Archive.route,
    Screen.About.route
  )

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = ShimenaCotton,
    bottomBar = {
      AnimatedVisibility(
        visible = showBottomBar,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
      ) {
        NavigationBar(
          containerColor = ShimenaCottonLight,
          contentColor = ShimenaCharcoal,
          tonalElevation = 8.dp,
          modifier = Modifier
            .border(1.dp, ShimenaCottonDark)
            .testTag("main_bottom_nav")
        ) {
          val navItems = listOf(Screen.Home, Screen.Gallery, Screen.Archive, Screen.About)
          navItems.forEach { screen ->
            val isSelected = currentRoute == screen.route
            NavigationBarItem(
              icon = {
                Icon(
                  imageVector = screen.icon,
                  contentDescription = screen.title,
                  modifier = Modifier.size(24.dp)
                )
              },
              label = {
                Text(
                  text = screen.title,
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                  )
                )
              },
              selected = isSelected,
              onClick = {
                if (currentRoute != screen.route) {
                  navController.navigate(screen.route) {
                    popUpTo(Screen.Home.route) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                  }
                }
              },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ShimenaCharcoalDark,
                selectedTextColor = ShimenaCharcoalDark,
                indicatorColor = ShimenaGold.copy(alpha = 0.35f),
                unselectedIconColor = TextSecondaryLight,
                unselectedTextColor = TextSecondaryLight
              ),
              modifier = Modifier.testTag("nav_item_${screen.route}")
            )
          }
        }
      }
    }
  ) { innerPadding ->
    NavHost(
      navController = navController,
      startDestination = Screen.Home.route,
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      composable(Screen.Home.route) {
        HomeScreen(
          viewModel = viewModel,
          onSelectStage = { stageId ->
            viewModel.selectStage(stageId)
            navController.navigate(Screen.Stage.createRoute(stageId))
          },
          onNavigateToGallery = { navController.navigate(Screen.Gallery.route) },
          onNavigateToArchive = { navController.navigate(Screen.Archive.route) },
          onNavigateToAbout = { navController.navigate(Screen.About.route) }
        )
      }

      composable(Screen.Gallery.route) {
        ArtisansGalleryScreen(
          onSelectStage = { stageId ->
            viewModel.selectStage(stageId)
            navController.navigate(Screen.Stage.createRoute(stageId))
          }
        )
      }

      composable(Screen.Archive.route) {
        TextileArchiveScreen(
          viewModel = viewModel,
          onStartNewJourney = {
            viewModel.selectStage(1)
            navController.navigate(Screen.Stage.createRoute(1))
          }
        )
      }

      composable(Screen.About.route) {
        AboutShimenaScreen(
          viewModel = viewModel,
          onResetJourney = {
            navController.navigate(Screen.Home.route) {
              popUpTo(Screen.Home.route) { inclusive = true }
            }
          }
        )
      }

      composable(
        route = Screen.Stage.route,
        arguments = listOf(navArgument("stageId") { type = NavType.IntType })
      ) { backStackEntry ->
        val stageId = backStackEntry.arguments?.getInt("stageId") ?: 1
        StageContainerScreen(
          stageId = stageId,
          viewModel = viewModel,
          onNavigateBack = { navController.popBackStack() },
          onNavigateToShare = {
            navController.navigate(Screen.Stage.createRoute(10)) {
              popUpTo(Screen.Home.route)
            }
          }
        )
      }
    }
  }
}
