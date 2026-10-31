package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.NAIJA_PRESET_WORKOUTS
import com.example.data.model.PresetWorkout
import com.example.data.model.WorkoutSessionEntity
import com.example.data.repository.FitnessRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

data class LiveWorkoutState(
    val isActive: Boolean = false,
    val isPaused: Boolean = false,
    val selectedPreset: PresetWorkout = NAIJA_PRESET_WORKOUTS.first(),
    val elapsedSeconds: Int = 0,
    val caloriesBurned: Float = 0f,
    val currentHeartRate: Int = 118,
    val heartRateHistory: List<Int> = listOf(115, 118, 120, 119, 122),
    val distanceKm: Float = 0f,
    val currentSpeedKmh: Float = 0f,
    val intensity: String = "High",
    val milestoneMessage: String? = null,
    val isFinished: Boolean = false,
    val lastCompletedWorkout: WorkoutSessionEntity? = null
)

class LiveWorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = FitnessRepository(database.fitnessDao())

    private val _uiState = MutableStateFlow(LiveWorkoutState())
    val uiState: StateFlow<LiveWorkoutState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun selectPreset(preset: PresetWorkout) {
        if (!_uiState.value.isActive) {
            _uiState.value = _uiState.value.copy(
                selectedPreset = preset,
                currentHeartRate = when (preset.category) {
                    "HIIT" -> 130
                    "Cardio" -> 122
                    "Dance" -> 120
                    else -> 115
                }
            )
        }
    }

    fun startWorkout() {
        if (_uiState.value.isActive && !_uiState.value.isPaused) return

        if (_uiState.value.isPaused) {
            // Resume
            _uiState.value = _uiState.value.copy(isPaused = false)
        } else {
            // Fresh start
            _uiState.value = _uiState.value.copy(
                isActive = true,
                isPaused = false,
                elapsedSeconds = 0,
                caloriesBurned = 0f,
                distanceKm = 0f,
                isFinished = false,
                milestoneMessage = "Workout started! Oya let's go!",
                heartRateHistory = listOf(112, 115, 118, 120)
            )
        }

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                if (!_uiState.value.isPaused) {
                    tick()
                }
            }
        }
    }

    fun pauseWorkout() {
        _uiState.value = _uiState.value.copy(isPaused = true)
    }

    fun resumeWorkout() {
        _uiState.value = _uiState.value.copy(isPaused = false)
    }

    fun finishWorkout(notes: String = "") {
        timerJob?.cancel()
        val current = _uiState.value
        val totalSecs = current.elapsedSeconds.coerceAtLeast(1)
        val burnedInt = current.caloriesBurned.toInt().coerceAtLeast(5)

        val workout = WorkoutSessionEntity(
            title = current.selectedPreset.title,
            category = current.selectedPreset.category,
            caloriesBurned = burnedInt,
            durationSeconds = totalSecs,
            avgHeartRate = if (current.heartRateHistory.isNotEmpty()) current.heartRateHistory.average().toInt() else current.currentHeartRate,
            distanceKm = current.distanceKm,
            timestamp = System.currentTimeMillis(),
            intensity = current.intensity,
            notes = if (notes.isNotEmpty()) notes else "Completed ${current.selectedPreset.title} in Mayor Spends!"
        )

        viewModelScope.launch {
            repository.recordCompletedWorkout(workout)
            _uiState.value = _uiState.value.copy(
                isActive = false,
                isPaused = false,
                isFinished = true,
                lastCompletedWorkout = workout,
                milestoneMessage = "E choke! Workout Saved! You spent $burnedInt kcal!"
            )
        }
    }

    fun resetState() {
        timerJob?.cancel()
        _uiState.value = LiveWorkoutState(selectedPreset = _uiState.value.selectedPreset)
    }

    fun dismissMilestone() {
        _uiState.value = _uiState.value.copy(milestoneMessage = null)
    }

    private fun tick() {
        val state = _uiState.value
        val newSecs = state.elapsedSeconds + 1

        // Burn rate: base MET * (intensity factor) per minute / 60
        val basePerMin = state.selectedPreset.baseCalPerMin
        val calPerSec = (basePerMin / 60f) * (1f + (state.currentHeartRate - 100) / 100f).coerceIn(0.8f, 1.6f)
        val newCalories = state.caloriesBurned + calPerSec

        // Heart rate fluctuation simulation
        val targetBpm = when (state.selectedPreset.category) {
            "HIIT" -> 162
            "Cardio" -> 148
            "Dance" -> 142
            "Strength" -> 132
            else -> 128
        }
        val variance = Random.nextInt(-2, 3)
        // Gradually drift towards workout target BPM
        val stepDrift = if (state.currentHeartRate < targetBpm) 1 else if (state.currentHeartRate > targetBpm + 8) -1 else 0
        val newBpm = (state.currentHeartRate + stepDrift + variance).coerceIn(95, 185)

        // Waveform history: keep last 35 points
        val newHistory = (state.heartRateHistory + newBpm).takeLast(35)

        // Distance for outdoor/cardio
        val speedKmh = if (state.selectedPreset.category in listOf("Cardio", "Outdoor")) 10.5f + Random.nextFloat() * 1.5f else 0f
        val newDistance = if (speedKmh > 0) state.distanceKm + (speedKmh / 3600f) else 0f

        // Milestone messages
        val newBurnedInt = newCalories.toInt()
        val milestone = when {
            newBurnedInt == 100 && state.caloriesBurned < 100 -> "🔥 100 kcal spent! Body dey hot!"
            newBurnedInt == 250 && state.caloriesBurned < 250 -> "⚡ 250 kcal crushed! Oya no dulling!"
            newBurnedInt == 500 && state.caloriesBurned < 500 -> "🏆 500 kcal milestone! Senior Man energy!"
            newSecs == 600 && state.elapsedSeconds < 600 -> "⏱️ 10 minutes active! Keep moving!"
            newSecs == 1800 && state.elapsedSeconds < 1800 -> "⏱️ 30 minutes solid! Pure Naija stamina!"
            else -> state.milestoneMessage
        }

        _uiState.value = state.copy(
            elapsedSeconds = newSecs,
            caloriesBurned = newCalories,
            currentHeartRate = newBpm,
            heartRateHistory = newHistory,
            distanceKm = newDistance,
            currentSpeedKmh = speedKmh,
            milestoneMessage = milestone
        )
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
