package com.example.android_development_capstone

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.android_development_capstone.ui.theme.OnPrimary
import com.example.android_development_capstone.ui.theme.OnSurface
import com.example.android_development_capstone.ui.theme.Primary
import com.example.android_development_capstone.ui.theme.Surface

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf("home", "search", "camera", "garden", "settings", "profile")
    val icons = listOf(Icons.Outlined.Home, Icons.Outlined.Search, Icons.Outlined.Camera, Icons.Outlined.LocalFlorist, Icons.Outlined.Settings, Icons.Outlined.Person)

    val routes = listOf("home", "classes", "roadmap", "leaderboard", "settings", "profile")
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Surface ,
                contentColor = OnSurface,
                tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = currentRoute == routes[index],
                onClick = {
                    val route = routes[index]
                    if (navController.currentBackStackEntry?.destination?.route != route) {
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = item,
                        modifier = Modifier.size(30.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary,
                    unselectedIconColor = Color(0xFF9BA8B0),
                    indicatorColor = Primary.copy(alpha = 0.2f)
                )
            )
        }
    }
}