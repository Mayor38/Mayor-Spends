package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyLogEntity
import com.example.ui.theme.NaijaDarkCard
import com.example.ui.theme.NaijaGold
import com.example.ui.theme.NaijaGreenAccent
import com.example.ui.theme.NaijaGreenPrimary

@Composable
fun CalorieTrendChart(
    logs: List<DailyLogEntity>,
    targetCal: Int,
    modifier: Modifier = Modifier
) {
    if (logs.isEmpty()) return

    val displayLogs = logs.takeLast(7)
    val maxCal = maxOf(targetCal, displayLogs.maxOfOrNull { it.caloriesSpent } ?: targetCal)
    val ceiling = (maxCal * 1.15f).toInt()

    var selectedIndex by remember { mutableStateOf(displayLogs.size - 1) }
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(logs) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(1f, tween(800))
    }

    val selectedLog = displayLogs.getOrNull(selectedIndex) ?: displayLogs.last()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("calorie_trend_chart_card"),
        colors = CardDefaults.cardColors(containerColor = NaijaDarkCard),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with Selected Day Tooltip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(NaijaGreenPrimary.copy(alpha = 0.25f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "Trend",
                            tint = NaijaGreenAccent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "7-Day Energy Spend",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Target: $targetCal kcal/day",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFFA5C2B2),
                                fontSize = 12.sp
                            )
                        )
                    }
                }

                // Selected pill
                Box(
                    modifier = Modifier
                        .background(
                            if (selectedLog.caloriesSpent >= targetCal) NaijaGreenPrimary else NaijaGold.copy(alpha = 0.2f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "${selectedLog.dayOfWeek}: ${selectedLog.caloriesSpent} kcal",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (selectedLog.caloriesSpent >= targetCal) Color.White else NaijaGold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Chart Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .pointerInput(displayLogs) {
                            detectTapGestures { offset ->
                                val count = displayLogs.size
                                val barSlot = size.width / count
                                val tapped = (offset.x / barSlot).toInt().coerceIn(0, count - 1)
                                selectedIndex = tapped
                            }
                        }
                ) {
                    val width = size.width
                    val height = size.height - 30.dp.toPx() // leave bottom room for text
                    val count = displayLogs.size
                    val slotWidth = width / count
                    val barWidth = slotWidth * 0.48f

                    // Target line
                    val targetY = height - (height * (targetCal.toFloat() / ceiling))
                    drawLine(
                        color = NaijaGold.copy(alpha = 0.5f),
                        start = Offset(0f, targetY),
                        end = Offset(width, targetY),
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
                    )

                    // Draw bars
                    displayLogs.forEachIndexed { i, item ->
                        val barHeight = (height * (item.caloriesSpent.toFloat() / ceiling)) * animatedProgress.value
                        val left = (i * slotWidth) + ((slotWidth - barWidth) / 2f)
                        val top = height - barHeight

                        val isSelected = i == selectedIndex
                        val isGoalMet = item.caloriesSpent >= targetCal

                        // Bar background track
                        drawRoundRect(
                            color = Color(0xFF1E2F26),
                            topLeft = Offset(left, 0f),
                            size = Size(barWidth, height),
                            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                        )

                        // Filled bar
                        val gradient = if (isGoalMet) {
                            Brush.verticalGradient(
                                colors = listOf(NaijaGreenAccent, NaijaGreenPrimary),
                                startY = top,
                                endY = height
                            )
                        } else {
                            Brush.verticalGradient(
                                colors = listOf(NaijaGold, Color(0xFFC68400)),
                                startY = top,
                                endY = height
                            )
                        }

                        drawRoundRect(
                            brush = gradient,
                            topLeft = Offset(left, top),
                            size = Size(barWidth, barHeight.coerceAtLeast(6.dp.toPx())),
                            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                        )

                        // Selected indicator indicator line / ring
                        if (isSelected) {
                            drawCircle(
                                color = Color.White,
                                radius = 4.dp.toPx(),
                                center = Offset(left + (barWidth / 2f), top.coerceAtLeast(6.dp.toPx()))
                            )
                        }
                    }
                }
            }

            // Labels row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                displayLogs.forEachIndexed { i, log ->
                    val isSelected = i == selectedIndex
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { selectedIndex = i }
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = log.dayOfWeek,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                color = if (isSelected) NaijaGreenAccent else Color(0xFFA5C2B2),
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
