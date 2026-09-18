package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.core.model.AiCapability
import com.example.core.prompt.PromptEnhancer
import com.example.core.provider.ProviderRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("SATURN C", appName)
  }

  @Test
  fun `provider registry initializes built-in catalog`() {
    val registry = ProviderRegistry()
    val providers = registry.providers.value
    assertTrue("Should have providers", providers.isNotEmpty())
    assertNotNull("Should contain Google provider", registry.getProvider("google"))
    assertNotNull("Should contain Higgsfield provider", registry.getProvider("higgsfield"))
  }

  @Test
  fun `prompt enhancer expands cinematic directions`() {
    val original = "spaceship near ring planet"
    val result = PromptEnhancer.enhancePrompt(original, AiCapability.VIDEO)
    assertTrue("Should contain original prompt", result.enhanced.contains(original))
    assertTrue("Should contain cinematic keywords", result.enhanced.contains("cinematic"))
  }
}
