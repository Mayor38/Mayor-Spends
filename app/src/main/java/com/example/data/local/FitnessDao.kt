package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.DailyLogEntity
import com.example.data.model.FitnessGoalEntity
import com.example.data.model.WorkoutSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FitnessDao {

    @Query("SELECT * FROM fitness_goals WHERE id = 1 LIMIT 1")
    fun getGoals(): Flow<FitnessGoalEntity?>

    @Query("SELECT * FROM fitness_goals WHERE id = 1 LIMIT 1")
    suspend fun getGoalsSync(): FitnessGoalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGoals(goals: FitnessGoalEntity)

    @Query("SELECT * FROM workout_sessions ORDER BY timestamp DESC")
    fun getAllWorkouts(): Flow<List<WorkoutSessionEntity>>

    @Query("SELECT * FROM workout_sessions ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentWorkouts(limit: Int = 10): Flow<List<WorkoutSessionEntity>>

    @Insert
    suspend fun insertWorkout(workout: WorkoutSessionEntity): Long

    @Delete
    suspend fun deleteWorkout(workout: WorkoutSessionEntity)

    @Query("SELECT * FROM daily_logs ORDER BY date ASC")
    fun getDailyLogs(): Flow<List<DailyLogEntity>>

    @Query("SELECT * FROM daily_logs WHERE date = :date LIMIT 1")
    fun getTodayLog(date: String): Flow<DailyLogEntity?>

    @Query("SELECT * FROM daily_logs WHERE date = :date LIMIT 1")
    suspend fun getTodayLogSync(date: String): DailyLogEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDailyLog(log: DailyLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyLogs(logs: List<DailyLogEntity>)
}
