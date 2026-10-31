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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyLogEntity
import com.example.data.model.FitnessGoalEntity
import com.example.data.model.WorkoutSessionEntity
import com.example.ui.components.ArewaKnotBadge
import com.example.ui.components.CalorieTrendChart
import com.example.ui.components.NaijaMotivationalCard
import com.example.ui.components.QuickStatCard
import com.example.ui.components.SpendProgressRings
import com.example.ui.components.WeeklyCalorieGoalCard
import com.example.ui.theme.NaijaDarkCard
import com.example.ui.theme.NaijaGold
import com.example.ui.theme.NaijaGreenAccent
import com.example.ui.theme.NaijaGreenDark
import com.example.ui.theme.NaijaGreenPrimary
import com.example.ui.theme.NaijaOrange
import com.example.ui.viewmodel.FitnessViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: FitnessViewModel,
    onNavigateToLive: () -> Unit,
    modifier: Modifier = Modifier
) {
    val goals by viewModel.goals.collectAsState()
    val todayLog by viewModel.todayLog.collectAsState()
    val dailyLogs by viewModel.dailyLogs.collectAsState()
    val recentWorkouts by viewModel.recentWorkouts.collectAsState()
    val currentNaijaQuote by viewModel.currentNaijaQuote.collectAsState()
    val weeklyCaloriesBurned by viewModel.weeklyCaloriesBurned.collectAsState()

    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.refreshMotivationalQuote()
    }

    var showAddWorkoutDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Header: Title & Nigerian Flag Accent with Arewa Knot
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ArewaKnotBadge(size = 24.dp, color = NaijaGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mayor Spends",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    letterSpacing = (-0.5).sp
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            // Nigeria Flag Pill
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .size(width = 24.dp, height = 14.dp)
                            ) {
                                Box(modifier = Modifier.weight(1f).fillMaxSize().background(NaijaGreenPrimary))
                                Box(modifier = Modifier.weight(1f).fillMaxSize().background(Color.White))
                                Box(modifier = Modifier.weight(1f).fillMaxSize().background(NaijaGreenPrimary))
                            }
                        }
                        Text(
                            text = "${goals.mayorTitle} • ${goals.arewaTitle} 🇳🇬",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = NaijaGreenAccent,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    // Streak Badge
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(listOf(NaijaGreenDark, Color(0xFF1B3D2A))),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = "Streak",
                                tint = NaijaGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${goals.currentStreak}d Streak",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = NaijaGold
                                )
                            )
                        }
                    }
                }
            }

            // Motivational Banner: Random Nigerian-inspired quote on app open & interactive shuffle
            item {
                NaijaMotivationalCard(
                    quote = currentNaijaQuote,
                    greeting = viewModel.timeGreeting,
                    onRefresh = { viewModel.refreshMotivationalQuote() }
                )
            }

            // Hero: Concentric Concentrated Spend Rings
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("hero_spend_card"),
                    colors = CardDefaults.cardColors(containerColor = NaijaDarkCard),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "TODAY'S ENERGY SPEND",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = NaijaGreenAccent,
                                letterSpacing = 1.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        SpendProgressRings(
                            caloriesSpent = todayLog.caloriesSpent,
                            calorieTarget = goals.dailyCalorieTarget,
                            steps = todayLog.stepsCount,
                            stepTarget = goals.dailyStepTarget,
                            activeMinutes = todayLog.activeMinutes,
                            activeMinutesTarget = goals.dailyActiveMinutesTarget
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Ring Legend
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            LegendItem(color = NaijaGold, label = "Calories")
                            LegendItem(color = NaijaGreenAccent, label = "Steps")
                            LegendItem(color = Color(0xFF00E5FF), label = "Active Mins")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // CTA to start live session
                        Button(
                            onClick = onNavigateToLive,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("start_live_workout_cta_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NaijaGreenPrimary
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.DirectionsRun,
                                contentDescription = "Run",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Start Live Workout Session",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Quick Stats Grid: Steps & Water
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Steps Card
                    QuickStatCard(
                        title = "Daily Steps",
                        value = "${todayLog.stepsCount}",
                        target = "Goal: ${goals.dailyStepTarget}",
                        progress = todayLog.stepsCount.toFloat() / goals.dailyStepTarget,
                        icon = Icons.AutoMirrored.Filled.DirectionsRun,
                        iconTint = NaijaGreenAccent,
                        accentColor = NaijaGreenAccent,
                        modifier = Modifier.weight(1f),
                        actionButton = {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(NaijaGreenPrimary)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "+500",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.testTag("add_steps_button")
                                )
                            }
                        }
                    )

                    // Water Card (Pure Water homage)
                    val sachets = todayLog.waterIntakeMl / 500
                    QuickStatCard(
                        title = "Pure Water",
                        value = "${todayLog.waterIntakeMl} ml",
                        target = "($sachets sachets)",
                        progress = todayLog.waterIntakeMl.toFloat() / goals.dailyWaterTargetMl,
                        icon = Icons.Default.WaterDrop,
                        iconTint = Color(0xFF00B0FF),
                        accentColor = Color(0xFF00B0FF),
                        modifier = Modifier.weight(1f),
                        actionButton = {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFF0091EA))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "+1 Sachet",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.testTag("add_water_sachet_button")
                                )
                            }
                        }
                    )
                }
            }

            // Weekly Calorie Burn Goal Tracking (with Visual Indicator for Completion & Arewa Vibe)
            item {
                WeeklyCalorieGoalCard(
                    weeklyCaloriesSpent = weeklyCaloriesBurned,
                    weeklyCalorieTarget = goals.weeklyCalorieTarget,
                    dailyLogs = dailyLogs,
                    onLogDailyWorkoutClick = { showAddWorkoutDialog = true }
                )
            }

            // 7-Day Interactive Calorie Trend Chart
            item {
                CalorieTrendChart(
                    logs = dailyLogs,
                    targetCal = goals.dailyCalorieTarget
                )
            }

            // Recent Workouts Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Energy Spends",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "${recentWorkouts.size} logged",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color(0xFFA5C2B2)
                        )
                    )
                }
            }

            if (recentWorkouts.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = NaijaDarkCard),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = "No workouts",
                                tint = Color(0xFF7A9E8C),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No workouts logged today!",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Text(
                                text = "Oya hit 'Start Live Workout' or log manually.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFFA5C2B2)
                                )
                            )
                        }
                    }
                }
            } else {
                items(recentWorkouts) { workout ->
                    WorkoutItemCard(
                        workout = workout,
                        onDelete = { viewModel.deleteWorkout(workout) }
                    )
                }
            }
        }

        // Floating Action Button for Manual Log
        FloatingActionButton(
            onClick = { showAddWorkoutDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 80.dp, end = 20.dp)
                .testTag("add_manual_workout_fab"),
            containerColor = NaijaGold,
            contentColor = Color(0xFF1E2822)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Log Workout")
        }
    }

    if (showAddWorkoutDialog) {
        AddWorkoutDialog(
            onDismiss = { showAddWorkoutDialog = false },
            onConfirm = { title, cat, cal, mins, intensity, notes ->
                viewModel.logManualWorkout(title, cat, cal, mins, intensity, notes)
                showAddWorkoutDialog = false
            }
        )
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
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
fun WorkoutItemCard(
    workout: WorkoutSessionEntity,
    onDelete: () -> Unit
) {
    val timeFormat = SimpleDateFormat("h:mm a", Locale.US)
    val formattedTime = timeFormat.format(Date(workout.timestamp))

    val icon = when (workout.category) {
        "Dance" -> Icons.Default.MusicNote
        "Cardio", "Outdoor" -> Icons.AutoMirrored.Filled.DirectionsRun
        "HIIT" -> Icons.Default.LocalFireDepartment
        else -> Icons.Default.FitnessCenter
    }

    val iconColor = when (workout.category) {
        "Dance" -> NaijaGold
        "Cardio" -> NaijaGreenAccent
        "Outdoor" -> NaijaGreenPrimary
        "HIIT" -> NaijaOrange
        else -> Color(0xFF00B0FF)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("workout_item_${workout.id}"),
        colors = CardDefaults.cardColors(containerColor = NaijaDarkCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(iconColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = workout.category,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = workout.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${workout.durationSeconds / 60} mins",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFFA5C2B2)
                        )
                    )
                    Text(
                        text = " • ",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFA5C2B2))
                    )
                    Text(
                        text = "$formattedTime",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFFA5C2B2)
                        )
                    )
                    if (workout.intensity.isNotEmpty()) {
                        Text(
                            text = " • ${workout.intensity}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NaijaGold,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${workout.caloriesBurned}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = NaijaGreenAccent
                    )
                )
                Text(
                    text = "kcal spent",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        color = Color(0xFFA5C2B2)
                    )
                )
            }

            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFF6B8A7A),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
