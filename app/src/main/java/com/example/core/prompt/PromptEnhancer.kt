package com.example.core.prompt

import com.example.core.model.AiCapability
import com.example.core.model.CameraMotion

object PromptEnhancer {

    val styleChips = listOf(
        "Cinematic 35mm",
        "Anamorphic 2.39:1",
        "Volumetric Fog",
        "Chiaroscuro Light",
        "Kodak Vision3",
        "Raytraced Reflections",
        "Unreal Engine 5",
        "Macro Shot",
        "Cyberpunk Neon",
        "Golden Hour Warmth",
        "Photorealistic 8K",
        "IMAX 70mm"
    )

    fun enhancePrompt(original: String, capability: AiCapability, cameraMotion: CameraMotion? = null): EnhancedPromptResult {
        val trimmed = original.trim()
        if (trimmed.isEmpty()) return EnhancedPromptResult(original, original, "Empty prompt")

        val enhancements = mutableListOf<String>()
        val negativeTags = mutableListOf("blurry", "low quality", "deformed", "amateur", "bad anatomy")

        when (capability) {
            AiCapability.VIDEO -> {
                enhancements.add("cinematic continuous fluid motion")
                enhancements.add("high framerate 60fps pacing")
                enhancements.add("photorealistic atmospheric lighting with particle physics")
                negativeTags.addAll(listOf("jitter", "temporal flickering", "warped limbs", "static camera"))
            }
            AiCapability.IMAGE -> {
                enhancements.add("shot on ARRI Alexa Mini LF")
                enhancements.add("35mm anamorphic prime lens")
                enhancements.add("hyper-detailed textures, volumetric depth of field, master color graded")
                negativeTags.addAll(listOf("watermark", "oversaturated", "plastic skin", "extra fingers"))
            }
            AiCapability.AUDIO, AiCapability.VOICE -> {
                enhancements.add("studio master acoustic clarity")
                enhancements.add("binaural spatial presence, zero background hiss")
                negativeTags.addAll(listOf("clipping", "distortion", "muffled", "robotic artifact"))
            }
            AiCapability.UPSCALE -> {
                enhancements.add("micro-detail reconstruction, natural film grain retention, crisp edges")
            }
            else -> {
                enhancements.add("ultra high fidelity studio quality")
            }
        }

        if (cameraMotion != null) {
            enhancements.add(cameraMotion.toStructuredPrompt())
        }

        val enhanced = "$trimmed, " + enhancements.joinToString(", ")
        val negative = negativeTags.joinToString(", ")
        val diffExplanation = "Added cinematic lens staging, volumetric lighting parameters, and professional noise suppression."

        return EnhancedPromptResult(
            original = trimmed,
            enhanced = enhanced,
            negativePrompt = negative,
            explanation = diffExplanation
        )
    }

    data class EnhancedPromptResult(
        val original: String,
        val enhanced: String,
        val negativePrompt: String = "",
        val explanation: String = ""
    )
}
