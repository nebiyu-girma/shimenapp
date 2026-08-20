package com.example.ui.stages

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CheckCircle
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
import com.example.ui.theme.*
import kotlin.math.abs
import kotlin.math.sin

/**
 * 02 / SPIN — Women's Spinning Cooperatives
 * Micro-game: Balance the Inzirt drop spindle rotation and thread draw to spin uniform yarn.
 */
@Composable
fun StageSpinScreen(
  onComplete: (score: Int, notes: String) -> Unit,
  modifier: Modifier = Modifier
) {
  val stage = ProductionStages.stages[1]

  // Spinning physics state
  var spindleRotationSpeed by remember { mutableFloatStateOf(0f) }
  var totalThreadSpun by remember { mutableFloatStateOf(0f) } // 0f to 100f
  var yarnUniformityScore by remember { mutableIntStateOf(92) }
  var isSpinningActive by remember { mutableStateOf(false) }

  // Rotation animation
  val infiniteTransition = rememberInfiniteTransition(label = "spindleSpin")
  val spinAngle by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(
        durationMillis = (1000 / (spindleRotationSpeed.coerceAtLeast(0.2f))).toInt(),
        easing = LinearEasing
      ),
      repeatMode = RepeatMode.Restart
    ),
    label = "spinAngle"
  )

  // Target sweet spot is speed between 1.5f and 3.5f
  val inSweetSpot = spindleRotationSpeed in 1.2f..3.8f

  LaunchedEffect(isSpinningActive, spindleRotationSpeed) {
    while (isSpinningActive && spindleRotationSpeed > 0.1f) {
      kotlinx.coroutines.delay(100)
      if (inSweetSpot) {
        totalThreadSpun = (totalThreadSpun + 1.2f).coerceAtMost(100f)
      } else {
        totalThreadSpun = (totalThreadSpun + 0.4f).coerceAtMost(100f)
        if (spindleRotationSpeed > 4.5f) {
          yarnUniformityScore = (yarnUniformityScore - 1).coerceAtLeast(80)
        }
      }
      // Natural friction decay
      spindleRotationSpeed = (spindleRotationSpeed - 0.05f).coerceAtLeast(0f)
      if (spindleRotationSpeed <= 0.1f) {
        isSpinningActive = false
      }
    }
  }

  val isFinished = totalThreadSpun >= 100f

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "THE INZIRT DROP SPINDLE",
      style = MaterialTheme.typography.labelLarge,
      color = ShimenaOchre
    )
    Text(
      text = "Flick or drag in upward circular motion to spin the Inzirt. Keep speed in the green sweet spot to spin strong, even yarn.",
      style = MaterialTheme.typography.bodyMedium,
      color = TextSecondaryLight,
      modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
    )

    // Interactive Inzirt Canvas Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .testTag("spindle_interactive_card"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = ShimenaCottonLight),
      border = CardDefaults.outlinedCardBorder()
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .pointerInput(Unit) {
            detectDragGestures(
              onDragStart = { isSpinningActive = true },
              onDrag = { change, dragAmount ->
                change.consume()
                val delta = abs(dragAmount.y) + abs(dragAmount.x)
                if (delta > 8f) {
                  spindleRotationSpeed = (spindleRotationSpeed + delta * 0.04f).coerceIn(0f, 5.5f)
                  isSpinningActive = true
                }
              }
            )
          }
      ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val w = size.width
          val h = size.height
          val centerX = w / 2f

          // 1. Raw Cotton Rolag at Top
          drawOval(
            brush = Brush.radialGradient(
              listOf(Color.White, ShimenaCottonDark),
              center = Offset(centerX, h * 0.12f),
              radius = 90f
            ),
            topLeft = Offset(centerX - 90f, h * 0.04f),
            size = androidx.compose.ui.geometry.Size(180f, 80f)
          )

          // 2. Drawn Thread flowing down from Rolag to Spindle
          val threadLength = h * 0.45f
          val threadWiggle = sin(spinAngle * 0.05f) * (if (isSpinningActive) 8f else 1f)
          val path = Path().apply {
            moveTo(centerX, h * 0.14f)
            quadraticTo(
              centerX + threadWiggle,
              h * 0.3f,
              centerX,
              h * 0.45f
            )
          }
          drawLine(
            color = ShimenaCotton,
            start = Offset(centerX, h * 0.14f),
            end = Offset(centerX, h * 0.45f),
            strokeWidth = if (inSweetSpot) 3.5f else 5.5f
          )

          // 3. Drop Spindle Shaft (Vertical wooden pin)
          val spindleTopY = h * 0.45f
          val spindleBottomY = h * 0.88f
          drawLine(
            color = ShimenaEarthDark,
            start = Offset(centerX, spindleTopY),
            end = Offset(centerX, spindleBottomY),
            strokeWidth = 6f
          )

          // 4. Wound Yarn Core building up on spindle shaft
          val woundRadius = 14f + (totalThreadSpun * 0.35f)
          drawOval(
            brush = Brush.horizontalGradient(
              listOf(ShimenaCottonDark, Color.White, ShimenaCottonDark)
            ),
            topLeft = Offset(centerX - woundRadius, h * 0.55f),
            size = androidx.compose.ui.geometry.Size(woundRadius * 2, 70f)
          )

          // 5. Clay Spindle Whorl (Disc) spinning
          val discW = 90f + sin(spinAngle * Math.PI.toFloat() / 180f) * 10f
          drawOval(
            color = ShimenaTerracotta,
            topLeft = Offset(centerX - discW / 2f, h * 0.76f),
            size = androidx.compose.ui.geometry.Size(discW, 22f)
          )
        }

        // Speed Gauge Indicator Overlay
        Column(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(16.dp)
            .background(ShimenaCotton.copy(alpha = 0.9f), RoundedCornerShape(12.dp))
            .border(1.dp, ShimenaEarthLight, RoundedCornerShape(12.dp))
            .padding(12.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "SPINDLE CADENCE",
            style = MaterialTheme.typography.labelSmall,
            color = ShimenaEarthDark
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = when {
              spindleRotationSpeed < 0.2f -> "Resting (Swipe to spin)"
              spindleRotationSpeed < 1.2f -> "Too Slow (Loose ply)"
              inSweetSpot -> "★ Perfect Artisan Tension"
              else -> "Too Fast (Risk of snap!)"
            },
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = when {
              inSweetSpot -> ShimenaLeaf
              spindleRotationSpeed > 3.8f -> ShimenaDyeRed
              else -> ShimenaOchre
            }
          )
        }

        if (totalThreadSpun < 10f && !isSpinningActive) {
          Box(
            modifier = Modifier
              .align(Alignment.Center)
              .background(ShimenaCharcoal.copy(alpha = 0.85f), RoundedCornerShape(10.dp))
              .padding(horizontal = 16.dp, vertical = 10.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.TouchApp,
                contentDescription = null,
                tint = ShimenaGold,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Flick upward to start the spindle spinning",
                style = MaterialTheme.typography.bodyMedium,
                color = ShimenaCottonLight
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Spun Yarn Skein Progress
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Thread Spun: ${totalThreadSpun.toInt()}%",
          style = MaterialTheme.typography.titleMedium,
          color = ShimenaCharcoal
        )
        Text(
          text = "Ply Uniformity: $yarnUniformityScore%",
          style = MaterialTheme.typography.titleMedium,
          color = if (yarnUniformityScore >= 90) ShimenaLeaf else ShimenaOchre
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      LinearProgressIndicator(
        progress = { totalThreadSpun / 100f },
        modifier = Modifier
          .fillMaxWidth()
          .height(10.dp)
          .clip(RoundedCornerShape(5.dp)),
        color = ShimenaOchre,
        trackColor = ShimenaCottonDark
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Button(
      onClick = {
        onComplete(
          yarnUniformityScore,
          "Spun by Tigist's cooperative; high-tensile single-ply with $yarnUniformityScore% uniformity."
        )
      },
      enabled = isFinished,
      colors = ButtonDefaults.buttonColors(containerColor = ShimenaCharcoal),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("complete_spin_stage_button")
    ) {
      Text("Complete Stage 02 & Pass Skein to Dyemaster")
    }
  }
}
