package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.AiCapability
import com.example.core.model.AiModel
import com.example.core.model.AiProvider
import com.example.ui.theme.SaturnTheme
import com.example.ui.viewmodel.SaturnViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ModelHubScreen(
    viewModel: SaturnViewModel,
    onNavigate: (String) -> Unit
) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact

    val providers by viewModel.providers.collectAsState()
    val selectedModel by viewModel.selectedModel.collectAsState()
    var selectedCategory by remember { mutableStateOf<AiCapability?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val allModels = remember(providers) {
        providers.flatMap { provider ->
            provider.models.map { model -> Pair(provider, model) }
        }
    }

    val filteredModels = remember(allModels, selectedCategory, searchQuery) {
        allModels.filter { (provider, model) ->
            val matchCat = selectedCategory == null || model.capability == selectedCategory
            val matchQuery = searchQuery.isBlank() ||
                model.displayName.contains(searchQuery, ignoreCase = true) ||
                provider.name.contains(searchQuery, ignoreCase = true)
            matchCat && matchQuery
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(horizontal = if (isCompact) 8.dp else 16.dp),
        verticalArrangement = Arrangement.spacedBy(if (isCompact) 10.dp else 14.dp),
        contentPadding = PaddingValues(top = if (isCompact) 4.dp else 8.dp, bottom = 100.dp)
    ) {
        // Title Header
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("MODEL HUB", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text("Global AI Model Ecosystem", color = colors.textPrimary, fontSize = if (isCompact) 18.sp else 22.sp, fontWeight = FontWeight.Bold)
                Text("${filteredModels.size} production models available across ${providers.size} providers • Zero Paywalls", color = colors.secondaryAccent, fontSize = 12.sp)
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search DeepSeek, GLM, OpenAI, Claude, Wan, Gemini...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = colors.textSecondary) },
                shape = CircleShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primaryAccent,
                    unfocusedBorderColor = colors.border,
                    focusedTextColor = colors.textPrimary,
                    unfocusedTextColor = colors.textPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("model_search_input")
            )
        }

        // Capability Category Tabs
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    CategoryTab(
                        label = "All Models",
                        isSelected = selectedCategory == null,
                        onClick = { selectedCategory = null }
                    )
                }
                items(AiCapability.values()) { cap ->
                    CategoryTab(
                        label = cap.label,
                        isSelected = selectedCategory == cap,
                        onClick = { selectedCategory = cap }
                    )
                }
            }
        }

        // Model Cards List
        items(filteredModels) { (provider, model) ->
            val isCurrent = model.id == selectedModel.id
            ModelCard(
                provider = provider,
                model = model,
                isSelected = isCurrent,
                onSelect = {
                    viewModel.selectModel(model)
                    onNavigate("create")
                }
            )
        }
    }
}

@Composable
private fun CategoryTab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = SaturnTheme.colors
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (isSelected) colors.primaryAccent else colors.surfaceCard)
            .border(1.dp, if (isSelected) colors.primaryAccent else colors.border, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) (if (colors.isDark) colors.background else Color.White) else colors.textPrimary,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ModelCard(
    provider: AiProvider,
    model: AiModel,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isSelected) colors.primaryAccent else colors.border,
                RoundedCornerShape(28.dp)
            )
    ) {
        Column(modifier = Modifier.padding(if (isCompact) 10.dp else 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(colors.primaryAccent.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(provider.name, color = colors.primaryAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(colors.secondaryAccent.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(model.capability.label, color = colors.secondaryAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Text("Zero Paywall", color = Color(0xFF34D399), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(model.displayName, color = colors.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(4.dp))
            Text("Speed: ${model.speedScore} • Quality: ${model.qualityScore}", color = colors.primaryAccent, fontSize = 11.sp)

            Spacer(modifier = Modifier.height(10.dp))
            // Capability Features
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (model.supportsImageToVideo) FeaturePill("Image-to-Video")
                if (model.supportsAudio) FeaturePill("Native Audio")
                if (model.supportsReferenceImages) FeaturePill("Ref Images")
                if (model.supportsCharacterConsistency) FeaturePill("Character Consistent")
                if (model.supportsUpscale) FeaturePill("AI Upscale")
                model.aspectRatios.forEach { FeaturePill(it) }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = onSelect,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) colors.surfaceCardLight else colors.primaryAccent
                ),
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isSelected) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = colors.secondaryAccent, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Selected in Studio", color = colors.secondaryAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = if (colors.isDark) colors.background else Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Select & Launch Model", color = if (colors.isDark) colors.background else Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun FeaturePill(text: String) {
    val colors = SaturnTheme.colors
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(colors.surfaceCardLight)
            .border(1.dp, colors.border, CircleShape)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(text, color = colors.textSecondary, fontSize = 9.sp)
    }
}
