package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.DailyLogEntity
import com.example.data.model.FitnessGoalEntity
import com.example.data.model.NAIJA_MOTIVATIONAL_QUOTES
import com.example.data.model.WorkoutSessionEntity
import com.example.data.repository.FitnessRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FitnessViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    val repository = FitnessRepository(database.fitnessDao())

    private val defaultGoal = FitnessGoalEntity()
    val goals: StateFlow<FitnessGoalEntity> = repository.goals
        .map { it ?: defaultGoal }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            defaultGoal
        )

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val dayNameFormat = SimpleDateFormat("EEE", Locale.US)

    private val defaultToday = DailyLogEntity(
        date = dateFormat.format(Date()),
        dayOfWeek = dayNameFormat.format(Date()),
        caloriesSpent = 0,
        stepsCount = 0,
        activeMinutes = 0,
        waterIntakeMl = 0,
        workoutsCount = 0
    )

    val todayLog: StateFlow<DailyLogEntity> = repository.getTodayLog()
        .map { it ?: defaultToday }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            defaultToday
        )

    val dailyLogs: StateFlow<List<DailyLogEntity>> = repository.dailyLogs
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val recentWorkouts: StateFlow<List<WorkoutSessionEntity>> = repository.recentWorkouts
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val allWorkouts: StateFlow<List<WorkoutSessionEntity>> = repository.allWorkouts
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val weeklyCaloriesBurned: StateFlow<Int> = dailyLogs
        .map { logs ->
            logs.takeLast(7).sumOf { it.caloriesSpent }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    private val _selectedMapLocation = MutableStateFlow<com.example.data.model.FitnessLocation?>(com.example.data.model.AREWA_NIGERIA_LOCATIONS.first())
    val selectedMapLocation: StateFlow<com.example.data.model.FitnessLocation?> = _selectedMapLocation.asStateFlow()

    fun selectMapLocation(location: com.example.data.model.FitnessLocation?) {
        _selectedMapLocation.value = location
    }

    private val _currentNaijaQuote = MutableStateFlow(com.example.data.model.NAIJA_QUOTE_COLLECTION.random())
    val currentNaijaQuote: StateFlow<com.example.data.model.NaijaQuote> = _currentNaijaQuote.asStateFlow()

    private val _randomQuote = MutableStateFlow(_currentNaijaQuote.value.quote)
    val randomQuote: StateFlow<String> = _randomQuote.asStateFlow()

    val timeGreeting: String
        get() {
            val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
            return when (hour) {
                in 5..11 -> "Good morning, Mayor! 🌅"
                in 12..16 -> "Good afternoon, Mayor! ☀️"
                in 17..21 -> "Good evening, Mayor! 🌆"
                else -> "Night owl fitness, Mayor! 🌙"
            }
        }

    init {
        refreshMotivationalQuote()
    }

    fun refreshMotivationalQuote() {
        val collection = com.example.data.model.NAIJA_QUOTE_COLLECTION
        // Pick a different random quote if more than 1 available
        val current = _currentNaijaQuote.value
        val pool = collection.filter { it.quote != current.quote }.ifEmpty { collection }
        val next = pool.random()
        _currentNaijaQuote.value = next
        _randomQuote.value = next.quote
    }


    fun addWater(amountMl: Int = 250) {
        viewModelScope.launch {
            repository.addWaterIntake(amountMl)
        }
    }

    fun addSteps(addedSteps: Int = 500) {
        viewModelScope.launch {
            repository.addManualSteps(addedSteps)
        }
    }

    fun logManualWorkout(
        title: String,
        category: String,
        calories: Int,
        durationMinutes: Int,
        intensity: String,
        notes: String
    ) {
        viewModelScope.launch {
            val workout = WorkoutSessionEntity(
                title = title,
                category = category,
                caloriesBurned = calories,
                durationSeconds = durationMinutes * 60,
                avgHeartRate = when (intensity) {
                    "Beast Mode" -> 165
                    "High" -> 150
                    "Moderate" -> 135
                    else -> 115
                },
                distanceKm = if (category in listOf("Cardio", "Outdoor")) durationMinutes * 0.16f else 0f,
                intensity = intensity,
                notes = notes
            )
            repository.recordCompletedWorkout(workout)
        }
    }

    fun updateGoals(
        calorieTarget: Int,
        weeklyCalorieTarget: Int = 4500,
        stepTarget: Int,
        activeMinutesTarget: Int,
        waterTargetMl: Int,
        targetWeightKg: Float,
        currentWeightKg: Float,
        mayorTitle: String,
        arewaTitle: String = "Sarkin Motsa Jiki"
    ) {
        viewModelScope.launch {
            val current = goals.value
            val updated = current.copy(
                dailyCalorieTarget = calorieTarget,
                weeklyCalorieTarget = weeklyCalorieTarget,
                dailyStepTarget = stepTarget,
                dailyActiveMinutesTarget = activeMinutesTarget,
                dailyWaterTargetMl = waterTargetMl,
                targetWeightKg = targetWeightKg,
                currentWeightKg = currentWeightKg,
                mayorTitle = mayorTitle,
                arewaTitle = arewaTitle
            )
            repository.updateGoals(updated)
        }
    }

    fun deleteWorkout(workout: WorkoutSessionEntity) {
        viewModelScope.launch {
            repository.deleteWorkout(workout)
        }
    }
}
