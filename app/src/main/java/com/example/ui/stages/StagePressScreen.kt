package com.example.ui.stages

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
 * 07 / PRESS — Traditional Ironing & Relaxation
 * Micro-game: Glide the heavy cast iron across the fabric zones with steam bursts to relax cotton fibers.
 */
@Composable
fun StagePressScreen(
  onComplete: (score: Int, notes: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var ironX by remember { mutableFloatStateOf(0.5f) }
  var ironY by remember { mutableFloatStateOf(0.5f) }
  var pressedCoverage by remember { mutableFloatStateOf(0f) } // 0 to 100%
  var isSteaming by remember { mutableStateOf(false) }

  val infiniteTransition = rememberInfiniteTransition(label = "steamPuffs")
  val steamAlpha by infiniteTransition.animateFloat(
    initialValue = 0.2f,
    targetValue = 0.9f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "steamAlpha"
  )

  val isFinished = pressedCoverage >= 100f

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "THERMAL STEAM RELAXATION",
      style = MaterialTheme.typography.labelLarge,
      color = ShimenaCharcoal
    )
    Text(
      text = "Glide the heavy cast iron smoothly over the woven fabric to relax the tight cotton crimps and unlock the signature cloud-soft hand-feel.",
      style = MaterialTheme.typography.bodyMedium,
      color = TextSecondaryLight,
      modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
    )

    // Interactive Pressing Table Canvas
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .testTag("iron_press_card"),
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
              ironX = (ironX + dragAmount.x / 400f).coerceIn(0.15f, 0.85f)
              ironY = (ironY + dragAmount.y / 400f).coerceIn(0.15f, 0.85f)

              val delta = abs(dragAmount.x) + abs(dragAmount.y)
              if (delta > 4f) {
                pressedCoverage = (pressedCoverage + delta * 0.15f).coerceAtMost(100f)
                isSteaming = true
              }
            }
          }
      ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val w = size.width
          val h = size.height

          // 1. Wooden Tailor's Pressing Board Table
          drawRoundRect(
            brush = Brush.verticalGradient(
              listOf(Color(0xFF8A5F3E), Color(0xFF6B4527))
            ),
            topLeft = Offset(w * 0.05f, h * 0.05f),
            size = Size(w * 0.9f, h * 0.9f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(24f, 24f)
          )

          // 2. Cotton Textile under Iron
          // Color shifts from slightly dull unpressed cream to radiant, lustrous soft cream
          val textileLuster = pressedCoverage / 100f
          val fabricColor = Color(
            red = (0.95f + textileLuster * 0.05f).coerceAtMost(1f),
            green = (0.92f + textileLuster * 0.06f).coerceAtMost(1f),
            blue = (0.86f + textileLuster * 0.08f).coerceAtMost(1f)
          )

          drawRoundRect(
            color = fabricColor,
            topLeft = Offset(w * 0.12f, h * 0.12f),
            size = Size(w * 0.76f, h * 0.76f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
          )

          // Decorative Tibeb Border on the fabric
          drawRect(
            color = ShimenaIndigo.copy(alpha = 0.85f),
            topLeft = Offset(w * 0.12f, h * 0.72f),
            size = Size(w * 0.76f, 22f)
          )
          drawRect(
            color = ShimenaOchre.copy(alpha = 0.85f),
            topLeft = Offset(w * 0.12f, h * 0.76f),
            size = Size(w * 0.76f, 8f)
          )

          // 3. Steam Clouds if steaming active
          if (isSteaming && pressedCoverage < 100f) {
            for (i in 0..4) {
              val sx = (ironX * w) + (i * 24f - 48f)
              val sy = (ironY * h) - (20f + i * 12f)
              drawCircle(
                color = Color.White.copy(alpha = steamAlpha * 0.6f),
                radius = 16f + i * 4f,
                center = Offset(sx, sy)
              )
            }
          }

          // 4. Heavy Cast-Iron Press (Positioned at ironX, ironY)
          val ix = ironX * w
          val iy = ironY * h
          val ironW = 90f
          val ironH = 50f

          // Iron Soleplate
          drawRoundRect(
            color = Color(0xFF222220),
            topLeft = Offset(ix - ironW / 2f, iy - ironH / 2f),
            size = Size(ironW, ironH),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f)
          )
          // Wooden handle
          drawRoundRect(
            color = ShimenaEarthLight,
            topLeft = Offset(ix - 24f, iy - ironH / 2f - 24f),
            size = Size(48f, 14f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
          )
        }

        // Instructional Guide Overlay
        if (pressedCoverage < 10f) {
          Box(
            modifier = Modifier
              .align(Alignment.BottomCenter)
              .padding(16.dp)
              .background(ShimenaCharcoal.copy(alpha = 0.85f), RoundedCornerShape(10.dp))
              .padding(horizontal = 16.dp, vertical = 8.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.TouchApp,
                contentDescription = null,
                tint = ShimenaGold,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Drag iron across fabric to press and steam",
                style = MaterialTheme.typography.bodySmall,
                color = ShimenaCottonLight
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Pressing Metrics
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Fiber Softness: ${pressedCoverage.toInt()}%",
          style = MaterialTheme.typography.titleMedium,
          color = ShimenaCharcoal
        )
        Text(
          text = if (isFinished) "★ Cloud Softness Achieved" else "Conditioning...",
          style = MaterialTheme.typography.titleMedium,
          color = if (isFinished) ShimenaLeaf else ShimenaTerracotta
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      LinearProgressIndicator(
        progress = { pressedCoverage / 100f },
        modifier = Modifier
          .fillMaxWidth()
          .height(10.dp)
          .clip(RoundedCornerShape(5.dp)),
        color = ShimenaCharcoal,
        trackColor = ShimenaCottonDark
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Button(
      onClick = {
        onComplete(
          99,
          "Conditioned & relaxed by Kassahun; natural cotton luster fully unlocked."
        )
      },
      enabled = isFinished,
      colors = ButtonDefaults.buttonColors(containerColor = ShimenaCharcoal),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("complete_press_stage_button")
    ) {
      Text("Complete Stage 07 & Submit to Quality Audit")
    }
  }
}
