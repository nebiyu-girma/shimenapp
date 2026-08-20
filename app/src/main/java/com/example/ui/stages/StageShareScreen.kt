package com.example.ui.stages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ProductionStages
import com.example.data.TextileEntity
import com.example.ui.components.ContinuousThreadLine
import com.example.ui.components.DepartmentBadge
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 10 / SHARE — The Living Thread & Provenance
 * Final Stage: The customer / cultural custodian holds the complete continuous thread.
 * Displays the bespoke interactive textile, official Certificate of Provenance, and shareable story card.
 */
@Composable
fun StageShareScreen(
  primaryColorHex: String,
  secondaryColorHex: String,
  colorRecipeName: String,
  motifName: String,
  motifIndex: Int,
  overallScore: Int,
  onSaveToArchive: (TextileEntity) -> Unit,
  onRestartJourney: () -> Unit,
  modifier: Modifier = Modifier
) {
  val serialNumber = remember { "SHM-GM-" + (1000..9999).random() }
  val dateString = remember { SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date()) }
  var isSaved by remember { mutableStateOf(false) }
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Certificate, 1: Drape View, 2: Meet All 10 Hands

  val primaryColor = try {
    Color(android.graphics.Color.parseColor(primaryColorHex))
  } catch (e: Exception) {
    ShimenaIndigo
  }

  val secondaryColor = try {
    Color(android.graphics.Color.parseColor(secondaryColorHex))
  } catch (e: Exception) {
    ShimenaOchre
  }

  val textileEntity = remember {
    TextileEntity(
      serialNumber = serialNumber,
      title = "Gamo Living Heritage Scarf",
      motifName = motifName.ifEmpty { "Dorze Diamond Meskel" },
      motifPatternIndex = motifIndex,
      primaryColorHex = primaryColorHex,
      secondaryColorHex = secondaryColorHex,
      colorRecipeName = colorRecipeName.ifEmpty { "Wild Indigo & Mineral Ochre" },
      yarnPlyScore = 96,
      weavingTensionScore = 98,
      fringeKnotScore = 97,
      pressLusterScore = 99,
      overallScore = overallScore.coerceIn(94, 100),
      qualityGrade = "Heirloom Masterpiece Grade",
      cottonPurity = "100% Rainfed Arba Minch Organic Cotton",
      provenanceCertificate = "Handcrafted across 10 stages in Arba Minch, Ethiopia"
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Header
    Text(
      text = "10 / SHARE • THE LIVING THREAD",
      style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.5.sp),
      color = ShimenaGold
    )
    Text(
      text = "You are now the custodian of this textile's story",
      style = MaterialTheme.typography.displaySmall.copy(fontSize = 20.sp),
      color = ShimenaCharcoal,
      textAlign = TextAlign.Center,
      modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
    )

    ContinuousThreadLine(
      height = 24.dp,
      primaryColor = primaryColor,
      secondaryColor = secondaryColor,
      strokeWidth = 3f
    )

    Spacer(modifier = Modifier.height(10.dp))

    // Navigation Sub-tabs
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = ShimenaCottonLight,
      contentColor = ShimenaCharcoal,
      modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .border(1.dp, ShimenaCottonDark, RoundedCornerShape(12.dp))
    ) {
      Tab(
        selected = selectedTab == 0,
        onClick = { selectedTab = 0 },
        text = { Text("Provenance Certificate", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = selectedTab == 1,
        onClick = { selectedTab = 1 },
        text = { Text("Textile Drape", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = selectedTab == 2,
        onClick = { selectedTab = 2 },
        text = { Text("The 10 Hands", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
      )
    }

    Spacer(modifier = Modifier.height(14.dp))

    when (selectedTab) {
      0 -> {
        // TAB 0: CERTIFICATE OF PROVENANCE
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("certificate_card"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = ShimenaCotton),
          border = BorderStroke(2.dp, ShimenaGold.copy(alpha = 0.6f)),
          elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            // Certificate Seal Header
            Box(
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(ShimenaCharcoalDark)
                .border(2.dp, ShimenaGold, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = ShimenaGold,
                modifier = Modifier.size(32.dp)
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "SHIMENA ETHICAL TEXTILES",
              style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 2.sp),
              color = ShimenaCharcoal
            )
            Text(
              text = "Official Certificate of Provenance",
              style = MaterialTheme.typography.titleLarge.copy(fontFamily = FontFamily.Serif),
              color = ShimenaCharcoalDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "Serial No: $serialNumber • $dateString",
              style = MaterialTheme.typography.bodySmall,
              color = ShimenaEarthDark
            )

            Divider(
              modifier = Modifier.padding(vertical = 12.dp),
              color = ShimenaEarthLight.copy(alpha = 0.5f)
            )

            // Provenance Details Matrix
            Column(
              modifier = Modifier.fillMaxWidth(),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              ProvenanceRow("Origin", "Arba Minch Lowlands, Rift Valley, Ethiopia")
              ProvenanceRow("Material", "100% Rainfed Hand-Picked Organic Cotton")
              ProvenanceRow("Spinning", "Women's Guild • Inzirt Single-Ply Drop Spindle")
              ProvenanceRow("Natural Dye", colorRecipeName.ifEmpty { "Wild Indigo & Madder Root" })
              ProvenanceRow("Loom & Motif", "${motifName.ifEmpty { "Dorze Diamond Meskel" }} on Pit Loom")
              ProvenanceRow("Finishing", "Hand-Twisted Mefrecha Tassels & Steam Relaxed")
              ProvenanceRow("Craft Grade", "Masterpiece Tier • $overallScore% Audit Score")
            }

            Divider(
              modifier = Modifier.padding(vertical = 12.dp),
              color = ShimenaEarthLight.copy(alpha = 0.5f)
            )

            // Guild Seal Stamp & Statement
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "\"Crafted by 10 pairs of hands with living dignity and fair guild wages.\"",
                  style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic
                  ),
                  color = TextSecondaryLight
                )
              }

              Surface(
                shape = CircleShape,
                color = ShimenaDyeRed.copy(alpha = 0.15f),
                border = BorderStroke(1.5.dp, ShimenaDyeRed),
                modifier = Modifier.size(54.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Text(
                    text = "SEALED\n2026",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp, fontWeight = FontWeight.Bold),
                    color = ShimenaDyeRed,
                    textAlign = TextAlign.Center
                  )
                }
              }
            }
          }
        }
      }

      1 -> {
        // TAB 1: BESPOKE TEXTILE DRAPE & SHOWCASE
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("textile_showcase_card"),
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
              text = "YOUR CUSTOM WOVEN HEIRLOOM",
              style = MaterialTheme.typography.labelLarge,
              color = ShimenaTerracotta
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Canvas Textile Drape Rendering
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(ShimenaCharcoalSurface)
            ) {
              Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val centerX = w / 2f

                // Drape fold shadows
                drawRect(
                  brush = Brush.verticalGradient(
                    listOf(Color(0xFF282824), Color(0xFF181816))
                  )
                )

                // Draped Textile Main Body (Flowing organic folds)
                val fabricW = w * 0.72f
                val fabricX = (w - fabricW) / 2f

                drawRoundRect(
                  color = ShimenaCotton,
                  topLeft = Offset(fabricX, h * 0.08f),
                  size = androidx.compose.ui.geometry.Size(fabricW, h * 0.7f),
                  cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
                )

                // Woven Tibeb Border Stripes using player's chosen primary and secondary dyes
                drawRect(
                  color = primaryColor,
                  topLeft = Offset(fabricX, h * 0.52f),
                  size = androidx.compose.ui.geometry.Size(fabricW, 36f)
                )
                drawRect(
                  color = secondaryColor,
                  topLeft = Offset(fabricX, h * 0.59f),
                  size = androidx.compose.ui.geometry.Size(fabricW, 14f)
                )

                // Geometric Motif Points on the border
                for (i in 0..7) {
                  val mx = fabricX + 16f + i * (fabricW / 8f)
                  drawCircle(color = Color.White, radius = 5f, center = Offset(mx, h * 0.55f))
                }

                // Hand-Twisted Fringes at bottom
                for (i in 0..9) {
                  val fx = fabricX + 12f + i * (fabricW / 10f)
                  drawLine(
                    color = ShimenaEarthDark,
                    start = Offset(fx, h * 0.78f),
                    end = Offset(fx, h * 0.92f),
                    strokeWidth = 3.5f
                  )
                  drawCircle(color = ShimenaTerracotta, radius = 4f, center = Offset(fx, h * 0.88f))
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Photo comparison showcase
            Image(
              painter = painterResource(id = R.drawable.img_finished_shimena_textile),
              contentDescription = "Finished Shimena Handwoven Textile",
              modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp)),
              contentScale = ContentScale.Crop
            )
          }
        }
      }

      2 -> {
        // TAB 2: MEET ALL 10 HANDS (Full Collective Summary)
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          ProductionStages.stages.forEach { st ->
            Surface(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp),
              color = ShimenaCottonLight,
              border = BorderStroke(1.dp, ShimenaCottonDark)
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                DepartmentBadge(
                  stageCode = st.code,
                  stageName = st.department,
                  stageId = st.id,
                  accentColor = st.accentColor
                )

                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = st.artisanName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = ShimenaCharcoal
                  )
                  Text(
                    text = st.artisanRole,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondaryLight
                  )
                }
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // Action Buttons: Save to Archive & Weave Another
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Button(
        onClick = {
          onSaveToArchive(textileEntity)
          isSaved = true
        },
        enabled = !isSaved,
        colors = ButtonDefaults.buttonColors(containerColor = ShimenaGold),
        modifier = Modifier
          .weight(1f)
          .testTag("save_textile_archive_button")
      ) {
        Icon(
          imageVector = if (isSaved) Icons.Default.Check else Icons.Default.Bookmark,
          contentDescription = null,
          tint = ShimenaCharcoalDark
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          if (isSaved) "Saved in Archive" else "Save to Living Archive",
          color = ShimenaCharcoalDark,
          fontWeight = FontWeight.Bold
        )
      }

      OutlinedButton(
        onClick = onRestartJourney,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = ShimenaCharcoal),
        border = BorderStroke(1.5.dp, ShimenaCharcoal),
        modifier = Modifier.weight(1f)
      ) {
        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
        Spacer(modifier = Modifier.width(6.dp))
        Text("Weave Another")
      }
    }
  }
}

@Composable
fun ProvenanceRow(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.Top
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
      color = ShimenaEarthDark,
      modifier = Modifier.width(90.dp)
    )
    Text(
      text = value,
      style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
      color = ShimenaCharcoal,
      textAlign = TextAlign.End,
      modifier = Modifier.weight(1f)
    )
  }
}
