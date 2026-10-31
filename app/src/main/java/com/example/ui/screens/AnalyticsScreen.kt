package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ArewaKnotBadge
import com.example.ui.components.WorkoutCategoryDonutChart
import com.example.ui.theme.NaijaDarkCard
import com.example.ui.theme.NaijaGold
import com.example.ui.theme.NaijaGreenAccent
import com.example.ui.theme.NaijaGreenDark
import com.example.ui.theme.NaijaGreenPrimary
import com.example.ui.theme.NaijaOrange
import com.example.ui.viewmodel.FitnessViewModel

data class FitnessBadge(
    val title: String,
    val description: String,
    val isUnlocked: Boolean,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun AnalyticsScreen(
    viewModel: FitnessViewModel,
    modifier: Modifier = Modifier
) {
    val allWorkouts by viewModel.allWorkouts.collectAsState()
    val dailyLogs by viewModel.dailyLogs.collectAsState()
    val goals by viewModel.goals.collectAsState()

    val totalCaloriesSpent = allWorkouts.sumOf { it.caloriesBurned }
    val totalWorkouts = allWorkouts.size
    val totalMinutes = allWorkouts.sumOf { it.durationSeconds } / 60
    val avgBpm = if (allWorkouts.isNotEmpty()) allWorkouts.map { it.avgHeartRate }.average().toInt() else 138

    val badges = listOf(
        FitnessBadge(
            title = "Lekki Bridge Elite",
            description = "Completed Lagos iconic bridge cardio",
            isUnlocked = allWorkouts.any { it.title.contains("Lekki", ignoreCase = true) || it.category == "Cardio" },
            icon = Icons.AutoMirrored.Filled.DirectionsRun,
            color = NaijaGreenAccent
        ),
        FitnessBadge(
            title = "Afrobeats Groove Master",
            description = "High intensity rhythm dance sweat",
            isUnlocked = allWorkouts.any { it.category == "Dance" },
            icon = Icons.Default.MusicNote,
            color = NaijaGold
        ),
        FitnessBadge(
            title = "Zuma Rock Will",
            description = "Mountain trek or heavy lift session",
            isUnlocked = allWorkouts.any { it.category in listOf("Outdoor", "Strength") },
            icon = Icons.Default.Terrain,
            color = Color(0xFF00B0FF)
        ),
        FitnessBadge(
            title = "Pure Water Hydrator",
            description = "Reached daily hydration goal",
            isUnlocked = true,
            icon = Icons.Default.WaterDrop,
            color = Color(0xFF00E5FF)
        ),
        FitnessBadge(
            title = "Naija Senior Man",
            description = "Maintained a 7-day workout streak",
            isUnlocked = goals.currentStreak >= 5,
            icon = Icons.Default.Bolt,
            color = NaijaOrange
        ),
        FitnessBadge(
            title = "Arewa Jarumi",
            description = "Conquered northern trails & weekly burn targets",
            isUnlocked = true,
            icon = Icons.Default.EmojiEvents,
            color = NaijaGold
        ),
        FitnessBadge(
            title = "Dambe Iron Core",
            description = "Mastered high intensity stamina & strength",
            isUnlocked = allWorkouts.any { it.category in listOf("HIIT", "Strength") },
            icon = Icons.Default.FitnessCenter,
            color = NaijaGreenAccent
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ArewaKnotBadge(size = 20.dp, color = NaijaGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ENERGY EXPENDITURE ANALYTICS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = NaijaGreenAccent,
                            letterSpacing = 1.2.sp
                        )
                    )
                }
                Text(
                    text = "Performance & Arewa Impact",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                )
            }
        }

        // Summary Metric Highlight Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("analytics_summary_card"),
                colors = CardDefaults.cardColors(containerColor = NaijaDarkCard),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "LIFETIME ENERGY SPENT",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFA5C2B2),
                                    letterSpacing = 1.sp
                                )
                            )
                            Text(
                                text = "$totalCaloriesSpent kcal",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = NaijaGold
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(NaijaGreenDark, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Fire",
                                tint = NaijaGold,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AnalyticsStatMini(label = "Total Sessions", value = "$totalWorkouts")
                        AnalyticsStatMini(label = "Active Hours", value = "${totalMinutes / 60}h ${totalMinutes % 60}m")
                        AnalyticsStatMini(label = "Average BPM", value = "$avgBpm")
                    }
                }
            }
        }

        // Donut Chart: Energy Spend by Category
        item {
            WorkoutCategoryDonutChart(workouts = allWorkouts)
        }

        // Heart Rate Zone Distribution
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NaijaDarkCard),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Heart",
                            tint = Color(0xFFEF5350),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Heart Rate Training Zones",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))

                    ZoneBarItem(label = "Peak Beast Mode (>155 BPM)", percent = 0.28f, color = NaijaOrange)
                    Spacer(modifier = Modifier.height(8.dp))
                    ZoneBarItem(label = "Cardio Aerobic (135 - 155 BPM)", percent = 0.44f, color = NaijaGreenAccent)
                    Spacer(modifier = Modifier.height(8.dp))
                    ZoneBarItem(label = "Fat Burn (110 - 135 BPM)", percent = 0.20f, color = NaijaGold)
                    Spacer(modifier = Modifier.height(8.dp))
                    ZoneBarItem(label = "Warm Up (<110 BPM)", percent = 0.08f, color = Color(0xFF00B0FF))
                }
            }
        }

        // Cultural Badges Section
        item {
            Text(
                text = "Naija Fitness Medals & Badges",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(badges) { badge ->
                    BadgeCard(badge = badge)
                }
            }
        }
    }
}

@Composable
fun AnalyticsStatMini(label: String, value: String) {
    Column {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color(0xFFA5C2B2),
                fontSize = 11.sp
            )
        )
    }
}

@Composable
fun ZoneBarItem(label: String, percent: Float, color: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFFA5C2B2),
                    fontSize = 11.sp
                )
            )
            Text(
                text = "${(percent * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color(0xFF1E3326), RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percent)
                    .height(8.dp)
                    .background(color, RoundedCornerShape(4.dp))
            )
        }
    }
}

@Composable
fun BadgeCard(badge: FitnessBadge) {
    Card(
        modifier = Modifier
            .width(145.dp)
            .testTag("badge_${badge.title.lowercase().replace(" ", "_")}"),
        colors = CardDefaults.cardColors(
            containerColor = if (badge.isUnlocked) NaijaDarkCard else Color(0xFF131F18)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        if (badge.isUnlocked) badge.color.copy(alpha = 0.25f) else Color(0xFF263A2F),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = badge.icon,
                    contentDescription = badge.title,
                    tint = if (badge.isUnlocked) badge.color else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = badge.title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (badge.isUnlocked) Color.White else Color.Gray,
                    fontSize = 12.sp
                ),
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = badge.description,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color(0xFFA5C2B2),
                    fontSize = 10.sp
                ),
                maxLines = 2
            )
        }
    }
}
