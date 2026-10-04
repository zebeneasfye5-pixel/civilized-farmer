package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.locale.AppLanguage
import com.example.ui.locale.AppStrings

@Composable
fun SystemIntegrationScreen(
    currentLanguage: AppLanguage,
    onVoiceClick: (String) -> Unit,
    onShareAction: () -> Unit = {},
    onSecretAdminTap: () -> Unit = {}
) {
    val context = LocalContext.current
    val strings = AppStrings.get(currentLanguage)
    var tapCounter by remember { mutableIntStateOf(0) }

    val playStorePackageName = "com.aistudio.arsoader.fgqtxz"
    val playStoreWebUrl = "https://play.google.com/store/apps/details?id=$playStorePackageName"
    val playStoreMarketUri = "market://details?id=$playStorePackageName"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("system_integration_screen_content"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Voice Guide & Header
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)),
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
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "የሲስተም ማገናኛዎች እና ፕሌይ ስቶር",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "በቀላሉ ወደ ስልክ መጫኛ፣ ማጋሪያ እና የኦፊሴላዊ አገልግሎቶች መገናኛ",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            onVoiceClick("ይህ ገጽ አፕሊኬሽኑን በፕሌይ ስቶር ለማግኘት፣ ለሌሎች አርሶ አደሮች ለማጋራት እና የቴሌብር፣ የሲቢኢ እና የግብርና ሚኒስቴር ሲስተሞችን በቀጥታ ለማገናኘት የተዘጋጀ ነው።")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = strings.listen,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Section 1: Google Play Store Direct Installation & Updates (የፕሌይ ስቶር ማገናኛ)
        item {
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("play_store_integration_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(Color(0xFF01875F), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shop,
                                    contentDescription = "Google Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Google Play Store (ጉግል ፕሌይ)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "ኦፊሴላዊ የስልክ መጫኛ እና ቋሚ ዝመናዎች",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "የተረጋገጠ",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "ይህን 'አርሶ አደር' መተግበሪያ በቀጥታ ከጉግል ፕሌይ ስቶር በስልክዎ ላይ መጫን፣ የቅርብ ጊዜ ዝመናዎችን መከታተል እና ለሌሎች አርሶ አደሮች ማጋራት ይችላሉ።",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Buttons Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(playStoreMarketUri)).apply {
                                        setPackage("com.android.vending")
                                    }
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(playStoreWebUrl))
                                    context.startActivity(webIntent)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF01875F)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_open_play_store")
                        ) {
                            Icon(Icons.Default.Shop, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("በፕሌይ ስቶር ክፈት", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                onShareAction()
                                val shareText = "የኢትዮጵያ አርሶ አደሮች የማዳበሪያ መውሰጃ ተራ፣ የግብአት ክፍያና የሰብል ገበያ ዲጂታል መተግበሪያን በስልክዎ ይጫኑ፦ $playStoreWebUrl"
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, "አርሶ አደር መተግበሪያ")
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "አፑን ለሌሎች አርሶ አደሮች አጋራ"))
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_share_app")
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("ለአርሶ አደር አጋራ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Section 2: Easy Offline Installation & Bluetooth/ShareIt Guide (በቀላሉ ወደስልክ መጫኛ)
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                border = BorderStroke(1.2.dp, Color(0xFF81C784)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFF2E7D32), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Android, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "ኢንተርኔት በሌለበት በቀላሉ ስልክ ላይ መጫኛ መመሪያ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF1B5E20)
                            )
                            Text(
                                text = "በቀበሌ ውስጥ በብሉቱዝ ወይም ሼርኢት (Bluetooth / ShareIt)",
                                fontSize = 11.sp,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        InstallStepRow(number = "1", text = "የቀበሌው ግብርና ባለሙያ ወይም ወዳጅዎ አፑን በብሉቱዝ/ሼርኢት ይልክልዎታል")
                        InstallStepRow(number = "2", text = "የተላከውን የ'አርሶ አደር' APK ፋይል በስልክዎ ላይ ይጫኑት")
                        InstallStepRow(number = "3", text = "አፑ ከተጫነ በኋላ ያለ ምንም ኢንተርኔት (ኦፍላይን) ወዲያውኑ መስራት ይጀምራል")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Arso Ader Link", playStoreWebUrl)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "የመተግበሪያው ማውረጃ ሊንክ ኮፒ ተደርጓል!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(5.dp))
                            Text("የማውረጃ ሊንኩን ኮፒ አድርግ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Section 3: Essential System Linkage Buttons (አስፈላጊ የሆኑ የሲስተም ማገናኛ ቁልፎች)
        item {
            Text(
                text = "አስፈላጊ የሆኑ የሲስተም ማገናኛ ቁልፎች",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // 3.1 Payment System Linkages (የክፍያ ሲስተሞች)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "1. የፋይናንስና ክፍያ ሲስተም ማገናኛዎች",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SystemLinkTile(
                        title = "Telebirr (ቴሌብር)",
                        subtitle = "ቀጥታ የቴሌብር ሲስተም ክፈት",
                        icon = Icons.Default.Payment,
                        badgeColor = Color(0xFFFFF3E0),
                        iconColor = Color(0xFFE65100),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("telebirr://"))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://telebirr.et"))
                                context.startActivity(webIntent)
                            }
                        }
                    )

                    SystemLinkTile(
                        title = "CBE Birr (ሲቢኢ)",
                        subtitle = "የኢትዮጵያ ንግድ ባንክ ሲስተም",
                        icon = Icons.Default.AccountBalance,
                        badgeColor = Color(0xFFEDE7F6),
                        iconColor = Color(0xFF512DA8),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://combanketh.et"))
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }

        // 3.2 Government & Agricultural Systems (የግብርና እና የመንግስት ተቋማት)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "2. የግብርናና መሬት አስተዳደር መረጃ ሲስተሞች",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                SystemLinkListTile(
                    title = "የግብርና ሚኒስቴር (Ministry of Agriculture)",
                    subtitle = "የማዳበሪያ ኮታ፣ የግብርና ፖሊሲና አገራዊ መረጃዎች",
                    url = "https://www.moa.gov.et",
                    icon = Icons.Default.Public,
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.moa.gov.et"))
                        context.startActivity(intent)
                    }
                )

                SystemLinkListTile(
                    title = "የኢትዮጵያ ግብርና ትራንስፎርሜሽን ኢንስቲትዩት (ATI)",
                    subtitle = "የአፈር ለምነት ካርታ፣ ምርጥ ዘርና የግብርና ቴክኖሎጂ",
                    url = "https://ati.gov.et",
                    icon = Icons.Default.Verified,
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ati.gov.et"))
                        context.startActivity(intent)
                    }
                )

                SystemLinkListTile(
                    title = "የኢትዮጵያ ሚቲዎሮሎጂ ኢንስቲትዩት (የአየር ሁኔታ)",
                    subtitle = "የዝናብ ወቅትና የቀበሌዎች የአየር ሁኔታ ትንበያ",
                    url = "https://www.ethiomet.gov.et",
                    icon = Icons.Default.WbSunny,
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ethiomet.gov.et"))
                        context.startActivity(intent)
                    }
                )
            }
        }

        // 3.3 Direct Hotlines & Emergency Support
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "3. የቀጥታ የእርዳታ የስልክ መስመሮች",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HotlineTile(
                        title = "8028",
                        subtitle = "ነፃ የግብርና ምክር",
                        phoneNumber = "8028",
                        modifier = Modifier.weight(1f)
                    )

                    HotlineTile(
                        title = "994",
                        subtitle = "የቴሌብር ድጋፍ",
                        phoneNumber = "994",
                        modifier = Modifier.weight(1f)
                    )

                    HotlineTile(
                        title = "951",
                        subtitle = "የሲቢኢ ባንክ ድጋፍ",
                        phoneNumber = "951",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // System Specification Badge (Discreet secret developer trigger)
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        tapCounter++
                        if (tapCounter >= 5) {
                            tapCounter = 0
                            onSecretAdminTap()
                        }
                    }
                    .testTag("system_spec_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "የሲስተም መረጃ (System Specification)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "መተግበሪያ፦ አርሶ አደር (Arso Ader) • Version 1.0",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "መለያ ቁጥር፦ $playStorePackageName",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "ተኳኋኝነት፦ Android 7.0+ (Nougat - Android 16) • Play Protect Verified",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun InstallStepRow(number: String, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(Color(0xFF2E7D32), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = number, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 12.sp, color = Color(0xFF1B5E20))
    }
}

@Composable
private fun SystemLinkTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = badgeColor),
        modifier = modifier
            .height(100.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(iconColor.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
                }
                Icon(Icons.Default.OpenInNew, contentDescription = null, tint = iconColor.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
            }
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = iconColor)
                Text(text = subtitle, fontSize = 10.sp, color = iconColor.copy(alpha = 0.8f))
            }
        }
    }
}

@Composable
private fun SystemLinkListTile(
    title: String,
    subtitle: String,
    url: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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
                        .size(38.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = subtitle, fontSize = 11.sp, color = Color.Gray)
                }
            }
            Icon(Icons.Default.OpenInNew, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun HotlineTile(
    title: String,
    subtitle: String,
    phoneNumber: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        border = BorderStroke(1.dp, Color(0xFFC8E6C9)),
        modifier = modifier
            .clickable {
                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                context.startActivity(dialIntent)
            }
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Call, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color(0xFF1B5E20))
            Text(text = subtitle, fontSize = 10.sp, color = Color(0xFF2E7D32))
        }
    }
}
