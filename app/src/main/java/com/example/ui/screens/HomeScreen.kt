package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.ViewKanban
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SaturnBorder
import com.example.ui.theme.SaturnBorderHighlight
import com.example.ui.theme.SaturnCyan
import com.example.ui.theme.SaturnGold
import com.example.ui.theme.SaturnObsidian
import com.example.ui.theme.SaturnSurface
import com.example.ui.theme.SaturnSurfaceCard
import com.example.ui.theme.SaturnTextPrimary
import com.example.ui.theme.SaturnTextSecondary
import com.example.ui.theme.SaturnViolet
import com.example.ui.viewmodel.SaturnViewModel

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.ui.theme.SaturnTheme

@Composable
fun HomeScreen(
    viewModel: SaturnViewModel,
    onNavigate: (String) -> Unit
) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact

    val currentProject by viewModel.currentProject.collectAsState()
    val promptText by viewModel.promptText.collectAsState()
    val selectedProvider by viewModel.selectedProvider.collectAsState()
    val selectedModel by viewModel.selectedModel.collectAsState()
    val selectedAspectRatio by viewModel.selectedAspectRatio.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(horizontal = if (isCompact) 8.dp else 16.dp),
        verticalArrangement = Arrangement.spacedBy(if (isCompact) 10.dp else 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Hero Branding Header with Circular App Icon
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = if (isCompact) 4.dp else 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Strictly Circular App Icon
                    Box(
                        modifier = Modifier
                            .size(if (isCompact) 44.dp else 52.dp)
                            .clip(CircleShape)
                            .background(colors.surfaceCardLight)
                            .border(2.dp, colors.primaryAccent, CircleShape)
                            .padding(2.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.saturn_circle_icon_1789712389721),
                            contentDescription = "Saturn App Icon",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "SATURN C",
                                color = colors.textPrimary,
                                fontSize = if (isCompact) 20.sp else 24.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.5.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(colors.primaryAccent.copy(alpha = 0.2f))
                                    .border(1.dp, colors.primaryAccent, CircleShape)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "STUDIO",
                                    color = colors.primaryAccent,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = "Create beyond reality.",
                            color = colors.secondaryAccent,
                            fontSize = if (isCompact) 11.sp else 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Quick Aspect Ratio Pill
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(colors.surfaceCardLight)
                        .border(1.dp, colors.border, CircleShape)
                        .clickable { viewModel.openCustomAspectRatio() }
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = colors.primaryAccent,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = selectedAspectRatio,
                            color = colors.textPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Quick Launch Floating Studio Prompt Bar
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.borderHighlight, RoundedCornerShape(28.dp))
            ) {
                Column(modifier = Modifier.padding(if (isCompact) 10.dp else 16.dp)) {
                    Text(
                        text = "FAST CREATIVE PROMPT",
                        color = colors.primaryAccent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = promptText,
                        color = SaturnTextPrimary,
                        fontSize = 14.sp,
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextButton(
                                onClick = { viewModel.triggerPromptEnhancement() },
                                modifier = Modifier.testTag("home_enhance_button")
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SaturnViolet, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Enhance", color = SaturnViolet, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Button(
                            onClick = { onNavigate("create") },
                            colors = ButtonDefaults.buttonColors(containerColor = SaturnGold),
                            shape = CircleShape,
                            modifier = Modifier.testTag("home_open_studio_button")
                        ) {
                            Text("Open in Studio", color = SaturnObsidian, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Active Project Showcase Card
        item {
            currentProject?.let { project ->
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131722)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SaturnBorder, RoundedCornerShape(28.dp))
                        .clickable { onNavigate("projects") }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "ACTIVE PROJECT",
                                    color = SaturnTextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color(0xFF212738))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(project.version, color = SaturnCyan, fontSize = 10.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = project.name,
                                color = SaturnTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${project.sceneCount} Scenes • ${project.shotCount} Shots",
                                color = SaturnTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                        Button(
                            onClick = { onNavigate("storyboard") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2436)),
                            shape = CircleShape
                        ) {
                            Text("Storyboard", color = SaturnCyan, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // Creative Studios & Tool Workspaces Grid Header
        item {
            Text(
                text = "CREATIVE WORKSPACES",
                color = SaturnTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        // Grid of Studios
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StudioTile(
                        title = "Cinema Studio",
                        subtitle = "Camera, 35mm Lens & LUT",
                        icon = Icons.Default.Movie,
                        accentColor = SaturnGold,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("cinema") }
                    )
                    StudioTile(
                        title = "Storyboard Studio",
                        subtitle = "Shot by shot director breakdown",
                        icon = Icons.Default.ViewKanban,
                        accentColor = SaturnCyan,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("storyboard") }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StudioTile(
                        title = "Media & Filter Editor",
                        subtitle = "GPU Grade, inpaint & upscale",
                        icon = Icons.Default.Image,
                        accentColor = SaturnViolet,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("editor") }
                    )
                    StudioTile(
                        title = "Audio Studio",
                        subtitle = "TTS Voice, Sound FX & Music",
                        icon = Icons.Default.GraphicEq,
                        accentColor = Color(0xFF34D399),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("audio") }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StudioTile(
                        title = "Marketing Studio",
                        subtitle = "Product Hero, TikTok 9:16 & Ads",
                        icon = Icons.Default.Campaign,
                        accentColor = Color(0xFFFB7185),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("marketing") }
                    )
                    StudioTile(
                        title = "Custom API Builder",
                        subtitle = "Integrate any AI model endpoint",
                        icon = Icons.Default.Api,
                        accentColor = SaturnGold,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("custom_api") }
                    )
                }
            }
        }

        // Curated Inspiration & Remix Showcase
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "INSPIRATION & REMIX",
                    color = SaturnTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        item {
            val inspirations = listOf(
                InspirationItem(
                    title = "Cyberpunk Neo-Tokyo Rain",
                    prompt = "Rain-soaked neon reflections, Aero GT supercar cruising at 60fps, 35mm anamorphic lens, amber rim light",
                    model = "Kling v2.5 Turbo Pro",
                    aspect = "16:9"
                ),
                InspirationItem(
                    title = "Titan Camera Commercial",
                    prompt = "Close up product macro shot of Black Titan Camera on spinning titanium turntable, volumetric godrays",
                    model = "Nano Banana Pro (Gemini)",
                    aspect = "1:1"
                ),
                InspirationItem(
                    title = "Deep Space Orbital Teaser",
                    prompt = "Anamorphic wide establishing shot of glass exploration spacecraft hovering over Saturn icy rings, 4K HDR",
                    model = "Veo 3.1 Cinematic",
                    aspect = "21:9"
                )
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(end = 16.dp)
            ) {
                items(inspirations) { item ->
                    InspirationCard(
                        item = item,
                        onRemix = {
                            viewModel.promptText.value = item.prompt
                            onNavigate("create")
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun StudioTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = SaturnSurfaceCard),
        modifier = modifier
            .border(1.dp, SaturnBorder, RoundedCornerShape(28.dp))
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1D2230)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = accentColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(title, color = SaturnTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, color = SaturnTextSecondary, fontSize = 11.sp, maxLines = 2)
        }
    }
}

data class InspirationItem(
    val title: String,
    val prompt: String,
    val model: String,
    val aspect: String
)

@Composable
private fun InspirationCard(
    item: InspirationItem,
    onRemix: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = SaturnSurfaceCard),
        modifier = Modifier
            .width(260.dp)
            .border(1.dp, SaturnBorder, RoundedCornerShape(28.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF1D2230))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(item.aspect, color = SaturnCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                Text(item.model, color = SaturnTextSecondary, fontSize = 10.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(item.title, color = SaturnTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(item.prompt, color = SaturnTextSecondary, fontSize = 12.sp, maxLines = 3)

            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onRemix,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22293D)),
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Repeat, contentDescription = "Remix", tint = SaturnGold, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Remix Prompt", color = SaturnGold, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
