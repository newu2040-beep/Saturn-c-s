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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.SaturnTheme
import com.example.ui.viewmodel.SaturnViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MarketingStudioScreen(
    viewModel: SaturnViewModel,
    onBack: () -> Unit
) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact

    var productName by remember { mutableStateOf("Aura Chronograph Watch") }
    var productCategory by remember { mutableStateOf("Luxury Horology / Fashion") }
    var selectedCampaignType by remember { mutableStateOf("Studio Packshot Hero") }
    var selectedFormat by remember { mutableStateOf("9:16 TikTok / Reels") }

    val campaignTypes = listOf(
        "Studio Packshot Hero",
        "Lifestyle In-Context",
        "Cinematic Product Reveal",
        "Social UGC Creator Ad",
        "Billboard 16:9 Banner"
    )

    val formats = listOf("1:1 Square Feed", "9:16 TikTok / Reels", "16:9 YouTube Banner", "4:5 Portrait Feed")

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
                    Text("MARKETING STUDIO", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text("Commercial & Brand Creative", color = colors.textPrimary, fontSize = if (isCompact) 16.sp else 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Campaign Parameters
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.border, RoundedCornerShape(28.dp))
            ) {
                Column(modifier = Modifier.padding(if (isCompact) 10.dp else 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("BRAND & PRODUCT IDENTITY", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)

                    OutlinedTextField(
                        value = productName,
                        onValueChange = { productName = it },
                        label = { Text("Product / Brand Name") },
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primaryAccent,
                            unfocusedBorderColor = colors.border,
                            focusedTextColor = colors.textPrimary,
                            unfocusedTextColor = colors.textPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = productCategory,
                        onValueChange = { productCategory = it },
                        label = { Text("Category & Material Aesthetic") },
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primaryAccent,
                            unfocusedBorderColor = colors.border,
                            focusedTextColor = colors.textPrimary,
                            unfocusedTextColor = colors.textPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Campaign Presets
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.border, RoundedCornerShape(28.dp))
            ) {
                Column(modifier = Modifier.padding(if (isCompact) 10.dp else 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("CAMPAIGN SCENARIO PRESET", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)

                    campaignTypes.forEach { type ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(CircleShape)
                                .background(if (selectedCampaignType == type) colors.primaryAccent.copy(alpha = 0.15f) else colors.surfaceCardLight)
                                .border(1.dp, if (selectedCampaignType == type) colors.primaryAccent else colors.border, CircleShape)
                                .clickable { selectedCampaignType = type }
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(type, color = if (selectedCampaignType == type) colors.primaryAccent else colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            if (selectedCampaignType == type) {
                                Icon(Icons.Default.Campaign, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text("OUTPUT AD FORMAT", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        formats.forEach { fmt ->
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (selectedFormat == fmt) colors.secondaryAccent else colors.surfaceCardLight)
                                    .border(1.dp, if (selectedFormat == fmt) colors.secondaryAccent else colors.border, CircleShape)
                                    .clickable { selectedFormat = fmt }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = fmt,
                                    color = if (selectedFormat == fmt) (if (colors.isDark) colors.background else Color.White) else colors.textPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Launch Action
        item {
            Button(
                onClick = {
                    val prompt = "Ultra premium commercial campaign for $productName ($productCategory). $selectedCampaignType aesthetic, master lighting, photorealistic 8K, commercial color graded"
                    viewModel.promptText.value = prompt
                    viewModel.selectedAspectRatio.value = if (selectedFormat.contains("9:16")) "9:16" else if (selectedFormat.contains("16:9")) "16:9" else "1:1"
                    viewModel.generate()
                },
                colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                shape = CircleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("launch_commercial_campaign_button")
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = if (colors.isDark) colors.background else Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Render Commercial Asset", color = if (colors.isDark) colors.background else Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
