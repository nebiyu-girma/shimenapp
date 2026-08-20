package com.example.ui.stages

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Redo
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

/**
 * 09 / CARE — Preservation & Archival Packaging
 * Micro-game: Methodical folding sequence, natural cedar protection, and tying Shimena's signature unbroken thread ribbon.
 */
@Composable
fun StageCareScreen(
  onComplete: (score: Int, notes: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var packagingStep by remember { mutableIntStateOf(1) } // 1: Fold, 2: Cedar, 3: Provenance Card, 4: Unbroken Thread Ribbon

  val isFinished = packagingStep >= 4

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "ARCHIVAL PRESERVATION",
      style = MaterialTheme.typography.labelLarge,
      color = ShimenaEarthDark
    )
    Text(
      text = "Fold the textile with precision, place cedar protection and the artisan provenance card, then tie Shimena's continuous unbroken ribbon.",
      style = MaterialTheme.typography.bodyMedium,
      color = TextSecondaryLight,
      modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
    )

    // Interactive Packaging Canvas Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .testTag("packaging_canvas_card"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = ShimenaCottonLight),
      border = CardDefaults.outlinedCardBorder()
    ) {
      Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val w = size.width
          val h = size.height

          // 1. Archival Craft Presentation Box (Kraft card)
          drawRoundRect(
            color = Color(0xFFDCC8AE),
            topLeft = Offset(w * 0.1f, h * 0.12f),
            size = Size(w * 0.8f, h * 0.72f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f)
          )

          // 2. Folded Cotton Textile inside Box
          val foldW = w * (if (packagingStep >= 1) 0.64f else 0.76f)
          val foldH = h * (if (packagingStep >= 1) 0.45f else 0.65f)
          val foldX = (w - foldW) / 2f
          val foldY = (h - foldH) / 2f

          drawRoundRect(
            color = ShimenaCotton,
            topLeft = Offset(foldX, foldY),
            size = Size(foldW, foldH),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f)
          )

          // Woven Tibeb Border stripe visible on top fold
          drawRect(
            color = ShimenaIndigo,
            topLeft = Offset(foldX, foldY + foldH * 0.35f),
            size = Size(foldW, 18f)
          )

          // 3. Cedar Shavings
          if (packagingStep >= 2) {
            for (i in 0..12) {
              val cx = foldX + 30f + (i * 37f) % (foldW - 60f)
              val cy = foldY + 20f + (i * 29f) % (foldH - 40f)
              drawCircle(color = ShimenaEarth, radius = 5f, center = Offset(cx, cy))
            }
          }

          // 4. Handwritten Artisan Provenance Card
          if (packagingStep >= 3) {
            drawRoundRect(
              color = Color.White,
              topLeft = Offset(w * 0.28f, h * 0.38f),
              size = Size(w * 0.44f, h * 0.22f),
              cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
            )
            // Lines on provenance card
            drawLine(
              color = ShimenaCharcoal,
              start = Offset(w * 0.34f, h * 0.44f),
              end = Offset(w * 0.66f, h * 0.44f),
              strokeWidth = 2f
            )
            drawLine(
              color = ShimenaTerracotta,
              start = Offset(w * 0.34f, h * 0.5f),
              end = Offset(w * 0.6f, h * 0.5f),
              strokeWidth = 2f
            )
          }

          // 5. Continuous Unbroken Thread Ribbon tying the box
          if (packagingStep >= 4) {
            // Vertical ribbon
            drawRect(
              color = ShimenaDyeRed,
              topLeft = Offset(w * 0.5f - 8f, h * 0.12f),
              size = Size(16f, h * 0.72f)
            )
            // Horizontal ribbon
            drawRect(
              color = ShimenaIndigo,
              topLeft = Offset(w * 0.1f, h * 0.48f - 8f),
              size = Size(w * 0.8f, 16f)
            )
            // Center Gold Wax Seal
            drawCircle(color = ShimenaGold, radius = 22f, center = Offset(w * 0.5f, h * 0.48f))
          }
        }

        // Active Step Bar
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
            Text(
              text = when (packagingStep) {
                1 -> "Step 1: Perform Geometric 4-Fold"
                2 -> "Step 2: Add Natural Aromatic Cedar Shavings"
                3 -> "Step 3: Insert Handcrafted Provenance Card"
                else -> "Step 4: Tie Unbroken Continuous Thread Ribbon"
              },
              style = MaterialTheme.typography.titleMedium,
              color = ShimenaCottonLight
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
              onClick = {
                if (packagingStep < 4) {
                  packagingStep++
                }
              },
              enabled = packagingStep < 4,
              colors = ButtonDefaults.buttonColors(containerColor = ShimenaGold),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                when (packagingStep) {
                  1 -> "Fold Textile"
                  2 -> "Add Cedar"
                  3 -> "Place Card"
                  else -> "Sealed with Continuous Thread"
                },
                color = ShimenaCharcoalDark,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Completion Button
    Button(
      onClick = {
        onComplete(
          99,
          "Methodically folded and tied with Shimena's continuous unbroken ribbon by Marta."
        )
      },
      enabled = isFinished,
      colors = ButtonDefaults.buttonColors(containerColor = ShimenaCharcoal),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("complete_care_stage_button")
    ) {
      Text("Complete Stage 09 & Reveal Living Thread (Stage 10)")
    }
  }
}
