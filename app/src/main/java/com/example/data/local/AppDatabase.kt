package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.DailyLogEntity
import com.example.data.model.FitnessGoalEntity
import com.example.data.model.WorkoutSessionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Database(
    entities = [
        FitnessGoalEntity::class,
        WorkoutSessionEntity::class,
        DailyLogEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fitnessDao(): FitnessDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mayor_spends_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database.fitnessDao())
                }
            }
        }

        private suspend fun populateInitialData(dao: FitnessDao) {
            // Default Goal
            dao.saveGoals(
                FitnessGoalEntity(
                    id = 1,
                    dailyCalorieTarget = 750,
                    weeklyCalorieTarget = 4500,
                    dailyStepTarget = 10000,
                    dailyActiveMinutesTarget = 45,
                    dailyWaterTargetMl = 3000,
                    weeklyWorkoutDaysTarget = 5,
                    currentWeightKg = 78.5f,
                    targetWeightKg = 72.0f,
                    currentStreak = 7,
                    mayorTitle = "Lekki Fitness Boss",
                    arewaTitle = "Sarkin Motsa Jiki",
                    pidginLevel = "Senior Man"
                )
            )

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val dayNameFormat = SimpleDateFormat("EEE", Locale.US)
            val calendar = Calendar.getInstance()

            val pastLogs = mutableListOf<DailyLogEntity>()
            val sampleWorkouts = mutableListOf<WorkoutSessionEntity>()

            // 7 Days of realistic data
            val pastCalories = listOf(680, 820, 610, 910, 740, 860, 520)
            val pastSteps = listOf(8400, 11200, 7900, 12500, 9800, 11400, 6800)
            val pastMinutes = listOf(40, 55, 35, 65, 45, 60, 30)
            val pastWater = listOf(2500, 3200, 2750, 3500, 3000, 3250, 2000)

            // Populate back 6 days + today
            for (i in 6 downTo 0) {
                calendar.time = Date()
                calendar.add(Calendar.DAY_OF_YEAR, -i)
                val dateStr = dateFormat.format(calendar.time)
                val dayStr = dayNameFormat.format(calendar.time)
                val idx = 6 - i

                pastLogs.add(
                    DailyLogEntity(
                        date = dateStr,
                        dayOfWeek = dayStr,
                        caloriesSpent = pastCalories[idx],
                        stepsCount = pastSteps[idx],
                        activeMinutes = pastMinutes[idx],
                        waterIntakeMl = pastWater[idx],
                        workoutsCount = if (pastCalories[idx] > 700) 2 else 1
                    )
                )
            }
            dao.insertDailyLogs(pastLogs)

            // Seed a few rich past workouts
            val now = System.currentTimeMillis()
            sampleWorkouts.add(
                WorkoutSessionEntity(
                    title = "Lekki-Ikoyi Bridge 10k Run",
                    category = "Cardio",
                    caloriesBurned = 520,
                    durationSeconds = 2700, // 45 mins
                    avgHeartRate = 152,
                    distanceKm = 8.2f,
                    timestamp = now - (3600 * 4 * 1000),
                    intensity = "Beast Mode",
                    notes = "Early morning run across Lekki Bridge. Breeze was fresh!"
                )
            )
            sampleWorkouts.add(
                WorkoutSessionEntity(
                    title = "Afrobeats High-Energy Cardio",
                    category = "Dance",
                    caloriesBurned = 380,
                    durationSeconds = 2100, // 35 mins
                    avgHeartRate = 144,
                    distanceKm = 0f,
                    timestamp = now - (86400 * 1000L),
                    intensity = "High",
                    notes = "Burna Boy playlist on blast. Sweat poured!"
                )
            )
            sampleWorkouts.add(
                WorkoutSessionEntity(
                    title = "Iron Benders Heavy Gym Lift",
                    category = "Strength",
                    caloriesBurned = 440,
                    durationSeconds = 3000, // 50 mins
                    avgHeartRate = 135,
                    distanceKm = 0f,
                    timestamp = now - (86400 * 2 * 1000L),
                    intensity = "High",
                    notes = "Squats and Romanian deadlifts. Strong reps."
                )
            )

            for (w in sampleWorkouts) {
                dao.insertWorkout(w)
            }
        }
    }
}
