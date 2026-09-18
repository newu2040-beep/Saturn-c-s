package com.example.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val description: String = "",
    val coverUri: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val version: String = "v1.0",
    val sceneCount: Int = 1,
    val shotCount: Int = 3
)

@Entity(tableName = "generations")
data class GenerationEntity(
    @PrimaryKey val id: String,
    val projectId: Long = 1L,
    val providerId: String,
    val modelId: String,
    val prompt: String,
    val negativePrompt: String = "",
    val capability: String,
    val status: String,
    val progress: Float = 0f,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val estimatedCost: String = "Free",
    val outputUrls: String = "", // Comma-separated or JSON list
    val outputType: String = "image",
    val localFileUris: String = "",
    val errorMessage: String? = null,
    val rawRequest: String? = null,
    val rawResponse: String? = null
)

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: String,
    val name: String,
    val referenceUri: String = "",
    val description: String = "",
    val appearance: String = "",
    val clothing: String = "",
    val hair: String = "",
    val voice: String = "Adam (ElevenLabs)",
    val style: String = "Cinematic Hyper-realistic",
    val identityToken: String = "[char_token]"
)

@Entity(tableName = "elements")
data class ElementEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String, // Product, Character, Location, Vehicle, Prop, Logo, Object
    val referenceUri: String = "",
    val description: String = "",
    val promptTag: String = ""
)

@Entity(tableName = "custom_providers")
data class CustomProviderEntity(
    @PrimaryKey val id: String,
    val name: String,
    val iconKey: String = "custom",
    val baseUrl: String,
    val authType: String,
    val endpoint: String = "/v1/generate",
    val httpMethod: String = "POST",
    val contentType: String = "application/json",
    val authHeaderName: String = "Authorization",
    val apiKeyEncrypted: String = "",
    val statusEndpoint: String = "",
    val resultJsonPath: String = "output.url",
    val errorJsonPath: String = "error.message",
    val inputMappingsJson: String = "",
    val isConfigured: Boolean = false
)

@Entity(tableName = "storyboard_shots")
data class StoryboardShotEntity(
    @PrimaryKey val shotId: String,
    val projectId: Long = 1L,
    val sceneNumber: Int,
    val shotNumber: Int,
    val title: String,
    val prompt: String,
    val referenceUri: String? = null,
    val characterName: String? = null,
    val location: String = "Studio Stage",
    val cameraMotionJson: String = "",
    val durationSeconds: Int = 4,
    val aspectRatio: String = "16:9",
    val providerId: String = "higgsfield",
    val modelId: String = "kling-video/v2.5-turbo/pro/image-to-video",
    val generatedResultUri: String? = null,
    val isGenerated: Boolean = false
)

@Entity(tableName = "workflows")
data class WorkflowEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val nodesJson: String,
    val isTemplate: Boolean = false
)

@Entity(tableName = "api_logs")
data class ApiLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
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
