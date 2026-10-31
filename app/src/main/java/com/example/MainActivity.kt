package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GoalsScreen
import com.example.ui.screens.LiveWorkoutScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NaijaDarkBg
import com.example.ui.theme.NaijaDarkCard
import com.example.ui.theme.NaijaGold
import com.example.ui.theme.NaijaGreenAccent
import com.example.ui.theme.NaijaGreenPrimary
import com.example.ui.viewmodel.FitnessViewModel
import com.example.ui.viewmodel.LiveWorkoutViewModel

import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.outlined.Explore
import com.example.ui.screens.LocationMapScreen

enum class Screen(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    Dashboard("Spends", Icons.Filled.LocalFireDepartment, Icons.Outlined.LocalFireDepartment, "nav_tab_dashboard"),
    Map("Map", Icons.Filled.Explore, Icons.Outlined.Explore, "nav_tab_map"),
    Live("Live", Icons.AutoMirrored.Filled.DirectionsRun, Icons.AutoMirrored.Outlined.DirectionsRun, "nav_tab_live"),
    Analytics("Analytics", Icons.Filled.Leaderboard, Icons.Outlined.Leaderboard, "nav_tab_analytics"),
    Goals("Goals", Icons.Filled.Flag, Icons.Outlined.Flag, "nav_tab_goals")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent()
            }
        }
    }
}

@Composable
fun MainAppContent(
    fitnessViewModel: FitnessViewModel = viewModel(),
    liveViewModel: LiveWorkoutViewModel = viewModel()
) {
    var currentScreen by remember { mutableStateOf(Screen.Dashboard) }
    val liveState by liveViewModel.uiState.collectAsState()

    // Back handling: pop back to Dashboard if on another screen
    BackHandler(enabled = currentScreen != Screen.Dashboard) {
        currentScreen = Screen.Dashboard
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        containerColor = NaijaDarkBg,
        bottomBar = {
            NavigationBar(
                containerColor = NaijaDarkCard,
                contentColor = Color.White
            ) {
                Screen.values().forEach { screen ->
                    val isSelected = currentScreen == screen
                    val isLiveActive = screen == Screen.Live && liveState.isActive && !liveState.isPaused

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentScreen = screen },
                        modifier = Modifier.testTag(screen.testTag),
                        icon = {
                            Box(contentAlignment = Alignment.TopEnd) {
                                Icon(
                                    imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                                    contentDescription = screen.title,
                                    tint = if (isSelected) NaijaGreenAccent else Color(0xFFA5C2B2),
                                    modifier = Modifier.size(24.dp)
                                )
                                // Active glowing indicator on Live tab during ongoing workout
                                if (isLiveActive) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(NaijaGold, CircleShape)
                                    )
                                }
                            }
                        },
                        label = {
                            Text(
                                text = screen.title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp,
                                    color = if (isSelected) NaijaGreenAccent else Color(0xFFA5C2B2)
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = NaijaGreenPrimary.copy(alpha = 0.25f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.Dashboard -> DashboardScreen(
                    viewModel = fitnessViewModel,
                    onNavigateToLive = { currentScreen = Screen.Live }
                )
                Screen.Map -> LocationMapScreen(
                    viewModel = fitnessViewModel,
                    liveViewModel = liveViewModel,
                    onNavigateToLive = { currentScreen = Screen.Live }
                )
                Screen.Live -> LiveWorkoutScreen(
                    liveViewModel = liveViewModel
                )
                Screen.Analytics -> AnalyticsScreen(
                    viewModel = fitnessViewModel
                )
                Screen.Goals -> GoalsScreen(
                    viewModel = fitnessViewModel
                )
            }
        }
    }
}
