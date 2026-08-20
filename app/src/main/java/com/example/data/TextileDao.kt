package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TextileDao {
  @Query("SELECT * FROM crafted_textiles ORDER BY createdAt DESC")
  fun getAllTextiles(): Flow<List<TextileEntity>>

  @Query("SELECT * FROM crafted_textiles WHERE id = :id")
  suspend fun getTextileById(id: Int): TextileEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTextile(textile: TextileEntity): Long

  @Delete
  suspend fun deleteTextile(textile: TextileEntity)

  @Query("SELECT COUNT(*) FROM crafted_textiles")
  fun getTextileCount(): Flow<Int>
}
