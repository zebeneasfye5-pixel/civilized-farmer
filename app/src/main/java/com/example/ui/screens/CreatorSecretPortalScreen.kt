package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CreatorEarningsSummary
import com.example.data.model.CreatorPayoutRecord
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CreatorSecretPortalScreen(
    earnings: CreatorEarningsSummary?,
    payouts: List<CreatorPayoutRecord>,
    onUpdatePayoutDetails: (method: String, accountNumber: String, accountName: String) -> Unit,
    onWithdraw: (amount: Double, method: String, targetAccount: String, recipientName: String) -> Unit,
    onLockAndExit: () -> Unit
) {
    val context = LocalContext.current
    val currentData = earnings ?: CreatorEarningsSummary()

    var selectedMethod by remember(currentData.payoutMethod) { mutableStateOf(currentData.payoutMethod) }
    var accountNumber by remember(currentData.payoutAccountNumber) { mutableStateOf(currentData.payoutAccountNumber) }
    var accountName by remember(currentData.payoutAccountName) { mutableStateOf(currentData.payoutAccountName) }

    var withdrawAmountText by remember { mutableStateOf("1000") }

    val formatter = remember { NumberFormat.getNumberInstance(Locale.US) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("creator_secret_portal_content"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Security & Owner Header
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(Color(0xFF10B981), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "ሚስጥራዊ የገንቢ ማዕከል",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = currentData.ownerEmail,
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }

                        Button(
                            onClick = onLockAndExit,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("btn_lock_creator_portal")
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("ቆልፍና ውጣ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF334155)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Color(0xFF34D399), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "🔒 ይህ ገጽ ለርስዎ (ለገንቢው) ብቻ የሚታይ ሲሆን ለአጠቃላይ ተጠቃሚዎች በፍጹም አይታይም።",
                                fontSize = 11.sp,
                                color = Color(0xFFE2E8F0)
                            )
                        }
                    }
                }
            }
        }

        // Available Balance Card (የተጣራ ቀሪ ሂሳብ)
        item {
            ElevatedCard(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.Transparent),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF0F766E), Color(0xFF047857), Color(0xFF065F46))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "የተከማቸ ቀሪ ገቢ (Available Balance)",
                                fontSize = 13.sp,
                                color = Color(0xFFA7F3D0),
                                fontWeight = FontWeight.SemiBold
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0x33FFFFFF)
                            ) {
                                Text(
                                    text = "የኢትዮጵያ ብር (ETB)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${formatter.format(currentData.availableBalanceBirr)} ETB",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        HorizontalDivider(color = Color(0x33FFFFFF))

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("እስካሁን የተከፈለ (Withdrawn)", fontSize = 10.sp, color = Color(0xFFA7F3D0))
                                Text("${formatter.format(currentData.totalWithdrawnBirr)} ETB", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("አጠቃላይ ድምር ገቢ (Lifetime)", fontSize = 10.sp, color = Color(0xFFA7F3D0))
                                val lifetime = currentData.availableBalanceBirr + currentData.totalWithdrawnBirr
                                Text("${formatter.format(lifetime)} ETB", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // Live Monetization Revenue Streams (የገቢ ምንጮች ዝርዝር)
        item {
            Text(
                text = "የገቢ ምንጮች እና ስሌት (የተጠቃሚዎች፣ ላይክ፣ ሼር እና ቴሌኮም)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // 1. Registrations
                val regIncome = currentData.totalRegistrations * currentData.regCommissionRateBirr
                RevenueStreamTile(
                    title = "የተጠቃሚዎች ምዝገባ (Farmer Registrations)",
                    metric = "${currentData.totalRegistrations} አርሶ አደሮች ተመዝግበዋል",
                    rate = "${currentData.regCommissionRateBirr.toInt()} ብር / አርሶ አደር",
                    earned = "${formatter.format(regIncome)} ETB",
                    icon = Icons.Default.Person,
                    accentColor = Color(0xFF2563EB)
                )

                // 2. Shares
                val shareIncome = currentData.totalShares * currentData.shareCommissionRateBirr
                RevenueStreamTile(
                    title = "የሊንክና የአፕሊኬሽን ሼር (Shares & Referrals)",
                    metric = "${currentData.totalShares} ሼሮች ተደርገዋል",
                    rate = "${currentData.shareCommissionRateBirr.toInt()} ብር / ሼር",
                    earned = "${formatter.format(shareIncome)} ETB",
                    icon = Icons.Default.Share,
                    accentColor = Color(0xFF7C3AED)
                )

                // 3. Likes & Views
                val likeIncome = currentData.totalLikesAndImpressions * currentData.likeCommissionRateBirr
                RevenueStreamTile(
                    title = "የላይክና የመስተጋብር ገቢ (Likes & Views)",
                    metric = "${currentData.totalLikesAndImpressions} ላይኮች ተመዝግበዋል",
                    rate = "${currentData.likeCommissionRateBirr.toInt()} ብር / ላይክ",
                    earned = "${formatter.format(likeIncome)} ETB",
                    icon = Icons.Default.ThumbUp,
                    accentColor = Color(0xFFD97706)
                )

                // 4. Ethio Telecom VAS & Partner Ads
                RevenueStreamTile(
                    title = "የኢንተርኔትና የቴሌኮም ድርጅቶች ገቢ (Ethio Telecom VAS)",
                    metric = "የቴሌኮም ተጨማሪ እሴትና የማስታወቂያ አጋሮች ገቢ",
                    rate = "ወርሃዊ ድርሻ (Telecom Revenue Share)",
                    earned = "${formatter.format(currentData.ethioTelecomVasEarnedBirr)} ETB",
                    icon = Icons.Default.Wifi,
                    accentColor = Color(0xFF059669)
                )
            }
        }

        // Section: Instant Payout to Ethiopian Methods (የኢትዮጵያ ክፍያ ማገናኛና ማስተላለፊያ)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.2.dp, Color(0xFF10B981)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(Color(0xFFE8F5E9), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Payment, contentDescription = null, tint = Color(0xFF047857), modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "ገንዘብ ማስተላለፊያ ወደ ኢትዮጵያ ክፍያ ዘዴ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "በቴሌብር (Telebirr) ወይም በኢትዮጵያ ንግድ ባንክ (CBE) ወዲያውኑ ውሰድ",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Method Selector Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedMethod == "Telebirr",
                            onClick = { selectedMethod = "Telebirr" },
                            label = { Text("Telebirr (ቴሌብር)") },
                            leadingIcon = {
                                if (selectedMethod == "Telebirr") {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFE8F5E9),
                                selectedLabelColor = Color(0xFF047857)
                            )
                        )

                        FilterChip(
                            selected = selectedMethod == "CBE",
                            onClick = { selectedMethod = "CBE" },
                            label = { Text("CBE (ንግድ ባንክ)") },
                            leadingIcon = {
                                if (selectedMethod == "CBE") {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFEDE7F6),
                                selectedLabelColor = Color(0xFF5B21B6)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Account fields
                    OutlinedTextField(
                        value = accountNumber,
                        onValueChange = { accountNumber = it },
                        label = { Text(if (selectedMethod == "Telebirr") "የቴሌብር ስልክ ቁጥር (Telebirr Phone)" else "የCBE ሂሳብ ቁጥር (CBE Account)") },
                        placeholder = { Text(if (selectedMethod == "Telebirr") "0921458976" else "1000284910294") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = accountName,
                        onValueChange = { accountName = it },
                        label = { Text("የሂሳቡ ባለቤት ስም (Account Holder Name)") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = {
                                onUpdatePayoutDetails(selectedMethod, accountNumber, accountName)
                            },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("የመቀበያ መረጃን መዝግብ", fontSize = 11.sp)
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    // Withdrawal Amount
                    Text(
                        text = "የሚወጣው የገንዘብ መጠን (Withdrawal Amount in ETB)፦",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("500", "1000", "3000", "5000").forEach { preset ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (withdrawAmountText == preset) Color(0xFF047857) else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { withdrawAmountText = preset }
                            ) {
                                Text(
                                    text = "$preset ብር",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (withdrawAmountText == preset) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = withdrawAmountText,
                        onValueChange = { withdrawAmountText = it },
                        label = { Text("መጠን በብር (Amount ETB)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val amount = withdrawAmountText.toDoubleOrNull() ?: 0.0
                            if (amount <= 0) {
                                Toast.makeText(context, "እባክዎ ትክክለኛ የብር መጠን ያስገቡ!", Toast.LENGTH_SHORT).show()
                            } else if (amount > currentData.availableBalanceBirr) {
                                Toast.makeText(context, "በቂ ያልሆነ ቀሪ ሂሳብ!", Toast.LENGTH_SHORT).show()
                            } else {
                                onWithdraw(amount, selectedMethod, accountNumber, accountName)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_process_creator_payout")
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ወደ $selectedMethod ወዲያውኑ አስተላልፍ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Payout History Log
        item {
            Text(
                text = "የተፈፀሙ የክፍያ ዝርዝሮች (Payout History)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (payouts.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                        Text("እስካሁን የተፈጸመ የክፍያ ማስተላለፍ የለም", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        } else {
            items(payouts) { payout ->
                PayoutReceiptRow(payout = payout, formatter = formatter)
            }
        }
    }
}

@Composable
private fun RevenueStreamTile(
    title: String,
    metric: String,
    rate: String,
    earned: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(accentColor.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = metric, fontSize = 11.sp, color = Color.Gray)
                    Text(text = "ተመን፦ $rate", fontSize = 10.sp, color = accentColor, fontWeight = FontWeight.Medium)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = earned, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = accentColor)
                Text(text = "የተረጋገጠ ገቢ", fontSize = 9.sp, color = Color(0xFF047857))
            }
        }
    }
}

@Composable
private fun PayoutReceiptRow(payout: CreatorPayoutRecord, formatter: NumberFormat) {
    val dateStr = remember(payout.timestamp) {
        val sdf = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault())
        sdf.format(Date(payout.timestamp))
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFFC8E6C9)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFE8F5E9), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF047857), modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "${payout.method} • ${payout.targetAccount}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(text = "ማመሳከሪያ፦ ${payout.referenceNumber}", fontSize = 10.sp, color = Color.Gray)
                    Text(text = dateStr, fontSize = 9.sp, color = Color.Gray)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "+${formatter.format(payout.amountBirr)} ETB",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color(0xFF047857)
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFE8F5E9)
                ) {
                    Text(
                        text = payout.status,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
