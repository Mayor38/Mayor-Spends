package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WorkoutSessionEntity
import com.example.ui.theme.NaijaDarkCard
import com.example.ui.theme.NaijaGold
import com.example.ui.theme.NaijaGreenAccent
import com.example.ui.theme.NaijaGreenPrimary
import com.example.ui.theme.NaijaOrange

private data class CategorySlice(
    val category: String,
    val calories: Int,
    val percentage: Float,
    val color: Color
)

@Composable
fun WorkoutCategoryDonutChart(
    workouts: List<WorkoutSessionEntity>,
    modifier: Modifier = Modifier
) {
    val categoryColors = mapOf(
        "Cardio" to NaijaGreenAccent,
        "Dance" to NaijaGold,
        "Strength" to Color(0xFF00B0FF),
        "Outdoor" to NaijaGreenPrimary,
        "HIIT" to NaijaOrange
    )

    // Group workouts
    val grouped = workouts.groupBy { it.category }
    val totalCalories = workouts.sumOf { it.caloriesBurned }.coerceAtLeast(1)

    val slices = categoryColors.keys.mapNotNull { cat ->
        val list = grouped[cat] ?: emptyList()
        val catCal = list.sumOf { it.caloriesBurned }
        if (catCal > 0) {
            CategorySlice(
                category = cat,
                calories = catCal,
                percentage = (catCal.toFloat() / totalCalories),
                color = categoryColors[cat] ?: Color.Gray
            )
        } else null
    }.ifEmpty {
        listOf(
            CategorySlice("Cardio", 450, 0.45f, NaijaGreenAccent),
            CategorySlice("Dance", 300, 0.30f, NaijaGold),
            CategorySlice("Strength", 250, 0.25f, Color(0xFF00B0FF))
        )
    }

    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(workouts) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(1f, tween(700))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("workout_category_donut_card"),
        colors = CardDefaults.cardColors(containerColor = NaijaDarkCard),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Energy Spend by Workout",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Text(
                text = "Distribution across fitness disciplines",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFFA5C2B2),
                    fontSize = 12.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Donut Canvas
                Box(
                    modifier = Modifier.size(130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(130.dp)) {
                        val strokeWidth = 18.dp.toPx()
                        val diameter = size.minDimension - strokeWidth
                        val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
                        val arcSize = Size(diameter, diameter)

                        var startAngle = -90f
                        for (slice in slices) {
                            val sweep = slice.percentage * 360f * animatedProgress.value
                            drawArc(
                                color = slice.color,
                                startAngle = startAngle,
                                sweepAngle = (sweep - 2f).coerceAtLeast(1f),
                                useCenter = false,
                                topLeft = topLeft,
                                size = arcSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                            startAngle += sweep
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${workouts.size}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Sessions",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFFA5C2B2),
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                // Legend
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    slices.forEach { slice ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(slice.color, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = slice.category,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${(slice.percentage * 100).toInt()}%",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFFA5C2B2),
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
