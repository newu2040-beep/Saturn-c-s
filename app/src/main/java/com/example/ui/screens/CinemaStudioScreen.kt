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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.core.model.CameraMotion
import com.example.ui.theme.SaturnTheme
import com.example.ui.viewmodel.SaturnViewModel

import androidx.compose.material.icons.filled.AspectRatio
import com.example.core.model.MediaCategory
import com.example.ui.components.AspectRatioSelectorBar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CinemaStudioScreen(
    viewModel: SaturnViewModel,
    onBack: () -> Unit
) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact
    val currentCamera by viewModel.cameraMotion.collectAsState()
    val selectedAspectRatio by viewModel.selectedAspectRatio.collectAsState()

    var motionType by remember { mutableStateOf(currentCamera.motionType) }
    var lens by remember { mutableStateOf(currentCamera.lens) }
    var aperture by remember { mutableStateOf(currentCamera.aperture) }
    var lighting by remember { mutableStateOf(currentCamera.lighting) }
    var colorGrade by remember { mutableStateOf(currentCamera.colorGrade) }
    var iso by remember { mutableIntStateOf(currentCamera.iso) }
    var fov by remember { mutableIntStateOf(currentCamera.fov) }

    val motionTypes = listOf(
        "Static", "Dolly In", "Dolly Out", "Pan Left", "Pan Right",
        "Tilt Up", "Tilt Down", "Orbit Right", "Tracking", "Crane Up",
        "Handheld", "Drone FPV"
    )

    val lenses = listOf(
        "18mm Ultra-Wide", "24mm Wide", "35mm Standard",
        "50mm Prime", "85mm Portrait", "135mm Telephoto", "Anamorphic 2.39:1"
    )

    val apertures = listOf("f/1.4", "f/2.0", "f/2.8", "f/4.0", "f/5.6", "f/8.0", "f/11")

    val lightings = listOf(
        "Dramatic Chiaroscuro", "Natural Sunlight", "Studio 3-Point",
        "Golden Hour Warmth", "Moonlight Glow", "Cyber Neon Rim", "Volumetric Godrays"
    )

    val luts = listOf(
        "Cinematic Teal & Orange", "Film 35mm Kodak", "Vintage 70s Warm",
        "Monochrome Noir", "Bleach Bypass Pro", "Clean Neutral"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(horizontal = if (isCompact) 8.dp else 16.dp),
        verticalArrangement = Arrangement.spacedBy(if (isCompact) 10.dp else 16.dp),
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
                    Text("CINEMA STUDIO", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text("Camera & Optics Director", color = colors.textPrimary, fontSize = if (isCompact) 16.sp else 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Live Lens & Motion Summary Banner
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.borderHighlight, RoundedCornerShape(28.dp))
            ) {
                Column(modifier = Modifier.padding(if (isCompact) 10.dp else 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("ACTIVE CAMERA RIG", color = colors.secondaryAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("ARRI Alexa Simulation", color = colors.textSecondary, fontSize = 10.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$motionType • $lens @ $aperture",
                        color = colors.textPrimary,
                        fontSize = if (isCompact) 14.sp else 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Lighting: $lighting | LUT: $colorGrade | ISO $iso",
                        color = colors.primaryAccent,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Section 1: Camera Movements
        item {
            SectionHeader(icon = Icons.Default.Movie, title = "CAMERA MOTION & TRAJECTORY")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                motionTypes.forEach { type ->
                    SelectableCinemaPill(
                        label = type,
                        isSelected = type == motionType,
                        onClick = { motionType = type }
                    )
                }
            }
        }

        // Section 2: Focal Lens Selection
        item {
            SectionHeader(icon = Icons.Default.CameraAlt, title = "FOCAL LENGTH & LENS OPTICS")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                lenses.forEach { l ->
                    SelectableCinemaPill(
                        label = l,
                        isSelected = l == lens,
                        onClick = { lens = l }
                    )
                }
            }
        }

        // Section 3: Aperture & ISO Dials
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.border, RoundedCornerShape(28.dp))
            ) {
                Column(modifier = Modifier.padding(if (isCompact) 10.dp else 14.dp)) {
                    Text("Aperture / Depth of Field", color = colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        apertures.forEach { a ->
                            SelectableCinemaPill(
                                label = a,
                                isSelected = a == aperture,
                                onClick = { aperture = a }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Sensor Sensitivity (ISO)", color = colors.textPrimary, fontSize = 12.sp)
                        Text("ISO $iso", color = colors.secondaryAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = iso.toFloat(),
                        onValueChange = { iso = (it / 100).toInt() * 100 },
                        valueRange = 100f..3200f,
                        colors = SliderDefaults.colors(
                            thumbColor = colors.primaryAccent,
                            activeTrackColor = colors.primaryAccent,
                            inactiveTrackColor = colors.surfaceCardLight
                        )
                    )
                }
            }
        }

        // Section 4: Lighting Atmosphere
        item {
            SectionHeader(icon = Icons.Default.WbSunny, title = "LIGHTING & ATMOSPHERE")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                lightings.forEach { light ->
                    SelectableCinemaPill(
                        label = light,
                        isSelected = light == lighting,
                        onClick = { lighting = light }
                    )
                }
            }
        }

        // Section 5: Color Grade / LUT
        item {
            SectionHeader(icon = Icons.Default.Palette, title = "COLOR GRADE & FILM LUT")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                luts.forEach { lut ->
                    SelectableCinemaPill(
                        label = lut,
                        isSelected = lut == colorGrade,
                        onClick = { colorGrade = lut }
                    )
                }
            }
        }

        // Section 6: Video Aspect Ratio & Anamorphic Framing
        item {
            SectionHeader(icon = Icons.Default.AspectRatio, title = "CINEMA ASPECT RATIO & ANAMORPHIC FRAMING")
            Box(modifier = Modifier.padding(top = 8.dp)) {
                AspectRatioSelectorBar(
                    currentRatio = selectedAspectRatio,
                    currentCategory = MediaCategory.VIDEO,
                    onSelectRatio = { viewModel.selectedAspectRatio.value = it },
                    onOpenCustomDialog = { viewModel.openCustomAspectRatio(MediaCategory.VIDEO) }
                )
            }
        }

        // Apply Button
        item {
            Button(
                onClick = {
                    val updated = CameraMotion(
                        motionType = motionType,
                        lens = lens,
                        aperture = aperture,
                        fov = fov,
                        iso = iso,
                        lighting = lighting,
                        colorGrade = colorGrade
                    )
                    viewModel.updateCameraMotion(updated)
                    onBack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                shape = CircleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("apply_cinema_settings_button")
            ) {
                Icon(Icons.Default.Done, contentDescription = null, tint = if (colors.isDark) colors.background else Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Apply Cinema Parameters to Studio", color = if (colors.isDark) colors.background else Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun SectionHeader(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    val colors = SaturnTheme.colors
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(title, color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
    }
}

@Composable
private fun SelectableCinemaPill(
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
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) (if (colors.isDark) colors.background else Color.White) else colors.textPrimary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
