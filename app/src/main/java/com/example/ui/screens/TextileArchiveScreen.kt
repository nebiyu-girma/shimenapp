package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TextileEntity
import com.example.ui.components.ContinuousThreadLine
import com.example.ui.components.TextileGridBackground
import com.example.ui.stages.ProvenanceRow
import com.example.ui.theme.*
import com.example.viewmodel.ShimenaViewModel

/**
 * TextileArchiveScreen: Living Archive of Crafted Heirlooms.
 * Shows player's saved textiles, serial numbers, audit grades, and certificates.
 */
@Composable
fun TextileArchiveScreen(
  viewModel: ShimenaViewModel,
  onStartNewJourney: () -> Unit,
  modifier: Modifier = Modifier
) {
  val textiles by viewModel.craftedTextiles.collectAsState()
  var selectedTextileForDialog by remember { mutableStateOf<TextileEntity?>(null) }

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
            text = "LIVING ARCHIVE",
            style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp),
            color = ShimenaIndigo
          )
          Text(
            text = "Your Handcrafted Heirlooms",
            style = MaterialTheme.typography.displayMedium.copy(
              fontFamily = FontFamily.Serif,
              color = ShimenaCharcoal,
              fontSize = 24.sp
            )
          )
          Text(
            text = "Every piece in your archive carries the indelible signature of the 10 artisans who spun, dyed, and wove it into existence.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondaryLight,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
          )

          ContinuousThreadLine(
            height = 16.dp,
            primaryColor = ShimenaIndigo,
            secondaryColor = ShimenaGold,
            strokeWidth = 2.5f
          )
        }
      }

      if (textiles.isEmpty()) {
        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ShimenaCottonLight),
            border = CardDefaults.outlinedCardBorder()
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Icon(
                imageVector = Icons.Default.BookmarkBorder,
                contentDescription = null,
                tint = ShimenaEarthDark,
                modifier = Modifier.size(48.dp)
              )
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = "No Heirlooms in Archive Yet",
                style = MaterialTheme.typography.titleMedium.copy(fontFamily = FontFamily.Serif),
                color = ShimenaCharcoal
              )
              Text(
                text = "Complete the 10 production stages from LAND to SHARE to weave and certify your first authentic textile.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondaryLight,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
              )
              Button(
                onClick = onStartNewJourney,
                colors = ButtonDefaults.buttonColors(containerColor = ShimenaCharcoal)
              ) {
                Text("Start the Journey (Stage 01)")
              }
            }
          }
        }
      } else {
        items(textiles) { textile ->
          val primaryColor = try {
            Color(android.graphics.Color.parseColor(textile.primaryColorHex))
          } catch (e: Exception) {
            ShimenaIndigo
          }

          val secondaryColor = try {
            Color(android.graphics.Color.parseColor(textile.secondaryColorHex))
          } catch (e: Exception) {
            ShimenaOchre
          }

          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(16.dp))
              .clickable { selectedTextileForDialog = textile }
              .testTag("archive_item_${textile.serialNumber}"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ShimenaCottonLight),
            border = BorderStroke(1.5.dp, primaryColor.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(24.dp)
                      .clip(CircleShape)
                      .background(primaryColor)
                  )
                  Box(
                    modifier = Modifier
                      .size(24.dp)
                      .clip(CircleShape)
                      .background(secondaryColor)
                  )
                  Text(
                    text = textile.serialNumber,
                    style = MaterialTheme.typography.labelMedium.copy(
                      fontWeight = FontWeight.Bold,
                      letterSpacing = 1.sp
                    ),
                    color = ShimenaCharcoal
                  )
                }

                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = ShimenaLeaf.copy(alpha = 0.15f)
                ) {
                  Text(
                    text = "${textile.overallScore}% Grade",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = ShimenaLeaf,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = textile.title,
                style = MaterialTheme.typography.titleMedium.copy(
                  fontFamily = FontFamily.Serif,
                  fontWeight = FontWeight.Bold
                ),
                color = ShimenaCharcoalDark
              )

              Text(
                text = "Motif: ${textile.motifName} • Palette: ${textile.colorRecipeName}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondaryLight
              )

              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Tap to view Certificate of Provenance",
                  style = MaterialTheme.typography.labelSmall,
                  color = ShimenaIndigo
                )

                IconButton(
                  onClick = { viewModel.deleteTextile(textile) },
                  modifier = Modifier.size(28.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete",
                    tint = TextSecondaryLight,
                    modifier = Modifier.size(18.dp)
                  )
                }
              }
            }
          }
        }
      }
    }
  }

  // Certificate Modal Dialog
  selectedTextileForDialog?.let { textile ->
    val primaryColor = try {
      Color(android.graphics.Color.parseColor(textile.primaryColorHex))
    } catch (e: Exception) {
      ShimenaIndigo
    }

    val secondaryColor = try {
      Color(android.graphics.Color.parseColor(textile.secondaryColorHex))
    } catch (e: Exception) {
      ShimenaOchre
    }

    AlertDialog(
      onDismissRequest = { selectedTextileForDialog = null },
      title = {
        Text(
          text = "Certificate of Provenance",
          style = MaterialTheme.typography.titleLarge.copy(fontFamily = FontFamily.Serif),
          color = ShimenaCharcoal
        )
      },
      text = {
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = "Serial: ${textile.serialNumber}",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = ShimenaEarthDark
          )
          Divider(color = ShimenaEarthLight.copy(alpha = 0.4f))
          ProvenanceRow("Textile", textile.title)
          ProvenanceRow("Motif", textile.motifName)
          ProvenanceRow("Dye Recipe", textile.colorRecipeName)
          ProvenanceRow("Cotton", textile.cottonPurity)
          ProvenanceRow("Craft Score", "${textile.overallScore}% (${textile.qualityGrade})")
          ProvenanceRow("Origin", "Gamo Highlands & Arba Minch, Ethiopia")
          Divider(color = ShimenaEarthLight.copy(alpha = 0.4f))
          Text(
            text = "Sealed and verified by Shimena's 10 Master Artisans.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondaryLight
          )
        }
      },
      confirmButton = {
        Button(
          onClick = { selectedTextileForDialog = null },
          colors = ButtonDefaults.buttonColors(containerColor = ShimenaCharcoal)
        ) {
          Text("Close")
        }
      },
      containerColor = ShimenaCottonLight
    )
  }
}
