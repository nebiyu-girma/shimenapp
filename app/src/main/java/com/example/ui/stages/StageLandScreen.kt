package com.example.ui.stages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductionStages
import com.example.ui.components.ContinuousThreadLine
import com.example.ui.theme.*
import kotlin.math.abs
import kotlin.random.Random

/**
 * 01 / LAND — Cotton Farming & Ginning
 * Micro-game: Harvest mature cotton bolls and gin seeds using the traditional Midit rod.
 */
@Composable
fun StageLandScreen(
  onComplete: (score: Int, notes: String) -> Unit,
  modifier: Modifier = Modifier
) {
  val stage = ProductionStages.stages[0]
  var subPhase by remember { mutableStateOf(1) } // 1: Harvest, 2: Ginning (Midit), 3: Done

  // Harvest state
  val bolls = remember {
    mutableStateListOf(
      CottonBoll(id = 1, x = 0.2f, y = 0.25f, isRipe = true),
      CottonBoll(id = 2, x = 0.75f, y = 0.2f, isRipe = true),
      CottonBoll(id = 3, x = 0.45f, y = 0.45f, isRipe = true),
      CottonBoll(id = 4, x = 0.22f, y = 0.7f, isRipe = true),
      CottonBoll(id = 5, x = 0.78f, y = 0.65f, isRipe = true),
      CottonBoll(id = 6, x = 0.5f, y = 0.8f, isRipe = true)
    )
  }
  var harvestedCount by remember { mutableIntStateOf(0) }

  // Ginning state (Midit rod stroke)
  var ginningProgress by remember { mutableFloatStateOf(0f) }
  var dragOffsetX by remember { mutableFloatStateOf(0f) }

  val harvestFinished = harvestedCount >= bolls.size
  val ginningFinished = ginningProgress >= 1f

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Stage Header
    Text(
      text = if (subPhase == 1) "STEP 1: HARVEST MATURE BOLLS" else "STEP 2: GINNING WITH MIDIT ROD",
      style = MaterialTheme.typography.labelLarge,
      color = ShimenaTerracotta
    )
    Text(
      text = if (subPhase == 1) "Tap the ripe white cotton bolls in the morning sunlight" else "Drag the wooden Midit rod across the basalt stone to separate seeds",
      style = MaterialTheme.typography.bodyMedium,
      color = TextSecondaryLight,
      modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
    )

    if (subPhase == 1) {
      // HARVEST INTERACTIVE FIELD
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .testTag("harvest_field_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ShimenaCottonLight),
        border = CardDefaults.outlinedCardBorder()
      ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
          val widthPx = constraints.maxWidth.toFloat()
          val heightPx = constraints.maxHeight.toFloat()

          // Background soil & cotton plant foliage canvas
          Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
              brush = Brush.verticalGradient(
                listOf(
                  Color(0xFFEFE8DA),
                  Color(0xFFE4D7C2)
                )
              )
            )
            // Draw subtle plant branches
            for (boll in bolls) {
              val bx = boll.x * widthPx
              val by = boll.y * heightPx
              drawLine(
                color = ShimenaLeaf.copy(alpha = 0.6f),
                start = Offset(bx, by + 40f),
                end = Offset(bx, by),
                strokeWidth = 4f
              )
            }
          }

          // Render interactive Cotton Bolls
          bolls.forEach { boll ->
            val bx = (boll.x * widthPx).dp / 2.7f
            val by = (boll.y * heightPx).dp / 2.7f

            Box(
              modifier = Modifier
                .offset(x = bx, y = by)
                .size(64.dp)
                .clip(CircleShape)
                .background(if (boll.harvested) ShimenaCottonDark.copy(alpha = 0.4f) else Color.White)
                .border(
                  width = 2.dp,
                  color = if (boll.harvested) ShimenaEarthLight else ShimenaEarth,
                  shape = CircleShape
                )
                .pointerInput(boll.id) {
                  detectDragGestures(
                    onDragStart = {
                      if (!boll.harvested) {
                        boll.harvested = true
                        harvestedCount++
                      }
                    },
                    onDrag = { _, _ -> }
                  )
                }
                .testTag("boll_${boll.id}"),
              contentAlignment = Alignment.Center
            ) {
              if (boll.harvested) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = "Harvested",
                  tint = ShimenaLeaf,
                  modifier = Modifier.size(28.dp)
                )
              } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = "Ripe Cotton",
                    tint = ShimenaEarth,
                    modifier = Modifier.size(24.dp)
                  )
                  Text(
                    text = "Pick",
                    style = MaterialTheme.typography.labelSmall,
                    color = ShimenaEarthDark
                  )
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Progress bar & Continue
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Harvested: $harvestedCount / ${bolls.size} bolls",
          style = MaterialTheme.typography.titleMedium,
          color = ShimenaCharcoal
        )

        Button(
          onClick = { subPhase = 2 },
          enabled = harvestFinished,
          colors = ButtonDefaults.buttonColors(containerColor = ShimenaEarth),
          modifier = Modifier.testTag("proceed_to_ginning_button")
        ) {
          Text("Proceed to Ginning")
        }
      }
    } else {
      // GINNING WITH MIDIT ROD
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .testTag("ginning_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ShimenaCottonLight),
        border = CardDefaults.outlinedCardBorder()
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          // Basalt Stone & Midit Rod Canvas
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(240.dp)
              .clip(RoundedCornerShape(16.dp))
              .background(ShimenaCharcoalSurface)
              .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                  change.consume()
                  dragOffsetX += dragAmount.x
                  if (abs(dragAmount.x) > 10f) {
                    ginningProgress = (ginningProgress + 0.04f).coerceIn(0f, 1f)
                  }
                }
              }
              .testTag("midit_drag_area")
          ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
              val w = size.width
              val h = size.height

              // Basalt stone texture base
              drawRect(
                brush = Brush.verticalGradient(
                  listOf(Color(0xFF383834), Color(0xFF232320))
                )
              )

              // Separated black seeds dropping down
              val seedCount = (ginningProgress * 12).toInt()
              for (i in 0 until seedCount) {
                val sx = (i * 73f) % (w - 60f) + 30f
                val sy = h * 0.78f + ((i * 17f) % 20f)
                drawCircle(color = Color.Black, radius = 6f, center = Offset(sx, sy))
              }

              // Fluffy seedless rolags appearing at top
              val rolagWidth = w * ginningProgress
              drawRoundRect(
                color = ShimenaCottonLight,
                topLeft = Offset(w * 0.1f, h * 0.15f),
                size = androidx.compose.ui.geometry.Size(rolagWidth * 0.8f, 50f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(20f, 20f)
              )

              // Midit Wooden Rod (dragged by player)
              val rodX = (w * 0.2f + (dragOffsetX % (w * 0.6f))).coerceIn(w * 0.1f, w * 0.85f)
              drawRoundRect(
                color = ShimenaEarthLight,
                topLeft = Offset(rodX - 12f, h * 0.08f),
                size = androidx.compose.ui.geometry.Size(24f, h * 0.8f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f)
              )
            }

            // Instructional Overlay
            if (ginningProgress < 0.15f) {
              Box(
                modifier = Modifier
                  .align(Alignment.Center)
                  .background(ShimenaCharcoal.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                  .padding(horizontal = 12.dp, vertical = 6.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.TouchApp,
                    contentDescription = null,
                    tint = ShimenaGold,
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "Slide finger back and forth to roll the Midit",
                    style = MaterialTheme.typography.bodySmall,
                    color = ShimenaCottonLight
                  )
                }
              }
            }
          }

          // Ginning Progress & Completion
          Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            LinearProgressIndicator(
              progress = { ginningProgress },
              modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
              color = ShimenaEarth,
              trackColor = ShimenaCottonDark
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Ginning Purity: ${(ginningProgress * 100).toInt()}%",
              style = MaterialTheme.typography.titleMedium,
              color = ShimenaCharcoal
            )
          }

          Button(
            onClick = {
              val purityScore = 95 + (ginningProgress * 5).toInt()
              onComplete(
                purityScore,
                "Hand-picked organic cotton from Lake Abaya lowlands; ginned with traditional basalt stone."
              )
            },
            enabled = ginningFinished,
            colors = ButtonDefaults.buttonColors(containerColor = ShimenaCharcoal),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("complete_land_stage_button")
          ) {
            Text("Complete Stage 01 & Deliver Rolags to Spinners")
          }
        }
      }
    }
  }
}

class CottonBoll(
  val id: Int,
  val x: Float,
  val y: Float,
  val isRipe: Boolean,
  var harvested: Boolean = false
)
