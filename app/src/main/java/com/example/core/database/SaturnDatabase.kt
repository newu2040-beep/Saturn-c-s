package com.example.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProjectEntity::class,
        GenerationEntity::class,
        CharacterEntity::class,
        ElementEntity::class,
        CustomProviderEntity::class,
        StoryboardShotEntity::class,
        WorkflowEntity::class,
        ApiLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SaturnDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun generationDao(): GenerationDao
    abstract fun characterDao(): CharacterDao
    abstract fun elementDao(): ElementDao
    abstract fun customProviderDao(): CustomProviderDao
    abstract fun storyboardDao(): StoryboardDao
    abstract fun workflowDao(): WorkflowDao
    abstract fun apiLogDao(): ApiLogDao

    companion object {
        @Volatile
        private var INSTANCE: SaturnDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): SaturnDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SaturnDatabase::class.java,
                    "saturn_c_database.db"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }
        }

        private suspend fun populateInitialData(database: SaturnDatabase) {
            // Seed initial project
            val defaultProject = ProjectEntity(
                id = 1L,
                name = "Aero Prime Commercial",
                description = "High-octane product launch with anamorphic 35mm lens and volumetric neon lighting.",
                coverUri = "",
                sceneCount = 2,
                shotCount = 4,
                version = "v1.2"
            )
            database.projectDao().insertProject(defaultProject)

            val cinemaProject = ProjectEntity(
                id = 2L,
                name = "Chronicles of Saturn: Teaser",
                description = "Sci-fi cinematic visual sequence exploring deep space orbital rings.",
                coverUri = "",
                sceneCount = 3,
                shotCount = 6,
                version = "v1.0"
            )
            database.projectDao().insertProject(cinemaProject)

            // Seed initial characters
            database.characterDao().insertCharacter(
                CharacterEntity(
                    id = "char_01",
                    name = "Aria Vance",
                    description = "Lead protagonist, sharp jawline, high-tech flight jacket with cybernetic sleeve.",
                    appearance = "Athletic build, hazel eyes, sharp cheekbones",
                    clothing = "Matte obsidian bomber jacket, silver trim",
                    hair = "Sleek obsidian bob cut",
                    voice = "Aria (ElevenLabs)",
                    style = "Anamorphic 35mm Cyberpunk Cinema",
                    identityToken = "[char_aria_vance]"
                )
            )

            database.characterDao().insertCharacter(
                CharacterEntity(
                    id = "char_02",
                    name = "Commander Kaelen",
                    description = "Veteran space explorer, weathered facial features, titanium field suit.",
                    appearance = "Tall, silver-streaked beard, intense amber gaze",
                    clothing = "Pressurized carbon-fiber combat rig",
                    hair = "Close cropped silver hair",
                    voice = "Adam (ElevenLabs)",
                    style = "Kodak 500T 35mm Film Grain",
                    identityToken = "[char_kaelen]"
                )
            )

            // Seed initial elements
            database.elementDao().insertElement(
                ElementEntity(
                    id = "elem_01",
                    name = "Black Titan Camera",
                    category = "Product",
                    description = "Modular cinema camera with gold accent dial and matte carbon fiber body.",
                    promptTag = "[elem_black_camera]"
                )
            )

            database.elementDao().insertElement(
                ElementEntity(
                    id = "elem_02",
                    name = "Neo-Tokyo Penthouse",
                    category = "Location",
                    description = "Panoramic wet neon glass balcony overlooking rainy skyscrapers.",
                    promptTag = "[elem_tokyo_penthouse]"
                )
            )

            database.elementDao().insertElement(
                ElementEntity(
                    id = "elem_03",
                    name = "Aero GT Electric Supercar",
                    category = "Vehicle",
                    description = "Sleek satin crimson hypercar with active aero spoiler and glowing cyan taillights.",
                    promptTag = "[elem_aero_gt]"
                )
            )

            // Seed initial Storyboard shots for project 1
            database.storyboardDao().insertShots(
                listOf(
                    StoryboardShotEntity(
                        shotId = "shot_01",
                        projectId = 1L,
                        sceneNumber = 1,
                        shotNumber = 1,
                        title = "Hero Reveal",
                        prompt = "Close up product macro shot of Black Titan Camera on spinning titanium turntable, amber volumetric rim light.",
                        characterName = null,
                        location = "Studio Dark Stage",
                        cameraMotionJson = "Dolly In + Slow Orbit",
                        durationSeconds = 4,
                        aspectRatio = "16:9",
                        providerId = "higgsfield",
                        modelId = "kling-video/v2.5-turbo/pro/image-to-video",
                        isGenerated = false
                    ),
                    StoryboardShotEntity(
                        shotId = "shot_02",
                        projectId = 1L,
                        sceneNumber = 1,
                        shotNumber = 2,
                        title = "Cinematic Pullback",
                        prompt = "Wide shot of neon rain reflection on asphalt as Aero GT supercar glides smoothly past camera at 60fps.",
                        characterName = null,
                        location = "Rainy Boulevard",
                        cameraMotionJson = "Tracking Right + Crane Down",
                        durationSeconds = 6,
                        aspectRatio = "16:9",
                        providerId = "google",
                        modelId = "veo-3.1-generate-preview",
                        isGenerated = false
                    ),
                    StoryboardShotEntity(
                        shotId = "shot_03",
                        projectId = 1L,
                        sceneNumber = 2,
                        shotNumber = 3,
                        title = "Character Reaction",
                        prompt = "Medium portrait shot of Aria Vance checking holographic wrist console inside dark cockpit, cockpit glow on visor.",
                        characterName = "Aria Vance",
                        location = "Cockpit Interior",
                        cameraMotionJson = "Handheld Subtle Shake",
                        durationSeconds = 5,
                        aspectRatio = "16:9",
                        providerId = "runway",
                        modelId = "gen4.5",
                        isGenerated = false
                    )
                )
            )

            // Seed initial reusable Workflows
            database.workflowDao().insertWorkflow(
                WorkflowEntity(
                    id = "wf_cinematic_commercial",
                    name = "Cinematic Product Commercial",
                    description = "End-to-end pipeline: Prompt → Image Model → Upscaler → Image-to-Video → Voice Dubbing → Color Grade",
                    nodesJson = "Prompt -> Flux Pro -> 4K Upscale -> Kling v2.5 -> ElevenLabs TTS -> Cinematic LUT -> MP4 4K",
                    isTemplate = true
                )
            )

            database.workflowDao().insertWorkflow(
                WorkflowEntity(
                    id = "wf_social_ugc",
                    name = "Social UGC Vertical (9:16)",
                    description = "Fast turnaround pipeline optimized for TikTok, Reels and Shorts with auto-captioning.",
                    nodesJson = "Prompt -> Nano Banana Pro -> Veo 3.1 Fast -> Speech-01 -> Auto-Captions -> 9:16 Export",
                    isTemplate = true
                )
            )
        }
    }
}
