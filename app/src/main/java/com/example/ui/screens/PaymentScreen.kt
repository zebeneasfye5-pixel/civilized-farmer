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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Remove
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PaymentScreen(
    profile: FarmerProfile?,
    pastPayments: List<PaymentRecord>,
    onProcessPayment: (type: String, amount: Double, provider: String, phone: String, notes: String) -> Unit,
    onVoiceClick: (String) -> Unit
) {
    var selectedProvider by remember { mutableStateOf("Telebirr (ቴሌብር)") }
    var selectedCategory by remember { mutableStateOf("የአፈር ማዳበሪያ") } // "የአፈር ማዳበሪያ", "የመሬት ግብር", "የተሻሻለ ዘር"
    var npsbBags by remember { mutableIntStateOf(2) }
    var ureaBags by remember { mutableIntStateOf(2) }
    var phoneInput by remember { mutableStateOf(profile?.phoneNumber ?: "0911234567") }

    val npsbPrice = 4200.0 // ETB per quintal
    val ureaPrice = 3950.0 // ETB per quintal
    val landTax = profile?.annualTaxBirr ?: 450.0
    val seedPrice = 5200.0 // ETB for improved seed pack

    val totalAmount = when (selectedCategory) {
        "የአፈር ማዳበሪያ" -> (npsbBags * npsbPrice) + (ureaBags * ureaPrice)
        "የመሬት ግብር" -> landTax
        "የተሻሻለ ዘር" -> seedPrice
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
                                text = "ከቤት ሆነው በሞባይል ይክፈሉ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFFE65100)
                            )
                            Text(
                                text = "በቀጥታ በቴሌብር ወይም ሲቢኢ ደረሰኝ ወዲያው ይሰጥዎታል",
                                fontSize = 11.sp,
                                color = Color(0xFF7E3800)
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            onVoiceClick(
                                "የማዳበሪያ፣ የምርጥ ዘር ወይም የመሬት ግብር ክፍያዎን በቴሌብር ወይም በሲቢኢ ብር ከቤትዎ ሆነው መክፈል ይችላሉ። ክፍያው እንዳለቀ ደረሰኙ በስልክዎ ውስጥ ከመስመር ውጭ ይቀመጣል።"
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

        // Step 1: የክፍያ ዓይነት ምረጥ (Category Selection)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. የሚከፍሉትን የግብአት ዓይነት ይምረጡ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CategoryChip(
                            title = "የአፈር ማዳበሪያ",
                            icon = Icons.Default.Science,
                            isSelected = selectedCategory == "የአፈር ማዳበሪያ",
                            modifier = Modifier.weight(1f),
                            onClick = { selectedCategory = "የአፈር ማዳበሪያ" }
                        )

                        CategoryChip(
                            title = "የመሬት ግብር",
                            icon = Icons.Default.Landscape,
                            isSelected = selectedCategory == "የመሬት ግብር",
                            modifier = Modifier.weight(1f),
                            onClick = { selectedCategory = "የመሬት ግብር" }
                        )

                        CategoryChip(
                            title = "የተሻሻለ ዘር",
                            icon = Icons.Default.Grass,
                            isSelected = selectedCategory == "የተሻሻለ ዘር",
                            modifier = Modifier.weight(1f),
                            onClick = { selectedCategory = "የተሻሻለ ዘር" }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Dynamic Quantity controls if Fertilizer
                    if (selectedCategory == "የአፈር ማዳበሪያ") {
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
                    } else if (selectedCategory == "የመሬት ግብር") {
                        Text(
                            text = "ለመሬትዎ የተመደበው ዓመታዊ ግብር፦ ${"%,.2f".format(landTax)} ብር",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color(0xFF2E7D32)
                        )
                    } else {
                        Text(
                            text = "1 ጆንያ (50 ኪ.ግ) የተመረጠ ማኛ ጤፍ/ስንዴ ምርጥ ዘር፦ 5,200 ብር",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }
        }

        // Step 2: የክፍያ መንገድ ምረጥ (Telebirr / CBE Birr)
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
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
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
                            name = "Coopay (ኮኦፕ)",
                            color = Color(0xFFE65100),
                            isSelected = selectedProvider.contains("Coopay"),
                            modifier = Modifier.weight(1f),
                            onClick = { selectedProvider = "Coopay (ኮኦፕ)" }
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
                            val notes = when (selectedCategory) {
                                "የአፈር ማዳበሪያ" -> "$npsbBags ኩንታል NPSB + $ureaBags ኩንታል ዩሪያ"
                                "የመሬት ግብር" -> "የ ${profile?.fullName ?: "አርሶ አደር"} ዓመታዊ የመሬት ግብር"
                                else -> "የተሻሻለ ምርጥ ዘር ጥቅል"
                            }
                            onProcessPayment(
                                selectedCategory,
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
                            text = "በ $selectedProvider አሁን ይክፈሉ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }

        // Past Payments History
        item {
            Text(
                text = "የቀደሙ የክፍያ ደረሰኞች",
                fontSize = 16.sp,
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
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
                                    text = payment.paymentType,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "መለያ፦ ${payment.referenceId} | ${payment.provider}",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Text(
                            text = "${"%,.0f".format(payment.amountBirr)} ብር",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
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
