package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ProductionStages
import com.example.data.StageProgressEntity
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.ShimenaViewModel

/**
 * HomeScreen: The Continuous Thread Journey Map.
 * Renders the 10 production stages as an interactive connected timeline,
 * along with the hero story of Arba Minch and quick progress stats.
 */
@Composable
fun HomeScreen(
  viewModel: ShimenaViewModel,
  onSelectStage: (stageId: Int) -> Unit,
  onNavigateToGallery: () -> Unit,
  onNavigateToArchive: () -> Unit,
  onNavigateToAbout: () -> Unit,
  modifier: Modifier = Modifier
) {
  val stageProgressList by viewModel.stageProgressList.collectAsState()
  val completedCount by viewModel.completedStageCount.collectAsState()
  val craftedTextiles by viewModel.craftedTextiles.collectAsState()

  TextileGridBackground(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
    ) {
      // 1. HERO HEADER WITH DOCUMENTARY LANDSCAPE
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("hero_banner_card"),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = ShimenaCharcoal),
          elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
          Box(modifier = Modifier.fillMaxWidth()) {
            Image(
              painter = painterResource(id = R.drawable.img_hero_arba_minch),
              contentDescription = "Arba Minch Landscape and Artisan Village",
              modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
              contentScale = ContentScale.Crop
            )

            // Gradient Overlay
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                  Brush.verticalGradient(
                    listOf(
                      Color.Transparent,
                      ShimenaCharcoal.copy(alpha = 0.85f),
                      ShimenaCharcoalDark
                    )
                  )
                )
            )

            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
                .align(Alignment.BottomStart)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Text(
                  text = "SHIMENA",
                  style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 4.sp,
                    color = ShimenaGold
                  )
                )
                Text(
                  text = "• ARBA MINCH, ETHIOPIA",
                  style = MaterialTheme.typography.labelSmall,
                  color = ShimenaCottonDark
                )
              }

              Spacer(modifier = Modifier.height(4.dp))

              Text(
                text = "The Continuous Thread",
                style = MaterialTheme.typography.displayMedium.copy(
                  fontFamily = FontFamily.Serif,
                  color = ShimenaCottonLight,
                  fontSize = 24.sp
                )
              )

              Text(
                text = "A 10-stage artisan journey from raw organic cotton to living heirloom.",
                style = MaterialTheme.typography.bodySmall,
                color = ShimenaCottonDark
              )
            }
          }
        }
      }

      // 2. UNIFYING CONTINUOUS THREAD CALLOUT
      item {
        ContinuousThreadLine(
          height = 20.dp,
          primaryColor = ShimenaTerracotta,
          secondaryColor = ShimenaGold,
          strokeWidth = 3f
        )
      }

      // 3. STATS & CRAFT STATUS ROW
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          StatCard(
            title = "STAGES DONE",
            value = "$completedCount / 10",
            icon = Icons.Default.LinearScale,
            accentColor = ShimenaTerracotta,
            modifier = Modifier.weight(1f)
          )
          StatCard(
            title = "HEIRLOOMS",
            value = "${craftedTextiles.size} Woven",
            icon = Icons.Default.Bookmark,
            accentColor = ShimenaIndigo,
            modifier = Modifier.weight(1f)
          )
          StatCard(
            title = "ARTISANS",
            value = "10 Hands",
            icon = Icons.Default.Groups,
            accentColor = ShimenaOchre,
            modifier = Modifier.weight(1f)
          )
        }
      }

      // 4. TIMELINE TITLE
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "THE 10 PRODUCTION STAGES",
            style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 1.8.sp),
            color = ShimenaCharcoal
          )
          Text(
            text = "Tap any unlocked stage",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondaryLight
          )
        }
      }

      // 5. THE 10 STAGE TIMELINE NODES
      itemsIndexed(ProductionStages.stages) { index, stage ->
        val progress = stageProgressList.find { it.stageId == stage.id }
        val isUnlocked = progress?.unlocked ?: (index == 0)
        val isCompleted = progress?.completed ?: false
        val isCurrent = isUnlocked && !isCompleted

        StageTimelineCard(
          stage = stage,
          isUnlocked = isUnlocked,
          isCompleted = isCompleted,
          isCurrent = isCurrent,
          qualityScore = progress?.qualityScore ?: 0,
          onClick = {
            if (isUnlocked) {
              onSelectStage(stage.id)
            }
          }
        )
      }
    }
  }
}

@Composable
fun StatCard(
  title: String,
  value: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  accentColor: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(14.dp),
    color = ShimenaCottonLight,
    border = BorderStroke(1.dp, ShimenaCottonDark)
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = accentColor,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = ShimenaCharcoal
      )
      Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
        color = TextSecondaryLight
      )
    }
  }
}

@Composable
fun StageTimelineCard(
  stage: com.example.data.StageInfo,
  isUnlocked: Boolean,
  isCompleted: Boolean,
  isCurrent: Boolean,
  qualityScore: Int,
  onClick: () -> Unit
) {
  val cardBg = when {
    isCompleted -> ShimenaCottonLight
    isCurrent -> ShimenaCottonLight
    else -> ShimenaCottonDark.copy(alpha = 0.35f)
  }

  val borderBrush = when {
    isCurrent -> Brush.linearGradient(listOf(stage.accentColor, ShimenaGold))
    isCompleted -> Brush.linearGradient(listOf(stage.accentColor.copy(alpha = 0.6f), ShimenaCottonDark))
    else -> Brush.linearGradient(listOf(ShimenaCottonDark, ShimenaCottonDark))
  }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .clickable(enabled = isUnlocked, onClick = onClick)
      .testTag("stage_card_${stage.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = cardBg),
    border = BorderStroke(if (isCurrent) 2.dp else 1.dp, borderBrush),
    elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrent) 4.dp else 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Stage Node Icon / Number
      Box(
        modifier = Modifier
          .size(52.dp)
          .clip(CircleShape)
          .background(
            if (isUnlocked) stage.accentColor.copy(alpha = 0.15f) else ShimenaCottonDark
          )
          .border(
            width = if (isCurrent) 2.dp else 1.dp,
            color = if (isUnlocked) stage.accentColor else ShimenaEarthLight,
            shape = CircleShape
          ),
        contentAlignment = Alignment.Center
      ) {
        if (isCompleted) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Completed",
            tint = ShimenaLeaf,
            modifier = Modifier.size(28.dp)
          )
        } else if (!isUnlocked) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Locked",
            tint = TextSecondaryLight,
            modifier = Modifier.size(20.dp)
          )
        } else {
          DepartmentIcon(
            stageId = stage.id,
            size = 28.dp,
            tint = stage.accentColor,
            strokeWidthDp = 2.dp
          )
        }
      }

      // Stage Title & Artisan info
      Column(modifier = Modifier.weight(1f)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = "${stage.code} / ${stage.department}",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.5.sp
            ),
            color = if (isUnlocked) stage.accentColor else TextSecondaryLight
          )

          if (isCurrent) {
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = ShimenaGold,
              modifier = Modifier.padding(start = 4.dp)
            ) {
              Text(
                text = "ACTIVE",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 8.sp,
                  fontWeight = FontWeight.Bold
                ),
                color = ShimenaCharcoalDark,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = stage.title,
          style = MaterialTheme.typography.titleMedium.copy(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold
          ),
          color = if (isUnlocked) ShimenaCharcoal else TextSecondaryLight
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = "Maker: ${stage.artisanName}",
          style = MaterialTheme.typography.bodySmall,
          color = if (isUnlocked) ShimenaEarthDark else TextSecondaryLight
        )

        if (isCompleted && qualityScore > 0) {
          Text(
            text = "Quality Score: $qualityScore%",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = ShimenaLeaf
          )
        }
      }

      // Chevron or arrow
      if (isUnlocked) {
        Icon(
          imageVector = Icons.Default.ChevronRight,
          contentDescription = null,
          tint = if (isCurrent) stage.accentColor else TextSecondaryLight,
          modifier = Modifier.size(24.dp)
        )
      }
    }
  }
}
