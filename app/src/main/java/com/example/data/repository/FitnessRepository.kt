package com.example.data.repository

import com.example.data.local.FitnessDao
import com.example.data.model.DailyLogEntity
import com.example.data.model.FitnessGoalEntity
import com.example.data.model.WorkoutSessionEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FitnessRepository(private val dao: FitnessDao) {

    val goals: Flow<FitnessGoalEntity?> = dao.getGoals()
    val allWorkouts: Flow<List<WorkoutSessionEntity>> = dao.getAllWorkouts()
    val recentWorkouts: Flow<List<WorkoutSessionEntity>> = dao.getRecentWorkouts(10)
    val dailyLogs: Flow<List<DailyLogEntity>> = dao.getDailyLogs()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val dayNameFormat = SimpleDateFormat("EEE", Locale.US)

    fun getTodayLog(): Flow<DailyLogEntity?> {
        val todayStr = dateFormat.format(Date())
        return dao.getTodayLog(todayStr)
    }

    suspend fun getTodayLogSync(): DailyLogEntity {
        val todayStr = dateFormat.format(Date())
        val dayStr = dayNameFormat.format(Date())
        var current = dao.getTodayLogSync(todayStr)
        if (current == null) {
            current = DailyLogEntity(
                date = todayStr,
                dayOfWeek = dayStr,
                caloriesSpent = 0,
                stepsCount = 0,
                activeMinutes = 0,
                waterIntakeMl = 0,
                workoutsCount = 0
            )
            dao.saveDailyLog(current)
        }
        return current
    }

    suspend fun recordCompletedWorkout(workout: WorkoutSessionEntity) {
        dao.insertWorkout(workout)

        // Update or create today's daily log
        val todayStr = dateFormat.format(Date(workout.timestamp))
        val dayStr = dayNameFormat.format(Date(workout.timestamp))
        val current = dao.getTodayLogSync(todayStr) ?: DailyLogEntity(
            date = todayStr,
            dayOfWeek = dayStr,
            caloriesSpent = 0,
            stepsCount = 0,
            activeMinutes = 0,
            waterIntakeMl = 0,
            workoutsCount = 0
        )

        val updated = current.copy(
            caloriesSpent = current.caloriesSpent + workout.caloriesBurned,
            activeMinutes = current.activeMinutes + (workout.durationSeconds / 60),
            workoutsCount = current.workoutsCount + 1,
            // approximate steps if walking/running/dance
            stepsCount = if (workout.category in listOf("Cardio", "Outdoor", "Dance")) {
                current.stepsCount + (workout.durationSeconds * 2.2f).toInt()
            } else {
                current.stepsCount
            }
        )
        dao.saveDailyLog(updated)
    }

    suspend fun addWaterIntake(amountMl: Int) {
        val todayLog = getTodayLogSync()
        val updated = todayLog.copy(waterIntakeMl = (todayLog.waterIntakeMl + amountMl).coerceAtLeast(0))
        dao.saveDailyLog(updated)
    }

    suspend fun addManualSteps(addedSteps: Int) {
        val todayLog = getTodayLogSync()
        val updated = todayLog.copy(
            stepsCount = (todayLog.stepsCount + addedSteps).coerceAtLeast(0),
            caloriesSpent = todayLog.caloriesSpent + (addedSteps * 0.04f).toInt()
        )
        dao.saveDailyLog(updated)
    }

    suspend fun updateGoals(goals: FitnessGoalEntity) {
        dao.saveGoals(goals)
    }

    suspend fun deleteWorkout(workout: WorkoutSessionEntity) {
        dao.deleteWorkout(workout)
    }
}
