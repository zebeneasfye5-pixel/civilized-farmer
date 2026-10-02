package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.ExpenseRecord
import com.example.data.model.FarmerProfile
import com.example.data.model.KebeleStatistics
import com.example.data.model.PaymentRecord
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LandAdminOfficeScreen(
    statistics: KebeleStatistics,
    farmers: List<FarmerProfile>,
    payments: List<PaymentRecord>,
    expenses: List<ExpenseRecord>,
    onRecordExpense: (title: String, category: String, amount: Double, kebele: String, paidTo: String, notes: String) -> Unit,
    onViewReceipt: (PaymentRecord) -> Unit,
    onVoiceClick: (String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: አጠቃላይ መረጃና ስታቲስቲክስ, 1: የአርሶ አደሮች ማህደር, 2: የወጪና ገቢ ሂሳብ
    var showExpenseDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredFarmers = remember(farmers, searchQuery) {
        if (searchQuery.isEmpty()) farmers
        else farmers.filter {
            it.fullName.contains(searchQuery, ignoreCase = true) ||
            it.phoneNumber.contains(searchQuery) ||
            it.bankAccountNumber.contains(searchQuery)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("land_admin_office_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Office Title Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalance,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "የግብርና እና መሬት አስተዳደር ጽ/ቤት",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "${statistics.kebeleName} | ይስማላ ወረዳ",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                onVoiceClick(
                                    "በ ${statistics.kebeleName} ጠቅላላ ${statistics.totalFarmers} አርሶ አደሮች ተመዝግበዋል። ከነዚህም ${statistics.maleFarmers} ወንዶች እና ${statistics.femaleFarmers} ሴቶች ናቸው። ጠቅላላ ገቢ ${"%,.0f".format(statistics.totalRevenueBirr)} ብር ሲሆን ወጪ ${"%,.0f".format(statistics.totalExpenseBirr)} ብር ነው።"
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
        }

        // Tabs Row: 3 Core views
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .testTag("admin_tabs_row")
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("1. ስታቲስቲክስ", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("2. የአርሶ አደር ማህደር", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("3. ወጪና ገቢ ሂሳብ", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        // TAB 0: Automatic Kebele Demographic & Land Statistics
        if (selectedTab == 0) {
            // Male/Female & Total Farmers Count (ድምሩን እራሱ ሲስተሙ እንዲሰራ)
            item {
                ElevatedCard(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.People,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "የአርሶ አደር ብዛት (በጾታ ተሰልቶ)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFE8F5E9)
                            ) {
                                Text(
                                    text = "ጠቅላላ ድምር፦ ${statistics.totalFarmers}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF1B5E20),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 3 Counter Boxes: Total, Male, Female
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StatBox(
                                title = "ወንድ አርሶ አደር",
                                count = "${statistics.maleFarmers}",
                                percentage = "${"%.0f".format(statistics.malePercentage)}%",
                                icon = Icons.Default.Male,
                                color = Color(0xFF1976D2),
                                modifier = Modifier.weight(1f)
                            )

                            StatBox(
                                title = "ሴት አርሶ አደር",
                                count = "${statistics.femaleFarmers}",
                                percentage = "${"%.0f".format(statistics.femalePercentage)}%",
                                icon = Icons.Default.Female,
                                color = Color(0xFFC2185B),
                                modifier = Modifier.weight(1f)
                            )

                            StatBox(
                                title = "የመሬት ድምር",
                                count = "${"%.1f".format(statistics.totalLandHectares)}",
                                percentage = "ሄክታር",
                                icon = Icons.Default.Landscape,
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Ratio Visual Bar
                        Text(
                            text = "የሴት እና የወንድ አርሶ አደሮች ተሳትፎ ንፅፅር",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(statistics.malePercentage.coerceAtLeast(1f))
                                    .fillMaxSize()
                                    .background(Color(0xFF1976D2))
                            )
                            Box(
                                modifier = Modifier
                                    .weight(statistics.femalePercentage.coerceAtLeast(1f))
                                    .fillMaxSize()
                                    .background(Color(0xFFC2185B))
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "ወንድ፦ ${statistics.maleFarmers} (${"%.1f".format(statistics.malePercentage)}%)", fontSize = 11.sp, color = Color(0xFF1976D2), fontWeight = FontWeight.Bold)
                            Text(text = "ሴት፦ ${statistics.femaleFarmers} (${"%.1f".format(statistics.femalePercentage)}%)", fontSize = 11.sp, color = Color(0xFFC2185B), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Financial Summary Card
            item {
                ElevatedCard(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "የቀበሌው አጠቃላይ የወጪና ገቢ ሂሳብ ሪፖርት",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            FinanceTile(
                                label = "ጠቅላላ ገቢ (ብር)",
                                amount = statistics.totalRevenueBirr,
                                icon = Icons.Default.ArrowDownward,
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.weight(1f)
                            )

                            FinanceTile(
                                label = "ጠቅላላ ወጪ (ብር)",
                                amount = statistics.totalExpenseBirr,
                                icon = Icons.Default.ArrowUpward,
                                color = Color(0xFFD32F2F),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Net Balance Bar
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (statistics.netBalanceBirr >= 0) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                            border = BorderStroke(1.dp, if (statistics.netBalanceBirr >= 0) Color(0xFF81C784) else Color(0xFFE57373)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "የቀበሌው የተጣራ ቀሪ ሂሳብ (Net Balance)፦",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${"%,.2f".format(statistics.netBalanceBirr)} ብር",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (statistics.netBalanceBirr >= 0) Color(0xFF1B5E20) else Color(0xFFC62828)
                                )
                            }
                        }
                    }
                }
            }
        }

        // TAB 1: Farmers Dossiers (የአርሶ አደር ማህደር - ፎቶ፣ መታወቂያና የባንክ አካውንት)
        if (selectedTab == 1) {
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("አርሶ አደር በስም፣ ስልክ ወይም ባንክ ሂሳብ ፈልግ...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_farmer_search_field"),
                    singleLine = true
                )
            }

            items(filteredFarmers) { farmer ->
                FarmerDossierCard(farmer = farmer)
            }
        }

        // TAB 2: Financial Ledger (የገንዘቡ ወጪ ገቢ ዝርዝር ሂሳብ)
        if (selectedTab == 2) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "የገቢና ወጪ ግልፅ የሂሳብ መዝገብ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Button(
                        onClick = { showExpenseDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("add_expense_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("አዲስ ወጪ መዝግብ", fontSize = 12.sp)
                    }
                }
            }

            // Income Section
            item {
                Text(
                    text = "📥 የተሰበሰቡ ገቢዎች (የተሰጡ ህጋዊ ደረሰኞች)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
            }

            items(payments) { payment ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = payment.paymentTitle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "ከፋይ፦ ${payment.farmerName.ifEmpty { "አርሶ አደር" }} | ${payment.provider}",
                                fontSize = 11.sp,
                                color = Color.DarkGray
                            )
                            Text(
                                text = "ደረሰኝ ቁጥር፦ ${payment.receiptOfficialNumber}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF2E7D32)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "+${"%,.0f".format(payment.amountBirr)} ብር",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = Color(0xFF2E7D32)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                onClick = { onViewReceipt(payment) },
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFE8F5E9)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text("ደረሰኝ እይ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
                                }
                            }
                        }
                    }
                }
            }

            // Expense Section
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "📤 የተፈፀሙ ወጪዎች (የማዘዣ ቫውቸሮች)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC62828)
                )
            }

            items(expenses) { expense ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = expense.expenseTitle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "ተከፋይ፦ ${expense.paidTo} | ${expense.category}",
                                fontSize = 11.sp,
                                color = Color.DarkGray
                            )
                            Text(
                                text = "ቫውቸር፦ ${expense.voucherNumber}",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }

                        Text(
                            text = "-${"%,.0f".format(expense.amountBirr)} ብር",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = Color(0xFFC62828)
                        )
                    }
                }
            }
        }
    }

    // Add Expense Dialog
    if (showExpenseDialog) {
        var expenseTitle by remember { mutableStateOf("የማዳበሪያ ጭነት ትራንስፖርት ክፍያ") }
        var category by remember { mutableStateOf("ትራንስፖርትና ሎጀስቲክስ") }
        var amountStr by remember { mutableStateOf("3500") }
        var paidTo by remember { mutableStateOf("ጎጃም የጭነት ማህበር") }
        var notes by remember { mutableStateOf("ለአዴት ቀበሌ ማዳበሪያ ማጓጓዣ") }

        AlertDialog(
            onDismissRequest = { showExpenseDialog = false },
            title = {
                Text(text = "አዲስ የቀበሌ ጽ/ቤት ወጪ መዝግብ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = expenseTitle,
                        onValueChange = { expenseTitle = it },
                        label = { Text("የወጪው አርዕስት (ምክንያት)") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = amountStr,
                        onValueChange = { amountStr = it },
                        label = { Text("የወጪው መጠን (ብር)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = paidTo,
                        onValueChange = { paidTo = it },
                        label = { Text("ተከፋይ አካል / ግለሰብ") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("የወጪ ምድብ") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = amountStr.toDoubleOrNull() ?: 1000.0
                        onRecordExpense(expenseTitle, category, amount, statistics.kebeleName, paidTo, notes)
                        showExpenseDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("ቫውቸር አውጣና መዝግብ", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showExpenseDialog = false },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("ሰርዝ")
                }
            }
        )
    }
}

@Composable
private fun StatBox(
    title: String,
    count: String,
    percentage: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = color.copy(alpha = 0.09f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f)),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = count, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Text(text = title, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
            Text(text = percentage, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun FinanceTile(
    label: String,
    amount: Double,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = color.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.25f)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(color.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = label, fontSize = 11.sp, color = Color.Gray)
                Text(
                    text = "${"%,.0f".format(amount)} ብር",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = color
                )
            }
        }
    }
}

@Composable
private fun FarmerDossierCard(farmer: FarmerProfile) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Farmer Portrait Photo in Dossier
                val photoFile = if (farmer.gender == "ሴት") {
                    File("/app/src/main/res/drawable/farmer_ethiopian_woman_1790921555784.jpg")
                } else {
                    File("/app/src/main/res/drawable/farmer_ethiopian_man_1790921542582.jpg")
                }

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .border(2.dp, if (farmer.gender == "ሴት") Color(0xFFC2185B) else Color(0xFF1976D2), CircleShape)
                ) {
                    if (photoFile.exists()) {
                        AsyncImage(
                            model = photoFile,
                            contentDescription = farmer.fullName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(if (farmer.gender == "ሴት") Color(0xFFFCE4EC) else Color(0xFFE3F2FD)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (farmer.gender == "ሴት") Icons.Default.Female else Icons.Default.Male,
                                contentDescription = null,
                                tint = if (farmer.gender == "ሴት") Color(0xFFC2185B) else Color(0xFF1976D2),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = farmer.fullName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (farmer.gender == "ሴት") Color(0xFFFCE4EC) else Color(0xFFE3F2FD)
                        ) {
                            Text(
                                text = farmer.gender,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (farmer.gender == "ሴት") Color(0xFFC2185B) else Color(0xFF1976D2),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = "ስልክ፦ ${farmer.phoneNumber} | መታወቂያ፦ ${farmer.kebeleId.ifEmpty { "KB-041" }}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = "የመሬት ስፋት፦ ${farmer.landSizeHectares} ሄክታር (${farmer.landSizeTimad} ጥማድ)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2E7D32)
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFEEEEEE))

            // Linked Bank Account Bar
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF8F9FA),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = null,
                        tint = Color(0xFF005BAC),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "የተያያዘ የባንክ ሂሳብ (Bank Linked)፦",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "${farmer.bankName} - ${farmer.bankAccountNumber}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF005BAC)
                        )
                    }
                }
            }
        }
    }
}
