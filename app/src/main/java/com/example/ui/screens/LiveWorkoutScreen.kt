package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.model.NAIJA_PRESET_WORKOUTS
import com.example.data.model.PresetWorkout
import com.example.ui.components.LiveWorkoutWaveformChart
import com.example.ui.theme.NaijaDarkCard
import com.example.ui.theme.NaijaGold
import com.example.ui.theme.NaijaGreenAccent
import com.example.ui.theme.NaijaGreenDark
import com.example.ui.theme.NaijaGreenPrimary
import com.example.ui.theme.NaijaOrange
import com.example.ui.theme.NaijaRed
import com.example.ui.viewmodel.LiveWorkoutViewModel

@Composable
fun LiveWorkoutScreen(
    liveViewModel: LiveWorkoutViewModel,
    modifier: Modifier = Modifier
) {
    val state by liveViewModel.uiState.collectAsState()
    var showFinishConfirmDialog by remember { mutableStateOf(false) }

    val formattedTime = remember(state.elapsedSeconds) {
        val mins = state.elapsedSeconds / 60
        val secs = state.elapsedSeconds % 60
        String.format("%02d:%02d", mins, secs)
    }

    val calPerMin = remember(state.caloriesBurned, state.elapsedSeconds) {
        if (state.elapsedSeconds > 0) {
            (state.caloriesBurned / state.elapsedSeconds) * 60f
        } else {
            state.selectedPreset.baseCalPerMin
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Title
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                if (state.isActive && !state.isPaused) NaijaGreenAccent else Color.Gray,
                                CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (state.isActive && !state.isPaused) "LIVE WORKOUT IN PROGRESS" else "LIVE WORKOUT ARENA",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = if (state.isActive && !state.isPaused) NaijaGreenAccent else Color(0xFFA5C2B2),
                            letterSpacing = 1.2.sp
                        )
                    )
                }
                Text(
                    text = state.selectedPreset.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                )
                Text(
                    text = "${state.selectedPreset.location} • ${state.selectedPreset.category}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFFA5C2B2)
                    )
                )
            }
        }

        // Milestone Notification Banner
        item {
            AnimatedVisibility(
                visible = state.milestoneMessage != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                state.milestoneMessage?.let { msg ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { liveViewModel.dismissMilestone() },
                        colors = CardDefaults.cardColors(containerColor = NaijaGreenDark),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Milestone",
                                tint = NaijaGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = msg,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Dismiss",
                                tint = Color(0xFFA5C2B2),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // Preset Workout Selector (Visible when not actively locked into workout)
        if (!state.isActive) {
            item {
                Column {
                    Text(
                        text = "Choose Naija Workout Challenge",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(NAIJA_PRESET_WORKOUTS) { preset ->
                            val isSelected = preset.id == state.selectedPreset.id
                            PresetChip(
                                preset = preset,
                                isSelected = isSelected,
                                onClick = { liveViewModel.selectPreset(preset) }
                            )
                        }
                    }
                }
            }
        }

        // Giant Real-Time Telemetry Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("live_telemetry_hero_card"),
                colors = CardDefaults.cardColors(containerColor = NaijaDarkCard),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Timer Display
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 62.sp,
                            color = if (state.isPaused) NaijaGold else Color.White,
                            letterSpacing = 2.sp
                        )
                    )
                    Text(
                        text = if (state.isPaused) "PAUSED" else if (state.isActive) "ACTIVE DURATION" else "READY TO SPEND",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (state.isPaused) NaijaGold else NaijaGreenAccent,
                            letterSpacing = 1.5.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Calories Spent Real-Time Ticker
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFF1E3729), Color(0xFF162A1F))))
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Calories",
                            tint = NaijaGold,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "${state.caloriesBurned.toInt()}",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = NaijaGold
                                )
                            )
                            Text(
                                text = "CALORIES SPENT",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFA5C2B2),
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Live Metrics 3-Column Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        LiveMetricPill(
                            icon = Icons.Default.Favorite,
                            iconColor = NaijaRed,
                            value = "${state.currentHeartRate}",
                            unit = "BPM",
                            label = "Heart Rate"
                        )
                        LiveMetricPill(
                            icon = Icons.AutoMirrored.Filled.DirectionsRun,
                            iconColor = NaijaGreenAccent,
                            value = String.format("%.2f", state.distanceKm),
                            unit = "KM",
                            label = "Distance"
                        )
                        LiveMetricPill(
                            icon = Icons.Default.Speed,
                            iconColor = Color(0xFF00E5FF),
                            value = String.format("%.1f", state.currentSpeedKmh),
                            unit = "KM/H",
                            label = "Pace"
                        )
                    }
                }
            }
        }

        // Real-Time Waveform Chart
        item {
            LiveWorkoutWaveformChart(
                bpmHistory = state.heartRateHistory,
                currentBpm = state.currentHeartRate,
                currentCalPerMin = calPerMin
            )
        }

        // Naija Cultural Motivation
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF112318)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🇳🇬",
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "\"${state.selectedPreset.motivationalQuote}\"",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFD6EAE0)
                        )
                    )
                }
            }
        }

        // Live Controls (Start / Pause / Resume / Finish)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (!state.isActive) {
                    Button(
                        onClick = { liveViewModel.startWorkout() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("start_live_workout_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = NaijaGreenPrimary),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Start",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Start Workout",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                } else {
                    // Active Workout Controls
                    if (!state.isPaused) {
                        OutlinedButton(
                            onClick = { liveViewModel.pauseWorkout() },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .testTag("pause_workout_button"),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = NaijaGold
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = Brush.linearGradient(listOf(NaijaGold, NaijaOrange))
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Pause, contentDescription = "Pause")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pause", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = { liveViewModel.resumeWorkout() },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .testTag("resume_workout_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = NaijaGreenPrimary),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Resume")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Resume", fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = { showFinishConfirmDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .testTag("finish_workout_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = NaijaOrange),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Stop, contentDescription = "Finish")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Finish", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }

    // Finish Confirmation & Celebration Dialog
    if (showFinishConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showFinishConfirmDialog = false },
            title = {
                Text(
                    text = "Finish & Spend Workout?",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = {
                Column {
                    Text(
                        text = "E choke! You have spent ${state.caloriesBurned.toInt()} kcal across $formattedTime of intense Naija energy!",
                        color = Color(0xFFA5C2B2),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Save this session to your official Mayor Spends records?",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        liveViewModel.finishWorkout()
                        showFinishConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NaijaGreenPrimary)
                ) {
                    Text("Yes, Save & Spend", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showFinishConfirmDialog = false }) {
                    Text("Keep Going", color = Color(0xFFA5C2B2))
                }
            },
            containerColor = NaijaDarkCard,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun PresetChip(
    preset: PresetWorkout,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val icon = when (preset.category) {
        "Dance" -> Icons.Default.MusicNote
        "Cardio" -> Icons.AutoMirrored.Filled.DirectionsRun
        "Outdoor" -> Icons.Default.Terrain
        "Strength" -> Icons.Default.FitnessCenter
        else -> Icons.Default.LocalFireDepartment
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) NaijaGreenDark else NaijaDarkCard)
            .border(
                width = if (isSelected) 1.5.dp else 0.dp,
                color = if (isSelected) NaijaGreenAccent else Color.Transparent,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = preset.title,
                tint = if (isSelected) NaijaGold else Color(0xFFA5C2B2),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = preset.title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else Color(0xFFA5C2B2)
                    )
                )
                Text(
                    text = "${preset.baseCalPerMin} cal/min",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (isSelected) NaijaGreenAccent else Color(0xFF6B8A7A),
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}

@Composable
fun LiveMetricPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    value: String,
    unit: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = unit,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = Color(0xFFA5C2B2)
                )
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                color = Color(0xFFA5C2B2)
            )
        )
    }
}
