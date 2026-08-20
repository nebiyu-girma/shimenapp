package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StageDao {
  @Query("SELECT * FROM stage_progress ORDER BY stageId ASC")
  fun getAllStageProgress(): Flow<List<StageProgressEntity>>

  @Query("SELECT * FROM stage_progress WHERE stageId = :id")
  suspend fun getStageById(id: Int): StageProgressEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(stages: List<StageProgressEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(stage: StageProgressEntity)

  @Update
  suspend fun update(stage: StageProgressEntity)

  @Query("UPDATE stage_progress SET unlocked = 1 WHERE stageId = :id")
  suspend fun unlockStage(id: Int)

  @Query("UPDATE stage_progress SET completed = 1, qualityScore = :score, completionCount = completionCount + 1, completedAt = :timestamp, playerChoiceNotes = :notes WHERE stageId = :id")
  suspend fun completeStage(id: Int, score: Int, timestamp: Long, notes: String)

  @Query("SELECT COUNT(*) FROM stage_progress WHERE completed = 1")
  fun getCompletedStageCount(): Flow<Int>
}
