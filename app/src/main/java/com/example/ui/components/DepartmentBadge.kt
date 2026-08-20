package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

/**
 * Standard department label formatted strictly with uppercase letter spacing,
 * department number, and signature icon.
 */
@Composable
fun DepartmentBadge(
  stageCode: String,
  stageName: String,
  stageId: Int,
  modifier: Modifier = Modifier,
  accentColor: Color = ShimenaTerracotta,
  isDark: Boolean = false
) {
  val bgColor = if (isDark) ShimenaCharcoalSurface else ShimenaCottonDark.copy(alpha = 0.5f)
  val textColor = if (isDark) ShimenaCottonLight else ShimenaCharcoal
  val borderColor = if (isDark) ShimenaCharcoalBorder else ShimenaEarthLight.copy(alpha = 0.5f)

  Row(
    modifier = modifier
      .clip(RoundedCornerShape(6.dp))
      .background(bgColor)
      .border(1.dp, borderColor, RoundedCornerShape(6.dp))
      .padding(horizontal = 10.dp, vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    DepartmentIcon(
      stageId = stageId,
      size = 18.dp,
      tint = accentColor,
      strokeWidthDp = 1.8.dp
    )

    Text(
      text = "$stageCode / $stageName",
      style = MaterialTheme.typography.labelMedium.copy(
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
        fontSize = 11.sp
      ),
      color = textColor
    )
  }
}
