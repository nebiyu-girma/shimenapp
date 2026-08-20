package com.example.ui.stages

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductionStages
import com.example.ui.theme.*
import kotlin.math.sin

data class DyeRecipe(
  val id: Int,
  val name: String,
  val botanicalSource: String,
  val color: Color,
  val hexString: String,
  val culturalMeaning: String
)

val availableDyes = listOf(
  DyeRecipe(
    id = 1,
    name = "Wild Gamo Indigo",
    botanicalSource = "Fermented Indigofera leaves & wood ash",
    color = ShimenaIndigo,
    hexString = "#315A67",
    culturalMeaning = "Represents the infinite sky and lakes of the Rift Valley"
  ),
  DyeRecipe(
    id = 2,
    name = "Madder Root & Bark",
    botanicalSource = "Crushed Rubia tinctorum & acacia bark",
    color = ShimenaDyeRed,
    hexString = "#A34D3D",
    culturalMeaning = "Symbolizes vitality, celebration, and earth energy"
  ),
  DyeRecipe(
    id = 3,
    name = "Rift Valley Ochre",
    botanicalSource = "Volcanic clay minerals & turmeric root",
    color = ShimenaOchre,
    hexString = "#C39345",
    culturalMeaning = "Honors the golden morning sun over Lake Abaya"
  ),
  DyeRecipe(
    id = 4,
    name = "Highland Leaf Green",
    botanicalSource = "Steeped guava leaves & wild rosemary",
    color = ShimenaLeaf,
    hexString = "#687254",
    culturalMeaning = "Evokes the lush terraced mountain slopes of Dorze"
  ),
  DyeRecipe(
    id = 5,
    name = "Lake Chamo Water Blue",
    botanicalSource = "Blended indigo wash & river clay",
    color = ShimenaLakeBlue,
    hexString = "#5E8790",
    culturalMeaning = "Serenity, balance, and fresh mountain springs"
  )
)

/**
 * 03 / COLOR — Botanical & Artisan Dyeing
 * Micro-game: Formulate natural dye baths, steep hand-spun skeins, and dry in the sun.
 */
@Composable
fun StageColorScreen(
  onComplete: (score: Int, notes: String, primaryColorHex: String, secondaryColorHex: String, recipeName: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedPrimaryIndex by remember { mutableIntStateOf(0) }
  var selectedSecondaryIndex by remember { mutableIntStateOf(2) }

  var steepingTime by remember { mutableFloatStateOf(0f) } // 0 to 1
  var isSteeping by remember { mutableStateOf(false) }
  var isSunDried by remember { mutableStateOf(false) }

  val primaryDye = availableDyes[selectedPrimaryIndex]
  val secondaryDye = availableDyes[selectedSecondaryIndex]

  val infiniteTransition = rememberInfiniteTransition(label = "vatBubbles")
  val bubblePhase by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = (Math.PI * 2).toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(1800, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "bubblePhase"
  )

  LaunchedEffect(isSteeping) {
    while (isSteeping && steepingTime < 1f) {
      kotlinx.coroutines.delay(100)
      steepingTime = (steepingTime + 0.05f).coerceAtMost(1f)
      if (steepingTime >= 1f) {
        isSteeping = false
      }
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "BOTANICAL DYE FORMULATION",
      style = MaterialTheme.typography.labelLarge,
      color = ShimenaIndigo
    )
    Text(
      text = "Select your natural botanical pigments, then immerse the hand-spun yarn skein into the warm clay dye pot.",
      style = MaterialTheme.typography.bodyMedium,
      color = TextSecondaryLight,
      modifier = Modifier.padding(top = 4.dp, bottom = 10.dp)
    )

    // Palette Selector Row
    Text(
      text = "1. Choose Primary & Border Accent Dyestuffs",
      style = MaterialTheme.typography.titleSmall,
      color = ShimenaCharcoal,
      modifier = Modifier.align(Alignment.Start)
    )

    Spacer(modifier = Modifier.height(6.dp))

    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      items(availableDyes) { dye ->
        val isPrimary = availableDyes[selectedPrimaryIndex].id == dye.id
        val isSecondary = availableDyes[selectedSecondaryIndex].id == dye.id

        Surface(
          modifier = Modifier
            .width(130.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
              if (!isPrimary) {
                selectedSecondaryIndex = selectedPrimaryIndex
                selectedPrimaryIndex = availableDyes.indexOf(dye)
                steepingTime = 0f
                isSunDried = false
              }
            }
            .border(
              width = if (isPrimary) 2.5.dp else if (isSecondary) 1.5.dp else 1.dp,
              color = if (isPrimary) ShimenaCharcoal else if (isSecondary) ShimenaEarth else ShimenaCottonDark,
              shape = RoundedCornerShape(12.dp)
            ),
          color = ShimenaCottonLight
        ) {
          Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(dye.color)
                .border(1.dp, Color.White, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              if (isPrimary) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(20.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = dye.name,
              style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
              color = ShimenaCharcoal,
              maxLines = 1
            )
            Text(
              text = if (isPrimary) "Primary Base" else if (isSecondary) "Border Accent" else "Select",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
              color = if (isPrimary) ShimenaIndigo else ShimenaEarthDark
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Interactive Clay Dye Pot Canvas
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .testTag("dye_vat_card"),
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(containerColor = ShimenaCottonLight),
      border = CardDefaults.outlinedCardBorder()
    ) {
      Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val w = size.width
          val h = size.height
          val centerX = w / 2f

          // 1. Warm clay pot background
          drawRoundRect(
            brush = Brush.verticalGradient(
              listOf(Color(0xFF8A5A36), Color(0xFF5A381F))
            ),
            topLeft = Offset(w * 0.15f, h * 0.25f),
            size = androidx.compose.ui.geometry.Size(w * 0.7f, h * 0.68f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(30f, 30f)
          )

          // 2. Liquid Vat Surface with active botanical color
          val liquidColor = primaryDye.color.copy(alpha = 0.85f + (steepingTime * 0.15f))
          drawRoundRect(
            color = liquidColor,
            topLeft = Offset(w * 0.2f, h * 0.32f),
            size = androidx.compose.ui.geometry.Size(w * 0.6f, h * 0.58f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(20f, 20f)
          )

          // 3. Floating Dye Bubbles
          for (i in 0..6) {
            val bx = w * (0.28f + (i * 0.08f))
            val by = h * 0.45f + sin(bubblePhase + i) * 14f
            drawCircle(
              color = Color.White.copy(alpha = 0.4f),
              radius = 8f,
              center = Offset(bx, by)
            )
          }

          // 4. Yarn Skein being submerged
          val skeinY = h * (0.12f + steepingTime * 0.28f)
          val yarnColor = if (steepingTime > 0.1f) {
            // Blending from raw cotton cream to saturated dye color
            primaryDye.color
          } else {
            ShimenaCotton
          }

          drawOval(
            color = yarnColor,
            topLeft = Offset(centerX - 60f, skeinY),
            size = androidx.compose.ui.geometry.Size(120f, 80f)
          )

          // Wooden dipping rod
          drawLine(
            color = ShimenaEarthLight,
            start = Offset(centerX - 70f, skeinY - 20f),
            end = Offset(centerX + 70f, skeinY - 20f),
            strokeWidth = 8f
          )
        }

        // Steeping Status Overlay
        Column(
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(14.dp)
            .background(ShimenaCharcoal.copy(alpha = 0.85f), RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "Formula: ${primaryDye.name} + ${secondaryDye.name}",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = ShimenaCottonLight
          )
          Text(
            text = "Saturation Depth: ${(steepingTime * 100).toInt()}% ${if (isSunDried) "• Sun-Cured" else ""}",
            style = MaterialTheme.typography.labelSmall,
            color = ShimenaGold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Interactive Action Buttons (Steep & Sun-Dry)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Button(
        onClick = { isSteeping = true },
        enabled = steepingTime < 1f && !isSteeping,
        colors = ButtonDefaults.buttonColors(containerColor = primaryDye.color),
        modifier = Modifier.weight(1f)
      ) {
        Icon(imageVector = Icons.Default.InvertColors, contentDescription = null)
        Spacer(modifier = Modifier.width(6.dp))
        Text(if (isSteeping) "Steeping..." else if (steepingTime >= 1f) "Saturated" else "Steep in Vat")
      }

      Button(
        onClick = { isSunDried = true },
        enabled = steepingTime >= 1f && !isSunDried,
        colors = ButtonDefaults.buttonColors(containerColor = ShimenaOchre),
        modifier = Modifier.weight(1f)
      ) {
        Icon(imageVector = Icons.Default.WbSunny, contentDescription = null)
        Spacer(modifier = Modifier.width(6.dp))
        Text(if (isSunDried) "Cured in Sun" else "Solar Dry Rack")
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Button(
      onClick = {
        onComplete(
          96,
          "Botanical dye bath formulated by Dawit with ${primaryDye.name} and ${secondaryDye.name}.",
          primaryDye.hexString,
          secondaryDye.hexString,
          "${primaryDye.name} & ${secondaryDye.name}"
        )
      },
      enabled = steepingTime >= 1f && isSunDried,
      colors = ButtonDefaults.buttonColors(containerColor = ShimenaCharcoal),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("complete_color_stage_button")
    ) {
      Text("Complete Stage 03 & Deliver Dyed Yarn to Winding")
    }
  }
}
