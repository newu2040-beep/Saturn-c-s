package com.example.core.model

enum class AiCapability(val label: String, val description: String) {
    IMAGE("Image", "Text-to-image, inpainting, relighting, and photo generation"),
    VIDEO("Video", "Cinematic video generation, image-to-video, and camera motions"),
    AUDIO("Audio", "Text-to-speech, sound design, voice cloning, and music"),
    EDITING("Editing", "Generative fill, object removal, background swap, and color grade"),
    UPSCALE("Upscale", "Resolution expansion, face restore, and noise reduction"),
    VOICE("Voice", "High-fidelity multilingual speech and dubbing"),
    MULTIMODAL("Multimodal", "Cross-media analysis and prompt intelligence"),
    STORYBOARD("Storyboard", "Scene and shot sequencing with coherent styling")
}

enum class AuthType(val label: String) {
    BEARER_TOKEN("Bearer Token"),
    API_KEY_HEADER("API Key Header"),
    BASIC_AUTH("Basic Auth"),
    QUERY_PARAM("Query Parameter"),
    KEY_AND_SECRET("Key + Secret"),
    CUSTOM_HEADER("Custom Header"),
    NONE("No Authentication")
}

enum class JobStatus(val label: String) {
    QUEUED("Queued"),
    UPLOADING("Uploading Asset"),
    PROCESSING("Generating"),
    DOWNLOADING("Finalizing"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    CANCELED("Canceled")
}

data class CustomProviderConfig(
    val authHeaderName: String = "Authorization",
    val customHeaders: Map<String, String> = emptyMap(),
    val endpoint: String = "/v1/generate",
    val httpMethod: String = "POST",
    val contentType: String = "application/json",
    val timeoutSeconds: Int = 60,
    val retryCount: Int = 2,
    val pollingIntervalMs: Long = 2000L,
    val statusEndpoint: String = "",
    val resultJsonPath: String = "output.url",
    val errorJsonPath: String = "error.message",
    val inputMappings: Map<String, String> = emptyMap()
)

data class AiModel(
    val id: String,
    val providerId: String,
    val displayName: String,
    val capability: AiCapability,
    val inputTypes: List<String> = listOf("text"),
    val outputTypes: List<String> = listOf("image"),
    val resolutions: List<String> = listOf("1024x1024", "1280x720", "1920x1080"),
    val aspectRatios: List<String> = listOf("1:1", "16:9", "9:16", "4:3", "21:9"),
    val durations: List<Int> = listOf(5, 10),
    val supportsAudio: Boolean = false,
    val supportsReferenceImages: Boolean = true,
    val supportsImageToVideo: Boolean = false,
    val supportsVideoToVideo: Boolean = false,
    val supportsFirstLastFrame: Boolean = false,
    val supportsNegativePrompt: Boolean = true,
    val supportsSeed: Boolean = true,
    val supportsUpscale: Boolean = false,
    val supportsCharacterConsistency: Boolean = false,
    val supportsStreaming: Boolean = false,
    val supportsAsyncJobs: Boolean = true,
    val pricing: String = "Pricing unavailable",
    val speedScore: String = "Standard ~15s",
    val qualityScore: String = "Cinema 4K"
)

data class AiProvider(
    val id: String,
    val name: String,
    val iconKey: String,
    val baseUrl: String,
    val authType: AuthType,
    val capabilities: List<AiCapability>,
    val models: List<AiModel>,
    val isCustom: Boolean = false,
    val isConfigured: Boolean = false,
    val costInformation: String = "Official API Pay-as-you-go",
    val documentationUrl: String = "",
    val customConfig: CustomProviderConfig? = null
)

data class GenerationJob(
    val id: String,
    val projectId: Long = 1L,
    val providerId: String,
    val modelId: String,
    val prompt: String,
    val negativePrompt: String = "",
    val capability: AiCapability,
    val status: JobStatus = JobStatus.QUEUED,
    val progress: Float = 0f,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val estimatedCost: String = "Free / Tier Included",
    val outputUrls: List<String> = emptyList(),
    val outputType: String = "image",
    val localFileUris: List<String> = emptyList(),
    val errorMessage: String? = null,
    val rawRequest: String? = null,
    val rawResponse: String? = null
)

data class CameraMotion(
    val motionType: String = "Dolly In",
    val lens: String = "35mm",
    val aperture: String = "f/2.8",
    val fov: Int = 65,
    val iso: Int = 400,
    val shutter: String = "180° (1/50s)",
    val lighting: String = "Dramatic Chiaroscuro",
    val colorGrade: String = "Cinematic Teal & Orange",
    val secondaryMotion: String = "Subtle Orbit Right"
) {
    fun toStructuredPrompt(): String {
        return "Camera: $motionType with $secondaryMotion, Shot on $lens lens at $aperture, ISO $iso, $lighting lighting, graded in $colorGrade."
    }
}

data class StoryboardShot(
    val shotId: String,
    val sceneNumber: Int,
    val shotNumber: Int,
    val title: String,
    val prompt: String,
    val referenceUri: String? = null,
    val characterName: String? = null,
    val location: String = "Studio Stage",
    val cameraMotion: CameraMotion = CameraMotion(),
    val durationSeconds: Int = 4,
    val aspectRatio: String = "16:9",
    val providerId: String = "higgsfield",
    val modelId: String = "kling-video/v2.5-turbo/pro/image-to-video",
    val generatedResultUri: String? = null,
    val isGenerated: Boolean = false
)

data class StoryboardScene(
    val sceneNumber: Int,
    val title: String,
    val shots: List<StoryboardShot>
)

data class CharacterProfile(
    val id: String,
    val name: String,
    val referenceUri: String = "",
    val description: String = "",
    val appearance: String = "",
    val clothing: String = "",
    val hair: String = "",
    val voice: String = "ElevenLabs - Adam",
    val style: String = "Hyper-realistic Cinematic",
    val identityToken: String = "[char_token]"
)

data class CreativeElement(
    val id: String,
    val name: String,
    val category: String, // Product, Character, Location, Vehicle, Prop, Logo, Object
    val referenceUri: String = "",
    val description: String = "",
    val promptTag: String = ""
)

data class WorkflowNode(
    val id: String,
    val nodeType: String, // Prompt, Model, Upscale, Video, Voice, Music, ColorGrade, Export
    val title: String,
    val configSummary: String,
    val posX: Float = 0f,
    val posY: Float = 0f,
    val isConnected: Boolean = true
)

data class CreativeWorkflow(
    val id: String,
    val name: String,
    val description: String,
    val nodes: List<WorkflowNode>,
    val isTemplate: Boolean = false
)

data class SaturnProject(
    val id: Long = 0L,
    val name: String,
    val description: String = "",
    val coverUri: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val version: String = "v1.0",
    val sceneCount: Int = 1,
    val shotCount: Int = 3
)

data class ApiLog(
    val id: Long = 0L,
    val providerId: String,
    val modelId: String,
    val requestId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val durationMs: Long = 0L,
    val httpStatus: Int = 200,
    val status: String = "Success",
    val sanitizedParams: String = "",
    val responseMetadata: String = ""
)

data class ExportMasterSettings(
    val videoCodec: String = "Apple ProRes 422 HQ", // Apple ProRes 422 HQ, H.265 / HEVC 10-bit, AV1 Film Master, Lossless PNG Sequence
    val videoResolution: String = "4K Cinema (3840x2160)", // 8K DCI, 4K Cinema, 1440p QHD, 1080p Master
    val videoBitrate: String = "Master 100 Mbps", // Master 100 Mbps, Cinema 50 Mbps, Broadcast 30 Mbps
    val colorSpace: String = "DCI-P3 Cinema", // DCI-P3 Cinema, Rec.709 Standard, HDR10 PQ (1000 nits), ACEScc Cinema
    val fpsCadence: String = "24.000 fps (Cinema Film)", // 24.000 fps, 29.97 fps, 60.000 fps
    val audioFormat: String = "24-bit 96kHz Lossless FLAC", // 24-bit 96kHz FLAC, 32-bit Float WAV, 320kbps Spatial AAC
    val exportContainer: String = ".mov", // .mov, .mp4, .mkv, .wav
    val includeAlphaTransparency: Boolean = false,
    val embedWatermark: Boolean = false
)

data class ApiTestStatus(
    val isTesting: Boolean = false,
    val success: Boolean? = null,
    val message: String? = null,
    val latencyMs: Long? = null,
    val verifiedAt: Long? = null
)

enum class MediaCategory(val label: String) {
    PHOTO("Photos & Stills"),
    VIDEO("Videos & Motion")
}

data class AspectRatioOption(
    val ratioKey: String,
    val displayName: String,
    val category: MediaCategory,
    val widthRatio: Float,
    val heightRatio: Float,
    val description: String,
    val isCustom: Boolean = false
) {
    val numericRatio: Float get() = if (heightRatio > 0f) widthRatio / heightRatio else 1f
    val formattedDimensions: String get() = "${widthRatio.toInt()}:${heightRatio.toInt()}"

    companion object {
        val PHOTO_PRESETS = listOf(
            AspectRatioOption("1:1", "1:1 Square", MediaCategory.PHOTO, 1f, 1f, "Instagram Feed, Profile Avatars, Album Covers"),
            AspectRatioOption("4:5", "4:5 Portrait", MediaCategory.PHOTO, 4f, 5f, "Instagram Portrait Post, Mobile Feed Fill"),
            AspectRatioOption("3:4", "3:4 Editorial", MediaCategory.PHOTO, 3f, 4f, "Pinterest, Fashion Magazines, Art Prints"),
            AspectRatioOption("9:16", "9:16 Story", MediaCategory.PHOTO, 9f, 16f, "Mobile Wallpapers, Stories, Vertical Fullscreen"),
            AspectRatioOption("16:9", "16:9 Landscape", MediaCategory.PHOTO, 16f, 9f, "Desktop Displays, YouTube Banners, TV Screens"),
            AspectRatioOption("3:2", "3:2 Classic 35mm", MediaCategory.PHOTO, 3f, 2f, "Traditional 35mm DSLR & Film Photography"),
            AspectRatioOption("2:3", "2:3 Vertical Film", MediaCategory.PHOTO, 2f, 3f, "Poster Print, Portrait 35mm Film Frame"),
            AspectRatioOption("4:3", "4:3 Medium Format", MediaCategory.PHOTO, 4f, 3f, "Hasselblad Medium Format & Retro Digital"),
            AspectRatioOption("21:9", "21:9 Ultra-Wide", MediaCategory.PHOTO, 21f, 9f, "Cinematic Still Panorama, Triple Monitor"),
            AspectRatioOption("1:2", "1:2 Tall Banner", MediaCategory.PHOTO, 1f, 2f, "Skyscraper Banners, Bookmark Art")
        )

        val VIDEO_PRESETS = listOf(
            AspectRatioOption("16:9", "16:9 Cinema UHD", MediaCategory.VIDEO, 16f, 9f, "YouTube, 4K Broadcast TV, Desktop Monitors"),
            AspectRatioOption("9:16", "9:16 Vertical Reel", MediaCategory.VIDEO, 9f, 16f, "TikTok, Instagram Reels, YouTube Shorts"),
            AspectRatioOption("1:1", "1:1 Square Video", MediaCategory.VIDEO, 1f, 1f, "Instagram / Facebook Feed Video Ads"),
            AspectRatioOption("2.39:1", "2.39:1 CinemaScope", MediaCategory.VIDEO, 2.39f, 1f, "Hollywood Feature Films & Anamorphic"),
            AspectRatioOption("2.35:1", "2.35:1 Anamorphic", MediaCategory.VIDEO, 2.35f, 1f, "Widescreen Cinematic Blockbusters"),
            AspectRatioOption("1.85:1", "1.85:1 Academy Flat", MediaCategory.VIDEO, 1.85f, 1f, "Standard Theatrical Movie Screen"),
            AspectRatioOption("1.43:1", "1.43:1 IMAX 70mm", MediaCategory.VIDEO, 1.43f, 1f, "Giant IMAX Film & Documentary"),
            AspectRatioOption("21:9", "21:9 Ultra Cinema", MediaCategory.VIDEO, 21f, 9f, "Curved Ultrawide Screens & Film Masters"),
            AspectRatioOption("4:3", "4:3 Retro Broadcast", MediaCategory.VIDEO, 4f, 3f, "Vintage CRT TV, 90s Music Videos & Archival"),
            AspectRatioOption("4:5", "4:5 Social Video", MediaCategory.VIDEO, 4f, 5f, "Maximized Vertical Feed Social Video")
        )

        fun parseRatio(ratioStr: String, fallbackCategory: MediaCategory = MediaCategory.PHOTO): AspectRatioOption {
            val found = (PHOTO_PRESETS + VIDEO_PRESETS).firstOrNull { it.ratioKey.equals(ratioStr, ignoreCase = true) }
            if (found != null) return found

            // Parse custom "W:H" or "W.W:1"
            val parts = ratioStr.split(":")
            if (parts.size == 2) {
                val w = parts[0].trim().toFloatOrNull() ?: 1f
                val h = parts[1].trim().toFloatOrNull() ?: 1f
                return AspectRatioOption(
                    ratioKey = ratioStr,
                    displayName = "Custom $ratioStr",
                    category = fallbackCategory,
                    widthRatio = w,
                    heightRatio = h,
                    description = "User Custom Proportion ($ratioStr)",
                    isCustom = true
                )
            }
            return PHOTO_PRESETS.first()
        }
    }
}


