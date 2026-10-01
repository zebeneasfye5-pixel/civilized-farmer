package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FarmerProfile

@Composable
fun LandCalculatorScreen(
    currentProfile: FarmerProfile?,
    onSaveToProfile: (hectares: Double, timad: Double) -> Unit,
    onVoiceClick: (String) -> Unit
) {
    var isHectareUnit by remember { mutableStateOf(true) }
    var landSizeInput by remember { mutableStateOf(currentProfile?.landSizeHectares?.toString() ?: "2.0") }
    var selectedSoil by remember { mutableStateOf("ደለል አፈር (Loam)") }
    var selectedCrop by remember { mutableStateOf("ማኛ ጤፍ (Teff)") }

    val rawValue = landSizeInput.toDoubleOrNull() ?: 1.0
    val hectares = if (isHectareUnit) rawValue else (rawValue / 4.0)
    val timad = if (isHectareUnit) (rawValue * 4.0) else rawValue

    // Agronomic fertilizer requirement formula per hectare:
    // 1 hectare Teff: ~100kg NPSB (1 quintal) + 100kg Urea (1 quintal)
    // 1 hectare Maize: ~150kg NPSB (1.5 quintal) + 150kg Urea (1.5 quintal)
    val multiplier = if (selectedCrop.contains("በቆሎ")) 1.5 else 1.2
    val npsbBagsNeeded = (hectares * multiplier).coerceAtLeast(0.5)
    val ureaBagsNeeded = (hectares * multiplier).coerceAtLeast(0.5)
    val estimatedYieldQuintals = (hectares * (if (selectedCrop.contains("ጤፍ")) 18 else 32)).toInt()
    val estimatedCostBirr = (npsbBagsNeeded * 4200.0) + (ureaBagsNeeded * 3950.0)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("land_calc_screen_content"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Voice Explanation Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFF0277BD), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "የመሬት ልክ መመዝገቢያና የማዳበሪያ ስሌት",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF01579B)
                            )
                            Text(
                                text = "የመሬትዎን ስፋት አስገብተው ትክክለኛውን ኮታ ያግኙ",
                                fontSize = 11.sp,
                                color = Color(0xFF0277BD)
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            onVoiceClick(
                                "የመሬትዎን መጠን በሄክታር ወይም በጥማድ ያስገቡ፤ መተግበሪያው የሚፈልጉትን የNPSB እና የዩሪያ ማዳበሪያ መጠን በትክክል ያሰላልዎታል።"
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "በድምፅ ያዳምጡ",
                            tint = Color(0xFF0277BD)
                        )
                    }
                }
            }
        }

        // Unit Switcher & Land Input
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "የመሬት ስፋትዎን ያስገቡ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Toggle between Hectares and Timad (1 Hectare = 4 Timad)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF0F4F8), RoundedCornerShape(12.dp))
                            .padding(4.dp)
                    ) {
                        Surface(
                            onClick = {
                                if (!isHectareUnit) {
                                    val current = landSizeInput.toDoubleOrNull() ?: 4.0
                                    landSizeInput = "%.2f".format(current / 4.0)
                                    isHectareUnit = true
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isHectareUnit) MaterialTheme.colorScheme.primary else Color.Transparent,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "በሄክታር (Hectare)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (isHectareUnit) Color.White else Color.Black,
                                modifier = Modifier.padding(vertical = 10.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }

                        Surface(
                            onClick = {
                                if (isHectareUnit) {
                                    val current = landSizeInput.toDoubleOrNull() ?: 1.0
                                    landSizeInput = "%.1f".format(current * 4.0)
                                    isHectareUnit = false
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = if (!isHectareUnit) MaterialTheme.colorScheme.primary else Color.Transparent,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "በጥማድ / በቃዳ (Timad)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (!isHectareUnit) Color.White else Color.Black,
                                modifier = Modifier.padding(vertical = 10.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = landSizeInput,
                        onValueChange = { landSizeInput = it },
                        label = {
                            Text(if (isHectareUnit) "የመሬት ስፋት በሄክታር" else "የመሬት ስፋት በጥማድ/ቃዳ")
                        },
                        leadingIcon = { Icon(Icons.Default.Landscape, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("land_size_input_field"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "ተመጣጣኝ ስሌት፦ ${"%.2f".format(hectares)} ሄክታር = ${"%.1f".format(timad)} ጥማድ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }
            }
        }

        // Soil and Crop Selectors
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "የአፈር እና የሰብል ዓይነት",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "የአፈር ዓይነት፦", fontSize = 13.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("ደለል አፈር", "ጥቁር ኮትቻ", "ቀይ አፈር").forEach { soil ->
                            Surface(
                                onClick = { selectedSoil = soil },
                                shape = RoundedCornerShape(10.dp),
                                color = if (selectedSoil.startsWith(soil)) MaterialTheme.colorScheme.primaryContainer else Color(0xFFF5F5F5),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = soil,
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedSoil.startsWith(soil)) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedSoil.startsWith(soil)) MaterialTheme.colorScheme.onPrimaryContainer else Color.Black,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "የሚዘራው ሰብል፦", fontSize = 13.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("ማኛ ጤፍ", "ነጭ ስንዴ", "በቆሎ", "ገብስ").forEach { crop ->
                            Surface(
                                onClick = { selectedCrop = crop },
                                shape = RoundedCornerShape(10.dp),
                                color = if (selectedCrop.startsWith(crop)) MaterialTheme.colorScheme.secondaryContainer else Color(0xFFF5F5F5),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = crop,
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedCrop.startsWith(crop)) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedCrop.startsWith(crop)) MaterialTheme.colorScheme.onSecondaryContainer else Color.Black,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // Calculation Results Card
        item {
            ElevatedCard(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFE8F5E9)),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "የግብርና ባለሙያዎች ሳይንሳዊ የምክር ስሌት",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1B5E20)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ResultRow(
                        title = "የሚያስፈልገው NPSB ማዳበሪያ፡",
                        value = "${"%.1f".format(npsbBagsNeeded)} ኩንታል (${(npsbBagsNeeded * 100).toInt()} ኪ.ግ)"
                    )

                    ResultRow(
                        title = "የሚያስፈልገው ዩሪያ (Urea)፡",
                        value = "${"%.1f".format(ureaBagsNeeded)} ኩንታል (${(ureaBagsNeeded * 100).toInt()} ኪ.ግ)"
                    )

                    ResultRow(
                        title = "የሚገመተው ምርታማነት፡",
                        value = "$estimatedYieldQuintals ኩንታል $selectedCrop"
                    )

                    ResultRow(
                        title = "የማዳበሪያ ግዢ ግምት ዋጋ፡",
                        value = "${"%,.0f".format(estimatedCostBirr)} ብር"
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    Button(
                        onClick = { onSaveToProfile(hectares, timad) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("save_land_to_profile_button")
                    ) {
                        Icon(imageVector = Icons.Default.BookmarkAdded, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ይህንን የመሬት መጠን ወደ መገለጫዬ መዝግብ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultRow(title: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = title, fontSize = 13.sp, color = Color(0xFF2E7D32))
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B5E20)
        )
    }
}
