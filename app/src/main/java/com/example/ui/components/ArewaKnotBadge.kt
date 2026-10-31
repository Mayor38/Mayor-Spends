package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NaijaGold
import com.example.ui.theme.NaijaGoldLight
import com.example.ui.theme.NaijaGreenAccent

/**
 * Authentic Arewa Knot (Tambarin Arewa / Dabbare)
 * Symbol of Northern Nigerian unity, strength, and endurance.
 */
@Composable
fun ArewaKnotBadge(
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    color: Color = NaijaGold
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height
            val cx = w / 2f
            val cy = h / 2f
            val strokeW = (w * 0.09f).coerceAtLeast(1.5f)

            val grad = Brush.linearGradient(
                colors = listOf(NaijaGoldLight, color, NaijaGold),
                start = Offset(0f, 0f),
                end = Offset(w, h)
            )

            // 4 Looped Petals of the Arewa Knot
            val loopDist = w * 0.28f
            val loopRadius = w * 0.18f

            // North Loop
            drawCircle(
                brush = grad,
                radius = loopRadius,
                center = Offset(cx, cy - loopDist),
                style = Stroke(width = strokeW)
            )
            // South Loop
            drawCircle(
                brush = grad,
                radius = loopRadius,
                center = Offset(cx, cy + loopDist),
                style = Stroke(width = strokeW)
            )
            // East Loop
            drawCircle(
                brush = grad,
                radius = loopRadius,
                center = Offset(cx + loopDist, cy),
                style = Stroke(width = strokeW)
            )
            // West Loop
            drawCircle(
                brush = grad,
                radius = loopRadius,
                center = Offset(cx - loopDist, cy),
                style = Stroke(width = strokeW)
            )

            // Center Interlocking Diamond
            val diamond = Path().apply {
                moveTo(cx, cy - (loopDist * 0.65f))
                lineTo(cx + (loopDist * 0.65f), cy)
                lineTo(cx, cy + (loopDist * 0.65f))
                lineTo(cx - (loopDist * 0.65f), cy)
                close()
            }
            drawPath(
                path = diamond,
                brush = grad,
                style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Inner Core Dot
            drawCircle(
                color = NaijaGreenAccent,
                radius = w * 0.07f,
                center = Offset(cx, cy)
            )
        }
    }
}
