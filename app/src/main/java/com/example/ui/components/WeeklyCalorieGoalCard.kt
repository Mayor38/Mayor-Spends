package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyLogEntity
import com.example.ui.theme.NaijaDarkCard
import com.example.ui.theme.NaijaGold
import com.example.ui.theme.NaijaGoldLight
import com.example.ui.theme.NaijaGreenAccent
import com.example.ui.theme.NaijaGreenDark
import com.example.ui.theme.NaijaGreenPrimary
import com.example.ui.theme.NaijaOrange

@Composable
fun WeeklyCalorieGoalCard(
    weeklyCaloriesSpent: Int,
    weeklyCalorieTarget: Int,
    dailyLogs: List<DailyLogEntity>,
    onLogDailyWorkoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = (weeklyCaloriesSpent.toFloat() / weeklyCalorieTarget.coerceAtLeast(1)).coerceIn(0f, 1f)
    val isCompleted = weeklyCaloriesSpent >= weeklyCalorieTarget
    val percentInt = ((weeklyCaloriesSpent.toFloat() / weeklyCalorieTarget.coerceAtLeast(1)) * 100).toInt()
    val remainingCalories = (weeklyCalorieTarget - weeklyCaloriesSpent).coerceAtLeast(0)

    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(weeklyCaloriesSpent, weeklyCalorieTarget) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(progress, tween(1000, easing = FastOutSlowInEasing))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("weekly_calorie_goal_card")
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = if (isCompleted) 2.dp else 1.dp,
                brush = if (isCompleted) {
                    Brush.linearGradient(listOf(NaijaGold, NaijaGreenAccent, NaijaGold))
                } else {
                    Brush.linearGradient(listOf(NaijaGreenDark, Color(0xFF1E3A2B)))
                },
                shape = RoundedCornerShape(24.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = NaijaDarkCard),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header: Title with Arewa Vibe Knot Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ArewaKnotBadge(size = 32.dp, color = if (isCompleted) NaijaGold else NaijaGreenAccent)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "WEEKLY CALORIE GOAL",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = if (isCompleted) NaijaGold else NaijaGreenAccent,
                                    letterSpacing = 1.2.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "AREWA SPIRIT",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp,
                                    color = Color(0xFFA5C2B2)
                                )
                            )
                        }
                        Text(
                            text = "Weekly Energy Expenditure",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }

                // Visual Indicator for Completion / Progress Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isCompleted) {
                                Brush.horizontalGradient(listOf(NaijaGold, NaijaOrange))
                            } else {
                                Brush.horizontalGradient(listOf(NaijaGreenDark, Color(0xFF1B3828)))
                            }
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("weekly_completion_indicator")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Completed",
                                tint = Color(0xFF14241B),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = if (isCompleted) "100% COMPLETED!" else "$percentInt% MET",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = if (isCompleted) Color(0xFF14241B) else NaijaGreenAccent,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Main Visual Indicator Arc & Numerical Burn Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular Gauge for Completion
                Box(
                    modifier = Modifier.size(105.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(105.dp)) {
                        val strokeW = 11.dp.toPx()
                        val diameter = size.minDimension - strokeW
                        val topLeft = Offset(strokeW / 2f, strokeW / 2f)
                        val arcSize = Size(diameter, diameter)

                        // Track
                        drawArc(
                            color = Color(0xFF192A20),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeW, cap = StrokeCap.Round)
                        )

                        // Completion Arc
                        val gradient = if (isCompleted) {
                            Brush.sweepGradient(listOf(NaijaGoldLight, NaijaGold, NaijaOrange, NaijaGoldLight))
                        } else {
                            Brush.sweepGradient(listOf(NaijaGreenPrimary, NaijaGreenAccent, NaijaGold, NaijaGreenPrimary))
                        }

                        drawArc(
                            brush = gradient,
                            startAngle = -90f,
                            sweepAngle = animatedProgress.value * 360f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeW, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Default.EmojiEvents else Icons.Default.LocalFireDepartment,
                            contentDescription = "Weekly Status",
                            tint = if (isCompleted) NaijaGold else NaijaGreenAccent,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "$percentInt%",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.width(18.dp))

                // Stats & Target Details
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "$weeklyCaloriesSpent kcal",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = if (isCompleted) NaijaGold else Color.White
                        )
                    )
                    Text(
                        text = "Target: $weeklyCalorieTarget kcal / week",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFA5C2B2),
                            fontSize = 12.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isCompleted) {
                            "🏆 Goal reached! Sannu da aiki, Jarumi!"
                        } else {
                            "⚡ $remainingCalories kcal to complete this week"
                        },
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isCompleted) NaijaGold else NaijaGreenAccent
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Visual Completion Banner when 100% Reached
            AnimatedVisibility(
                visible = isCompleted,
                enter = fadeIn() + expandVertically()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("weekly_celebration_banner"),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF263F2C)
                    ),
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
                                .size(38.dp)
                                .background(NaijaGold.copy(alpha = 0.25f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "Trophy",
                                tint = NaijaGold,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "ALHAMDULILLAH! GOAL CRUSHED!",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = NaijaGold,
                                    letterSpacing = 1.sp
                                )
                            )
                            Text(
                                text = "Sarkin Motsa Jiki! You spent your full weekly calorie target with pure Arewa strength!",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            }

            // Day-by-Day Activity Tracker Matrix (Last 7 Days)
            Text(
                text = "Daily Workout Contribution (This Week)",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA5C2B2),
                    fontSize = 11.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            val displayDays = dailyLogs.takeLast(7)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                displayDays.forEach { day ->
                    val hasBurned = day.caloriesSpent > 0
                    val isHighBurn = day.caloriesSpent >= 600

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isHighBurn -> NaijaGreenPrimary
                                        hasBurned -> Color(0xFF1E402B)
                                        else -> Color(0xFF17241C)
                                    }
                                )
                                .border(
                                    width = if (hasBurned) 1.dp else 0.dp,
                                    color = if (hasBurned) NaijaGreenAccent else Color.Transparent,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (hasBurned) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Active",
                                    tint = if (isHighBurn) Color.White else NaijaGreenAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Text(
                                    text = "-",
                                    color = Color(0xFF557764),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = day.dayOfWeek,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                color = if (hasBurned) Color.White else Color(0xFF7A9E8C),
                                fontWeight = if (hasBurned) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                        Text(
                            text = "${day.caloriesSpent}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                color = if (hasBurned) NaijaGold else Color(0xFF557764)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Button: Log Today's Fitness Activity
            Button(
                onClick = onLogDailyWorkoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("log_daily_workout_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NaijaGreenPrimary
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Log",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Log Today's Workout Activity",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
    }
}
