package com.example.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.model.FitnessGoalEntity
import com.example.ui.theme.NaijaDarkCard
import com.example.ui.theme.NaijaGold
import com.example.ui.theme.NaijaGreenAccent
import com.example.ui.theme.NaijaGreenPrimary

@Composable
fun EditGoalsDialog(
    currentGoals: FitnessGoalEntity,
    onDismiss: () -> Unit,
    onConfirm: (
        calorieTarget: Int,
        weeklyCalorieTarget: Int,
        stepTarget: Int,
        activeMinutesTarget: Int,
        waterTargetMl: Int,
        targetWeightKg: Float,
        currentWeightKg: Float,
        mayorTitle: String,
        arewaTitle: String
    ) -> Unit
) {
    var calTargetStr by remember { mutableStateOf(currentGoals.dailyCalorieTarget.toString()) }
    var weeklyCalTargetStr by remember { mutableStateOf(currentGoals.weeklyCalorieTarget.toString()) }
    var stepTargetStr by remember { mutableStateOf(currentGoals.dailyStepTarget.toString()) }
    var minTargetStr by remember { mutableStateOf(currentGoals.dailyActiveMinutesTarget.toString()) }
    var waterTargetStr by remember { mutableStateOf(currentGoals.dailyWaterTargetMl.toString()) }
    var curWeightStr by remember { mutableStateOf(currentGoals.currentWeightKg.toString()) }
    var targetWeightStr by remember { mutableStateOf(currentGoals.targetWeightKg.toString()) }
    var title by remember { mutableStateOf(currentGoals.mayorTitle) }
    var arewaTitleStr by remember { mutableStateOf(currentGoals.arewaTitle) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit Fitness Targets",
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
                    label = { Text("Mayor Title (e.g. Lekki Fitness Boss)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = NaijaGold,
                        unfocusedBorderColor = Color(0xFF2C4A3A)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("mayor_title_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = arewaTitleStr,
                    onValueChange = { arewaTitleStr = it },
                    label = { Text("Arewa Title (e.g. Sarkin Motsa Jiki)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = NaijaGreenAccent,
                        unfocusedBorderColor = Color(0xFF2C4A3A)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("arewa_title_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = calTargetStr,
                    onValueChange = { calTargetStr = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Daily Energy Spend Target (kcal)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = NaijaGold,
                        unfocusedBorderColor = Color(0xFF2C4A3A)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("calorie_target_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = weeklyCalTargetStr,
                    onValueChange = { weeklyCalTargetStr = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Weekly Calorie Burn Goal (kcal)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = NaijaGold,
                        unfocusedBorderColor = Color(0xFF2C4A3A)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("weekly_calorie_target_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = stepTargetStr,
                    onValueChange = { stepTargetStr = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Daily Steps Target") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = NaijaGreenAccent,
                        unfocusedBorderColor = Color(0xFF2C4A3A)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = minTargetStr,
                    onValueChange = { minTargetStr = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Active Minutes Target") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF00E5FF),
                        unfocusedBorderColor = Color(0xFF2C4A3A)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = waterTargetStr,
                    onValueChange = { waterTargetStr = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Hydration Target (ml)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF00B0FF),
                        unfocusedBorderColor = Color(0xFF2C4A3A)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = curWeightStr,
                        onValueChange = { curWeightStr = it },
                        label = { Text("Current kg") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NaijaGreenAccent,
                            unfocusedBorderColor = Color(0xFF2C4A3A)
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = targetWeightStr,
                        onValueChange = { targetWeightStr = it },
                        label = { Text("Target kg") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NaijaGold,
                            unfocusedBorderColor = Color(0xFF2C4A3A)
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cal = calTargetStr.toIntOrNull() ?: currentGoals.dailyCalorieTarget
                    val weeklyCal = weeklyCalTargetStr.toIntOrNull() ?: currentGoals.weeklyCalorieTarget
                    val steps = stepTargetStr.toIntOrNull() ?: currentGoals.dailyStepTarget
                    val mins = minTargetStr.toIntOrNull() ?: currentGoals.dailyActiveMinutesTarget
                    val water = waterTargetStr.toIntOrNull() ?: currentGoals.dailyWaterTargetMl
                    val curW = curWeightStr.toFloatOrNull() ?: currentGoals.currentWeightKg
                    val tgtW = targetWeightStr.toFloatOrNull() ?: currentGoals.targetWeightKg
                    val t = if (title.isNotBlank()) title else currentGoals.mayorTitle
                    val arewaT = if (arewaTitleStr.isNotBlank()) arewaTitleStr else currentGoals.arewaTitle
                    onConfirm(cal, weeklyCal, steps, mins, water, tgtW, curW, t, arewaT)
                },
                colors = ButtonDefaults.buttonColors(containerColor = NaijaGreenPrimary),
                modifier = Modifier.testTag("save_goals_button")
            ) {
                Text("Save Targets", fontWeight = FontWeight.Bold)
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
