package com.example.ui.stages

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductionStages
import com.example.ui.theme.*
import kotlin.math.abs
import kotlin.math.sin

/**
 * 04 / WIND — Winding & Yarn Preparation
 * Micro-game: Rotate the wooden Meweria wheel and traverse guide to wind a flawless loom bobbin.
 */
@Composable
fun StageWindScreen(
  primaryColorHex: String,
  onComplete: (score: Int, notes: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var woundPercentage by remember { mutableFloatStateOf(0f) } // 0f to 100f
  var crankRotationAngle by remember { mutableFloatStateOf(0f) }
  var guidePositionX by remember { mutableFloatStateOf(0.5f) } // 0.2f to 0.8f
  var guideDirection by remember { mutableFloatStateOf(1f) }
  var isWinding by remember { mutableStateOf(false) }

  val yarnColor = try {
    Color(android.graphics.Color.parseColor(primaryColorHex))
  } catch (e: Exception) {
    ShimenaIndigo
  }

  // Guide auto-traverse when crank rotates
  LaunchedEffect(crankRotationAngle) {
    guidePositionX = (guidePositionX + (0.015f * guideDirection)).coerceIn(0.15f, 0.85f)
    if (guidePositionX >= 0.85f) guideDirection = -1f
    if (guidePositionX <= 0.15f) guideDirection = 1f
  }

  val isFinished = woundPercentage >= 100f

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "MEWERIA SPOOLING WHEEL",
      style = MaterialTheme.typography.labelLarge,
      color = ShimenaLakeBlue
    )
    Text(
      text = "Rotate the crank wheel in smooth circles to wind the dyed skein onto the wooden loom shuttle bobbin with even tension.",
      style = MaterialTheme.typography.bodyMedium,
      color = TextSecondaryLight,
      modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
    )

    // Interactive Meweria Canvas Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .testTag("meweria_wheel_card"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = ShimenaCottonLight),
      border = CardDefaults.outlinedCardBorder()
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
              change.consume()
              val magnitude = abs(dragAmount.x) + abs(dragAmount.y)
              if (magnitude > 6f) {
                crankRotationAngle += magnitude * 1.5f
                woundPercentage = (woundPercentage + magnitude * 0.12f).coerceAtMost(100f)
                isWinding = true
              }
            }
          }
      ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val w = size.width
          val h = size.height
          val centerX = w * 0.35f
          val centerY = h * 0.45f

          // 1. Large Wooden Meweria Crank Wheel (Left)
          val wheelRadius = 85f
          drawCircle(
            color = ShimenaEarthDark,
            radius = wheelRadius,
            center = Offset(centerX, centerY),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 8f)
          )
          // Wheel Spokes
          for (i in 0..5) {
            val angleRad = (crankRotationAngle + i * 60) * (Math.PI / 180).toFloat()
            val sx = centerX + kotlin.math.cos(angleRad) * wheelRadius
            val sy = centerY + kotlin.math.sin(angleRad) * wheelRadius
            drawLine(
              color = ShimenaEarth,
              start = Offset(centerX, centerY),
              end = Offset(sx, sy),
              strokeWidth = 4f
            )
          }
          // Center hub
          drawCircle(color = ShimenaTerracotta, radius = 16f, center = Offset(centerX, centerY))

          // 2. Receiving Shuttle Bobbin (Right)
          val bobbinCenterX = w * 0.75f
          val bobbinCenterY = h * 0.45f
          val bobbinW = 90f
          val bobbinH = 34f

          // Wooden core
          drawRoundRect(
            color = ShimenaEarthDark,
            topLeft = Offset(bobbinCenterX - bobbinW / 2f, bobbinCenterY - bobbinH / 2f),
            size = androidx.compose.ui.geometry.Size(bobbinW, bobbinH),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
          )

          // Wound dyed yarn layer building up
          val yarnLayerHeight = (woundPercentage / 100f) * 44f
          if (yarnLayerHeight > 2f) {
            drawRoundRect(
              color = yarnColor,
              topLeft = Offset(bobbinCenterX - (bobbinW * 0.8f) / 2f, bobbinCenterY - yarnLayerHeight / 2f),
              size = androidx.compose.ui.geometry.Size(bobbinW * 0.8f, yarnLayerHeight),
              cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f)
            )
          }

          // 3. Traveling Thread line connecting wheel to bobbin through guide
          val guideX = bobbinCenterX - (bobbinW * 0.4f) + (guidePositionX * bobbinW * 0.8f)
          val guideY = bobbinCenterY - 45f

          drawLine(
            color = yarnColor,
            start = Offset(centerX + wheelRadius * 0.7f, centerY - 20f),
            end = Offset(guideX, guideY),
            strokeWidth = 3f
          )
          drawLine(
            color = yarnColor,
            start = Offset(guideX, guideY),
            end = Offset(guideX, bobbinCenterY),
            strokeWidth = 3.5f
          )

          // Metallic Eyelet Guide
          drawCircle(color = ShimenaCharcoal, radius = 6f, center = Offset(guideX, guideY))
        }

        // Action prompt
        if (woundPercentage < 10f) {
          Box(
            modifier = Modifier
              .align(Alignment.BottomCenter)
              .padding(16.dp)
              .background(ShimenaCharcoal.copy(alpha = 0.85f), RoundedCornerShape(8.dp))
              .padding(horizontal = 14.dp, vertical = 8.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.TouchApp,
                contentDescription = null,
                tint = ShimenaGold,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Swirl finger on wheel to wind bobbin",
                style = MaterialTheme.typography.bodySmall,
                color = ShimenaCottonLight
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Bobbin Status
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Bobbin Wound: ${woundPercentage.toInt()}%",
          style = MaterialTheme.typography.titleMedium,
          color = ShimenaCharcoal
        )
        Text(
          text = if (isFinished) "Ready for Shuttle" else "In Tension",
          style = MaterialTheme.typography.labelMedium,
          color = if (isFinished) ShimenaLeaf else ShimenaLakeBlue
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      LinearProgressIndicator(
        progress = { woundPercentage / 100f },
        modifier = Modifier
          .fillMaxWidth()
          .height(10.dp)
          .clip(RoundedCornerShape(5.dp)),
        color = ShimenaLakeBlue,
        trackColor = ShimenaCottonDark
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Button(
      onClick = {
        onComplete(
          98,
          "Spool wound by Meseret on Meweria wheel with uniform tension."
        )
      },
      enabled = isFinished,
      colors = ButtonDefaults.buttonColors(containerColor = ShimenaCharcoal),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("complete_wind_stage_button")
    ) {
      Text("Complete Stage 04 & Mount Bobbin on Master Loom")
    }
  }
}
