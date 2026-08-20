package com.example.ui.stages

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductionStages
import com.example.ui.theme.*

data class TibebMotif(
  val name: String,
  val amharicName: String,
  val symbolism: String,
  val patternType: Int // 0: Diamond Meskel, 1: Chevron, 2: Waves, 3: Stepped
)

val tibebMotifs = listOf(
  TibebMotif("Dorze Diamond (Meskel)", "መስቀል", "Ancestral protection, balance, and communal unity", 0),
  TibebMotif("Gamo Terraced Chevron", "ጋሞ ጋራ", "The majestic highland peaks and fertile terraces", 1),
  TibebMotif("Rift Valley Water Wave", "የሐይቅ ማዕበል", "The sacred twin waters of Abaya and Chamo", 2),
  TibebMotif("Sunburst Stepped Stair", "የፀሐይ ብርሃን", "Solar warmth, dawn harvest, and renewed hope", 3)
)

/**
 * 05 / WEAVE — Master Pit Loom Weaving
 * Micro-game: Coordinate foot treadles, flying shuttle passes, and reed beater strikes to weave authentic Tibeb motifs.
 */
@Composable
fun StageWeaveScreen(
  primaryColorHex: String,
  secondaryColorHex: String,
  onComplete: (score: Int, notes: String, motifName: String, motifIndex: Int) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedMotifIndex by remember { mutableIntStateOf(0) }
  var wovenRowsCount by remember { mutableIntStateOf(0) } // Target: 10 rows
  var currentStep by remember { mutableIntStateOf(1) } // 1: Treadle pedal, 2: Shuttle throw, 3: Reed strike
  var leftTreadleDown by remember { mutableStateOf(false) }
  var shuttleSideRight by remember { mutableStateOf(false) }
  var reedBeaten by remember { mutableStateOf(false) }

  val targetRows = 10
  val isFinished = wovenRowsCount >= targetRows

  val primaryColor = try {
    Color(android.graphics.Color.parseColor(primaryColorHex))
  } catch (e: Exception) {
    ShimenaIndigo
  }

  val secondaryColor = try {
    Color(android.graphics.Color.parseColor(secondaryColorHex))
  } catch (e: Exception) {
    ShimenaOchre
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "MASTER PIT LOOM (MENBERE)",
      style = MaterialTheme.typography.labelLarge,
      color = ShimenaTerracotta
    )
    Text(
      text = "Select your Gamo Tibeb motif, then follow the master weaver's 3-beat rhythm: Treadle → Shuttle → Reed.",
      style = MaterialTheme.typography.bodyMedium,
      color = TextSecondaryLight,
      modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
    )

    // Motif Selection Carousel
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      itemsIndexed(tibebMotifs) { index, motif ->
        val isSelected = selectedMotifIndex == index
        Surface(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { selectedMotifIndex = index }
            .border(
              width = if (isSelected) 2.dp else 1.dp,
              color = if (isSelected) ShimenaCharcoal else ShimenaCottonDark,
              shape = RoundedCornerShape(10.dp)
            ),
          color = if (isSelected) ShimenaCottonDark.copy(alpha = 0.5f) else ShimenaCottonLight
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            if (isSelected) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = ShimenaTerracotta,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
              text = motif.name,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              ),
              color = ShimenaCharcoal
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Real-Time Weaving Loom Cloth Canvas
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .testTag("loom_canvas_card"),
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(containerColor = ShimenaCottonLight),
      border = CardDefaults.outlinedCardBorder()
    ) {
      Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val w = size.width
          val h = size.height

          // 1. Loom Wooden Frame
          drawRect(
            color = ShimenaEarthDark,
            topLeft = Offset(w * 0.08f, 0f),
            size = Size(16f, h)
          )
          drawRect(
            color = ShimenaEarthDark,
            topLeft = Offset(w * 0.92f - 16f, 0f),
            size = Size(16f, h)
          )

          // 2. Vertical Warp Threads (White Cotton)
          val warpCount = 28
          val startX = w * 0.12f
          val endX = w * 0.88f
          val stepX = (endX - startX) / warpCount

          for (i in 0..warpCount) {
            val wx = startX + i * stepX
            val isRaised = if (leftTreadleDown) i % 2 == 0 else i % 2 != 0
            val warpAlpha = if (isRaised) 0.95f else 0.45f
            drawLine(
              color = ShimenaCotton.copy(alpha = warpAlpha),
              start = Offset(wx, 0f),
              end = Offset(wx, h),
              strokeWidth = if (isRaised) 3f else 1.8f
            )
          }

          // 3. Woven Cloth Fabric building up at the bottom
          val maxClothHeight = h * 0.55f
          val currentClothHeight = (wovenRowsCount.toFloat() / targetRows) * maxClothHeight

          if (currentClothHeight > 4f) {
            val clothTopY = h - currentClothHeight

            // White cotton fabric base
            drawRect(
              color = ShimenaCotton,
              topLeft = Offset(startX, clothTopY),
              size = Size(endX - startX, currentClothHeight)
            )

            // Woven Tibeb Border Stripes with Chosen Motif
            val motif = tibebMotifs[selectedMotifIndex]
            val rowH = currentClothHeight / wovenRowsCount.coerceAtLeast(1)

            for (r in 0 until wovenRowsCount) {
              val ry = h - (r + 1) * rowH
              val isAccentRow = r % 3 == 0 || r == 1 || r == 5

              if (isAccentRow) {
                // Draw decorative Tibeb motifs
                val rowColor = if (r % 2 == 0) primaryColor else secondaryColor
                drawRect(
                  color = rowColor.copy(alpha = 0.85f),
                  topLeft = Offset(startX, ry),
                  size = Size(endX - startX, rowH * 0.85f)
                )

                // Geometric Tibeb diamond or chevron points
                for (c in 0 until 6) {
                  val cx = startX + c * ((endX - startX) / 6f) + ((endX - startX) / 12f)
                  when (motif.patternType) {
                    0 -> { // Diamond
                      val path = Path().apply {
                        moveTo(cx, ry)
                        lineTo(cx + 8f, ry + rowH * 0.4f)
                        lineTo(cx, ry + rowH * 0.8f)
                        lineTo(cx - 8f, ry + rowH * 0.4f)
                        close()
                      }
                      drawPath(path, color = Color.White)
                    }
                    1 -> { // Chevron
                      val path = Path().apply {
                        moveTo(cx - 8f, ry + rowH * 0.7f)
                        lineTo(cx, ry + rowH * 0.2f)
                        lineTo(cx + 8f, ry + rowH * 0.7f)
                      }
                      drawPath(
                        path,
                        color = Color.White,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5f)
                      )
                    }
                    2 -> { // Waves
                      drawCircle(color = Color.White, radius = 4f, center = Offset(cx, ry + rowH * 0.4f))
                    }
                    else -> {
                      drawRect(
                        color = Color.White,
                        topLeft = Offset(cx - 4f, ry + 2f),
                        size = Size(8f, rowH * 0.6f)
                      )
                    }
                  }
                }
              }
            }
          }

          // 4. Wooden Reed Beater (Rech)
          val reedY = h - currentClothHeight - 24f
          drawRoundRect(
            color = ShimenaEarthDark,
            topLeft = Offset(startX - 10f, reedY - 8f),
            size = Size((endX - startX) + 20f, 16f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
          )

          // 5. Flying Shuttle (Mekuamia) passing through shed
          val shuttleX = if (shuttleSideRight) endX - 60f else startX + 10f
          val shuttleY = reedY - 35f
          drawRoundRect(
            color = ShimenaTerracotta,
            topLeft = Offset(shuttleX, shuttleY),
            size = Size(50f, 16f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
          )
          // Shuttle center pirn thread color
          drawRect(
            color = primaryColor,
            topLeft = Offset(shuttleX + 12f, shuttleY + 4f),
            size = Size(26f, 8f)
          )
        }

        // Weaving Pulse instructions
        Surface(
          modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(12.dp),
          shape = RoundedCornerShape(20.dp),
          color = ShimenaCharcoal.copy(alpha = 0.9f)
        ) {
          Text(
            text = when (currentStep) {
              1 -> "Step 1: Press Foot Treadle (Open Warp Shed)"
              2 -> "Step 2: Throw Shuttle (Pass Weft Thread)"
              else -> "Step 3: Strike Reed Beater (Compact Row)"
            },
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = ShimenaCottonLight,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Interactive Master Loom Controls (3-Beat Action)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      // Button 1: Foot Treadle
      Button(
        onClick = {
          leftTreadleDown = !leftTreadleDown
          currentStep = 2
        },
        enabled = currentStep == 1 && !isFinished,
        colors = ButtonDefaults.buttonColors(
          containerColor = if (currentStep == 1) ShimenaTerracotta else ShimenaCottonDark
        ),
        modifier = Modifier.weight(1f).testTag("treadle_button")
      ) {
        Text("1. Treadle", fontSize = 11.sp)
      }

      // Button 2: Throw Shuttle
      Button(
        onClick = {
          shuttleSideRight = !shuttleSideRight
          currentStep = 3
        },
        enabled = currentStep == 2 && !isFinished,
        colors = ButtonDefaults.buttonColors(
          containerColor = if (currentStep == 2) ShimenaIndigo else ShimenaCottonDark
        ),
        modifier = Modifier.weight(1f).testTag("shuttle_button")
      ) {
        Text("2. Shuttle", fontSize = 11.sp)
      }

      // Button 3: Strike Reed
      Button(
        onClick = {
          wovenRowsCount++
          currentStep = 1
        },
        enabled = currentStep == 3 && !isFinished,
        colors = ButtonDefaults.buttonColors(
          containerColor = if (currentStep == 3) ShimenaEarth else ShimenaCottonDark
        ),
        modifier = Modifier.weight(1f).testTag("reed_button")
      ) {
        Text("3. Strike Reed", fontSize = 11.sp)
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Woven Rows: $wovenRowsCount / $targetRows",
        style = MaterialTheme.typography.titleMedium,
        color = ShimenaCharcoal
      )

      Button(
        onClick = {
          val motif = tibebMotifs[selectedMotifIndex]
          onComplete(
            98,
            "Master woven on pit loom by Berhanu & Hailu; featuring authentic ${motif.name} Tibeb motifs.",
            motif.name,
            selectedMotifIndex
          )
        },
        enabled = isFinished,
        colors = ButtonDefaults.buttonColors(containerColor = ShimenaCharcoal),
        modifier = Modifier.testTag("complete_weave_stage_button")
      ) {
        Text("Pass to Fringe Artisans")
      }
    }
  }
}
