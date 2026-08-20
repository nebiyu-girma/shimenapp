package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.StageInfo
import com.example.ui.theme.*

/**
 * "Meet the Hands" documentary card celebrating the real artisan,
 * their voice, tool, Amharic terminology, and cultural heritage.
 */
@Composable
fun ArtisanCard(
  stage: StageInfo,
  modifier: Modifier = Modifier,
  showFullBio: Boolean = true,
  onClick: (() -> Unit)? = null
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = ShimenaCottonLight
    ),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = Brush.linearGradient(
        listOf(
          ShimenaEarthLight.copy(alpha = 0.6f),
          ShimenaCottonDark
        )
      ),
      width = 1.dp
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      // Header: Department & Amharic term
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        DepartmentBadge(
          stageCode = stage.code,
          stageName = stage.department,
          stageId = stage.id,
          accentColor = stage.accentColor
        )

        Text(
          text = stage.amharicTerm,
          style = MaterialTheme.typography.titleMedium.copy(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
          ),
          color = ShimenaEarthDark
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Artisan Name & Role
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Artisan Avatar / Icon Circle
        Box(
          modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(stage.accentColor.copy(alpha = 0.15f))
            .border(1.5.dp, stage.accentColor, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          DepartmentIcon(
            stageId = stage.id,
            size = 28.dp,
            tint = stage.accentColor,
            strokeWidthDp = 2.dp
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = stage.artisanName,
            style = MaterialTheme.typography.titleLarge.copy(
              fontFamily = FontFamily.Serif,
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp
            ),
            color = ShimenaCharcoal
          )
          Text(
            text = stage.artisanRole,
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Medium
            ),
            color = ShimenaTerracotta
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Documentary Quote Block
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = ShimenaCottonDark.copy(alpha = 0.5f)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.FormatQuote,
            contentDescription = null,
            tint = stage.accentColor,
            modifier = Modifier.size(20.dp)
          )
          Text(
            text = "\"${stage.quote}\"",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontFamily = FontFamily.Serif,
              fontStyle = FontStyle.Italic,
              lineHeight = 20.sp
            ),
            color = ShimenaCharcoal
          )
        }
      }

      if (showFullBio) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = stage.artisanBio,
          style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
          color = TextSecondaryLight
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tool & Location Meta row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f)
          ) {
            Icon(
              imageVector = Icons.Default.PanTool,
              contentDescription = null,
              tint = ShimenaEarth,
              modifier = Modifier.size(14.dp)
            )
            Text(
              text = stage.toolUsed,
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
              color = ShimenaEarthDark,
              maxLines = 1
            )
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = null,
              tint = ShimenaEarth,
              modifier = Modifier.size(14.dp)
            )
            Text(
              text = stage.workshopLocation.split(",")[0],
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
              color = ShimenaEarthDark
            )
          }
        }
      }
    }
  }
}
