package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductionStages
import com.example.data.StageInfo
import com.example.ui.components.*
import com.example.ui.stages.*
import com.example.ui.theme.*
import com.example.viewmodel.ActiveTextileState
import com.example.viewmodel.ShimenaViewModel

/**
 * StageContainerScreen: Host for any of the 10 production stages.
 * Provides documentary context, artisan introduction, micro-game interaction, and completion outcomes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StageContainerScreen(
  stageId: Int,
  viewModel: ShimenaViewModel,
  onNavigateBack: () -> Unit,
  onNavigateToShare: () -> Unit,
  modifier: Modifier = Modifier
) {
  val stage = ProductionStages.stages.find { it.id == stageId } ?: ProductionStages.stages[0]
  val activeTextile by viewModel.activeTextile.collectAsState()

  var showArtisanDocDialog by remember { mutableStateOf(false) }
  var completionScore by remember { mutableStateOf<Int?>(null) }
  var completionNotes by remember { mutableStateOf("") }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = ShimenaCotton,
    topBar = {
      TopAppBar(
        title = {
          Column {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              DepartmentBadge(
                stageCode = stage.code,
                stageName = stage.department,
                stageId = stage.id,
                accentColor = stage.accentColor
              )
              Text(
                text = "STAGE ${stage.id}/10",
                style = MaterialTheme.typography.labelSmall,
                color = ShimenaEarthDark
              )
            }
            Text(
              text = stage.title,
              style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
              ),
              color = ShimenaCharcoal
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.testTag("stage_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back to Thread Map",
              tint = ShimenaCharcoal
            )
          }
        },
        actions = {
          IconButton(
            onClick = { showArtisanDocDialog = true },
            modifier = Modifier.testTag("artisan_info_button")
          ) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = "Artisan Documentary Info",
              tint = stage.accentColor
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = ShimenaCottonLight
        )
      )
    }
  ) { innerPadding ->
    TextileGridBackground(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        // Continuous Thread Progress Line at top
        ContinuousThreadLine(
          height = 14.dp,
          primaryColor = stage.accentColor,
          secondaryColor = ShimenaGold,
          strokeWidth = 2.5f
        )

        // Stage Host Router
        Box(modifier = Modifier.weight(1f)) {
          when (stage.id) {
            1 -> StageLandScreen(
              onComplete = { score, notes ->
                completionScore = score
                completionNotes = notes
                viewModel.completeStage(1, score, notes)
              }
            )
            2 -> StageSpinScreen(
              onComplete = { score, notes ->
                completionScore = score
                completionNotes = notes
                viewModel.completeStage(2, score, notes)
              }
            )
            3 -> StageColorScreen(
              onComplete = { score, notes, pColor, sColor, recipe ->
                completionScore = score
                completionNotes = notes
                viewModel.updateDyeRecipe(pColor, sColor, recipe, score)
                viewModel.completeStage(3, score, notes)
              }
            )
            4 -> StageWindScreen(
              primaryColorHex = activeTextile.primaryColorHex,
              onComplete = { score, notes ->
                completionScore = score
                completionNotes = notes
                viewModel.completeStage(4, score, notes)
              }
            )
            5 -> StageWeaveScreen(
              primaryColorHex = activeTextile.primaryColorHex,
              secondaryColorHex = activeTextile.secondaryColorHex,
              onComplete = { score, notes, motifName, motifIndex ->
                completionScore = score
                completionNotes = notes
                viewModel.updateMotif(motifName, motifIndex, score)
                viewModel.completeStage(5, score, notes)
              }
            )
            6 -> StageSewScreen(
              onComplete = { score, notes ->
                completionScore = score
                completionNotes = notes
                viewModel.completeStage(6, score, notes)
              }
            )
            7 -> StagePressScreen(
              onComplete = { score, notes ->
                completionScore = score
                completionNotes = notes
                viewModel.completeStage(7, score, notes)
              }
            )
            8 -> StageCheckScreen(
              onComplete = { score, notes ->
                completionScore = score
                completionNotes = notes
                viewModel.completeStage(8, score, notes)
              }
            )
            9 -> StageCareScreen(
              onComplete = { score, notes ->
                completionScore = score
                completionNotes = notes
                viewModel.completeStage(9, score, notes)
              }
            )
            10 -> StageShareScreen(
              primaryColorHex = activeTextile.primaryColorHex,
              secondaryColorHex = activeTextile.secondaryColorHex,
              colorRecipeName = activeTextile.colorRecipeName,
              motifName = activeTextile.motifName,
              motifIndex = activeTextile.motifIndex,
              overallScore = activeTextile.overallAverageScore,
              onSaveToArchive = { textile ->
                viewModel.saveTextile(textile)
              },
              onRestartJourney = {
                viewModel.resetJourney()
                onNavigateBack()
              }
            )
          }
        }
      }
    }
  }

  // Artisan Documentary Info Dialog
  if (showArtisanDocDialog) {
    AlertDialog(
      onDismissRequest = { showArtisanDocDialog = false },
      title = {
        Text(
          text = "Meet the Maker • ${stage.department}",
          style = MaterialTheme.typography.titleLarge.copy(fontFamily = FontFamily.Serif),
          color = ShimenaCharcoal
        )
      },
      text = {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          ArtisanCard(stage = stage, showFullBio = true)

          Surface(
            shape = RoundedCornerShape(10.dp),
            color = ShimenaCottonDark.copy(alpha = 0.6f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text(
                text = "MATERIAL TRANSFORMATION",
                style = MaterialTheme.typography.labelSmall,
                color = ShimenaEarthDark
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "• Input: ${stage.materialInput}",
                style = MaterialTheme.typography.bodySmall,
                color = ShimenaCharcoal
              )
              Text(
                text = "• Output: ${stage.materialOutput}",
                style = MaterialTheme.typography.bodySmall,
                color = ShimenaCharcoal
              )
            }
          }

          Text(
            text = "Cultural Heritage: ${stage.culturalContext}",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondaryLight
          )
        }
      },
      confirmButton = {
        Button(
          onClick = { showArtisanDocDialog = false },
          colors = ButtonDefaults.buttonColors(containerColor = ShimenaCharcoal)
        ) {
          Text("Return to Craft")
        }
      },
      containerColor = ShimenaCottonLight
    )
  }

  // Stage Completion Celebration Dialog
  if (completionScore != null && stage.id < 10) {
    AlertDialog(
      onDismissRequest = { completionScore = null },
      icon = {
        Icon(
          imageVector = Icons.Default.Verified,
          contentDescription = null,
          tint = ShimenaGold,
          modifier = Modifier.size(44.dp)
        )
      },
      title = {
        Text(
          text = "Stage ${stage.code} Complete!",
          style = MaterialTheme.typography.titleLarge.copy(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold
          ),
          color = ShimenaCharcoal
        )
      },
      text = {
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "Craft Quality Score: $completionScore%",
            style = MaterialTheme.typography.headlineSmall.copy(color = ShimenaLeaf)
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = completionNotes,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondaryLight
          )
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "The continuous thread now travels to Stage ${stage.id + 1}: ${ProductionStages.stages[stage.id].department}.",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = ShimenaTerracotta
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val nextStageId = stage.id + 1
            completionScore = null
            if (nextStageId <= 10) {
              viewModel.selectStage(nextStageId)
            } else {
              onNavigateToShare()
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = ShimenaCharcoal),
          modifier = Modifier.testTag("next_stage_button")
        ) {
          Text("Continue to Stage ${stage.id + 1}")
        }
      },
      dismissButton = {
        TextButton(
          onClick = {
            completionScore = null
            onNavigateBack()
          }
        ) {
          Text("Thread Map")
        }
      },
      containerColor = ShimenaCottonLight
    )
  }
}
