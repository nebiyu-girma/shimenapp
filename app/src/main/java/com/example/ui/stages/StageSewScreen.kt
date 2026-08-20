package com.example.ui.stages

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductionStages
import com.example.ui.theme.*

class FringeTassel(
  val id: Int,
  var isTwisted: Boolean = false,
  var isKnotted: Boolean = false
)

/**
 * 06 / SEW & FRINGE — Construction & Fringe Finishing
 * Micro-game: Hand-twist warp thread pairs into uniform Mefrecha tassels and tie secure heirloom knots.
 */
@Composable
fun StageSewScreen(
  onComplete: (score: Int, notes: String) -> Unit,
  modifier: Modifier = Modifier
) {
  val fringes = remember {
    mutableStateListOf(
      FringeTassel(1),
      FringeTassel(2),
      FringeTassel(3),
      FringeTassel(4),
      FringeTassel(5)
    )
  }

  var selectedFringeIndex by remember { mutableIntStateOf(0) }
  val completedFringesCount = fringes.count { it.isTwisted && it.isKnotted }
  val isFinished = completedFringesCount == fringes.size

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "MEFRECHA FRINGE TWISTING",
      style = MaterialTheme.typography.labelLarge,
      color = ShimenaLeaf
    )
    Text(
      text = "Select each warp bundle, roll with palm pressure to twist the double plies, then tie the finishing knot.",
      style = MaterialTheme.typography.bodyMedium,
      color = TextSecondaryLight,
      modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
    )

    // Interactive Fringe Canvas Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .testTag("fringe_canvas_card"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = ShimenaCottonLight),
      border = CardDefaults.outlinedCardBorder()
    ) {
      Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val w = size.width
          val h = size.height

          // 1. Handwoven Textile Body (Top)
          drawRect(
            color = ShimenaCotton,
            topLeft = Offset(w * 0.08f, 0f),
            size = androidx.compose.ui.geometry.Size(w * 0.84f, h * 0.4f)
          )

          // Border Tibeb stripe
          drawRect(
            color = ShimenaIndigo,
            topLeft = Offset(w * 0.08f, h * 0.32f),
            size = androidx.compose.ui.geometry.Size(w * 0.84f, 18f)
          )

          // Hem Fold line
          drawLine(
            color = ShimenaEarthDark,
            start = Offset(w * 0.08f, h * 0.4f),
            end = Offset(w * 0.92f, h * 0.4f),
            strokeWidth = 3f
          )

          // 2. Hanging Warp Fringe Tassels
          val fringeSpacing = (w * 0.84f) / fringes.size
          fringes.forEachIndexed { index, fringe ->
            val fx = w * 0.08f + (index + 0.5f) * fringeSpacing
            val isCurrent = index == selectedFringeIndex

            // Selection glow
            if (isCurrent) {
              drawCircle(
                color = ShimenaLeaf.copy(alpha = 0.2f),
                radius = 28f,
                center = Offset(fx, h * 0.6f)
              )
            }

            if (fringe.isTwisted) {
              // Tightly twisted spiral tassel
              val path = Path().apply {
                moveTo(fx, h * 0.4f)
                cubicTo(fx - 4f, h * 0.5f, fx + 4f, h * 0.6f, fx, h * 0.72f)
              }
              drawPath(
                path,
                color = ShimenaEarthDark,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.5f)
              )

              if (fringe.isKnotted) {
                // Knot ball
                drawCircle(color = ShimenaTerracotta, radius = 6f, center = Offset(fx, h * 0.72f))
                // Bottom skirt fringe
                drawLine(
                  color = ShimenaCottonDark,
                  start = Offset(fx, h * 0.72f),
                  end = Offset(fx, h * 0.88f),
                  strokeWidth = 3f
                )
              }
            } else {
              // Loose parallel raw warp threads
              drawLine(
                color = ShimenaCottonDark,
                start = Offset(fx - 4f, h * 0.4f),
                end = Offset(fx - 6f, h * 0.75f),
                strokeWidth = 2f
              )
              drawLine(
                color = ShimenaCottonDark,
                start = Offset(fx + 4f, h * 0.4f),
                end = Offset(fx + 6f, h * 0.75f),
                strokeWidth = 2f
              )
            }
          }
        }

        // Action panel at bottom of card
        val currentFringe = fringes[selectedFringeIndex]
        Surface(
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(14.dp),
          shape = RoundedCornerShape(14.dp),
          color = ShimenaCharcoal.copy(alpha = 0.92f)
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "Tassel #${selectedFringeIndex + 1} of ${fringes.size}",
              style = MaterialTheme.typography.titleMedium,
              color = ShimenaCottonLight
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Button(
                onClick = {
                  currentFringe.isTwisted = true
                },
                enabled = !currentFringe.isTwisted,
                colors = ButtonDefaults.buttonColors(containerColor = ShimenaLeaf),
                modifier = Modifier.weight(1f)
              ) {
                Text(if (currentFringe.isTwisted) "✓ Twisted" else "1. Palm Roll Twist")
              }

              Button(
                onClick = {
                  currentFringe.isKnotted = true
                  if (selectedFringeIndex < fringes.size - 1) {
                    selectedFringeIndex++
                  }
                },
                enabled = currentFringe.isTwisted && !currentFringe.isKnotted,
                colors = ButtonDefaults.buttonColors(containerColor = ShimenaTerracotta),
                modifier = Modifier.weight(1f)
              ) {
                Text(if (currentFringe.isKnotted) "✓ Sealed" else "2. Tie Knot")
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Tassel Selector & Progress
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Fringes Sealed: $completedFringesCount / ${fringes.size}",
        style = MaterialTheme.typography.titleMedium,
        color = ShimenaCharcoal
      )

      Button(
        onClick = {
          onComplete(
            97,
            "Hand-twisted fringes finished by Zenebech & Genet with reinforced heirloom tassels."
          )
        },
        enabled = isFinished,
        colors = ButtonDefaults.buttonColors(containerColor = ShimenaCharcoal),
        modifier = Modifier.testTag("complete_sew_stage_button")
      ) {
        Text("Pass to Master Presser")
      }
    }
  }
}
