package com.eclock.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.eclock.app.ui.screens.AlarmScreen
import com.eclock.app.ui.screens.ClockScreen
import com.eclock.app.ui.screens.CountdownScreen
import com.eclock.app.ui.screens.TimerScreen
import com.eclock.app.ui.screens.WorldTimeScreen

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Clock : Screen("clock", "Clock", Icons.Default.AccessTime)
    object Timer : Screen("timer", "Timer", Icons.Default.Timer)
    object Countdown : Screen("countdown", "Countdown", Icons.Default.HourglassEmpty)
    object WorldTime : Screen("worldtime", "World Time", Icons.Default.Public)
    object Alarm : Screen("alarm", "Alarm", Icons.Default.Alarm)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    var bottomBarVisible by remember { mutableStateOf(true) }
    
    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                NavigationBar {
                    val items = listOf(
                        Screen.Clock,
                        Screen.Timer,
                        Screen.Countdown,
                        Screen.WorldTime,
                        Screen.Alarm
                    )
                    
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Clock.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Clock.route) { ClockScreen() }
            composable(Screen.Timer.route) { TimerScreen() }
            composable(Screen.Countdown.route) { CountdownScreen() }
            composable(Screen.WorldTime.route) { WorldTimeScreen() }
            composable(Screen.Alarm.route) { AlarmScreen() }
        }
    }
}
