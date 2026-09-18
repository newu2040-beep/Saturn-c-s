package com.example.core.provider

import com.example.core.model.AiCapability
import com.example.core.model.AiModel
import com.example.core.model.AiProvider
import com.example.core.model.AuthType
import com.example.core.model.CustomProviderConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProviderRegistry {

    private val _providers = MutableStateFlow<List<AiProvider>>(emptyList())
    val providers: StateFlow<List<AiProvider>> = _providers.asStateFlow()

    private val _customProviders = MutableStateFlow<List<AiProvider>>(emptyList())
    val customProviders: StateFlow<List<AiProvider>> = _customProviders.asStateFlow()

    init {
        loadBuiltInCatalog()
    }

    private fun loadBuiltInCatalog() {
        val list = listOf(
            // 1. HIGGSFIELD API
            AiProvider(
                id = "higgsfield",
                name = "Higgsfield",
                iconKey = "higgsfield",
                baseUrl = "https://api.higgsfield.ai",
                authType = AuthType.KEY_AND_SECRET,
                capabilities = listOf(AiCapability.IMAGE, AiCapability.VIDEO, AiCapability.STORYBOARD),
                costInformation = "Pay-per-second / credit balance",
                documentationUrl = "https://api.higgsfield.ai/docs",
                models = listOf(
                    AiModel(
                        id = "kling-video/v2.5-turbo/pro/image-to-video",
                        providerId = "higgsfield",
                        displayName = "Kling v2.5 Turbo Pro (I2V)",
                        capability = AiCapability.VIDEO,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("video"),
                        resolutions = listOf("1080p", "720p"),
                        aspectRatios = listOf("16:9", "9:16", "1:1"),
                        durations = listOf(5, 10),
                        supportsAudio = false,
                        supportsReferenceImages = true,
                        supportsImageToVideo = true,
                        supportsVideoToVideo = false,
                        supportsFirstLastFrame = true,
                        supportsNegativePrompt = true,
                        supportsSeed = true,
                        supportsAsyncJobs = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Turbo ~12s",
                        qualityScore = "Cinema 1080p"
                    ),
                    AiModel(
                        id = "kling-video/omni/image-reference",
                        providerId = "higgsfield",
                        displayName = "Kling Omni Reference",
                        capability = AiCapability.VIDEO,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("video"),
                        resolutions = listOf("1080p", "720p"),
                        aspectRatios = listOf("16:9", "9:16"),
                        durations = listOf(5, 10),
                        supportsAudio = false,
                        supportsReferenceImages = true,
                        supportsImageToVideo = true,
                        supportsCharacterConsistency = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Standard ~25s",
                        qualityScore = "High Consistency"
                    ),
                    AiModel(
                        id = "soul-cinema-2",
                        providerId = "higgsfield",
                        displayName = "Higgsfield Soul Cinema 2",
                        capability = AiCapability.IMAGE,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("image"),
                        resolutions = listOf("1024x1024", "1920x1080", "1080x1920", "2048x1152"),
                        aspectRatios = listOf("16:9", "9:16", "1:1", "21:9"),
                        durations = emptyList(),
                        supportsReferenceImages = true,
                        supportsCharacterConsistency = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Fast ~6s",
                        qualityScore = "Ultra 35mm Grain"
                    ),
                    AiModel(
                        id = "marketing-studio-image",
                        providerId = "higgsfield",
                        displayName = "Marketing Studio Image",
                        capability = AiCapability.IMAGE,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("image"),
                        resolutions = listOf("1080x1080", "1080x1350", "1080x1920", "1920x1080"),
                        aspectRatios = listOf("1:1", "4:5", "9:16", "16:9"),
                        supportsReferenceImages = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Fast ~4s",
                        qualityScore = "Commercial Packshot"
                    )
                )
            ),

            // 2. GOOGLE GEMINI & VEO API
            AiProvider(
                id = "google",
                name = "Google AI (Gemini & Veo)",
                iconKey = "google",
                baseUrl = "https://generativelanguage.googleapis.com",
                authType = AuthType.QUERY_PARAM,
                capabilities = listOf(AiCapability.IMAGE, AiCapability.VIDEO, AiCapability.AUDIO, AiCapability.MULTIMODAL),
                costInformation = "Official Google Cloud / AI Studio Quota",
                documentationUrl = "https://ai.google.dev",
                models = listOf(
                    AiModel(
                        id = "gemini-3.1-flash-image-preview",
                        providerId = "google",
                        displayName = "Nano Banana 2 (Gemini 3.1 Flash Image)",
                        capability = AiCapability.IMAGE,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("image"),
                        resolutions = listOf("512px", "1K", "2K", "4K"),
                        aspectRatios = listOf("1:1", "16:9", "9:16", "4:3", "3:4"),
                        durations = emptyList(),
                        supportsReferenceImages = true,
                        supportsCharacterConsistency = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Ultra Fast ~3s",
                        qualityScore = "High Precision 4K"
                    ),
                    AiModel(
                        id = "gemini-3-pro-image-preview",
                        providerId = "google",
                        displayName = "Nano Banana Pro (Gemini 3 Pro Image)",
                        capability = AiCapability.IMAGE,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("image"),
                        resolutions = listOf("1K", "2K", "4K"),
                        aspectRatios = listOf("1:1", "16:9", "9:16", "4:3"),
                        supportsReferenceImages = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Standard ~8s",
                        qualityScore = "Studio Master"
                    ),
                    AiModel(
                        id = "veo-3.1-generate-preview",
                        providerId = "google",
                        displayName = "Veo 3.1",
                        capability = AiCapability.VIDEO,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("video"),
                        resolutions = listOf("1080p", "4K"),
                        aspectRatios = listOf("16:9", "9:16"),
                        durations = listOf(5, 10),
                        supportsAudio = true,
                        supportsReferenceImages = true,
                        supportsImageToVideo = true,
                        supportsFirstLastFrame = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Cinematic ~35s",
                        qualityScore = "Native Audio 4K"
                    ),
                    AiModel(
                        id = "veo-3.1-fast-generate-preview",
                        providerId = "google",
                        displayName = "Veo 3.1 Fast",
                        capability = AiCapability.VIDEO,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("video"),
                        resolutions = listOf("720p", "1080p"),
                        aspectRatios = listOf("16:9", "9:16"),
                        durations = listOf(5, 10),
                        supportsAudio = true,
                        supportsReferenceImages = true,
                        supportsImageToVideo = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Fast ~15s",
                        qualityScore = "Broadcast HD"
                    ),
                    AiModel(
                        id = "gemini-2.5-flash-preview-tts",
                        providerId = "google",
                        displayName = "Gemini Native Speech (TTS)",
                        capability = AiCapability.AUDIO,
                        inputTypes = listOf("text"),
                        outputTypes = listOf("audio"),
                        supportsAudio = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Realtime ~1s",
                        qualityScore = "Natural Human Voice"
                    )
                )
            ),

            // 3. OPENAI
            AiProvider(
                id = "openai",
                name = "OpenAI",
                iconKey = "openai",
                baseUrl = "https://api.openai.com/v1",
                authType = AuthType.BEARER_TOKEN,
                capabilities = listOf(AiCapability.IMAGE, AiCapability.VIDEO, AiCapability.MULTIMODAL, AiCapability.EDITING),
                costInformation = "Zero Paywall • BYOK / Open Access",
                documentationUrl = "https://platform.openai.com/docs",
                models = listOf(
                    AiModel(
                        id = "sora-2",
                        providerId = "openai",
                        displayName = "Sora 2 Cinematic World Model",
                        capability = AiCapability.VIDEO,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("video"),
                        resolutions = listOf("1080p", "720p"),
                        aspectRatios = listOf("16:9", "9:16", "1:1"),
                        durations = listOf(5, 10, 15),
                        supportsAudio = true,
                        supportsReferenceImages = true,
                        supportsImageToVideo = true,
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Cinematic ~20s",
                        qualityScore = "Hyperrealistic World Physics"
                    ),
                    AiModel(
                        id = "dall-e-3",
                        providerId = "openai",
                        displayName = "DALL-E 3 Precision Studio",
                        capability = AiCapability.IMAGE,
                        inputTypes = listOf("text"),
                        outputTypes = listOf("image"),
                        resolutions = listOf("1024x1024", "1024x1792", "1792x1024"),
                        aspectRatios = listOf("1:1", "16:9", "9:16"),
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Fast ~8s",
                        qualityScore = "Master Prompt Fidelity"
                    ),
                    AiModel(
                        id = "gpt-4o",
                        providerId = "openai",
                        displayName = "GPT-4o Omnimodal Vision",
                        capability = AiCapability.MULTIMODAL,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("text"),
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Realtime ~1s",
                        qualityScore = "Director Visual Reasoning"
                    ),
                    AiModel(
                        id = "o3-mini",
                        providerId = "openai",
                        displayName = "o3-mini High Reasoning",
                        capability = AiCapability.MULTIMODAL,
                        inputTypes = listOf("text"),
                        outputTypes = listOf("text"),
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Fast ~2s",
                        qualityScore = "Deep Reasoning & Logic"
                    )
                )
            ),

            // 4. RUNWAY
            AiProvider(
                id = "runway",
                name = "Runway",
                iconKey = "runway",
                baseUrl = "https://api.dev.runwayml.com/v1",
                authType = AuthType.BEARER_TOKEN,
                capabilities = listOf(AiCapability.VIDEO, AiCapability.IMAGE),
                costInformation = "Runway Credits per second",
                documentationUrl = "https://docs.dev.runwayml.com",
                models = listOf(
                    AiModel(
                        id = "gen4.5",
                        providerId = "runway",
                        displayName = "Gen-4.5 Cinema Video",
                        capability = AiCapability.VIDEO,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("video"),
                        resolutions = listOf("720p", "1080p"),
                        aspectRatios = listOf("16:9", "9:16"),
                        durations = listOf(5, 10),
                        supportsReferenceImages = true,
                        supportsImageToVideo = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Standard ~25s",
                        qualityScore = "Hollywood Director Grade"
                    ),
                    AiModel(
                        id = "gen4_turbo",
                        providerId = "runway",
                        displayName = "Gen-4 Turbo",
                        capability = AiCapability.VIDEO,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("video"),
                        resolutions = listOf("720p"),
                        aspectRatios = listOf("16:9", "9:16"),
                        durations = listOf(5, 10),
                        supportsImageToVideo = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Fast ~12s",
                        qualityScore = "Dynamic Motion"
                    ),
                    AiModel(
                        id = "gen4_image",
                        providerId = "runway",
                        displayName = "Gen-4 Image Master",
                        capability = AiCapability.IMAGE,
                        inputTypes = listOf("text"),
                        outputTypes = listOf("image"),
                        resolutions = listOf("1024x1024", "1920x1080", "1080x1920"),
                        aspectRatios = listOf("1:1", "16:9", "9:16"),
                        pricing = "Pricing unavailable",
                        speedScore = "Fast ~6s",
                        qualityScore = "Hyper-detailed Cinematic"
                    )
                )
            ),

            // 5. FAL.AI
            AiProvider(
                id = "fal",
                name = "fal.ai",
                iconKey = "fal",
                baseUrl = "https://queue.fal.run",
                authType = AuthType.API_KEY_HEADER,
                capabilities = listOf(AiCapability.IMAGE, AiCapability.VIDEO, AiCapability.AUDIO, AiCapability.UPSCALE),
                costInformation = "Pay-per-compute second",
                documentationUrl = "https://fal.ai/docs",
                models = listOf(
                    AiModel(
                        id = "fal-ai/flux-pro/v1.1-ultra",
                        providerId = "fal",
                        displayName = "FLUX.1 Pro Ultra",
                        capability = AiCapability.IMAGE,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("image"),
                        resolutions = listOf("1024x1024", "2048x1152", "1152x2048"),
                        aspectRatios = listOf("1:1", "16:9", "9:16", "21:9"),
                        supportsReferenceImages = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Fast ~5s",
                        qualityScore = "State-of-the-Art 2K"
                    ),
                    AiModel(
                        id = "fal-ai/kling-video/v1.5/pro",
                        providerId = "fal",
                        displayName = "Kling Video v1.5 Pro",
                        capability = AiCapability.VIDEO,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("video"),
                        resolutions = listOf("1080p"),
                        aspectRatios = listOf("16:9", "9:16"),
                        durations = listOf(5, 10),
                        supportsImageToVideo = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Standard ~30s",
                        qualityScore = "Fluid Physics 1080p"
                    ),
                    AiModel(
                        id = "fal-ai/clarity-upscaler",
                        providerId = "fal",
                        displayName = "Clarity 4K AI Upscaler",
                        capability = AiCapability.UPSCALE,
                        inputTypes = listOf("image"),
                        outputTypes = listOf("image"),
                        supportsUpscale = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Fast ~8s",
                        qualityScore = "Zero Artifact 4K"
                    )
                )
            ),

            // 6. REPLICATE
            AiProvider(
                id = "replicate",
                name = "Replicate",
                iconKey = "replicate",
                baseUrl = "https://api.replicate.com/v1",
                authType = AuthType.BEARER_TOKEN,
                capabilities = listOf(AiCapability.IMAGE, AiCapability.VIDEO, AiCapability.AUDIO, AiCapability.UPSCALE),
                costInformation = "Per second GPU hardware billing",
                documentationUrl = "https://replicate.com/docs",
                models = listOf(
                    AiModel(
                        id = "black-forest-labs/flux-schnell",
                        providerId = "replicate",
                        displayName = "FLUX.1 Schnell",
                        capability = AiCapability.IMAGE,
                        inputTypes = listOf("text"),
                        outputTypes = listOf("image"),
                        resolutions = listOf("1024x1024", "1344x768", "768x1344"),
                        aspectRatios = listOf("1:1", "16:9", "9:16"),
                        pricing = "Pricing unavailable",
                        speedScore = "Ultra Fast ~2s",
                        qualityScore = "4-Step Rapid Pro"
                    ),
                    AiModel(
                        id = "stability-ai/stable-video-diffusion",
                        providerId = "replicate",
                        displayName = "Stable Video Diffusion (SVD-XT)",
                        capability = AiCapability.VIDEO,
                        inputTypes = listOf("image"),
                        outputTypes = listOf("video"),
                        resolutions = listOf("1024x576"),
                        aspectRatios = listOf("16:9"),
                        durations = listOf(4),
                        supportsImageToVideo = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Fast ~14s",
                        qualityScore = "Organic Motion"
                    )
                )
            ),

            // 7. STABILITY AI
            AiProvider(
                id = "stability",
                name = "Stability AI",
                iconKey = "stability",
                baseUrl = "https://api.stability.ai/v2beta",
                authType = AuthType.BEARER_TOKEN,
                capabilities = listOf(AiCapability.IMAGE, AiCapability.EDITING, AiCapability.UPSCALE),
                costInformation = "Credits based per resolution",
                documentationUrl = "https://platform.stability.ai/docs",
                models = listOf(
                    AiModel(
                        id = "stable-diffusion-3.5-large",
                        providerId = "stability",
                        displayName = "Stable Diffusion 3.5 Large",
                        capability = AiCapability.IMAGE,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("image"),
                        resolutions = listOf("1024x1024", "1344x768", "768x1344"),
                        aspectRatios = listOf("1:1", "16:9", "9:16", "21:9"),
                        supportsNegativePrompt = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Standard ~9s",
                        qualityScore = "8B Parameter Depth"
                    ),
                    AiModel(
                        id = "stable-image-inpaint",
                        providerId = "stability",
                        displayName = "Generative Inpaint & Replace",
                        capability = AiCapability.EDITING,
                        inputTypes = listOf("image", "text"),
                        outputTypes = listOf("image"),
                        pricing = "Pricing unavailable",
                        speedScore = "Fast ~5s",
                        qualityScore = "Seamless Blending"
                    )
                )
            ),

            // 8. KLING AI (Official / Compatible)
            AiProvider(
                id = "kling",
                name = "Kling AI",
                iconKey = "kling",
                baseUrl = "https://api.klingai.com/v1",
                authType = AuthType.BEARER_TOKEN,
                capabilities = listOf(AiCapability.VIDEO),
                costInformation = "Pricing unavailable",
                documentationUrl = "https://klingai.com",
                models = listOf(
                    AiModel(
                        id = "kling-v1.5-pro",
                        providerId = "kling",
                        displayName = "Kling AI v1.5 Cinematic",
                        capability = AiCapability.VIDEO,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("video"),
                        resolutions = listOf("1080p"),
                        aspectRatios = listOf("16:9", "9:16", "1:1"),
                        durations = listOf(5, 10),
                        supportsImageToVideo = true,
                        supportsCharacterConsistency = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Standard ~28s",
                        qualityScore = "Realistic Physics & Faces"
                    )
                )
            ),

            // 9. ELEVENLABS
            AiProvider(
                id = "elevenlabs",
                name = "ElevenLabs",
                iconKey = "elevenlabs",
                baseUrl = "https://api.elevenlabs.io/v1",
                authType = AuthType.API_KEY_HEADER,
                capabilities = listOf(AiCapability.AUDIO, AiCapability.VOICE),
                costInformation = "Character quota billing",
                documentationUrl = "https://elevenlabs.io/docs",
                models = listOf(
                    AiModel(
                        id = "eleven_multilingual_v2",
                        providerId = "elevenlabs",
                        displayName = "Eleven Multilingual v2",
                        capability = AiCapability.VOICE,
                        inputTypes = listOf("text"),
                        outputTypes = listOf("audio"),
                        supportsAudio = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Realtime ~1s",
                        qualityScore = "Emotionally Expressive"
                    ),
                    AiModel(
                        id = "sound-generation",
                        providerId = "elevenlabs",
                        displayName = "Cinematic Sound Effects FX",
                        capability = AiCapability.AUDIO,
                        inputTypes = listOf("text"),
                        outputTypes = listOf("audio"),
                        durations = listOf(3, 6, 11),
                        supportsAudio = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Ultra Fast ~2s",
                        qualityScore = "Spatial Binaural FX"
                    )
                )
            ),

            // 10. IDEOGRAM
            AiProvider(
                id = "ideogram",
                name = "Ideogram",
                iconKey = "ideogram",
                baseUrl = "https://api.ideogram.ai",
                authType = AuthType.API_KEY_HEADER,
                capabilities = listOf(AiCapability.IMAGE),
                costInformation = "Pricing unavailable",
                documentationUrl = "https://ideogram.ai",
                models = listOf(
                    AiModel(
                        id = "ideogram-v2",
                        providerId = "ideogram",
                        displayName = "Ideogram v2 (Typography Master)",
                        capability = AiCapability.IMAGE,
                        inputTypes = listOf("text"),
                        outputTypes = listOf("image"),
                        resolutions = listOf("1024x1024", "1280x720", "720x1280"),
                        aspectRatios = listOf("1:1", "16:9", "9:16", "3:4", "4:3"),
                        pricing = "Pricing unavailable",
                        speedScore = "Fast ~6s",
                        qualityScore = "Flawless Text Rendering"
                    )
                )
            ),

            // 11. LUMA AI
            AiProvider(
                id = "luma",
                name = "Luma AI",
                iconKey = "luma",
                baseUrl = "https://api.lumalabs.ai/dream-machine/v1",
                authType = AuthType.BEARER_TOKEN,
                capabilities = listOf(AiCapability.VIDEO),
                costInformation = "Pricing unavailable",
                documentationUrl = "https://lumalabs.ai",
                models = listOf(
                    AiModel(
                        id = "dream-machine-ray2",
                        providerId = "luma",
                        displayName = "Dream Machine Ray 2",
                        capability = AiCapability.VIDEO,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("video"),
                        resolutions = listOf("720p", "1080p"),
                        aspectRatios = listOf("16:9", "9:16"),
                        durations = listOf(5),
                        supportsImageToVideo = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Fast ~18s",
                        qualityScore = "Coherent 3D Camera Depth"
                    )
                )
            ),

            // 12. MINIMAX
            AiProvider(
                id = "minimax",
                name = "MiniMax",
                iconKey = "minimax",
                baseUrl = "https://api.minimax.chat/v1",
                authType = AuthType.BEARER_TOKEN,
                capabilities = listOf(AiCapability.VIDEO, AiCapability.AUDIO),
                costInformation = "Pricing unavailable",
                documentationUrl = "https://api.minimax.chat",
                models = listOf(
                    AiModel(
                        id = "video-01-live",
                        providerId = "minimax",
                        displayName = "Hailuo Video-01",
                        capability = AiCapability.VIDEO,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("video"),
                        resolutions = listOf("720p", "1080p"),
                        aspectRatios = listOf("16:9", "9:16"),
                        durations = listOf(6),
                        supportsImageToVideo = true,
                        pricing = "Pricing unavailable",
                        speedScore = "Standard ~22s",
                        qualityScore = "Exceptional Human Gestures"
                    )
                )
            ),

            // 13. XAI
            AiProvider(
                id = "xai",
                name = "xAI",
                iconKey = "xai",
                baseUrl = "https://api.x.ai/v1",
                authType = AuthType.BEARER_TOKEN,
                capabilities = listOf(AiCapability.IMAGE, AiCapability.MULTIMODAL),
                costInformation = "Pricing unavailable",
                documentationUrl = "https://docs.x.ai",
                models = listOf(
                    AiModel(
                        id = "grok-2-image",
                        providerId = "xai",
                        displayName = "Grok Imagine 1.5",
                        capability = AiCapability.IMAGE,
                        inputTypes = listOf("text"),
                        outputTypes = listOf("image"),
                        resolutions = listOf("1024x1024", "1280x720"),
                        aspectRatios = listOf("1:1", "16:9", "9:16"),
                        pricing = "Pricing unavailable",
                        speedScore = "Fast ~4s",
                        qualityScore = "Unfiltered Photorealism"
                    )
                )
            ),

            // 14. DEEPSEEK
            AiProvider(
                id = "deepseek",
                name = "DeepSeek AI",
                iconKey = "deepseek",
                baseUrl = "https://api.deepseek.com",
                authType = AuthType.BEARER_TOKEN,
                capabilities = listOf(AiCapability.MULTIMODAL, AiCapability.IMAGE, AiCapability.STORYBOARD),
                costInformation = "Zero Paywall • Open BYOK & Free Tier",
                documentationUrl = "https://platform.deepseek.com",
                models = listOf(
                    AiModel(
                        id = "deepseek-reasoner",
                        providerId = "deepseek",
                        displayName = "DeepSeek-R1 (Deep Thinking)",
                        capability = AiCapability.MULTIMODAL,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("text"),
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Chain-of-Thought ~4s",
                        qualityScore = "State-of-the-Art Logic"
                    ),
                    AiModel(
                        id = "deepseek-chat",
                        providerId = "deepseek",
                        displayName = "DeepSeek-V3 671B MoE",
                        capability = AiCapability.MULTIMODAL,
                        inputTypes = listOf("text"),
                        outputTypes = listOf("text"),
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Ultra Fast ~1s",
                        qualityScore = "Elite Creative Directing"
                    ),
                    AiModel(
                        id = "deepseek-vl2",
                        providerId = "deepseek",
                        displayName = "DeepSeek VL-2 Vision",
                        capability = AiCapability.MULTIMODAL,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("text"),
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Fast ~2s",
                        qualityScore = "Visual Scene Decomposition"
                    )
                )
            ),

            // 15. GLM (ZHIPU AI & COGVIDEO)
            AiProvider(
                id = "glm",
                name = "GLM & CogVideo",
                iconKey = "glm",
                baseUrl = "https://open.bigmodel.cn/api/paas/v4",
                authType = AuthType.BEARER_TOKEN,
                capabilities = listOf(AiCapability.VIDEO, AiCapability.IMAGE, AiCapability.MULTIMODAL),
                costInformation = "Zero Paywall • Open BYOK",
                documentationUrl = "https://bigmodel.cn",
                models = listOf(
                    AiModel(
                        id = "cogvideox-5b",
                        providerId = "glm",
                        displayName = "CogVideoX-5B Studio",
                        capability = AiCapability.VIDEO,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("video"),
                        resolutions = listOf("1080p", "720p"),
                        aspectRatios = listOf("16:9", "9:16"),
                        durations = listOf(5, 10),
                        supportsImageToVideo = true,
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Fast ~15s",
                        qualityScore = "Cinematic 3D Diffusion"
                    ),
                    AiModel(
                        id = "cogvideox-flash",
                        providerId = "glm",
                        displayName = "CogVideoX-Flash Turbo",
                        capability = AiCapability.VIDEO,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("video"),
                        resolutions = listOf("720p"),
                        aspectRatios = listOf("16:9", "9:16"),
                        durations = listOf(5),
                        supportsImageToVideo = true,
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Ultra Fast ~8s",
                        qualityScore = "Rapid Dynamic Video"
                    ),
                    AiModel(
                        id = "glm-4v-plus",
                        providerId = "glm",
                        displayName = "GLM-4V Plus Vision Master",
                        capability = AiCapability.MULTIMODAL,
                        inputTypes = listOf("text", "image", "video"),
                        outputTypes = listOf("text"),
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Fast ~2s",
                        qualityScore = "Multimodal Video Reasoning"
                    ),
                    AiModel(
                        id = "glm-4-plus",
                        providerId = "glm",
                        displayName = "GLM-4 Plus Flagship",
                        capability = AiCapability.MULTIMODAL,
                        inputTypes = listOf("text"),
                        outputTypes = listOf("text"),
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Fast ~1s",
                        qualityScore = "Flagship Director Intelligence"
                    )
                )
            ),

            // 16. CLAUDE AI (ANTHROPIC)
            AiProvider(
                id = "anthropic",
                name = "Claude AI (Anthropic)",
                iconKey = "anthropic",
                baseUrl = "https://api.anthropic.com/v1",
                authType = AuthType.API_KEY_HEADER,
                capabilities = listOf(AiCapability.MULTIMODAL, AiCapability.STORYBOARD),
                costInformation = "Zero Paywall • Open BYOK",
                documentationUrl = "https://docs.anthropic.com",
                models = listOf(
                    AiModel(
                        id = "claude-3-7-sonnet",
                        providerId = "anthropic",
                        displayName = "Claude 3.7 Sonnet (Hybrid)",
                        capability = AiCapability.MULTIMODAL,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("text"),
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Dynamic Hybrid ~3s",
                        qualityScore = "Premier Reasoning & Vision"
                    ),
                    AiModel(
                        id = "claude-3-5-sonnet",
                        providerId = "anthropic",
                        displayName = "Claude 3.5 Sonnet",
                        capability = AiCapability.MULTIMODAL,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("text"),
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Fast ~2s",
                        qualityScore = "Master Screenplay & Visuals"
                    ),
                    AiModel(
                        id = "claude-3-5-haiku",
                        providerId = "anthropic",
                        displayName = "Claude 3.5 Haiku",
                        capability = AiCapability.MULTIMODAL,
                        inputTypes = listOf("text"),
                        outputTypes = listOf("text"),
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Ultra Fast <1s",
                        qualityScore = "Instantaneous Directing"
                    )
                )
            ),

            // 17. WAN 2.1 VIDEO (ALIBABA)
            AiProvider(
                id = "wan",
                name = "Wan 2.1 (Alibaba)",
                iconKey = "wan",
                baseUrl = "https://dashscope.aliyuncs.com/api/v1",
                authType = AuthType.BEARER_TOKEN,
                capabilities = listOf(AiCapability.VIDEO),
                costInformation = "Zero Paywall • Open Source & BYOK",
                documentationUrl = "https://github.com/Wan-Video",
                models = listOf(
                    AiModel(
                        id = "wan2.1-t2v-14b",
                        providerId = "wan",
                        displayName = "Wan 2.1 14B Cinema Video",
                        capability = AiCapability.VIDEO,
                        inputTypes = listOf("text"),
                        outputTypes = listOf("video"),
                        resolutions = listOf("1080p", "720p"),
                        aspectRatios = listOf("16:9", "9:16", "1:1"),
                        durations = listOf(5, 10),
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Standard ~25s",
                        qualityScore = "Ultra 14B Hollywood Coherence"
                    ),
                    AiModel(
                        id = "wan2.1-i2v-14b",
                        providerId = "wan",
                        displayName = "Wan 2.1 Image-to-Video 14B",
                        capability = AiCapability.VIDEO,
                        inputTypes = listOf("image", "text"),
                        outputTypes = listOf("video"),
                        resolutions = listOf("1080p", "720p"),
                        aspectRatios = listOf("16:9", "9:16"),
                        durations = listOf(5),
                        supportsImageToVideo = true,
                        supportsReferenceImages = true,
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Standard ~22s",
                        qualityScore = "Photorealistic Motion & Physics"
                    )
                )
            ),

            // 18. MIDJOURNEY (API COMPATIBLE)
            AiProvider(
                id = "midjourney",
                name = "Midjourney v6.1",
                iconKey = "midjourney",
                baseUrl = "https://api.midjourney.com/v1",
                authType = AuthType.BEARER_TOKEN,
                capabilities = listOf(AiCapability.IMAGE),
                costInformation = "Zero Paywall • Open BYOK",
                documentationUrl = "https://docs.midjourney.com",
                models = listOf(
                    AiModel(
                        id = "mj-v6.1",
                        providerId = "midjourney",
                        displayName = "Midjourney v6.1 Photorealism",
                        capability = AiCapability.IMAGE,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("image"),
                        resolutions = listOf("1024x1024", "1920x1080", "1080x1920"),
                        aspectRatios = listOf("1:1", "16:9", "9:16", "21:9"),
                        supportsReferenceImages = true,
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Standard ~20s",
                        qualityScore = "Iconic Cinematic Texture"
                    ),
                    AiModel(
                        id = "mj-niji-v6",
                        providerId = "midjourney",
                        displayName = "Niji Journey v6 (Anime Cinema)",
                        capability = AiCapability.IMAGE,
                        inputTypes = listOf("text"),
                        outputTypes = listOf("image"),
                        resolutions = listOf("1024x1024", "1920x1080", "1080x1920"),
                        aspectRatios = listOf("1:1", "16:9", "9:16"),
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Standard ~18s",
                        qualityScore = "Animation Director Grade"
                    )
                )
            ),

            // 19. MISTRAL AI
            AiProvider(
                id = "mistral",
                name = "Mistral AI",
                iconKey = "mistral",
                baseUrl = "https://api.mistral.ai/v1",
                authType = AuthType.BEARER_TOKEN,
                capabilities = listOf(AiCapability.MULTIMODAL),
                costInformation = "Zero Paywall • Open BYOK",
                documentationUrl = "https://docs.mistral.ai",
                models = listOf(
                    AiModel(
                        id = "mistral-large-2411",
                        providerId = "mistral",
                        displayName = "Mistral Large 2",
                        capability = AiCapability.MULTIMODAL,
                        inputTypes = listOf("text"),
                        outputTypes = listOf("text"),
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Fast ~2s",
                        qualityScore = "Advanced Reasoning & Scripts"
                    ),
                    AiModel(
                        id = "pixtral-large",
                        providerId = "mistral",
                        displayName = "Pixtral Large Vision",
                        capability = AiCapability.MULTIMODAL,
                        inputTypes = listOf("text", "image"),
                        outputTypes = listOf("text"),
                        pricing = "Zero Paywall (BYOK)",
                        speedScore = "Fast ~3s",
                        qualityScore = "High Precision Visual Analysis"
                    )
                )
            )
        )

        _providers.value = list
    }

    fun getAllProviders(): List<AiProvider> {
        return _providers.value + _customProviders.value
    }

    fun getProvider(providerId: String): AiProvider? {
        return getAllProviders().find { it.id == providerId }
    }

    fun getModel(providerId: String, modelId: String): AiModel? {
        val provider = getProvider(providerId) ?: return null
        return provider.models.find { it.id == modelId }
    }

    fun getModelsForCapability(capability: AiCapability): List<AiModel> {
        return getAllProviders().flatMap { it.models }.filter { it.capability == capability }
    }

    fun addCustomProvider(provider: AiProvider) {
        _customProviders.value = _customProviders.value.filterNot { it.id == provider.id } + provider
    }

    fun removeCustomProvider(providerId: String) {
        _customProviders.value = _customProviders.value.filterNot { it.id == providerId }
    }

    /**
     * Smart Model Router:
     * Analyzes prompt keywords and required parameters to recommend optimal models.
     */
    fun routeSmartModel(
        prompt: String,
        preferredCapability: AiCapability? = null
    ): List<AiModel> {
        val lower = prompt.lowercase()
        val detectedCap = preferredCapability ?: when {
            lower.contains("video") || lower.contains("commercial") || lower.contains("cinematic") || lower.contains("animate") || lower.contains("reel") -> AiCapability.VIDEO
            lower.contains("audio") || lower.contains("voice") || lower.contains("sound") || lower.contains("speech") -> AiCapability.AUDIO
            lower.contains("upscale") || lower.contains("enhance resolution") -> AiCapability.UPSCALE
            lower.contains("edit") || lower.contains("remove") || lower.contains("inpaint") -> AiCapability.EDITING
            else -> AiCapability.IMAGE
        }

        val candidates = getModelsForCapability(detectedCap)
        return candidates.sortedByDescending { model ->
            var score = 0
            if (lower.contains("fast") && model.speedScore.contains("Fast", ignoreCase = true)) score += 5
            if (lower.contains("4k") && model.qualityScore.contains("4K", ignoreCase = true)) score += 5
            if (lower.contains("camera") && model.supportsReferenceImages) score += 3
            if (lower.contains("audio") && model.supportsAudio) score += 4
            score
        }
    }
}
