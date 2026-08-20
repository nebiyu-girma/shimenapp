package com.example.data

import kotlinx.coroutines.flow.Flow
import java.util.UUID

class ShimenaRepository(
  private val stageDao: StageDao,
  private val textileDao: TextileDao
) {
  val allStageProgress: Flow<List<StageProgressEntity>> = stageDao.getAllStageProgress()
  val allTextiles: Flow<List<TextileEntity>> = textileDao.getAllTextiles()
  val completedStageCount: Flow<Int> = stageDao.getCompletedStageCount()

  suspend fun initializeStagesIfNeeded() {
    val existing = stageDao.getStageById(1)
    if (existing == null) {
      val initialList = ProductionStages.stages.mapIndexed { index, stage ->
        StageProgressEntity(
          stageId = stage.id,
          stageCode = stage.code,
          stageName = stage.department,
          title = stage.title,
          artisanName = stage.artisanName,
          artisanRole = stage.artisanRole,
          unlocked = (index == 0), // Stage 1 is unlocked initially
          completed = false,
          qualityScore = 0,
          completionCount = 0,
          completedAt = 0L,
          playerChoiceNotes = ""
        )
      }
      stageDao.insertAll(initialList)
    }
  }

  suspend fun completeStage(
    stageId: Int,
    score: Int,
    notes: String = ""
  ) {
    val timestamp = System.currentTimeMillis()
    stageDao.completeStage(stageId, score, timestamp, notes)
    // Unlock next stage if exists
    if (stageId < 10) {
      stageDao.unlockStage(stageId + 1)
    }
  }

  suspend fun unlockStage(stageId: Int) {
    stageDao.unlockStage(stageId)
  }

  suspend fun resetAllProgress() {
    val initialList = ProductionStages.stages.mapIndexed { index, stage ->
      StageProgressEntity(
        stageId = stage.id,
        stageCode = stage.code,
        stageName = stage.department,
        title = stage.title,
        artisanName = stage.artisanName,
        artisanRole = stage.artisanRole,
        unlocked = (index == 0),
        completed = false,
        qualityScore = 0,
        completionCount = 0,
        completedAt = 0L,
        playerChoiceNotes = ""
      )
    }
    stageDao.insertAll(initialList)
  }

  suspend fun saveCraftedTextile(textile: TextileEntity): Long {
    return textileDao.insertTextile(textile)
  }

  suspend fun deleteTextile(textile: TextileEntity) {
    textileDao.deleteTextile(textile)
  }
}
