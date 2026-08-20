package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*
import kotlin.math.sin

/**
 * Animated Continuous Thread Line — Shimena's core unifying metaphor.
 * Gently flows across screens, undulating with organic harmonic motion.
 */
@Composable
fun ContinuousThreadLine(
  modifier: Modifier = Modifier,
  height: Dp = 32.dp,
  primaryColor: Color = ShimenaTerracotta,
  secondaryColor: Color = ShimenaGold,
  strokeWidth: Float = 4f,
  dashed: Boolean = false,
  amplitude: Float = 14f,
  frequency: Float = 2.5f
) {
  val infiniteTransition = rememberInfiniteTransition(label = "threadAnimation")
  val phase by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = (Math.PI * 2).toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 4000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "threadPhase"
  )

  Canvas(
    modifier = modifier
      .fillMaxWidth()
      .height(height)
  ) {
    val width = size.width
    val centerY = size.height / 2f
    val path = Path()

    val step = 4f
    var x = 0f
    var isFirst = true

    while (x <= width) {
      val normalizedX = (x / width) * (Math.PI.toFloat() * frequency * 2)
      val y = centerY + sin(normalizedX + phase) * amplitude

      if (isFirst) {
        path.moveTo(x, y)
        isFirst = false
      } else {
        path.lineTo(x, y)
      }
      x += step
    }

    val brush = Brush.horizontalGradient(
      colors = listOf(
        primaryColor.copy(alpha = 0.4f),
        secondaryColor,
        primaryColor,
        ShimenaIndigo.copy(alpha = 0.8f),
        secondaryColor
      )
    )

    drawPath(
      path = path,
      brush = brush,
      style = Stroke(
        width = strokeWidth,
        cap = StrokeCap.Round,
        pathEffect = if (dashed) PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f) else null
      )
    )
  }
}

/**
 * Vertical Continuous Thread Line connecting stages on timeline
 */
@Composable
fun VerticalThreadConnector(
  modifier: Modifier = Modifier,
  isCompleted: Boolean = false,
  isCurrent: Boolean = false,
  accentColor: Color = ShimenaTerracotta
) {
  val infiniteTransition = rememberInfiniteTransition(label = "connectorPulse")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseAlpha"
  )

  Canvas(modifier = modifier) {
    val startY = 0f
    val endY = size.height
    val centerX = size.width / 2f

    val color = when {
      isCompleted -> accentColor
      isCurrent -> accentColor.copy(alpha = pulseAlpha)
      else -> ShimenaCottonDark.copy(alpha = 0.7f)
    }

    drawLine(
      color = color,
      start = Offset(centerX, startY),
      end = Offset(centerX, endY),
      strokeWidth = if (isCurrent) 4.dp.toPx() else 2.5.dp.toPx(),
      cap = StrokeCap.Round,
      pathEffect = if (!isCompleted && !isCurrent) PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f) else null
    )
  }
}
