package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NaijaGold
import com.example.ui.theme.NaijaGreenAccent
import com.example.ui.theme.NaijaOrange

@Composable
fun SpendProgressRings(
    caloriesSpent: Int,
    calorieTarget: Int,
    steps: Int,
    stepTarget: Int,
    activeMinutes: Int,
    activeMinutesTarget: Int,
    modifier: Modifier = Modifier,
    size: Dp = 230.dp
) {
    val calorieProgress = (caloriesSpent.toFloat() / calorieTarget.coerceAtLeast(1)).coerceIn(0f, 1f)
    val stepProgress = (steps.toFloat() / stepTarget.coerceAtLeast(1)).coerceIn(0f, 1f)
    val minutesProgress = (activeMinutes.toFloat() / activeMinutesTarget.coerceAtLeast(1)).coerceIn(0f, 1f)

    val animatedCalorie = remember { Animatable(0f) }
    val animatedStep = remember { Animatable(0f) }
    val animatedMinutes = remember { Animatable(0f) }

    LaunchedEffect(calorieProgress, stepProgress, minutesProgress) {
        animatedCalorie.animateTo(calorieProgress, tween(900, easing = FastOutSlowInEasing))
        animatedStep.animateTo(stepProgress, tween(900, 150, easing = FastOutSlowInEasing))
        animatedMinutes.animateTo(minutesProgress, tween(900, 300, easing = FastOutSlowInEasing))
    }

    Box(
        modifier = modifier
            .size(size)
            .testTag("spend_progress_rings"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val strokeWidth = 14.dp.toPx()
            val spacing = 7.dp.toPx()
            val center = Offset(this.size.width / 2f, this.size.height / 2f)

            // Outer Ring: Calories Spend (Gold -> Orange)
            val outerRadius = (this.size.minDimension / 2f) - (strokeWidth / 2f)
            val outerTopLeft = Offset(center.x - outerRadius, center.y - outerRadius)
            val outerSize = Size(outerRadius * 2, outerRadius * 2)

            // Track
            drawArc(
                color = Color(0xFF26332B),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = outerTopLeft,
                size = outerSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            // Progress
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(NaijaGold, NaijaOrange, NaijaGold)
                ),
                startAngle = -90f,
                sweepAngle = animatedCalorie.value * 360f,
                useCenter = false,
                topLeft = outerTopLeft,
                size = outerSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Middle Ring: Steps (Emerald -> Neon Accent)
            val midRadius = outerRadius - strokeWidth - spacing
            val midTopLeft = Offset(center.x - midRadius, center.y - midRadius)
            val midSize = Size(midRadius * 2, midRadius * 2)

            // Track
            drawArc(
                color = Color(0xFF1E2E25),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = midTopLeft,
                size = midSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            // Progress
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(Color(0xFF008751), NaijaGreenAccent, Color(0xFF008751))
                ),
                startAngle = -90f,
                sweepAngle = animatedStep.value * 360f,
                useCenter = false,
                topLeft = midTopLeft,
                size = midSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Inner Ring: Active Minutes (Cyan / Mint)
            val innerRadius = midRadius - strokeWidth - spacing
            val innerTopLeft = Offset(center.x - innerRadius, center.y - innerRadius)
            val innerSize = Size(innerRadius * 2, innerRadius * 2)

            // Track
            drawArc(
                color = Color(0xFF16241C),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = innerTopLeft,
                size = innerSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            // Progress
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(Color(0xFF00B0FF), Color(0xFF00E5FF), Color(0xFF00B0FF))
                ),
                startAngle = -90f,
                sweepAngle = animatedMinutes.value * 360f,
                useCenter = false,
                topLeft = innerTopLeft,
                size = innerSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        // Center Content: Calories Spend Counter
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = "Energy Fire",
                tint = NaijaGold,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$caloriesSpent",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 28.sp,
                    color = Color.White
                )
            )
            Text(
                text = "SPENT / $calorieTarget",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = Color(0xFFA5C2B2),
                    letterSpacing = 1.sp
                )
            )
            val percent = (calorieProgress * 100).toInt()
            Text(
                text = "$percent% Met",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    color = NaijaGreenAccent
                )
            )
        }
    }
}
