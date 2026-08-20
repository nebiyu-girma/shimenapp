package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.*

/**
 * Textile Grid Canvas — Renders subtle tactile warp and weft weave crosshatching
 * to give every screen the organic feel of hand-spun Ethiopian cotton.
 */
@Composable
fun TextileGridBackground(
  modifier: Modifier = Modifier,
  gridAlpha: Float = 0.08f,
  content: @Composable () -> Unit
) {
  Box(modifier = modifier.fillMaxSize()) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val gridSpacing = 24f // Spacing in pixels
      val width = size.width
      val height = size.height
      val lineColor = ShimenaEarthDark.copy(alpha = gridAlpha)
      val accentLineColor = ShimenaTerracotta.copy(alpha = gridAlpha * 0.7f)

      // Vertical warp threads
      var x = 0f
      var colIndex = 0
      while (x <= width) {
        val color = if (colIndex % 6 == 0) accentLineColor else lineColor
        val stroke = if (colIndex % 6 == 0) 1.2f else 0.6f
        drawLine(
          color = color,
          start = Offset(x, 0f),
          end = Offset(x, height),
          strokeWidth = stroke
        )
        x += gridSpacing
        colIndex++
      }

      // Horizontal weft threads
      var y = 0f
      var rowIndex = 0
      while (y <= height) {
        val color = if (rowIndex % 6 == 0) accentLineColor else lineColor
        val stroke = if (rowIndex % 6 == 0) 1.2f else 0.6f
        drawLine(
          color = color,
          start = Offset(0f, y),
          end = Offset(width, y),
          strokeWidth = stroke
        )
        y += gridSpacing
        rowIndex++
      }
    }
    content()
  }
}
