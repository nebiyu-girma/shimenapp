package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ContinuousThreadLine
import com.example.ui.components.TextileGridBackground
import com.example.ui.theme.*
import com.example.viewmodel.ShimenaViewModel

/**
 * AboutShimenaScreen: Brand Manifesto, Values, and Ethical Commitment.
 */
@Composable
fun AboutShimenaScreen(
  viewModel: ShimenaViewModel,
  onResetJourney: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showResetDialog by remember { mutableStateOf(false) }

  TextileGridBackground(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
    ) {
      item {
        Column {
          Text(
            text = "OUR MANIFESTO",
            style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp),
            color = ShimenaTerracotta
          )
          Text(
            text = "The Human Hand in Every Thread",
            style = MaterialTheme.typography.displayMedium.copy(
              fontFamily = FontFamily.Serif,
              color = ShimenaCharcoal,
              fontSize = 24.sp
            )
          )
          Text(
            text = "Shimena is not merely a brand—it is a cultural preservation ecosystem dedicated to honoring the ancestral textile masters of Arba Minch and the Gamo Highlands.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondaryLight,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
          )

          ContinuousThreadLine(
            height = 16.dp,
            primaryColor = ShimenaTerracotta,
            secondaryColor = ShimenaIndigo,
            strokeWidth = 2.5f
          )
        }
      }

      // Brand Pillars
      item {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          BrandPillarCard(
            title = "1. Ancestral Craftsmanship",
            description = "We preserve centuries-old Gamo pit loom (Menbere) weaving, drop spindle (Inzirt) spinning, and natural botanical dyeing without industrial compromises.",
            icon = Icons.Default.Handyman,
            accentColor = ShimenaTerracotta
          )

          BrandPillarCard(
            title = "2. Radical Fair Wages & Dignity",
            description = "Our women's spinning cooperative unites over 120 artisan mothers with living wages, healthcare, and educational stipends for their children.",
            icon = Icons.Default.VolunteerActivism,
            accentColor = ShimenaLeaf
          )

          BrandPillarCard(
            title = "3. 100% Rainfed Organic Cotton",
            description = "Sourced directly from smallholder regenerative farmers in the Lake Abaya and Chamo lowlands—naturally pesticide-free and hand-ginned.",
            icon = Icons.Default.Eco,
            accentColor = ShimenaEarth
          )

          BrandPillarCard(
            title = "4. Pure Botanical Dye Baths",
            description = "Formulated from wild Gamo indigo leaves, madder root, mineral volcanic ochre, and acacia tree bark—zero synthetic petroleum dyes.",
            icon = Icons.Default.InvertColors,
            accentColor = ShimenaIndigo
          )

          BrandPillarCard(
            title = "5. The Unbroken Continuous Thread",
            description = "Every textile is traceable through 10 distinct artisan hands, culminating in an authenticated Certificate of Provenance.",
            icon = Icons.Default.LinearScale,
            accentColor = ShimenaGold
          )
        }
      }

      // Quote Callout
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = ShimenaCottonLight),
          border = BorderStroke(1.dp, ShimenaGold.copy(alpha = 0.6f))
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            Text(
              text = "\"When you wear a Shimena textile, you wrap yourself in the living prayer, breath, and laughter of ten artisan masters.\"",
              style = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic
              ),
              color = ShimenaCharcoal
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "— Shimena Artisan Collective, Arba Minch",
              style = MaterialTheme.typography.labelSmall,
              color = ShimenaEarthDark
            )
          }
        }
      }

      // Reset Journey Option
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = ShimenaCottonLight),
          border = CardDefaults.outlinedCardBorder()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "Replay the 10-Stage Storyteller Journey",
              style = MaterialTheme.typography.titleSmall,
              color = ShimenaCharcoal
            )
            Text(
              text = "Reset all stage locks to experience the production chain again from Stage 01 (LAND).",
              style = MaterialTheme.typography.bodySmall,
              color = TextSecondaryLight,
              modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )
            OutlinedButton(
              onClick = { showResetDialog = true },
              colors = ButtonDefaults.outlinedButtonColors(contentColor = ShimenaDyeRed),
              border = BorderStroke(1.dp, ShimenaDyeRed)
            ) {
              Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null)
              Spacer(modifier = Modifier.width(6.dp))
              Text("Reset Storyteller Progress")
            }
          }
        }
      }
    }
  }

  if (showResetDialog) {
    AlertDialog(
      onDismissRequest = { showResetDialog = false },
      title = { Text("Reset Story Progress?") },
      text = { Text("This will unlock Stage 01 (LAND) and reset your current in-progress textile. Your saved archive pieces will remain safe.") },
      confirmButton = {
        Button(
          onClick = {
            viewModel.resetJourney()
            showResetDialog = false
            onResetJourney()
          },
          colors = ButtonDefaults.buttonColors(containerColor = ShimenaDyeRed)
        ) {
          Text("Reset Progress")
        }
      },
      dismissButton = {
        TextButton(onClick = { showResetDialog = false }) {
          Text("Cancel")
        }
      },
      containerColor = ShimenaCottonLight
    )
  }
}

@Composable
fun BrandPillarCard(
  title: String,
  description: String,
  icon: ImageVector,
  accentColor: Color
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = ShimenaCottonLight),
    border = CardDefaults.outlinedCardBorder()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.Top,
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = accentColor.copy(alpha = 0.15f),
        modifier = Modifier.size(44.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(24.dp)
          )
        }
      }

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium.copy(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold
          ),
          color = ShimenaCharcoal
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = description,
          style = MaterialTheme.typography.bodySmall,
          color = TextSecondaryLight
        )
      }
    }
  }
}
