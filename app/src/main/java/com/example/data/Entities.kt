package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stage_progress")
data class StageProgressEntity(
  @PrimaryKey val stageId: Int, // 1 to 10
  val stageCode: String,        // "01", "02", ..., "10"
  val stageName: String,        // "LAND", "SPIN", "COLOR", etc.
  val title: String,            // "Cotton Farming", "Spinning Cooperative", etc.
  val artisanName: String,      // "Abebe & Farm Collective", etc.
  val artisanRole: String,      // "Gamo Cotton Growers", etc.
  val unlocked: Boolean = false,
  val completed: Boolean = false,
  val qualityScore: Int = 0,    // 0 to 100
  val completionCount: Int = 0,
  val completedAt: Long = 0L,
  val playerChoiceNotes: String = ""
)

@Entity(tableName = "crafted_textiles")
data class TextileEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val serialNumber: String,       // e.g. "SHM-GM-8492"
  val title: String,              // e.g. "Chamo Sunrise Gabi"
  val motifName: String,          // e.g. "Dorze Chevron & Meskel"
  val motifPatternIndex: Int,     // 0: Meskel, 1: Dorze Chevron, 2: Rift Wave, 3: Stepped Ochre
  val primaryColorHex: String,    // Hex code
  val secondaryColorHex: String,  // Hex code
  val colorRecipeName: String,    // e.g. "Wild Indigo & Madder Root"
  val yarnPlyScore: Int,          // From Spinning stage
  val weavingTensionScore: Int,   // From Weaving stage
  val fringeKnotScore: Int,       // From Fringe stage
  val pressLusterScore: Int,      // From Press stage
  val overallScore: Int,          // 0 to 100
  val qualityGrade: String,       // "Pristine Heirloom", "Artisan Masterpiece", "Handcrafted Classic"
  val cottonPurity: String,       // "100% Rainfed Arba Minch Organic Cotton"
  val provenanceCertificate: String,
  val createdAt: Long = System.currentTimeMillis()
)
