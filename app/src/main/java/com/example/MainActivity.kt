package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.components.CostConfirmationDialog
import com.example.ui.components.CustomAspectRatioDialog
import com.example.ui.components.GalleryPreviewDialog
import com.example.ui.components.NewProjectDialog
import com.example.ui.components.PermissionsManagerDialog
import com.example.ui.components.PromptEnhancerDialog
import com.example.ui.components.SaturnBottomNav
import com.example.ui.components.SaturnTopBar
import com.example.ui.screens.AudioStudioScreen
import com.example.ui.screens.CinemaStudioScreen
import com.example.ui.screens.CreateScreen
import com.example.ui.screens.CustomApiBuilderScreen
import com.example.ui.screens.GenerationQueueScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MarketingStudioScreen
import com.example.ui.screens.MediaEditorScreen
import com.example.ui.screens.ModelHubScreen
import com.example.ui.screens.ProjectsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StoryboardScreen
import com.example.ui.theme.SaturnTheme
import com.example.ui.viewmodel.SaturnViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SaturnViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val currentPalette by viewModel.currentThemePalette.collectAsState()
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            val isCompactMode by viewModel.isCompactMode.collectAsState()

            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val autoCompact = isCompactMode || maxWidth < 380.dp || maxHeight < 680.dp
                SaturnTheme(
                    palette = currentPalette,
                    darkTheme = isDarkMode,
                    isCompact = autoCompact
                ) {
                    SaturnApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun SaturnApp(viewModel: SaturnViewModel) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact

    val currentPalette by viewModel.currentThemePalette.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val isCompactMode by viewModel.isCompactMode.collectAsState()

    val activeScreen by viewModel.activeStudioScreen.collectAsState()
    val currentProject by viewModel.currentProject.collectAsState()
    val allProjects by viewModel.allProjects.collectAsState()
    val selectedProvider by viewModel.selectedProvider.collectAsState()
    val selectedModel by viewModel.selectedModel.collectAsState()
    val activeQueue by viewModel.activeQueue.collectAsState()

    val showCostDialog by viewModel.showCostDialog.collectAsState()
    val showEnhancerDialog by viewModel.showEnhancerDialog.collectAsState()
    val enhancedPromptResult by viewModel.enhancedPromptResult.collectAsState()
    val showNewProjectDialog by viewModel.showNewProjectDialog.collectAsState()

    val selectedResolution by viewModel.selectedResolution.collectAsState()
    val selectedDuration by viewModel.selectedDuration.collectAsState()
    val selectedAspectRatio by viewModel.selectedAspectRatio.collectAsState()
    val previewMediaJob by viewModel.previewMediaJob.collectAsState()
    val showPermissionsDialog by viewModel.showPermissionsDialog.collectAsState()
    val showCustomAspectRatioDialog by viewModel.showCustomAspectRatioDialog.collectAsState()
    val customAspectRatioCategory by viewModel.customAspectRatioCategory.collectAsState()
    val exportMasterSettings by viewModel.exportMasterSettings.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
        containerColor = colors.background,
        topBar = {
            SaturnTopBar(
                currentProject = currentProject,
                allProjects = allProjects,
                selectedProvider = selectedProvider,
                selectedModel = selectedModel,
                activeQueueCount = activeQueue.size,
                isDarkMode = isDarkMode,
                onToggleDarkMode = { viewModel.toggleDarkMode() },
                currentPalette = currentPalette,
                onSelectPalette = { viewModel.setPalette(it) },
                isCompactMode = isCompact,
                onToggleCompactMode = { viewModel.toggleCompactMode() },
                onSelectProject = { viewModel.selectProject(it) },
                onNewProjectClick = { viewModel.showNewProjectDialog.value = true },
                onQueueClick = { viewModel.activeStudioScreen.value = "queue" },
                onSettingsClick = { viewModel.activeStudioScreen.value = "settings" }
            )
        },
        bottomBar = {
            SaturnBottomNav(
                currentScreen = activeScreen,
                onNavigate = { screen ->
                    viewModel.activeStudioScreen.value = screen
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = activeScreen,
                transitionSpec = {
                    (slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth / 4 },
                        animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow)
                    ) + fadeIn(animationSpec = tween(220)))
                    .togetherWith(
                        slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth / 4 },
                            animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow)
                        ) + fadeOut(animationSpec = tween(180))
                    )
                },
                label = "StudioScreenTransition"
            ) { targetScreen ->
                when (targetScreen) {
                    "home" -> HomeScreen(
                        viewModel = viewModel,
                        onNavigate = { viewModel.activeStudioScreen.value = it }
                    )
                    "create" -> CreateScreen(
                        viewModel = viewModel,
                        onNavigate = { viewModel.activeStudioScreen.value = it }
                    )
                    "cinema" -> CinemaStudioScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.activeStudioScreen.value = "create" }
                    )
                    "storyboard" -> StoryboardScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.activeStudioScreen.value = "home" }
                    )
                    "editor" -> MediaEditorScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.activeStudioScreen.value = "home" }
                    )
                    "audio" -> AudioStudioScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.activeStudioScreen.value = "home" }
                    )
                    "marketing" -> MarketingStudioScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.activeStudioScreen.value = "home" }
                    )
                    "custom_api" -> CustomApiBuilderScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.activeStudioScreen.value = "home" }
                    )
                    "hub" -> ModelHubScreen(
                        viewModel = viewModel,
                        onNavigate = { viewModel.activeStudioScreen.value = it }
                    )
                    "queue" -> GenerationQueueScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.activeStudioScreen.value = "home" }
                    )
                    "projects" -> ProjectsScreen(
                        viewModel = viewModel,
                        onNavigate = { viewModel.activeStudioScreen.value = it }
                    )
                    "settings" -> SettingsScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.activeStudioScreen.value = "home" }
                    )
                    else -> HomeScreen(
                        viewModel = viewModel,
                        onNavigate = { viewModel.activeStudioScreen.value = it }
                    )
                }
            }
        }
    }

    // Cost Confirmation Safety Dialog
    if (showCostDialog) {
        CostConfirmationDialog(
            providerName = selectedProvider.name,
            modelName = selectedModel.displayName,
            resolution = selectedResolution,
            duration = selectedDuration,
            estimatedCost = "Zero Paywall • Unrestricted BYOK",
            onConfirm = {
                viewModel.showCostDialog.value = false
                viewModel.generate()
            },
            onDismiss = {
                viewModel.showCostDialog.value = false
            }
        )
    }

    // Prompt Enhancer Preview Dialog
    if (showEnhancerDialog) {
        enhancedPromptResult?.let { result ->
            PromptEnhancerDialog(
                result = result,
                onApply = { viewModel.applyEnhancedPrompt() },
                onDismiss = { viewModel.showEnhancerDialog.value = false }
            )
        }
    }

    // New Project Dialog
    if (showNewProjectDialog) {
        NewProjectDialog(
            onConfirm = { name, desc ->
                viewModel.createNewProject(name, desc)
                viewModel.showNewProjectDialog.value = false
            },
            onDismiss = { viewModel.showNewProjectDialog.value = false }
        )
    }

    // Inbuilt Gallery Preview Dialog (Video Player / Audio Waveform Player / High-Quality Master Exports)
    previewMediaJob?.let { job ->
        GalleryPreviewDialog(
            job = job,
            exportSettings = exportMasterSettings,
            onDismiss = { viewModel.closeMediaPreview() },
            onExportHighQuality = { settings: com.example.core.model.ExportMasterSettings ->
                viewModel.updateExportSettings(settings)
                viewModel.closeMediaPreview()
            }
        )
    }

    // Permissions Manager Dialog Flow
    if (showPermissionsDialog) {
        PermissionsManagerDialog(
            onDismiss = { viewModel.dismissPermissionsDialog() },
            onAllGranted = {
                viewModel.permissionsGrantedState.value = true
                viewModel.dismissPermissionsDialog()
            }
        )
    }

    // Custom Aspect Ratio & Framing Director Dialog
    if (showCustomAspectRatioDialog) {
        CustomAspectRatioDialog(
            initialRatio = selectedAspectRatio,
            initialCategory = customAspectRatioCategory,
            onDismiss = { viewModel.closeCustomAspectRatio() },
            onApplyRatio = { ratio, w, h ->
                viewModel.applyCustomAspectRatio(ratio, w, h)
            }
        )
    }
}
