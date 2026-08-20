package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ProductionStages
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app name from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Shimena", appName)
  }

  @Test
  fun `verify ten production stages integrity`() {
    val stages = ProductionStages.stages
    assertEquals(10, stages.size)
    assertEquals("01", stages[0].code)
    assertEquals("LAND", stages[0].department)
    assertEquals("10", stages[9].code)
    assertEquals("SHARE", stages[9].department)

    // Verify all stages have artisans, quotes, and cultural details
    stages.forEach { stage ->
      assertNotNull(stage.artisanName)
      assertNotNull(stage.quote)
      assertNotNull(stage.amharicTerm)
      assertNotNull(stage.materialInput)
      assertNotNull(stage.materialOutput)
    }
  }
}
