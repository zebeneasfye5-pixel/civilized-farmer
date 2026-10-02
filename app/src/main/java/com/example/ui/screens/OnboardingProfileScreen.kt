package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.FarmerProfile
import java.io.File

@Composable
fun OnboardingProfileScreen(
    currentProfile: FarmerProfile?,
    onSaveProfile: (
        fullName: String,
        phone: String,
        gender: String,
        nationality: String,
        region: String,
        zone: String,
        woreda: String,
        kebele: String,
        nationalId: String,
        kebeleId: String,
        landHectares: Double,
        landTimad: Double,
        taxBirr: Double,
        primaryCrops: String,
        bankName: String,
        bankAccountNumber: String,
        bankAccountHolder: String,
        avatarPreset: String,
        photoUri: String
    ) -> Unit,
    onVoiceClick: (String) -> Unit
) {
    var fullName by remember { mutableStateOf(currentProfile?.fullName ?: "") }
    var phone by remember { mutableStateOf(currentProfile?.phoneNumber ?: "") }
    var gender by remember { mutableStateOf(currentProfile?.gender ?: "ወንድ") }
    var nationality by remember { mutableStateOf(currentProfile?.nationality ?: "ኢትዮጵያዊ") }
    var region by remember { mutableStateOf(currentProfile?.region ?: "አማራ") }
    var zone by remember { mutableStateOf(currentProfile?.zone ?: "ምዕራብ ጎጃም") }
    var woreda by remember { mutableStateOf(currentProfile?.woreda ?: "ይስማላ / መርዓዊ") }
    var kebele by remember { mutableStateOf(currentProfile?.kebele ?: "አዴት 01 ቀበሌ") }
    var nationalId by remember { mutableStateOf(currentProfile?.nationalId ?: "") }
    var kebeleId by remember { mutableStateOf(currentProfile?.kebeleId ?: "") }
    var landHectaresStr by remember { mutableStateOf(currentProfile?.landSizeHectares?.toString() ?: "2.5") }
    var landTimadStr by remember { mutableStateOf(currentProfile?.landSizeTimad?.toString() ?: "10.0") }
    var taxBirrStr by remember { mutableStateOf(currentProfile?.annualTaxBirr?.toString() ?: "450.0") }
    var primaryCrops by remember { mutableStateOf(currentProfile?.primaryCrops ?: "ማኛ ጤፍ፣ ነጭ ስንዴ፣ በቆሎ") }

    // Bank Account State
    var bankName by remember { mutableStateOf(currentProfile?.bankName ?: "የኢትዮጵያ ንግድ ባንክ (CBE)") }
    var bankAccountNumber by remember { mutableStateOf(currentProfile?.bankAccountNumber ?: "1000284910294") }
    var bankAccountHolder by remember { mutableStateOf(currentProfile?.bankAccountHolder ?: (currentProfile?.fullName ?: "")) }

    // Photo Avatar Preset State
    var avatarPreset by remember { mutableStateOf(currentProfile?.avatarPreset ?: "man_1") }
    var photoUri by remember { mutableStateOf(currentProfile?.photoUri ?: "") }

    LaunchedEffect(currentProfile) {
        if (currentProfile != null) {
            fullName = currentProfile.fullName
            phone = currentProfile.phoneNumber
            gender = currentProfile.gender
            nationality = currentProfile.nationality
            region = currentProfile.region
            zone = currentProfile.zone
            woreda = currentProfile.woreda
            kebele = currentProfile.kebele
            nationalId = currentProfile.nationalId
            kebeleId = currentProfile.kebeleId
            landHectaresStr = currentProfile.landSizeHectares.toString()
            landTimadStr = currentProfile.landSizeTimad.toString()
            taxBirrStr = currentProfile.annualTaxBirr.toString()
            primaryCrops = currentProfile.primaryCrops
            bankName = currentProfile.bankName
            bankAccountNumber = currentProfile.bankAccountNumber
            bankAccountHolder = currentProfile.bankAccountHolder
            avatarPreset = currentProfile.avatarPreset
            photoUri = currentProfile.photoUri
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("profile_screen_content"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Voice Guide & Information Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
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
                                .background(MaterialTheme.colorScheme.primary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Badge,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "የአርሶ አደር ማህደርና ምዝገባ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "ፎቶ፣ የባንክ ሂሳብ፣ ጾታ እና የመሬት መረጃ ማህደር",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            onVoiceClick(
                                "እዚህ ማህደር ላይ ፎቶዎን፣ ጾታዎን፣ ስምዎን፣ አድራሻዎን፣ የባንክ አካውንትዎን እና የመሬትዎን ልክ ያስመዝግቡ። የቀበሌው ግብርና ጽ/ቤት የሴትና ወንድ ድምርን በዚህ መረጃ መሰረት ያሰላል።"
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "በድምፅ ያዳምጡ",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Section 0: የአርሶ አደሩ ፎቶ እና ጾታ (Photo & Gender in Dossier)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "የአርሶ አደሩ ፎቶ በማህደር (Profile Photo)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val photoFile = if (gender == "ሴት") {
                        File("/app/src/main/res/drawable/farmer_ethiopian_woman_1790921555784.jpg")
                    } else {
                        File("/app/src/main/res/drawable/farmer_ethiopian_man_1790921542582.jpg")
                    }

                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(3.dp, if (gender == "ሴት") Color(0xFFC2185B) else Color(0xFF1976D2), CircleShape)
                    ) {
                        if (photoFile.exists()) {
                            AsyncImage(
                                model = photoFile,
                                contentDescription = "የአርሶ አደር ፎቶ",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(if (gender == "ሴት") Color(0xFFFCE4EC) else Color(0xFFE3F2FD)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (gender == "ሴት") Icons.Default.Female else Icons.Default.Male,
                                    contentDescription = null,
                                    tint = if (gender == "ሴት") Color(0xFFC2185B) else Color(0xFF1976D2),
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Gender Selector (ወንድ / ሴት)
                    Text(
                        text = "ጾታ ይምረጡ (ለቀበሌው ጽ/ቤት ስታቲስቲክስ የሚሰላ)፦",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            onClick = { gender = "ወንድ" },
                            shape = RoundedCornerShape(12.dp),
                            color = if (gender == "ወንድ") Color(0xFF1976D2) else Color(0xFFF0F4F8),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("select_gender_male")
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Male,
                                    contentDescription = null,
                                    tint = if (gender == "ወንድ") Color.White else Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ወንድ",
                                    fontWeight = FontWeight.Bold,
                                    color = if (gender == "ወንድ") Color.White else Color.Black
                                )
                            }
                        }

                        Surface(
                            onClick = { gender = "ሴት" },
                            shape = RoundedCornerShape(12.dp),
                            color = if (gender == "ሴት") Color(0xFFC2185B) else Color(0xFFF0F4F8),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("select_gender_female")
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Female,
                                    contentDescription = null,
                                    tint = if (gender == "ሴት") Color.White else Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ሴት",
                                    fontWeight = FontWeight.Bold,
                                    color = if (gender == "ሴት") Color.White else Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 1: የግል እና የመታወቂያ መረጃ
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. የግል እና የመታወቂያ መረጃ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("ሙሉ ስም (Full Name) *") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_full_name"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("ስልክ ቁጥር (Phone Number) *") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_phone"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = nationality,
                        onValueChange = { nationality = it },
                        label = { Text("ዜግነት (Nationality)") },
                        leadingIcon = { Icon(Icons.Default.Flag, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = nationalId,
                            onValueChange = { nationalId = it },
                            label = { Text("ብሔራዊ መታወቂያ / ፋይዳ") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_national_id"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = kebeleId,
                            onValueChange = { kebeleId = it },
                            label = { Text("የቀበሌ መታወቂያ ቁጥር") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_kebele_id"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }
            }
        }

        // Section 2: የተያያዘ የባንክ ሂሳብ መረጃ (Bank Account Linkage)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "2. የተያያዘ የባንክ ሂሳብ (Bank Account Linkage)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF005BAC)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "የባንኩን ስም ይምረጡ፦", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(6.dp))

                    val bankOptions = listOf(
                        "የኢትዮጵያ ንግድ ባንክ (CBE)",
                        "አዋሽ ባንክ (Awash Bank)",
                        "የኦሮሚያ ህ/ስራ ባንክ (Coop)",
                        "ዳሸን ባንክ (Dashen Bank)",
                        "አባይ ባንክ (Abay Bank)"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        bankOptions.take(3).forEach { bName ->
                            Surface(
                                onClick = { bankName = bName },
                                shape = RoundedCornerShape(8.dp),
                                color = if (bankName == bName) Color(0xFF005BAC) else Color(0xFFF0F4F8),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = bName.substringBefore(" "),
                                    fontSize = 11.sp,
                                    fontWeight = if (bankName == bName) FontWeight.Bold else FontWeight.Normal,
                                    color = if (bankName == bName) Color.White else Color.Black,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = bankAccountNumber,
                        onValueChange = { bankAccountNumber = it },
                        label = { Text("የባንክ ሂሳብ ቁጥር (Account Number) *") },
                        leadingIcon = { Icon(Icons.Default.AccountBalance, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_bank_account"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = bankAccountHolder,
                        onValueChange = { bankAccountHolder = it },
                        label = { Text("የሂሳብ ባለቤት ሙሉ ስም") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
            }
        }

        // Section 3: አድራሻ (Address)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "3. የአርሶ አደሩ አድራሻ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = region,
                            onValueChange = { region = it },
                            label = { Text("ክልል (Region)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = zone,
                            onValueChange = { zone = it },
                            label = { Text("ዞን (Zone)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = woreda,
                            onValueChange = { woreda = it },
                            label = { Text("ወረዳ (Woreda)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = kebele,
                            onValueChange = { kebele = it },
                            label = { Text("ቀበሌ (Kebele)") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_kebele"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }
            }
        }

        // Section 4: የመሬት መጠን እና ዓመታዊ ግብር
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "4. የመሬት ስፋት፣ ሰብል እና ዓመታዊ ግብር",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = landHectaresStr,
                            onValueChange = {
                                landHectaresStr = it
                                val num = it.toDoubleOrNull()
                                if (num != null) {
                                    landTimadStr = "%.1f".format(num * 4)
                                }
                            },
                            label = { Text("መጠን በሄክታር") },
                            leadingIcon = { Icon(Icons.Default.Landscape, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_land_hectares"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = landTimadStr,
                            onValueChange = {
                                landTimadStr = it
                                val num = it.toDoubleOrNull()
                                if (num != null) {
                                    landHectaresStr = "%.2f".format(num / 4)
                                }
                            },
                            label = { Text("በጥማድ / በቃዳ") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_land_timad"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = taxBirrStr,
                        onValueChange = { taxBirrStr = it },
                        label = { Text("መክፈል ያለባቸው የግብር መጠን (ብር)") },
                        leadingIcon = { Icon(Icons.Default.Payments, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_tax_birr"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = primaryCrops,
                        onValueChange = { primaryCrops = it },
                        label = { Text("የሚያመርቷቸው ዋና ዋና ሰብሎች") },
                        leadingIcon = { Icon(Icons.Default.Grass, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
            }
        }

        // Save Button
        item {
            Button(
                onClick = {
                    val hectares = landHectaresStr.toDoubleOrNull() ?: 2.0
                    val timad = landTimadStr.toDoubleOrNull() ?: (hectares * 4)
                    val tax = taxBirrStr.toDoubleOrNull() ?: 380.0
                    onSaveProfile(
                        fullName,
                        phone,
                        gender,
                        nationality,
                        region,
                        zone,
                        woreda,
                        kebele,
                        nationalId,
                        kebeleId,
                        hectares,
                        timad,
                        tax,
                        primaryCrops,
                        bankName,
                        bankAccountNumber,
                        bankAccountHolder,
                        avatarPreset,
                        photoUri
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("save_profile_button")
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "መረጃዬን መዝግብና በማህደር አስቀምጥ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
