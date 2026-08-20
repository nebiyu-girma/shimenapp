package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ProductionStages
import com.example.ui.components.ArtisanCard
import com.example.ui.components.ContinuousThreadLine
import com.example.ui.components.TextileGridBackground
import com.example.ui.theme.*

/**
 * ArtisansGalleryScreen: "Meet the Hands" Documentary Exhibition.
 * Showcases the authentic profiles, quotes, and cultural wisdom of all 10 artisans.
 */
@Composable
fun ArtisansGalleryScreen(
  onSelectStage: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
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
            text = "DOCUMENTARY EXHIBITION",
            style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp),
            color = ShimenaTerracotta
          )
          Text(
            text = "Meet the Hands of Shimena",
            style = MaterialTheme.typography.displayMedium.copy(
              fontFamily = FontFamily.Serif,
              color = ShimenaCharcoal,
              fontSize = 24.sp
            )
          )
          Text(
            text = "Every textile is the collective achievement of ten distinct artisans working with ancestral knowledge across the Gamo Highlands.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondaryLight,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
          )

          ContinuousThreadLine(
            height = 16.dp,
            primaryColor = ShimenaIndigo,
            secondaryColor = ShimenaOchre,
            strokeWidth = 2.5f
          )
        }
      }

      // Feature Documentary Photo 1 (Women's Spinning Cooperative)
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = ShimenaCottonLight),
          border = CardDefaults.outlinedCardBorder()
        ) {
          Column {
            Image(
              painter = painterResource(id = R.drawable.img_spinning_cooperative),
              contentDescription = "Women's Spinning Cooperative in Arba Minch",
              modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
              contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "The Women's Spinning Cooperative",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontFamily = FontFamily.Serif,
                  fontWeight = FontWeight.Bold
                ),
                color = ShimenaCharcoal
              )
              Text(
                text = "Over 120 women artisans earn guaranteed fair-trade wages and healthcare through the continuous spinning of single-ply cotton thread.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondaryLight
              )
            }
          }
        }
      }

      // Feature Documentary Photo 2 (Master Pit Loom)
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = ShimenaCottonLight),
          border = CardDefaults.outlinedCardBorder()
        ) {
          Column {
            Image(
              painter = painterResource(id = R.drawable.img_master_loom_weaving),
              contentDescription = "Master Pit Loom Weaving in Gamo Highlands",
              modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
              contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "Master Pit Loom (Menbere) Heritage",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontFamily = FontFamily.Serif,
                  fontWeight = FontWeight.Bold
                ),
                color = ShimenaCharcoal
              )
              Text(
                text = "Hand-carved pit looms sunk into earthen floors provide the perfect micro-humidity for weaving intricate geometric Tibeb motifs without synthetic tension.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondaryLight
              )
            }
          }
        }
      }

      // Individual Artisan Cards (1 through 10)
      items(ProductionStages.stages) { stage ->
        ArtisanCard(
          stage = stage,
          showFullBio = true,
          onClick = { onSelectStage(stage.id) }
        )
      }
    }
  }
}
