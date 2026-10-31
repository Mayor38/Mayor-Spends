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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Color
import com.example.ui.components.ArewaKnotBadge
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NaijaDarkCard
import com.example.ui.theme.NaijaGold
import com.example.ui.theme.NaijaGreenAccent
import com.example.ui.theme.NaijaGreenDark
import com.example.ui.theme.NaijaGreenPrimary
import com.example.ui.theme.NaijaOrange
import com.example.ui.viewmodel.FitnessViewModel

@Composable
fun GoalsScreen(
    viewModel: FitnessViewModel,
    modifier: Modifier = Modifier
) {
    val goals by viewModel.goals.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }

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
                Text(
                    text = "MAYOR PROFILE & TARGETS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = NaijaGreenAccent,
                        letterSpacing = 1.2.sp
                    )
                )
                Text(
                    text = "Fitness Goals",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                )
            }
        }

        // Mayor Profile Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mayor_profile_card"),
                colors = CardDefaults.cardColors(containerColor = NaijaDarkCard),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(NaijaGreenDark, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Mayor Avatar",
                            tint = NaijaGold,
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Mayor",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            ArewaKnotBadge(size = 18.dp, color = NaijaGold)
                        }
                        Text(
                            text = "${goals.mayorTitle} • ${goals.arewaTitle}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = NaijaGreenAccent,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Rank: ${goals.pidginLevel} • Arewa Fitness Sardauna 🇳🇬",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFFA5C2B2),
                                fontSize = 11.sp
                            )
                        )
                    }

                    IconButton(
                        onClick = { showEditDialog = true },
                        modifier = Modifier.testTag("edit_goals_icon_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Goals",
                            tint = NaijaGold
                        )
                    }
                }
            }
        }

        // Weight Goal Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NaijaDarkCard),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFF00B0FF).copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MonitorWeight,
                                    contentDescription = "Weight",
                                    tint = Color(0xFF00B0FF),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Weight Target",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }

                        val diff = goals.currentWeightKg - goals.targetWeightKg
                        Text(
                            text = if (diff > 0) "${String.format("%.1f", diff)} kg to cut" else "Target Reached!",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NaijaGold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "${goals.currentWeightKg} kg",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Current Weight",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFA5C2B2))
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${goals.targetWeightKg} kg",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = NaijaGreenAccent
                                )
                            )
                            Text(
                                text = "Target Goal",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFA5C2B2))
                            )
                        }
                    }
                }
            }
        }

        // Goals List Cards
        item {
            Text(
                text = "Daily Fitness Targets",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }

        item {
            GoalDetailCard(
                icon = Icons.Default.LocalFireDepartment,
                iconColor = NaijaGold,
                title = "Daily Energy Spend Target",
                value = "${goals.dailyCalorieTarget} kcal",
                subtext = "Calorie burn expenditure across daily training"
            )
        }

        item {
            GoalDetailCard(
                icon = Icons.Default.LocalFireDepartment,
                iconColor = NaijaOrange,
                title = "Weekly Calorie Burn Goal",
                value = "${goals.weeklyCalorieTarget} kcal / week",
                subtext = "Weekly cumulative energy spend target with Arewa completion celebration"
            )
        }

        item {
            GoalDetailCard(
                icon = Icons.AutoMirrored.Filled.DirectionsRun,
                iconColor = NaijaGreenAccent,
                title = "Daily Steps Target",
                value = "${goals.dailyStepTarget} steps",
                subtext = "Active walking, running, and daily locomotion"
            )
        }

        item {
            GoalDetailCard(
                icon = Icons.Default.Timer,
                iconColor = Color(0xFF00E5FF),
                title = "Active Workout Minutes",
                value = "${goals.dailyActiveMinutesTarget} minutes",
                subtext = "Moderate to high intensity physical conditioning"
            )
        }

        item {
            GoalDetailCard(
                icon = Icons.Default.WaterDrop,
                iconColor = Color(0xFF00B0FF),
                title = "Hydration (Pure Water)",
                value = "${goals.dailyWaterTargetMl} ml (${goals.dailyWaterTargetMl / 500} sachets)",
                subtext = "Essential hydration for tropical climate recovery"
            )
        }

        // Edit Goals CTA
        item {
            Button(
                onClick = { showEditDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("edit_targets_button"),
                colors = ButtonDefaults.buttonColors(containerColor = NaijaGreenPrimary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Update My Fitness Targets",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
    }

    if (showEditDialog) {
        EditGoalsDialog(
            currentGoals = goals,
            onDismiss = { showEditDialog = false },
            onConfirm = { cal, weeklyCal, steps, mins, water, targetWeight, currentWeight, title, arewaT ->
                viewModel.updateGoals(cal, weeklyCal, steps, mins, water, targetWeight, currentWeight, title, arewaT)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun GoalDetailCard(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    value: String,
    subtext: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NaijaDarkCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFFA5C2B2),
                        fontSize = 12.sp
                    )
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = subtext,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF6B8A7A),
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}
