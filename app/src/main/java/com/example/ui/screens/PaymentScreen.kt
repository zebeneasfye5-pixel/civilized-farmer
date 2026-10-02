package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WaterDrop
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.PaymentRecord

@Composable
fun PaymentScreen(
    profile: FarmerProfile?,
    pastPayments: List<PaymentRecord>,
    onProcessPayment: (title: String, categoryKey: String, amount: Double, provider: String, phone: String, notes: String) -> Unit,
    onViewReceipt: (PaymentRecord) -> Unit,
    onVoiceClick: (String) -> Unit
) {
    var selectedProvider by remember { mutableStateOf("Telebirr (ቴሌብር)") }
    var selectedCategoryKey by remember { mutableStateOf("FERTILIZER") }
    var npsbBags by remember { mutableIntStateOf(2) }
    var ureaBags by remember { mutableIntStateOf(2) }
    var phoneInput by remember { mutableStateOf(profile?.phoneNumber ?: "0911234567") }

    val npsbPrice = 4200.0 // ETB per quintal
    val ureaPrice = 3950.0 // ETB per quintal
    val landTax = profile?.annualTaxBirr ?: 450.0
    val seedPrice = 5200.0 // ETB for improved seed pack
    val landTitlingPrice = 600.0 // ETB for land titling card
    val irrigationPrice = 350.0 // ETB for irrigation water fee

    val currentTitle = when (selectedCategoryKey) {
        "FERTILIZER" -> "የአፈር ማዳበሪያ ክፍያ (NPSB & ዩሪያ)"
        "LAND_TAX" -> "ዓመታዊ የመሬት መጠቀሚያ ግብር"
        "SEED" -> "የተሻሻለ ምርጥ ዘር (ማኛ ጤፍ/ስንዴ)"
        "LAND_TITLING" -> "የገጠር መሬት ይዞታ ማረጋገጫ ካርታ (ደብተር)"
        "IRRIGATION" -> "የቀበሌ የጋራ መስኖ ውሃ አገልግሎት ክፍያ"
        else -> "የግብአት ክፍያ"
    }

    val totalAmount = when (selectedCategoryKey) {
        "FERTILIZER" -> (npsbBags * npsbPrice) + (ureaBags * ureaPrice)
        "LAND_TAX" -> landTax
        "SEED" -> seedPrice
        "LAND_TITLING" -> landTitlingPrice
        "IRRIGATION" -> irrigationPrice
        else -> 0.0
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("payment_screen_content"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Voice Guide & Info Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
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
                                .background(Color(0xFFE65100), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Payment,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "ለእያንዳንዱ የክፍያ አርዕስት ህጋዊ ደረሰኝ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFFE65100)
                            )
                            Text(
                                text = "በሞባይል ወይም በተያያዘ የባንክ ሂሳብ ከቤትዎ ይክፈሉ",
                                fontSize = 11.sp,
                                color = Color(0xFF7E3800)
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            onVoiceClick(
                                "የማዳበሪያ፣ የመሬት ግብር፣ የምርጥ ዘር፣ የመሬት ካርታ ወይም የመስኖ ክፍያዎን በቴሌብር፣ በሲቢኢ ወይም በተያያዘ የባንክ ሂሳብዎ ይክፈሉ። ሲስተሙ ለእያንዳንዱ ክፍያ ህጋዊ ደረሰኝ በራሱ ያዘጋጅልዎታል።"
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "በድምፅ ያዳምጡ",
                            tint = Color(0xFFE65100)
                        )
                    }
                }
            }
        }

        // Linked Bank Account Notice
        if (profile != null && profile.bankAccountNumber.isNotEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFE3F2FD),
                    border = BorderStroke(1.dp, Color(0xFF90CAF9)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = null,
                            tint = Color(0xFF0D47A1),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "የተያያዘ ባንክዎ፦ ${profile.bankName} (${profile.bankAccountNumber})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0D47A1)
                        )
                    }
                }
            }
        }

        // Step 1: የክፍያ አርዕስት ምረጥ (5 Categories)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. የሚከፍሉትን የክፍያ አርዕስት ይምረጡ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        CategoryChip(
                            title = "ማዳበሪያ",
                            icon = Icons.Default.Science,
                            isSelected = selectedCategoryKey == "FERTILIZER",
                            modifier = Modifier.weight(1f),
                            onClick = { selectedCategoryKey = "FERTILIZER" }
                        )

                        CategoryChip(
                            title = "መሬት ግብር",
                            icon = Icons.Default.Landscape,
                            isSelected = selectedCategoryKey == "LAND_TAX",
                            modifier = Modifier.weight(1f),
                            onClick = { selectedCategoryKey = "LAND_TAX" }
                        )

                        CategoryChip(
                            title = "ምርጥ ዘር",
                            icon = Icons.Default.Grass,
                            isSelected = selectedCategoryKey == "SEED",
                            modifier = Modifier.weight(1f),
                            onClick = { selectedCategoryKey = "SEED" }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        CategoryChip(
                            title = "ይዞታ ካርታ",
                            icon = Icons.Default.Map,
                            isSelected = selectedCategoryKey == "LAND_TITLING",
                            modifier = Modifier.weight(1f),
                            onClick = { selectedCategoryKey = "LAND_TITLING" }
                        )

                        CategoryChip(
                            title = "መስኖ ውሃ",
                            icon = Icons.Default.WaterDrop,
                            isSelected = selectedCategoryKey == "IRRIGATION",
                            modifier = Modifier.weight(1f),
                            onClick = { selectedCategoryKey = "IRRIGATION" }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Dynamic details based on selected category
                    when (selectedCategoryKey) {
                        "FERTILIZER" -> {
                            QuantitySelector(
                                label = "NPSB ማዳበሪያ (በኩንታል)",
                                pricePerUnit = "4,200 ብር / ኩንታል",
                                count = npsbBags,
                                onIncrement = { npsbBags++ },
                                onDecrement = { if (npsbBags > 0) npsbBags-- }
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            QuantitySelector(
                                label = "ዩሪያ (Urea) ማዳበሪያ (በኩንታል)",
                                pricePerUnit = "3,950 ብር / ኩንታል",
                                count = ureaBags,
                                onIncrement = { ureaBags++ },
                                onDecrement = { if (ureaBags > 0) ureaBags-- }
                            )
                        }
                        "LAND_TAX" -> {
                            Text(
                                text = "ለመሬትዎ የተመደበው ዓመታዊ ግብር፦ ${"%,.2f".format(landTax)} ብር",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = Color(0xFF2E7D32)
                            )
                        }
                        "SEED" -> {
                            Text(
                                text = "1 ጆንያ (50 ኪ.ግ) የተመረጠ ማኛ ጤፍ/ስንዴ ምርጥ ዘር፦ 5,200 ብር",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = Color(0xFF2E7D32)
                            )
                        }
                        "LAND_TITLING" -> {
                            Text(
                                text = "የገጠር መሬት ይዞታ ማረጋገጫ ካርታ (የደብተር ክፍያ)፦ 600 ብር",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = Color(0xFF005BAC)
                            )
                        }
                        "IRRIGATION" -> {
                            Text(
                                text = "የቀበሌ የጋራ መስኖ ውሃ አገልግሎት የደረቅ ወቅት ክፍያ፦ 350 ብር",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = Color(0xFF0277BD)
                            )
                        }
                    }
                }
            }
        }

        // Step 2: የመክፈያ መንገድ ምረጥ
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "2. የመክፈያ መንገድ ይምረጡ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PaymentProviderCard(
                            name = "Telebirr (ቴሌብር)",
                            color = Color(0xFF005BAC),
                            isSelected = selectedProvider.contains("Telebirr"),
                            modifier = Modifier.weight(1f),
                            onClick = { selectedProvider = "Telebirr (ቴሌብር)" }
                        )

                        PaymentProviderCard(
                            name = "CBE Birr (ሲቢኢ)",
                            color = Color(0xFF7B1FA2),
                            isSelected = selectedProvider.contains("CBE"),
                            modifier = Modifier.weight(1f),
                            onClick = { selectedProvider = "CBE Birr (ሲቢኢ)" }
                        )

                        PaymentProviderCard(
                            name = "የባንክ አካውንት",
                            color = Color(0xFF0D47A1),
                            isSelected = selectedProvider.contains("ባንክ"),
                            modifier = Modifier.weight(1f),
                            onClick = { selectedProvider = "የባንክ ቀጥታ ዝውውር" }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        label = { Text("የከፋዩ ስልክ ቁጥር *") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("payment_phone_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
            }
        }

        // Total Amount & Pay Button Card
        item {
            ElevatedCard(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFF1F8E9)),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "የክፍያ አርዕስት፦ $currentTitle",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "ጠቅላላ ክፍያ መጠን፦",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF1B5E20)
                        )
                        Text(
                            text = "${"%,.2f".format(totalAmount)} ብር",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            color = Color(0xFF1B5E20)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val notes = when (selectedCategoryKey) {
                                "FERTILIZER" -> "$npsbBags ኩንታል NPSB + $ureaBags ኩንታል ዩሪያ"
                                "LAND_TAX" -> "የ ${profile?.fullName ?: "አርሶ አደር"} ዓመታዊ የመሬት ግብር"
                                "SEED" -> "የተሻሻለ ምርጥ ዘር ጥቅል"
                                "LAND_TITLING" -> "የይዞታ ደብተር ምዝገባ ክፍያ"
                                else -> "የቀበሌ መስኖ ውሃ አገልግሎት"
                            }
                            onProcessPayment(
                                currentTitle,
                                selectedCategoryKey,
                                totalAmount,
                                selectedProvider,
                                phoneInput,
                                notes
                            )
                        },
                        enabled = totalAmount > 0,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1B5E20)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("confirm_payment_button")
                    ) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "በ $selectedProvider አሁን ይክፈሉና ደረሰኝ ያግኙ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }

        // Section Title: የተሰጡ ህጋዊ ደረሰኞች
        item {
            Text(
                text = "የተሰጡ ህጋዊ ዲጂታል ደረሰኞች (ደረሰኙን ለማየት ይጫኑ)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (pastPayments.isEmpty()) {
            item {
                Text(
                    text = "ምንም የተከፈለ ደረሰኝ የለም።",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        } else {
            items(pastPayments) { payment ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onViewReceipt(payment) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFFE8F5E9), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Receipt,
                                    contentDescription = null,
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = payment.paymentTitle,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "ደረሰኝ፦ ${payment.receiptOfficialNumber} | ${payment.provider}",
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${"%,.0f".format(payment.amountBirr)} ብር",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "ደረሰኝ እይ →",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun QuantitySelector(
    label: String,
    pricePerUnit: String,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFAFAFA), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Column {
            Text(text = label, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Text(text = pricePerUnit, fontSize = 11.sp, color = Color.Gray)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onDecrement,
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFFE0E0E0), CircleShape)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "ቀንስ", modifier = Modifier.size(16.dp))
            }

            Text(
                text = "$count ኩንታል",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            IconButton(
                onClick = onIncrement,
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(Icons.Default.Add, contentDescription = "ጨምር", tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun PaymentProviderCard(
    name: String,
    color: Color,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) color.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) color else Color.Transparent
        ),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = name,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) color else Color.Black
            )
        }
    }
}
