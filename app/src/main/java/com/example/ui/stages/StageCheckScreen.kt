package com.example.ui.stages

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductionStages
import com.example.ui.theme.*

class AuditCheckpoint(
  val id: Int,
  val title: String,
  val description: String,
  val x: Float,
  val y: Float,
  var verified: Boolean = false
)

/**
 * 08 / CHECK — Quality Control & Craft Audit
 * Micro-game: Audit 4 critical inspection points under natural southern light, then apply the Shimena Guild Seal.
 */
@Composable
fun StageCheckScreen(
  onComplete: (score: Int, notes: String) -> Unit,
  modifier: Modifier = Modifier
) {
  val checkpoints = remember {
    mutableStateListOf(
      AuditCheckpoint(1, "Selvedge Edge", "Straight, even woven borders without bowing", 0.2f, 0.25f),
      AuditCheckpoint(2, "Tibeb Symmetry", "Flawless geometric alignment of border motifs", 0.5f, 0.45f),
      AuditCheckpoint(3, "Fringe Knots", "Hand-twisted Mefrecha tassels uniformly tensioned", 0.5f, 0.78f),
      AuditCheckpoint(4, "Fiber Purity", "100% organic cotton free of synthetic defects", 0.8f, 0.3f)
    )
  }

  var selectedCheckpointIndex by remember { mutableIntStateOf(0) }
  var isSealApplied by remember { mutableStateOf(false) }

  val allVerified = checkpoints.all { it.verified }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "CRAFT QUALITY AUDIT",
      style = MaterialTheme.typography.labelLarge,
      color = ShimenaDyeRed
    )
    Text(
      text = "Inspect all 4 craft checkpoints using Sara's audit loupe, then stamp the official Shimena Guild Seal of Authenticity.",
      style = MaterialTheme.typography.bodyMedium,
      color = TextSecondaryLight,
      modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
    )

    // Interactive Quality Audit Table Canvas
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .testTag("audit_canvas_card"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = ShimenaCottonLight),
      border = CardDefaults.outlinedCardBorder()
    ) {
      Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val w = size.width
          val h = size.height

          // 1. Natural light inspection table surface
          drawRoundRect(
            color = Color(0xFFF7F2E9),
            topLeft = Offset(w * 0.05f, h * 0.05f),
            size = Size(w * 0.9f, h * 0.9f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(20f, 20f)
          )

          // 2. The Finished Textile on the Table
          drawRoundRect(
            color = ShimenaCotton,
            topLeft = Offset(w * 0.12f, h * 0.12f),
            size = Size(w * 0.76f, h * 0.76f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f)
          )

          // Tibeb border
          drawRect(
            color = ShimenaIndigo,
            topLeft = Offset(w * 0.12f, h * 0.4f),
            size = Size(w * 0.76f, 24f)
          )
          drawRect(
            color = ShimenaOchre,
            topLeft = Offset(w * 0.12f, h * 0.44f),
            size = Size(w * 0.76f, 8f)
          )

          // 3. Render Audit Points with glow & status
          checkpoints.forEachIndexed { index, cp ->
            val cx = cp.x * w
            val cy = cp.y * h
            val isCurrent = index == selectedCheckpointIndex

            val pointColor = if (cp.verified) ShimenaLeaf else if (isCurrent) ShimenaDyeRed else ShimenaEarth

            drawCircle(
              color = pointColor.copy(alpha = if (isCurrent) 0.35f else 0.15f),
              radius = 24f,
              center = Offset(cx, cy)
            )
            drawCircle(
              color = pointColor,
              radius = 8f,
              center = Offset(cx, cy)
            )
          }

          // 4. Stamped Guild Seal at bottom right when applied
          if (isSealApplied) {
            val sealX = w * 0.75f
            val sealY = h * 0.75f
            drawCircle(
              color = ShimenaDyeRed,
              radius = 36f,
              center = Offset(sealX, sealY)
            )
            drawCircle(
              color = ShimenaGold,
              radius = 32f,
              center = Offset(sealX, sealY),
              style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f)
            )
          }
        }

        // Active Checkpoint Info Card
        val activeCp = checkpoints[selectedCheckpointIndex]
        Surface(
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(14.dp),
          shape = RoundedCornerShape(14.dp),
          color = ShimenaCharcoal.copy(alpha = 0.94f)
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "${selectedCheckpointIndex + 1}/4: ${activeCp.title}",
                style = MaterialTheme.typography.titleMedium,
                color = ShimenaCottonLight
              )
              if (activeCp.verified) {
                Text(
                  text = "PASSED",
                  style = MaterialTheme.typography.labelSmall,
                  color = ShimenaLeaf
                )
              }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = activeCp.description,
              style = MaterialTheme.typography.bodySmall,
              color = TextSecondaryDark,
              modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Button(
                onClick = {
                  activeCp.verified = true
                  if (selectedCheckpointIndex < checkpoints.size - 1) {
                    selectedCheckpointIndex++
                  }
                },
                enabled = !activeCp.verified,
                colors = ButtonDefaults.buttonColors(containerColor = ShimenaDyeRed),
                modifier = Modifier.weight(1f)
              ) {
                Text(if (activeCp.verified) "✓ Verified" else "Inspect with Loupe")
              }

              if (allVerified) {
                Button(
                  onClick = { isSealApplied = true },
                  enabled = !isSealApplied,
                  colors = ButtonDefaults.buttonColors(containerColor = ShimenaGold),
                  modifier = Modifier.weight(1f)
                ) {
                  Text(if (isSealApplied) "✓ Seal Affixed" else "Affix Guild Seal")
                }
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Summary & Completion Button
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Audit: ${checkpoints.count { it.verified }} / 4 Verified",
        style = MaterialTheme.typography.titleMedium,
        color = ShimenaCharcoal
      )

      Button(
        onClick = {
          onComplete(
            100,
            "100% Quality certified by Sara; verified for selvedge straightness, Tibeb symmetry, and fringe durability."
          )
        },
        enabled = allVerified && isSealApplied,
        colors = ButtonDefaults.buttonColors(containerColor = ShimenaCharcoal),
        modifier = Modifier.testTag("complete_check_stage_button")
      ) {
        Text("Pass to Archival Packaging")
      }
    }
  }
}
