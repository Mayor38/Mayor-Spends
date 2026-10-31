package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NaijaDarkCard
import com.example.ui.theme.NaijaGold
import com.example.ui.theme.NaijaGreenAccent

@Composable
fun LiveWorkoutWaveformChart(
    bpmHistory: List<Int>,
    currentBpm: Int,
    currentCalPerMin: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("live_workout_waveform_card"),
        colors = CardDefaults.cardColors(containerColor = NaijaDarkCard),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(NaijaGreenAccent, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "REAL-TIME INTENSITY STREAM",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = NaijaGreenAccent,
                        letterSpacing = 1.2.sp
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${String.format("%.1f", currentCalPerMin)} kcal/min",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NaijaGold
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Canvas Waveform
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            ) {
                val width = size.width
                val height = size.height
                val points = if (bpmHistory.size < 2) listOf(110, 115, 120, 118, 125, 130) else bpmHistory

                val minVal = 80f
                val maxVal = 185f
                val range = (maxVal - minVal).coerceAtLeast(1f)

                val stepX = width / (points.size - 1).coerceAtLeast(1)

                val path = Path()
                val fillPath = Path()

                points.forEachIndexed { i, bpm ->
                    val normY = ((bpm - minVal) / range).coerceIn(0.1f, 0.95f)
                    val x = i * stepX
                    val y = height - (normY * height)

                    if (i == 0) {
                        path.moveTo(x, y)
                        fillPath.moveTo(x, height)
                        fillPath.lineTo(x, y)
                    } else {
                        // Smooth quadratic bezier to next point
                        val prevBpm = points[i - 1]
                        val prevNormY = ((prevBpm - minVal) / range).coerceIn(0.1f, 0.95f)
                        val prevX = (i - 1) * stepX
                        val prevY = height - (prevNormY * height)

                        val cx = (prevX + x) / 2f
                        path.cubicTo(cx, prevY, cx, y, x, y)
                        fillPath.cubicTo(cx, prevY, cx, y, x, y)
                    }
                }

                fillPath.lineTo(width, height)
                fillPath.close()

                // Draw gradient under-glow
                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            NaijaGreenAccent.copy(alpha = 0.35f),
                            Color.Transparent
                        )
                    )
                )

                // Draw neon wave stroke
                drawPath(
                    path = path,
                    brush = Brush.horizontalGradient(
                        colors = listOf(NaijaGold, NaijaGreenAccent)
                    ),
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )

                // Current pulsing point at end
                val lastX = width
                val lastBpm = points.last()
                val lastY = height - (((lastBpm - minVal) / range).coerceIn(0.1f, 0.95f) * height)
                drawCircle(
                    color = Color.White,
                    radius = 4.dp.toPx(),
                    center = Offset(lastX, lastY)
                )
                drawCircle(
                    color = NaijaGreenAccent.copy(alpha = 0.5f),
                    radius = 8.dp.toPx(),
                    center = Offset(lastX, lastY)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Live Heart Rate: $currentBpm BPM",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                val zoneName = when {
                    currentBpm < 110 -> "Warm Up"
                    currentBpm < 135 -> "Fat Burn Zone"
                    currentBpm < 155 -> "Cardio Zone"
                    else -> "Beast Mode 🔥"
                }
                Text(
                    text = zoneName,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (currentBpm >= 155) NaijaGold else NaijaGreenAccent
                    )
                )
            }
        }
    }
}
