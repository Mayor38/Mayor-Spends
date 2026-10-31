package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NaijaDarkCard
import com.example.ui.theme.NaijaGold
import com.example.ui.theme.NaijaGreenAccent
import com.example.ui.theme.NaijaGreenDark
import com.example.ui.theme.NaijaGreenPrimary

@Composable
fun AddWorkoutDialog(
    onDismiss: () -> Unit,
    onConfirm: (
        title: String,
        category: String,
        calories: Int,
        durationMinutes: Int,
        intensity: String,
        notes: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("Lekki Bridge Evening Jog") }
    val categories = listOf("Cardio", "Strength", "Dance", "Outdoor", "HIIT")
    var selectedCategory by remember { mutableStateOf("Cardio") }

    var caloriesStr by remember { mutableStateOf("380") }
    var durationStr by remember { mutableStateOf("30") }

    val intensities = listOf("Moderate", "High", "Beast Mode")
    var selectedIntensity by remember { mutableStateOf("High") }

    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Log Energy Spend",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Activity Title") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = NaijaGreenAccent,
                        unfocusedBorderColor = Color(0xFF2C4A3A)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("workout_title_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Category", color = Color(0xFFA5C2B2), style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = cat == selectedCategory
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) NaijaGreenPrimary else Color(0xFF1E3326))
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = cat,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSelected) Color.White else Color(0xFFA5C2B2),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = caloriesStr,
                        onValueChange = { caloriesStr = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Calories (kcal)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NaijaGold,
                            unfocusedBorderColor = Color(0xFF2C4A3A)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("workout_calories_input")
                    )

                    OutlinedTextField(
                        value = durationStr,
                        onValueChange = { durationStr = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Duration (min)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NaijaGreenAccent,
                            unfocusedBorderColor = Color(0xFF2C4A3A)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("workout_duration_input")
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text("Intensity", color = Color(0xFFA5C2B2), style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    intensities.forEach { intVal ->
                        val isSelected = intVal == selectedIntensity
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) NaijaGold else Color(0xFF1E3326))
                                .clickable { selectedIntensity = intVal }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = intVal,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSelected) Color(0xFF14241B) else Color(0xFFA5C2B2),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (optional)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = NaijaGreenAccent,
                        unfocusedBorderColor = Color(0xFF2C4A3A)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cal = caloriesStr.toIntOrNull() ?: 250
                    val dur = durationStr.toIntOrNull() ?: 30
                    onConfirm(title, selectedCategory, cal, dur, selectedIntensity, notes)
                },
                colors = ButtonDefaults.buttonColors(containerColor = NaijaGreenPrimary),
                modifier = Modifier.testTag("save_workout_confirm_button")
            ) {
                Text("Save Session", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFFA5C2B2))
            }
        },
        containerColor = NaijaDarkCard,
        shape = RoundedCornerShape(20.dp)
    )
}
