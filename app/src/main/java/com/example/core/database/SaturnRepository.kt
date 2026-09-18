package com.example.core.database

import com.example.core.model.ApiLog
import com.example.core.model.CharacterProfile
import com.example.core.model.CreativeElement
import com.example.core.model.CreativeWorkflow
import com.example.core.model.GenerationJob
import com.example.core.model.JobStatus
import com.example.core.model.SaturnProject
import com.example.core.model.StoryboardShot
import com.example.core.model.AiCapability
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SaturnRepository(private val db: SaturnDatabase) {

    // Projects
    val allProjects: Flow<List<SaturnProject>> = db.projectDao().getAllProjects().map { list ->
        list.map { entity ->
            SaturnProject(
                id = entity.id,
                name = entity.name,
                description = entity.description,
                coverUri = entity.coverUri,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                version = entity.version,
                sceneCount = entity.sceneCount,
                shotCount = entity.shotCount
            )
        }
    }

    suspend fun createProject(name: String, description: String): Long {
        return db.projectDao().insertProject(
            ProjectEntity(
                name = name,
                description = description,
                coverUri = "",
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteProject(id: Long) {
        db.projectDao().deleteProjectById(id)
    }

    // Generations
    val activeQueue: Flow<List<GenerationJob>> = db.generationDao().getActiveQueue().map { list ->
        list.map { it.toDomain() }
    }

    val allGenerations: Flow<List<GenerationJob>> = db.generationDao().getAllGenerations().map { list ->
        list.map { it.toDomain() }
    }

    fun getGenerationsForProject(projectId: Long): Flow<List<GenerationJob>> {
        return db.generationDao().getGenerationsForProject(projectId).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun saveGeneration(job: GenerationJob) {
        db.generationDao().insertGeneration(job.toEntity())
    }

    suspend fun updateGeneration(job: GenerationJob) {
        db.generationDao().updateGeneration(job.toEntity())
    }

    suspend fun deleteGeneration(id: String) {
        db.generationDao().deleteGenerationById(id)
    }

    // Characters
    val allCharacters: Flow<List<CharacterProfile>> = db.characterDao().getAllCharacters().map { list ->
        list.map {
            CharacterProfile(
                id = it.id,
                name = it.name,
                referenceUri = it.referenceUri,
                description = it.description,
                appearance = it.appearance,
                clothing = it.clothing,
                hair = it.hair,
                voice = it.voice,
                style = it.style,
                identityToken = it.identityToken
            )
        }
    }

    suspend fun addCharacter(char: CharacterProfile) {
        db.characterDao().insertCharacter(
            CharacterEntity(
                id = char.id,
                name = char.name,
                referenceUri = char.referenceUri,
                description = char.description,
                appearance = char.appearance,
                clothing = char.clothing,
                hair = char.hair,
                voice = char.voice,
                style = char.style,
                identityToken = char.identityToken
            )
        )
    }

    suspend fun deleteCharacter(id: String) {
        db.characterDao().deleteCharacterById(id)
    }

    // Elements
    val allElements: Flow<List<CreativeElement>> = db.elementDao().getAllElements().map { list ->
        list.map {
            CreativeElement(
                id = it.id,
                name = it.name,
                category = it.category,
                referenceUri = it.referenceUri,
                description = it.description,
                promptTag = it.promptTag
            )
        }
    }

    suspend fun addElement(elem: CreativeElement) {
        db.elementDao().insertElement(
            ElementEntity(
                id = elem.id,
                name = elem.name,
                category = elem.category,
                referenceUri = elem.referenceUri,
                description = elem.description,
                promptTag = elem.promptTag
            )
        )
    }

    suspend fun deleteElement(id: String) {
        db.elementDao().deleteElementById(id)
    }

    // Storyboard Shots
    fun getShotsForProject(projectId: Long): Flow<List<StoryboardShot>> {
        return db.storyboardDao().getShotsForProject(projectId).map { list ->
            list.map {
                StoryboardShot(
                    shotId = it.shotId,
                    sceneNumber = it.sceneNumber,
                    shotNumber = it.shotNumber,
                    title = it.title,
                    prompt = it.prompt,
                    referenceUri = it.referenceUri,
                    characterName = it.characterName,
                    location = it.location,
                    durationSeconds = it.durationSeconds,
                    aspectRatio = it.aspectRatio,
                    providerId = it.providerId,
                    modelId = it.modelId,
                    generatedResultUri = it.generatedResultUri,
                    isGenerated = it.isGenerated
                )
            }
        }
    }

    suspend fun addShot(shot: StoryboardShot, projectId: Long) {
        db.storyboardDao().insertShot(
            StoryboardShotEntity(
                shotId = shot.shotId,
                projectId = projectId,
                sceneNumber = shot.sceneNumber,
                shotNumber = shot.shotNumber,
                title = shot.title,
                prompt = shot.prompt,
                referenceUri = shot.referenceUri,
                characterName = shot.characterName,
                location = shot.location,
                durationSeconds = shot.durationSeconds,
                aspectRatio = shot.aspectRatio,
                providerId = shot.providerId,
                modelId = shot.modelId,
                generatedResultUri = shot.generatedResultUri,
                isGenerated = shot.isGenerated
            )
        )
    }

    suspend fun updateShot(shot: StoryboardShot, projectId: Long) {
        db.storyboardDao().updateShot(
            StoryboardShotEntity(
                shotId = shot.shotId,
                projectId = projectId,
                sceneNumber = shot.sceneNumber,
                shotNumber = shot.shotNumber,
                title = shot.title,
                prompt = shot.prompt,
                referenceUri = shot.referenceUri,
                characterName = shot.characterName,
                location = shot.location,
                durationSeconds = shot.durationSeconds,
                aspectRatio = shot.aspectRatio,
                providerId = shot.providerId,
                modelId = shot.modelId,
                generatedResultUri = shot.generatedResultUri,
                isGenerated = shot.isGenerated
            )
        )
    }

    // Workflows
    val allWorkflows: Flow<List<CreativeWorkflow>> = db.workflowDao().getAllWorkflows().map { list ->
        list.map {
            CreativeWorkflow(
                id = it.id,
                name = it.name,
                description = it.description,
                nodes = emptyList(), // Can parse node list or node string
                isTemplate = it.isTemplate
            )
        }
    }

    // API Logs
    val recentLogs: Flow<List<ApiLog>> = db.apiLogDao().getRecentLogs().map { list ->
        list.map {
            ApiLog(
                id = it.id,
                providerId = it.providerId,
                modelId = it.modelId,
                requestId = it.requestId,
                timestamp = it.timestamp,
                durationMs = it.durationMs,
                httpStatus = it.httpStatus,
                status = it.status,
                sanitizedParams = it.sanitizedParams,
                responseMetadata = it.responseMetadata
            )
        }
    }

    suspend fun logApiCall(log: ApiLog) {
        db.apiLogDao().insertLog(
            ApiLogEntity(
                providerId = log.providerId,
                modelId = log.modelId,
                requestId = log.requestId,
                timestamp = log.timestamp,
                durationMs = log.durationMs,
                httpStatus = log.httpStatus,
                status = log.status,
                sanitizedParams = log.sanitizedParams,
                responseMetadata = log.responseMetadata
            )
        )
    }

    suspend fun clearLogs() {
        db.apiLogDao().clearLogs()
    }

    private fun GenerationEntity.toDomain(): GenerationJob {
        val cap = try {
            AiCapability.valueOf(capability)
        } catch (_: Exception) {
            AiCapability.IMAGE
        }
        val jobStatus = try {
            JobStatus.valueOf(status)
        } catch (_: Exception) {
            JobStatus.QUEUED
        }
        return GenerationJob(
            id = id,
            projectId = projectId,
            providerId = providerId,
            modelId = modelId,
            prompt = prompt,
            negativePrompt = negativePrompt,
            capability = cap,
            status = jobStatus,
            progress = progress,
            createdAt = createdAt,
            completedAt = completedAt,
            estimatedCost = estimatedCost,
            outputUrls = if (outputUrls.isBlank()) emptyList() else outputUrls.split(";;;"),
            outputType = outputType,
            localFileUris = if (localFileUris.isBlank()) emptyList() else localFileUris.split(";;;"),
            errorMessage = errorMessage,
            rawRequest = rawRequest,
            rawResponse = rawResponse
        )
    }

    private fun GenerationJob.toEntity(): GenerationEntity {
        return GenerationEntity(
            id = id,
            projectId = projectId,
            providerId = providerId,
            modelId = modelId,
            prompt = prompt,
            negativePrompt = negativePrompt,
            capability = capability.name,
            status = status.name,
            progress = progress,
            createdAt = createdAt,
            completedAt = completedAt,
            estimatedCost = estimatedCost,
            outputUrls = outputUrls.joinToString(";;;"),
            outputType = outputType,
            localFileUris = localFileUris.joinToString(";;;"),
            errorMessage = errorMessage,
            rawRequest = rawRequest,
            rawResponse = rawResponse
        )
    }
}
