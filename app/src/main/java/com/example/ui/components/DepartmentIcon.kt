package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

/**
 * Custom line-drawn icons for Shimena's 10 departments, constructed from the
 * exact same line weight as the continuous thread logo.
 */
@Composable
fun DepartmentIcon(
  stageId: Int,
  modifier: Modifier = Modifier,
  size: Dp = 28.dp,
  tint: Color = ShimenaCharcoal,
  strokeWidthDp: Dp = 2.dp
) {
  Canvas(modifier = modifier.size(size)) {
    val w = this.size.width
    val h = this.size.height
    val strokeWidth = strokeWidthDp.toPx()
    val style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)

    when (stageId) {
      1 -> {
        // 01 LAND: Cotton boll with sepals
        val path = Path().apply {
          // 3 rounded petals/boll segments
          moveTo(w * 0.35f, h * 0.45f)
          cubicTo(w * 0.2f, h * 0.25f, w * 0.5f, h * 0.15f, w * 0.5f, h * 0.35f)
          cubicTo(w * 0.5f, h * 0.15f, w * 0.8f, h * 0.25f, w * 0.65f, h * 0.45f)
          cubicTo(w * 0.85f, h * 0.55f, w * 0.7f, h * 0.8f, w * 0.5f, h * 0.75f)
          cubicTo(w * 0.3f, h * 0.8f, w * 0.15f, h * 0.55f, w * 0.35f, h * 0.45f)
          close()
          // Calyx base
          moveTo(w * 0.5f, h * 0.75f)
          lineTo(w * 0.5f, h * 0.92f)
          moveTo(w * 0.38f, h * 0.82f)
          lineTo(w * 0.5f, h * 0.75f)
          lineTo(w * 0.62f, h * 0.82f)
        }
        drawPath(path, color = tint, style = style)
      }
      2 -> {
        // 02 SPIN: Inzirt drop spindle with rotating thread whorl
        val path = Path().apply {
          // Central spindle shaft
          moveTo(w * 0.5f, h * 0.1f)
          lineTo(w * 0.5f, h * 0.9f)
          // Top hook
          moveTo(w * 0.5f, h * 0.1f)
          cubicTo(w * 0.58f, h * 0.05f, w * 0.65f, h * 0.12f, w * 0.58f, h * 0.18f)
          // Spindle clay whorl (disc)
          moveTo(w * 0.22f, h * 0.7f)
          lineTo(w * 0.78f, h * 0.7f)
          lineTo(w * 0.65f, h * 0.77f)
          lineTo(w * 0.35f, h * 0.77f)
          close()
          // Spiral thread wound around shaft
          moveTo(w * 0.38f, h * 0.45f)
          cubicTo(w * 0.5f, h * 0.38f, w * 0.62f, h * 0.52f, w * 0.5f, h * 0.6f)
        }
        drawPath(path, color = tint, style = style)
      }
      3 -> {
        // 03 COLOR: Traditional dye pot with rising botanical aroma/liquid
        val path = Path().apply {
          // Pot rim
          moveTo(w * 0.25f, h * 0.32f)
          lineTo(w * 0.75f, h * 0.32f)
          // Pot body
          moveTo(w * 0.28f, h * 0.32f)
          cubicTo(w * 0.12f, h * 0.6f, w * 0.2f, h * 0.88f, w * 0.5f, h * 0.88f)
          cubicTo(w * 0.8f, h * 0.88f, w * 0.88f, h * 0.6f, w * 0.72f, h * 0.32f)
          // Liquid surface line
          moveTo(w * 0.32f, h * 0.5f)
          cubicTo(w * 0.42f, h * 0.45f, w * 0.58f, h * 0.55f, w * 0.68f, h * 0.5f)
          // Botanical leaf rising
          moveTo(w * 0.5f, h * 0.32f)
          cubicTo(w * 0.42f, h * 0.18f, w * 0.58f, h * 0.12f, w * 0.5f, h * 0.1f)
        }
        drawPath(path, color = tint, style = style)
      }
      4 -> {
        // 04 WIND: Spooling bobbin / Meweria cone with wound threads
        val path = Path().apply {
          // Spool core
          moveTo(w * 0.28f, h * 0.25f)
          lineTo(w * 0.72f, h * 0.25f)
          moveTo(w * 0.32f, h * 0.25f)
          lineTo(w * 0.38f, h * 0.8f)
          moveTo(w * 0.68f, h * 0.25f)
          lineTo(w * 0.62f, h * 0.8f)
          moveTo(w * 0.25f, h * 0.8f)
          lineTo(w * 0.75f, h * 0.8f)
          // Yarn diagonal winding bands
          moveTo(w * 0.34f, h * 0.4f)
          lineTo(w * 0.66f, h * 0.48f)
          moveTo(w * 0.36f, h * 0.55f)
          lineTo(w * 0.64f, h * 0.63f)
        }
        drawPath(path, color = tint, style = style)
      }
      5 -> {
        // 05 WEAVE: Traditional wooden loom with shuttle & warp/weft cross
        val path = Path().apply {
          // Loom frame
          moveTo(w * 0.2f, h * 0.15f)
          lineTo(w * 0.2f, h * 0.85f)
          moveTo(w * 0.8f, h * 0.15f)
          lineTo(w * 0.8f, h * 0.85f)
          moveTo(w * 0.15f, h * 0.25f)
          lineTo(w * 0.85f, h * 0.25f)
          moveTo(w * 0.15f, h * 0.75f)
          lineTo(w * 0.85f, h * 0.75f)
          // Warp vertical threads
          moveTo(w * 0.35f, h * 0.25f)
          lineTo(w * 0.35f, h * 0.75f)
          moveTo(w * 0.5f, h * 0.25f)
          lineTo(w * 0.5f, h * 0.75f)
          moveTo(w * 0.65f, h * 0.25f)
          lineTo(w * 0.65f, h * 0.75f)
          // Shuttle passing through
          moveTo(w * 0.15f, h * 0.5f)
          lineTo(w * 0.85f, h * 0.5f)
        }
        drawPath(path, color = tint, style = style)
      }
      6 -> {
        // 06 SEW / FRINGE: Parallel fringe threads with finishing needle
        val path = Path().apply {
          // Textile hem edge
          moveTo(w * 0.15f, h * 0.35f)
          lineTo(w * 0.85f, h * 0.35f)
          moveTo(w * 0.15f, h * 0.42f)
          lineTo(w * 0.85f, h * 0.42f)
          // Parallel fringe tassels
          for (i in 0..4) {
            val fx = w * (0.22f + i * 0.14f)
            moveTo(fx, h * 0.42f)
            lineTo(fx, h * 0.75f)
            // Little tassel knot
            moveTo(fx - w * 0.03f, h * 0.75f)
            lineTo(fx + w * 0.03f, h * 0.75f)
            moveTo(fx, h * 0.75f)
            lineTo(fx, h * 0.88f)
          }
          // Needle crossing top
          moveTo(w * 0.2f, h * 0.15f)
          lineTo(w * 0.8f, h * 0.3f)
        }
        drawPath(path, color = tint, style = style)
      }
      7 -> {
        // 07 PRESS: Traditional tailor's iron with steam waves
        val path = Path().apply {
          // Iron soleplate & body
          moveTo(w * 0.18f, h * 0.75f)
          lineTo(w * 0.78f, h * 0.75f)
          lineTo(w * 0.88f, h * 0.55f)
          lineTo(w * 0.35f, h * 0.55f)
          lineTo(w * 0.18f, h * 0.75f)
          // Handle
          moveTo(w * 0.32f, h * 0.55f)
          lineTo(w * 0.32f, h * 0.35f)
          lineTo(w * 0.68f, h * 0.35f)
          lineTo(w * 0.78f, h * 0.55f)
          // Steam puffs
          moveTo(w * 0.3f, h * 0.85f)
          lineTo(w * 0.35f, h * 0.9f)
          moveTo(w * 0.5f, h * 0.85f)
          lineTo(w * 0.55f, h * 0.9f)
          moveTo(w * 0.7f, h * 0.85f)
          lineTo(w * 0.75f, h * 0.9f)
        }
        drawPath(path, color = tint, style = style)
      }
      8 -> {
        // 08 CHECK: Artisan hand holding craft inspection loupe / seal
        val path = Path().apply {
          // Loupe lens circle
          addOval(Rect(center = Offset(w * 0.45f, h * 0.42f), radius = w * 0.26f))
          // Loupe handle
          moveTo(w * 0.63f, h * 0.6f)
          lineTo(w * 0.85f, h * 0.82f)
          // Authentic checkmark inside lens
          moveTo(w * 0.34f, h * 0.42f)
          lineTo(w * 0.43f, h * 0.52f)
          lineTo(w * 0.58f, h * 0.32f)
        }
        drawPath(path, color = tint, style = style)
      }
      9 -> {
        // 09 CARE: Archival folded textile tied with ribbon
        val path = Path().apply {
          // Folded textile box/stack
          moveTo(w * 0.2f, h * 0.35f)
          lineTo(w * 0.8f, h * 0.35f)
          lineTo(w * 0.8f, h * 0.75f)
          lineTo(w * 0.2f, h * 0.75f)
          close()
          // Inner fold layer
          moveTo(w * 0.2f, h * 0.55f)
          lineTo(w * 0.8f, h * 0.55f)
          // Ribbon vertical
          moveTo(w * 0.5f, h * 0.22f)
          lineTo(w * 0.5f, h * 0.75f)
          // Ribbon bow at top
          moveTo(w * 0.5f, h * 0.22f)
          cubicTo(w * 0.38f, h * 0.12f, w * 0.38f, h * 0.28f, w * 0.5f, h * 0.22f)
          cubicTo(w * 0.62f, h * 0.12f, w * 0.62f, h * 0.28f, w * 0.5f, h * 0.22f)
        }
        drawPath(path, color = tint, style = style)
      }
      10 -> {
        // 10 SHARE: Continuous thread seal & living heritage certificate
        val path = Path().apply {
          // Certificate parchment
          moveTo(w * 0.25f, h * 0.18f)
          lineTo(w * 0.75f, h * 0.18f)
          lineTo(w * 0.75f, h * 0.82f)
          lineTo(w * 0.25f, h * 0.82f)
          close()
          // Document lines
          moveTo(w * 0.35f, h * 0.32f)
          lineTo(w * 0.65f, h * 0.32f)
          moveTo(w * 0.35f, h * 0.44f)
          lineTo(w * 0.65f, h * 0.44f)
          // Continuous thread looping medallion seal
          addOval(Rect(center = Offset(w * 0.5f, h * 0.62f), radius = w * 0.12f))
          moveTo(w * 0.5f, h * 0.74f)
          lineTo(w * 0.44f, h * 0.86f)
          moveTo(w * 0.5f, h * 0.74f)
          lineTo(w * 0.56f, h * 0.86f)
        }
        drawPath(path, color = tint, style = style)
      }
      else -> {
        drawCircle(color = tint, radius = w * 0.35f, style = style)
      }
    }
  }
}
