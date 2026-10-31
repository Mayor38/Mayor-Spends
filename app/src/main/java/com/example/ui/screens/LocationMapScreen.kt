package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AREWA_NIGERIA_LOCATIONS
import com.example.data.model.FitnessLocation
import com.example.data.model.PresetWorkout
import com.example.ui.components.ArewaKnotBadge
import com.example.ui.theme.NaijaDarkCard
import com.example.ui.theme.NaijaGold
import com.example.ui.theme.NaijaGoldLight
import com.example.ui.theme.NaijaGreenAccent
import com.example.ui.theme.NaijaGreenDark
import com.example.ui.theme.NaijaGreenPrimary
import com.example.ui.theme.NaijaOrange
import com.example.ui.viewmodel.FitnessViewModel
import com.example.ui.viewmodel.LiveWorkoutViewModel
import kotlinx.coroutines.delay

@Composable
fun LocationMapScreen(
    viewModel: FitnessViewModel,
    liveViewModel: LiveWorkoutViewModel,
    onNavigateToLive: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedLocation by viewModel.selectedMapLocation.collectAsState()
    var selectedFilter by remember { mutableStateOf("All Routes") }
    val filters = listOf("All Routes", "Arewa / North 🇳🇬", "Southern Routes")

    val displayedLocations = remember(selectedFilter) {
        when (selectedFilter) {
            "Arewa / North 🇳🇬" -> AREWA_NIGERIA_LOCATIONS.filter { it.region == "Arewa / North" }
            "Southern Routes" -> AREWA_NIGERIA_LOCATIONS.filter { it.region != "Arewa / North" }
            else -> AREWA_NIGERIA_LOCATIONS
        }
    }

    var isSimulatingRoute by remember { mutableStateOf(false) }
    var simulationProgress by remember { mutableFloatStateOf(0.15f) }

    LaunchedEffect(isSimulatingRoute) {
        if (isSimulatingRoute) {
            while (true) {
                delay(200L)
                simulationProgress = (simulationProgress + 0.02f)
                if (simulationProgress > 1f) simulationProgress = 0f
            }
        }
    }

    val currentLocation = selectedLocation ?: AREWA_NIGERIA_LOCATIONS.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ArewaKnotBadge(size = 22.dp, color = NaijaGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "FITNESS TRAILS & AREWA ROUTES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = NaijaGreenAccent,
                            letterSpacing = 1.2.sp
                        )
                    )
                }
                Text(
                    text = "Nigeria Fitness Map",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                )
                Text(
                    text = "Explore historical Arewa hills, savanna trails, and iconic running routes",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFFA5C2B2)
                    )
                )
            }
        }

        // Region Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filters) { filter ->
                    val isSelected = filter == selectedFilter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) NaijaGreenDark else NaijaDarkCard)
                            .border(
                                width = if (isSelected) 1.5.dp else 0.dp,
                                color = if (isSelected) NaijaGreenAccent else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = filter,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFFA5C2B2)
                            )
                        )
                    }
                }
            }
        }

        // Interactive Nigeria Fitness Map Canvas
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(310.dp)
                    .testTag("nigeria_fitness_map_card"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0C1911)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(displayedLocations) {
                                detectTapGestures { tapOffset ->
                                    val w = size.width
                                    val h = size.height

                                    // Find closest location pin to tap
                                    val tapped = displayedLocations.minByOrNull { loc ->
                                        val locX = loc.xPercent * w
                                        val locY = loc.yPercent * h
                                        val dx = tapOffset.x - locX
                                        val dy = tapOffset.y - locY
                                        dx * dx + dy * dy
                                    }
                                    if (tapped != null) {
                                        viewModel.selectMapLocation(tapped)
                                    }
                                }
                            }
                    ) {
                        val w = size.width
                        val h = size.height

                        // Stylized Nigeria Silhouette Map Outline
                        val outline = Path().apply {
                            moveTo(w * 0.15f, h * 0.35f) // West border
                            lineTo(w * 0.28f, h * 0.12f) // Sokoto North-West
                            lineTo(w * 0.62f, h * 0.10f) // Katsina / Kano North
                            lineTo(w * 0.88f, h * 0.20f) // Lake Chad North-East
                            lineTo(w * 0.90f, h * 0.45f) // Adamawa East
                            lineTo(w * 0.85f, h * 0.68f) // Mambilla / Taraba
                            lineTo(w * 0.68f, h * 0.88f) // Cross River / Calabar
                            lineTo(w * 0.48f, h * 0.92f) // Niger Delta Coast
                            lineTo(w * 0.20f, h * 0.88f) // Lagos Coast
                            lineTo(w * 0.12f, h * 0.65f) // Ogun / Oyo border
                            close()
                        }

                        // Map Land Area Fill
                        drawPath(
                            path = outline,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF142B1F), // Northern Arewa Savanna tint
                                    Color(0xFF0F2218), // Central
                                    Color(0xFF0A1C13)  // Southern coastal
                                )
                            )
                        )

                        // Outline Border
                        drawPath(
                            path = outline,
                            color = Color(0xFF264734),
                            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Rivers Niger and Benue Confluence
                        val rivers = Path().apply {
                            // River Niger from NW to confluence at Lokoja (0.45, 0.60)
                            moveTo(w * 0.22f, h * 0.32f)
                            cubicTo(w * 0.30f, h * 0.42f, w * 0.38f, h * 0.52f, w * 0.46f, h * 0.60f)
                            // River Benue from East to Lokoja
                            moveTo(w * 0.85f, h * 0.50f)
                            cubicTo(w * 0.70f, h * 0.55f, w * 0.58f, h * 0.58f, w * 0.46f, h * 0.60f)
                            // Combined into Delta
                            lineTo(w * 0.48f, h * 0.88f)
                        }
                        drawPath(
                            path = rivers,
                            color = Color(0xFF0077B6).copy(alpha = 0.35f),
                            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Connecting Trail Route Lines (Arewa Circuit)
                        val trailRoute = Path().apply {
                            val kano = Offset(w * 0.58f, h * 0.22f)
                            val kaduna = Offset(w * 0.49f, h * 0.36f)
                            val jos = Offset(w * 0.63f, h * 0.42f)
                            val zuma = Offset(w * 0.46f, h * 0.48f)
                            val bauchi = Offset(w * 0.72f, h * 0.34f)
                            val mambilla = Offset(w * 0.82f, h * 0.62f)

                            moveTo(kano.x, kano.y)
                            lineTo(kaduna.x, kaduna.y)
                            lineTo(zuma.x, zuma.y)
                            lineTo(jos.x, jos.y)
                            lineTo(bauchi.x, bauchi.y)
                            lineTo(mambilla.x, mambilla.y)
                        }
                        drawPath(
                            path = trailRoute,
                            color = NaijaGreenAccent.copy(alpha = 0.25f),
                            style = Stroke(
                                width = 1.5.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
                            )
                        )

                        // Draw Plotted Fitness Location Pins
                        displayedLocations.forEach { loc ->
                            val locX = loc.xPercent * w
                            val locY = loc.yPercent * h
                            val isSelected = loc.id == currentLocation.id
                            val isArewa = loc.region == "Arewa / North"

                            val pinColor = when {
                                isSelected -> NaijaGold
                                isArewa -> NaijaGreenAccent
                                else -> Color(0xFF00E5FF)
                            }

                            if (isSelected) {
                                // Pulsing selection radar ring
                                drawCircle(
                                    color = NaijaGold.copy(alpha = 0.35f),
                                    radius = 16.dp.toPx(),
                                    center = Offset(locX, locY)
                                )
                                drawCircle(
                                    color = NaijaGold.copy(alpha = 0.2f),
                                    radius = 24.dp.toPx(),
                                    center = Offset(locX, locY)
                                )
                            }

                            // Base pin circle
                            drawCircle(
                                color = pinColor,
                                radius = if (isSelected) 7.dp.toPx() else 5.dp.toPx(),
                                center = Offset(locX, locY)
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 2.5.dp.toPx(),
                                center = Offset(locX, locY)
                            )
                        }

                        // Route Simulation Runner Marker
                        if (isSimulatingRoute) {
                            val startX = currentLocation.xPercent * w
                            val startY = currentLocation.yPercent * h
                            val trailOffset = Offset(
                                startX + (Math.sin(simulationProgress * 6.28) * 28.dp.toPx()).toFloat(),
                                startY + (Math.cos(simulationProgress * 6.28) * 20.dp.toPx()).toFloat()
                            )
                            drawCircle(
                                color = NaijaOrange,
                                radius = 8.dp.toPx(),
                                center = trailOffset
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 4.dp.toPx(),
                                center = trailOffset
                            )
                        }
                    }

                    // Top Overlay: Legend & Route Simulation Status
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xCC0D1F15))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).background(NaijaGreenAccent, CircleShape))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Arewa Trails",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontSize = 10.sp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Box(modifier = Modifier.size(8.dp).background(NaijaGold, CircleShape))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Selected",
                                    style = MaterialTheme.typography.labelSmall.copy(color = NaijaGold, fontSize = 10.sp)
                                )
                            }
                        }

                        // Simulation toggle
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSimulatingRoute) NaijaOrange else Color(0xCC0D1F15))
                                .clickable { isSimulatingRoute = !isSimulatingRoute }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (isSimulatingRoute) "Runner Live 🏃‍♂️" else "Simulate Route GPS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    // Bottom Overlay: Tap Hint
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 10.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xAA0B1811))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Tap any pin on the map to inspect terrain & stats",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFFA5C2B2),
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }
        }

        // Selected Location Detail Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("selected_location_detail_card"),
                colors = CardDefaults.cardColors(containerColor = NaijaDarkCard),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = currentLocation.arewaVibe,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = NaijaGold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = currentLocation.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "${currentLocation.stateCity} • ${currentLocation.category}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFFA5C2B2)
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(NaijaGreenDark)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = currentLocation.difficulty,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NaijaGreenAccent
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = currentLocation.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFFD3E7DC),
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Route Stats 3-Column Pill Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        RouteStatPill(
                            icon = Icons.AutoMirrored.Filled.DirectionsRun,
                            iconColor = NaijaGreenAccent,
                            value = "${currentLocation.distanceKm} km",
                            label = "Distance"
                        )
                        RouteStatPill(
                            icon = Icons.Default.Terrain,
                            iconColor = Color(0xFF00E5FF),
                            value = "${currentLocation.elevationGainM} m",
                            label = "Elevation"
                        )
                        RouteStatPill(
                            icon = Icons.Default.LocalFireDepartment,
                            iconColor = NaijaGold,
                            value = "${currentLocation.estimatedCalories} kcal",
                            label = "Burn Spend"
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                // Load this workout into Live Workout and navigate
                                val matchingPreset = PresetWorkout(
                                    id = currentLocation.id,
                                    title = currentLocation.name,
                                    location = currentLocation.stateCity,
                                    category = when {
                                        currentLocation.category.contains("Historic") -> "Cardio"
                                        currentLocation.category.contains("Savannah") -> "Outdoor"
                                        currentLocation.category.contains("Mountain") -> "Outdoor"
                                        else -> "Cardio"
                                    },
                                    baseCalPerMin = 11.5f,
                                    description = currentLocation.description,
                                    motivationalQuote = "Ba gudu, ba ja da baya! Spend on ${currentLocation.name}!"
                                )
                                liveViewModel.selectPreset(matchingPreset)
                                onNavigateToLive()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("start_workout_from_map_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = NaijaGreenPrimary),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Start",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Train on this Trail", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RouteStatPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    value: String,
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
                    color = Color.White,
                    fontSize = 15.sp
                )
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                color = Color(0xFFA5C2B2)
            )
        )
    }
}
