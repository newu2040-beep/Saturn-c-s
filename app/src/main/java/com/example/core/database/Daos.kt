package com.example.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY updatedAt DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: Long): ProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity): Long

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProjectById(id: Long)
}

@Dao
interface GenerationDao {
    @Query("SELECT * FROM generations ORDER BY createdAt DESC")
    fun getAllGenerations(): Flow<List<GenerationEntity>>

    @Query("SELECT * FROM generations WHERE projectId = :projectId ORDER BY createdAt DESC")
    fun getGenerationsForProject(projectId: Long): Flow<List<GenerationEntity>>

    @Query("SELECT * FROM generations WHERE status IN ('QUEUED', 'UPLOADING', 'PROCESSING', 'DOWNLOADING') ORDER BY createdAt ASC")
    fun getActiveQueue(): Flow<List<GenerationEntity>>

    @Query("SELECT * FROM generations WHERE id = :id LIMIT 1")
    suspend fun getGenerationById(id: String): GenerationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeneration(generation: GenerationEntity)

    @Update
    suspend fun updateGeneration(generation: GenerationEntity)

    @Query("DELETE FROM generations WHERE id = :id")
    suspend fun deleteGenerationById(id: String)
}

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters ORDER BY name ASC")
    fun getAllCharacters(): Flow<List<CharacterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity)

    @Query("DELETE FROM characters WHERE id = :id")
    suspend fun deleteCharacterById(id: String)
}

@Dao
interface ElementDao {
    @Query("SELECT * FROM elements ORDER BY name ASC")
    fun getAllElements(): Flow<List<ElementEntity>>

    @Query("SELECT * FROM elements WHERE category = :category ORDER BY name ASC")
    fun getElementsByCategory(category: String): Flow<List<ElementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElement(element: ElementEntity)

    @Query("DELETE FROM elements WHERE id = :id")
    suspend fun deleteElementById(id: String)
}

@Dao
interface CustomProviderDao {
    @Query("SELECT * FROM custom_providers ORDER BY name ASC")
    fun getAllCustomProviders(): Flow<List<CustomProviderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomProvider(provider: CustomProviderEntity)

    @Query("DELETE FROM custom_providers WHERE id = :id")
    suspend fun deleteCustomProviderById(id: String)
}

@Dao
interface StoryboardDao {
    @Query("SELECT * FROM storyboard_shots WHERE projectId = :projectId ORDER BY sceneNumber ASC, shotNumber ASC")
    fun getShotsForProject(projectId: Long): Flow<List<StoryboardShotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShot(shot: StoryboardShotEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShots(shots: List<StoryboardShotEntity>)

    @Update
    suspend fun updateShot(shot: StoryboardShotEntity)

    @Query("DELETE FROM storyboard_shots WHERE shotId = :shotId")
    suspend fun deleteShotById(shotId: String)
}

@Dao
interface WorkflowDao {
    @Query("SELECT * FROM workflows ORDER BY name ASC")
    fun getAllWorkflows(): Flow<List<WorkflowEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkflow(workflow: WorkflowEntity)

    @Query("DELETE FROM workflows WHERE id = :id")
    suspend fun deleteWorkflowById(id: String)
}

@Dao
interface ApiLogDao {
    @Query("SELECT * FROM api_logs ORDER BY timestamp DESC LIMIT 100")
    fun getRecentLogs(): Flow<List<ApiLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ApiLogEntity)

    @Query("DELETE FROM api_logs")
    suspend fun clearLogs()
}
