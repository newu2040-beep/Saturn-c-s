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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.ui.theme.SaturnTheme
import com.example.ui.viewmodel.SaturnViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AudioStudioScreen(
    viewModel: SaturnViewModel,
    onBack: () -> Unit
) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact

    var audioTab by remember { mutableStateOf("voice") } // voice, sfx, music
    var speechText by remember { mutableStateOf("In the deep orbital void of Saturn, sound does not travel, but reality vibrates with infinite potential.") }
    var selectedVoice by remember { mutableStateOf("Aria (Narrator / Cinematic)") }
    var sfxPrompt by remember { mutableStateOf("Heavy hydraulic airlock hiss with deep sub-bass metal groan") }
    var musicGenre by remember { mutableStateOf("Cinematic Orchestral Sci-Fi") }
    var voiceStability by remember { mutableFloatStateOf(0.75f) }

    val voices = listOf(
        "Aria (Narrator / Cinematic)",
        "Adam (Deep Commercial)",
        "Rachel (Warm Storyteller)",
        "Nicole (Ethereal / Soft)",
        "Antoni (Dramatic Trailer)"
    )

    val musicGenres = listOf(
        "Cinematic Orchestral Sci-Fi",
        "Cyberpunk Synthwave 120bpm",
        "Dark Ambient Space Drone",
        "Neo-Noir Piano & Strings",
        "High-Octane Action Percussion"
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
                    Text("AUDIO STUDIO", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text("TTS Voice, Sound FX & Music", color = colors.textPrimary, fontSize = if (isCompact) 16.sp else 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Mode Tabs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AudioModePill("Voice & Speech", Icons.Default.Mic, audioTab == "voice") { audioTab = "voice" }
                AudioModePill("Sound FX", Icons.Default.VolumeUp, audioTab == "sfx") { audioTab = "sfx" }
                AudioModePill("AI Music", Icons.Default.MusicNote, audioTab == "music") { audioTab = "music" }
            }
        }

        // Voice Tab Content
        if (audioTab == "voice") {
            item {
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, colors.border, RoundedCornerShape(28.dp))
                ) {
                    Column(modifier = Modifier.padding(if (isCompact) 10.dp else 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("VOICE MODEL CASTING", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)

                        voices.forEach { v ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                                    .background(if (selectedVoice == v) colors.primaryAccent.copy(alpha = 0.15f) else colors.surfaceCardLight)
                                    .border(1.dp, if (selectedVoice == v) colors.primaryAccent else colors.border, CircleShape)
                                    .clickable { selectedVoice = v }
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(v, color = if (selectedVoice == v) colors.primaryAccent else colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                if (selectedVoice == v) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(16.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text("SCRIPT TO SPEAK", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = speechText,
                            onValueChange = { speechText = it },
                            placeholder = { Text("Enter script text for neural speech synthesis...") },
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colors.primaryAccent,
                                unfocusedBorderColor = colors.border,
                                focusedTextColor = colors.textPrimary,
                                unfocusedTextColor = colors.textPrimary
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Voice Stability & Clarity", color = colors.textPrimary, fontSize = 12.sp)
                            Text("${(voiceStability * 100).toInt()}%", color = colors.secondaryAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Slider(
                            value = voiceStability,
                            onValueChange = { voiceStability = it },
                            colors = SliderDefaults.colors(
                                thumbColor = colors.primaryAccent,
                                activeTrackColor = colors.primaryAccent,
                                inactiveTrackColor = colors.surfaceCardLight
                            )
                        )
                    }
                }
            }
        }

        // Sound FX Tab Content
        if (audioTab == "sfx") {
            item {
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, colors.border, RoundedCornerShape(28.dp))
                ) {
                    Column(modifier = Modifier.padding(if (isCompact) 10.dp else 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("CINEMATIC FOLEY & SOUND EFFECTS", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("Generate hyper-realistic stereo acoustic audio from text descriptions.", color = colors.textSecondary, fontSize = 12.sp)

                        OutlinedTextField(
                            value = sfxPrompt,
                            onValueChange = { sfxPrompt = it },
                            label = { Text("Sound Effect Prompt") },
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colors.primaryAccent,
                                unfocusedBorderColor = colors.border,
                                focusedTextColor = colors.textPrimary,
                                unfocusedTextColor = colors.textPrimary
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("Cinematic Whoosh", "Laser Blast", "Thunderstorm", "Spaceship Engine", "Footsteps on Gravel").forEach { preset ->
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(colors.surfaceCardLight)
                                        .border(1.dp, colors.border, CircleShape)
                                        .clickable { sfxPrompt = preset }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(preset, color = colors.secondaryAccent, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Music Tab Content
        if (audioTab == "music") {
            item {
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, colors.border, RoundedCornerShape(28.dp))
                ) {
                    Column(modifier = Modifier.padding(if (isCompact) 10.dp else 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("SCORE & SOUNDTRACK GENERATOR", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)

                        musicGenres.forEach { g ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                                    .background(if (musicGenre == g) colors.primaryAccent.copy(alpha = 0.15f) else colors.surfaceCardLight)
                                    .border(1.dp, if (musicGenre == g) colors.primaryAccent else colors.border, CircleShape)
                                    .clickable { musicGenre = g }
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Text(g, color = if (musicGenre == g) colors.primaryAccent else colors.textPrimary, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }

        // Render Button
        item {
            Button(
                onClick = {
                    val prompt = if (audioTab == "voice") speechText else if (audioTab == "sfx") sfxPrompt else musicGenre
                    viewModel.promptText.value = prompt
                    viewModel.generate()
                },
                colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                shape = CircleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("render_audio_button")
            ) {
                Icon(Icons.Default.GraphicEq, contentDescription = null, tint = if (colors.isDark) colors.background else Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Render Studio Audio Stem", color = if (colors.isDark) colors.background else Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun AudioModePill(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = SaturnTheme.colors
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (isSelected) colors.primaryAccent else colors.surfaceCard)
            .border(1.dp, if (isSelected) colors.primaryAccent else colors.border, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = if (isSelected) (if (colors.isDark) colors.background else Color.White) else colors.textSecondary, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, color = if (isSelected) (if (colors.isDark) colors.background else Color.White) else colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}
