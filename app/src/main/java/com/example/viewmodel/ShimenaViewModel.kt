package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ActiveTextileState(
  val primaryColorHex: String = "#315A67", // Default Wild Indigo
  val secondaryColorHex: String = "#C39345", // Default Ochre
  val colorRecipeName: String = "Wild Indigo & Rift Valley Ochre",
  val motifName: String = "Dorze Diamond (Meskel)",
  val motifIndex: Int = 0,
  val landPurityScore: Int = 98,
  val yarnPlyScore: Int = 96,
  val dyeDepthScore: Int = 97,
  val windTensionScore: Int = 98,
  val weaveScore: Int = 98,
  val fringeScore: Int = 97,
  val pressScore: Int = 99,
  val auditScore: Int = 100,
  val careScore: Int = 99
) {
  val overallAverageScore: Int
    get() = ((landPurityScore + yarnPlyScore + dyeDepthScore + windTensionScore +
        weaveScore + fringeScore + pressScore + auditScore + careScore) / 9)
}

class ShimenaViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: ShimenaRepository

  val stageProgressList: StateFlow<List<StageProgressEntity>>
  val craftedTextiles: StateFlow<List<TextileEntity>>
  val completedStageCount: StateFlow<Int>

  private val _activeTextile = MutableStateFlow(ActiveTextileState())
  val activeTextile: StateFlow<ActiveTextileState> = _activeTextile.asStateFlow()

  private val _currentStageId = MutableStateFlow(1)
  val currentStageId: StateFlow<Int> = _currentStageId.asStateFlow()

  init {
    val database = AppDatabase.getDatabase(application)
    repository = ShimenaRepository(database.stageDao(), database.textileDao())

    stageProgressList = repository.allStageProgress
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    craftedTextiles = repository.allTextiles
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    completedStageCount = repository.completedStageCount
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    viewModelScope.launch {
      repository.initializeStagesIfNeeded()
    }
  }

  fun selectStage(stageId: Int) {
    _currentStageId.value = stageId
  }

  fun updateDyeRecipe(primaryHex: String, secondaryHex: String, recipeName: String, score: Int) {
    _activeTextile.value = _activeTextile.value.copy(
      primaryColorHex = primaryHex,
      secondaryColorHex = secondaryHex,
      colorRecipeName = recipeName,
      dyeDepthScore = score
    )
  }

  fun updateMotif(motifName: String, motifIndex: Int, score: Int) {
    _activeTextile.value = _activeTextile.value.copy(
      motifName = motifName,
      motifIndex = motifIndex,
      weaveScore = score
    )
  }

  fun completeStage(stageId: Int, score: Int, notes: String = "") {
    viewModelScope.launch {
      repository.completeStage(stageId, score, notes)
      // Automatically advance to next stage if applicable
      if (stageId < 10) {
        _currentStageId.value = stageId + 1
      }
    }
  }

  fun saveTextile(textile: TextileEntity) {
    viewModelScope.launch {
      repository.saveCraftedTextile(textile)
    }
  }

  fun deleteTextile(textile: TextileEntity) {
    viewModelScope.launch {
      repository.deleteTextile(textile)
    }
  }

  fun resetJourney() {
    viewModelScope.launch {
      repository.resetAllProgress()
      _currentStageId.value = 1
      _activeTextile.value = ActiveTextileState()
    }
  }
}
