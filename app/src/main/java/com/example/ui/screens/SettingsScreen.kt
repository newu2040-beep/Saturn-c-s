package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitScreen
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.ui.components.PermissionsManagerDialog
import com.example.ui.theme.SaturnTheme
import com.example.ui.theme.SaturnThemePalette
import com.example.ui.viewmodel.SaturnViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    viewModel: SaturnViewModel,
    onBack: () -> Unit
) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact

    val currentPalette by viewModel.currentThemePalette.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val isCompactMode by viewModel.isCompactMode.collectAsState()
    val exportMasterSettings by viewModel.exportMasterSettings.collectAsState()
    val apiTestStatusMap by viewModel.apiTestStatusMap.collectAsState()

    var showPermissionsModal by remember { mutableStateOf(false) }
    var editingProviderId by remember { mutableStateOf<String?>(null) }
    var editingProviderName by remember { mutableStateOf("") }
    var enteredKey by remember { mutableStateOf("") }

    val providers = listOf(
        Pair("google", "Google Gemini / Imagen 3 / Veo"),
        Pair("deepseek", "DeepSeek (V3 & R1 Reasoning)"),
        Pair("glm", "GLM (Zhipu AI GLM-4 & CogVideoX)"),
        Pair("openai", "OpenAI (Sora 2, o3-mini & DALL-E 3)"),
        Pair("claude", "Anthropic Claude (3.7 Sonnet & Opus)"),
        Pair("wan", "Wan 2.1 (Alibaba Open Video & Image)"),
        Pair("midjourney", "Midjourney (v7 & Niji 6)"),
        Pair("mistral", "Mistral AI (Large 2 & Pixtral)"),
        Pair("higgsfield", "Higgsfield AI (DoP / Cinema)"),
        Pair("runway", "Runway (Gen-3 Alpha)"),
        Pair("kling", "Kling AI (v2.5 Turbo Pro)"),
        Pair("fal", "fal.ai (Flux.1 Dev & Schnell)"),
        Pair("elevenlabs", "ElevenLabs (Multilingual Audio)"),
        Pair("replicate", "Replicate (Open-Source Models)")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(horizontal = if (isCompact) 8.dp else 16.dp),
        verticalArrangement = Arrangement.spacedBy(if (isCompact) 10.dp else 14.dp),
        contentPadding = PaddingValues(top = if (isCompact) 4.dp else 8.dp, bottom = 100.dp)
    ) {
        // Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = colors.textPrimary)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text("STUDIO SETTINGS", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text("Themes, Master Exports & Vault", color = colors.textPrimary, fontSize = if (isCompact) 15.sp else 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Developer Credit Card ("Made with ❤️ by Rahul Shah")
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.primaryAccent.copy(alpha = 0.6f), RoundedCornerShape(28.dp))
                    .testTag("developer_credit_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(if (isCompact) 12.dp else 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(colors.primaryAccent.copy(alpha = 0.2f))
                            .border(1.dp, colors.primaryAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFF43F5E), modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("DEVELOPER CREATION", color = colors.primaryAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Text("Made with ❤️ by Rahul Shah", color = colors.textPrimary, fontSize = 15.sp, fontWeight = FontWeight.Black)
                        Text(
                            "Universal AI Creative Studio • Android 15 Edge-to-Edge • Zero-Paywall Architecture",
                            color = colors.textSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Permissions Manager Section Card
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.border, RoundedCornerShape(28.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(colors.primaryAccent.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Permissions & Device Access", color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Notifications, Gallery storage, Mic & Full Access", color = colors.textSecondary, fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = { showPermissionsModal = true },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = colors.surfaceCardLight),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.primaryAccent.copy(alpha = 0.5f)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("manage_permissions_button")
                    ) {
                        Text("Manage", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Zero Paywall Guarantee Card
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.primaryAccent.copy(alpha = 0.4f), RoundedCornerShape(28.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(if (isCompact) 10.dp else 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(colors.primaryAccent.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.LockOpen, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Zero Paywalls • Unrestricted Studio", color = colors.primaryAccent, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "All multi-provider AI models (DeepSeek, GLM, OpenAI, Claude, Wan, Gemini) are 100% unlocked. Run with free AI Studio tiers or direct Bring-Your-Own-Key without subscriptions.",
                            color = colors.textSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Appearance & Themes Section Header
        item {
            Text("THEME & VISUALS", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }

        // Dark / Light Mode Card
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.border, RoundedCornerShape(28.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = null,
                            tint = colors.primaryAccent,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isDarkMode) "Dark Canvas" else "Light Canvas",
                                color = colors.textPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (isDarkMode) "High-contrast dark mode for cinematic studios" else "Clean, high-visibility light theme",
                                color = colors.textSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { viewModel.toggleDarkMode() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = colors.background,
                            checkedTrackColor = colors.primaryAccent,
                            uncheckedThumbColor = colors.textSecondary,
                            uncheckedTrackColor = colors.surfaceCardLight
                        ),
                        modifier = Modifier.testTag("dark_mode_switch")
                    )
                }
            }
        }

        // Pastel Palettes Selector Card
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.border, RoundedCornerShape(28.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Palette, contentDescription = null, tint = colors.secondaryAccent, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pastel & Studio Color Palettes",
                            color = colors.textPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Choose from elegant studio and pastel aesthetics. Themes dynamically apply to all screens and controls.",
                        color = colors.textSecondary,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SaturnThemePalette.values().forEach { palette ->
                            val isSelected = palette == currentPalette
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (isSelected) colors.primaryAccent.copy(alpha = 0.2f) else colors.surfaceCardLight)
                                    .border(
                                        1.dp,
                                        if (isSelected) colors.primaryAccent else colors.border,
                                        CircleShape
                                    )
                                    .clickable { viewModel.setPalette(palette) }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                                    .testTag("theme_palette_${palette.name.lowercase()}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clip(CircleShape)
                                            .background(palette.previewColor)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(palette.secondaryPreview)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = palette.label,
                                        color = if (isSelected) colors.primaryAccent else colors.textPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                    if (isSelected) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = colors.primaryAccent,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Compact Mode Card
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.border, RoundedCornerShape(28.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FitScreen,
                            contentDescription = null,
                            tint = colors.primaryAccent,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Compact Mode for Small Displays",
                                color = colors.textPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Dynamically shrinks paddings, top bars, bottom navigations, and tool cards so the entire UI fits smoothly on smaller phone screens.",
                                color = colors.textSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Switch(
                        checked = isCompactMode,
                        onCheckedChange = { viewModel.toggleCompactMode() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = colors.background,
                            checkedTrackColor = colors.primaryAccent,
                            uncheckedThumbColor = colors.textSecondary,
                            uncheckedTrackColor = colors.surfaceCardLight
                        ),
                        modifier = Modifier.testTag("compact_mode_switch")
                    )
                }
            }
        }

        // High Quality Master Exports Section Header
        item {
            Text("HIGH QUALITY EXPORT PRESETS", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }

        // High Quality Exports Configuration Card
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.border, RoundedCornerShape(28.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.HighQuality, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cinema Master Video & Audio Presets", color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Video Codec
                    Text("Master Video Codec", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    val codecs = listOf("Apple ProRes 422 HQ", "H.265 / HEVC 10-bit", "AV1 Film Master", "Lossless PNG Sequence")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(codecs) { codec ->
                            val isSel = codec == exportMasterSettings.videoCodec
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (isSel) colors.primaryAccent.copy(alpha = 0.2f) else colors.surfaceCardLight)
                                    .border(1.dp, if (isSel) colors.primaryAccent else colors.border, CircleShape)
                                    .clickable { viewModel.updateExportSettings(exportMasterSettings.copy(videoCodec = codec)) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(codec, color = if (isSel) colors.primaryAccent else colors.textPrimary, fontSize = 11.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Color Space
                    Text("Color Space & Dynamic Range", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    val colorSpaces = listOf("DCI-P3 Cinema", "Rec.709 Standard", "HDR10 PQ (1000 nits)", "ACEScc Cinema")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(colorSpaces) { cs ->
                            val isSel = cs == exportMasterSettings.colorSpace
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (isSel) colors.primaryAccent.copy(alpha = 0.2f) else colors.surfaceCardLight)
                                    .border(1.dp, if (isSel) colors.primaryAccent else colors.border, CircleShape)
                                    .clickable { viewModel.updateExportSettings(exportMasterSettings.copy(colorSpace = cs)) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(cs, color = if (isSel) colors.primaryAccent else colors.textPrimary, fontSize = 11.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Audio Export Quality
                    Text("Lossless Audio Master Export", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    val audioFormats = listOf("24-bit 96kHz Lossless FLAC", "32-bit Float WAV", "320kbps Spatial AAC")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(audioFormats) { af ->
                            val isSel = af == exportMasterSettings.audioFormat
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (isSel) colors.primaryAccent.copy(alpha = 0.2f) else colors.surfaceCardLight)
                                    .border(1.dp, if (isSel) colors.primaryAccent else colors.border, CircleShape)
                                    .clickable { viewModel.updateExportSettings(exportMasterSettings.copy(audioFormat = af)) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(af, color = if (isSel) colors.primaryAccent else colors.textPrimary, fontSize = 11.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }
                }
            }
        }

        // Provider Key Vault List Header
        item {
            Text("API CREDENTIAL VAULT & CONNECTION TESTER", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }

        // Keystore Security Guarantee Card
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCardLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.border, RoundedCornerShape(28.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(colors.primaryAccent.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = colors.secondaryAccent, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Hardware-Backed Keystore & Live Tester", color = colors.secondaryAccent, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "API credentials are encrypted with AES-256-GCM in the secure Android Keystore. Tap 'Test Connection' on any model provider to verify latency & ping instantly.",
                            color = colors.textSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        items(providers) { (id, name) ->
            val isConfigured = viewModel.keyStorage.hasApiKey(id)
            val testStatus = apiTestStatusMap[id]
            val keyVal = viewModel.keyStorage.getApiKey(id) ?: ""

            KeyVaultRow(
                name = name,
                isConfigured = isConfigured,
                testStatus = testStatus,
                onTestConnection = {
                    viewModel.testApiConnection(id, keyVal.ifBlank { "test-handshake-key-sim" })
                },
                onEdit = {
                    editingProviderId = id
                    editingProviderName = name
                    enteredKey = viewModel.keyStorage.getApiKey(id) ?: ""
                }
            )
        }

        // About Card
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.border, RoundedCornerShape(28.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(colors.surfaceCardLight)
                            .border(2.dp, colors.primaryAccent, CircleShape)
                            .padding(2.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.saturn_circle_icon_1789712389721),
                            contentDescription = "Saturn Studio Icon",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text("SATURN C", color = colors.primaryAccent, fontSize = 16.sp, fontWeight = FontWeight.Black)
                        Text("Create beyond reality.", color = colors.secondaryAccent, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Version 1.2.0 • Unrestricted Universal AI Creative Studio", color = colors.textSecondary, fontSize = 10.sp)
                        Text("Architected with ❤️ by Rahul Shah", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Key Edit & Live Test Dialog
    editingProviderId?.let { providerId ->
        val currentDialogTestStatus = apiTestStatusMap[providerId]

        AlertDialog(
            onDismissRequest = { editingProviderId = null },
            shape = RoundedCornerShape(28.dp),
            containerColor = colors.surfaceCard,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Key, contentDescription = null, tint = colors.primaryAccent)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Set $editingProviderName Key", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Enter your API credential. It will be encrypted into Android Keystore immediately. You can test the connection before saving.",
                        color = colors.textSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = enteredKey,
                        onValueChange = { enteredKey = it },
                        placeholder = { Text("sk-... or API Key") },
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primaryAccent,
                            unfocusedBorderColor = colors.border,
                            focusedTextColor = colors.textPrimary,
                            unfocusedTextColor = colors.textPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("api_key_dialog_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Test Connection In Dialog
                    Button(
                        onClick = {
                            viewModel.testApiConnection(providerId, enteredKey)
                        },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = colors.surfaceCardLight),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.primaryAccent.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("test_api_connection_dialog_button")
                    ) {
                        if (currentDialogTestStatus?.isTesting == true) {
                            CircularProgressIndicator(color = colors.primaryAccent, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Pinging API Endpoint...", color = colors.primaryAccent, fontSize = 12.sp)
                        } else {
                            Icon(Icons.Default.Refresh, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Test API Connection Now", color = colors.primaryAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Test Result Feedback
                    if (currentDialogTestStatus != null && !currentDialogTestStatus.isTesting) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (currentDialogTestStatus.success == true) Color(0xFF10B981).copy(alpha = 0.15f) else Color(0xFFEF4444).copy(alpha = 0.15f))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (currentDialogTestStatus.success == true) Icons.Default.CheckCircle else Icons.Default.Error,
                                contentDescription = null,
                                tint = if (currentDialogTestStatus.success == true) Color(0xFF10B981) else Color(0xFFEF4444),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = currentDialogTestStatus.message ?: "Connection verified",
                                color = if (currentDialogTestStatus.success == true) Color(0xFF10B981) else Color(0xFFEF4444),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (enteredKey.isNotBlank()) {
                            viewModel.keyStorage.saveApiKey(providerId, enteredKey.trim())
                        } else {
                            viewModel.keyStorage.deleteApiKey(providerId)
                        }
                        editingProviderId = null
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                    modifier = Modifier.testTag("save_api_key_button")
                ) {
                    Text("Save to Keystore", color = if (colors.isDark) colors.background else Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingProviderId = null }) {
                    Text("Cancel", color = colors.textSecondary)
                }
            }
        )
    }

    if (showPermissionsModal) {
        PermissionsManagerDialog(
            onDismiss = { showPermissionsModal = false },
            onAllGranted = {
                viewModel.permissionsGrantedState.value = true
                showPermissionsModal = false
            }
        )
    }
}

@Composable
private fun KeyVaultRow(
    name: String,
    isConfigured: Boolean,
    testStatus: com.example.core.model.ApiTestStatus?,
    onTestConnection: () -> Unit,
    onEdit: () -> Unit
) {
    val colors = SaturnTheme.colors
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.border, RoundedCornerShape(28.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(name, color = colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isConfigured) "Encrypted Key Active [AES-GCM]" else "Unconfigured (Simulated / Free Tier)",
                        color = if (isConfigured) Color(0xFF34D399) else colors.textSecondary,
                        fontSize = 10.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Test Button
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(colors.surfaceCardLight)
                            .border(1.dp, colors.border, CircleShape)
                            .clickable { onTestConnection() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("test_conn_${name.take(4).lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (testStatus?.isTesting == true) {
                            CircularProgressIndicator(color = colors.primaryAccent, modifier = Modifier.size(12.dp), strokeWidth = 2.dp)
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Refresh, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Test", color = colors.primaryAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Key", tint = colors.primaryAccent, modifier = Modifier.size(18.dp))
                    }
                }
            }

            // Connection Diagnostic Feedback Banner
            if (testStatus != null && !testStatus.isTesting) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (testStatus.success == true) Color(0xFF10B981).copy(alpha = 0.12f) else Color(0xFFEF4444).copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (testStatus.success == true) Icons.Default.CheckCircle else Icons.Default.Error,
                        contentDescription = null,
                        tint = if (testStatus.success == true) Color(0xFF10B981) else Color(0xFFEF4444),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = testStatus.message ?: "Diagnostic Verified",
                        color = if (testStatus.success == true) Color(0xFF10B981) else Color(0xFFEF4444),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

