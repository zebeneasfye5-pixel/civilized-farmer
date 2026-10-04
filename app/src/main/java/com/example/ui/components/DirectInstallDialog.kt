package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.locale.AppLanguage
import com.example.ui.locale.AppStrings

@Composable
fun DirectInstallDialog(
    currentLanguage: AppLanguage,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val strings = AppStrings.get(currentLanguage)

    val webAppUrl = "https://ais-pre-dtengkah6rddcwvehnlxfy-125744783365.europe-west1.run.app"
    val playStorePackageName = "com.aistudio.arsoader.fgqtxz"
    val playStoreUri = "market://details?id=$playStorePackageName"
    val playStoreWebUrl = "https://play.google.com/store/apps/details?id=$playStorePackageName"

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("📲 ቀጥታ ጭነት (APK)", "📷 በካሜራ ስካን (QR)", "🌐 ዋይብ / Web App")

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.testTag("direct_install_dialog"),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(Color(0xFF01875F), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhoneAndroid,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "አፕሊኬሽኑን ስልክዎ ላይ ይጫኑ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "በስልክዎ እና በዋይብ (Web) በቀጥታ እንዲሰራ",
                            fontSize = 11.sp,
                            color = Color(0xFF01875F),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_install_dialog")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "ዝጋ",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Feature Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    InstallBadge(text = "Android APK", color = Color(0xFF2E7D32))
                    InstallBadge(text = "Web / PWA", color = Color(0xFF1565C0))
                    InstallBadge(text = "Google Play", color = Color(0xFF01875F))
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tab Selector
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    edgePadding = 0.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Color(0xFF01875F)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 12.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == index) Color(0xFF01875F) else Color.Gray
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                when (selectedTab) {
                    0 -> {
                        // Direct APK & Play Store
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                                border = BorderStroke(1.dp, Color(0xFF81C784)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "የአርሶ አደር ኤፒኬ (APK) ቀጥታ ጭነት",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color(0xFF1B5E20)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "ኢንተርኔት ቢቋረጥም ሙሉ በሙሉ ከመስመር ውጭ (ኦፍላይን) የሚሰራውን የአርሶ አደር መተግበሪያ አሁኑኑ በስልክዎ ላይ ይጫኑት።",
                                        fontSize = 11.sp,
                                        color = Color(0xFF2E7D32),
                                        lineHeight = 16.sp
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webAppUrl))
                                        context.startActivity(intent)
                                        Toast.makeText(context, "የመተግበሪያው ማውረጃ በስልክዎ ተጀምሯል!", Toast.LENGTH_SHORT).show()
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "አገናኙ ተከፍቷል", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF01875F)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("btn_download_apk_direct")
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("የኤፒኬ ፋይሉን አሁኑኑ አውርድና ጫን", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(playStoreUri)).apply {
                                            setPackage("com.android.vending")
                                        }
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(playStoreWebUrl))
                                        context.startActivity(webIntent)
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("btn_open_play_store_dialog")
                            ) {
                                Icon(Icons.Default.Shop, contentDescription = null, tint = Color(0xFF01875F), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("በGoogle Play Store ላይ ክፈት / ጫን", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF01875F))
                            }
                        }
                    }

                    1 -> {
                        // QR Code Scanner from PC/Web to Phone
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "በስልክዎ ካሜራ ስካን በማድረግ በቀጥታ ይጫኑ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            QrCodeView(
                                data = webAppUrl,
                                size = 180.dp
                            )

                            Text(
                                text = "በኮምፒውተር ወይም በሌላ ስልክ ላይ እየተመለከቱ ከሆነ፣ የስልክዎ ካሜራን ወደዚህ QR Code በማዞር አፑን ወዲያውኑ ስልክዎ ላይ ይጫኑ!",
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                color = Color.Gray,
                                lineHeight = 16.sp
                            )

                            OutlinedButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Arso Ader URL", webAppUrl)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "የመተግበሪያው ሊንክ ኮፒ ተደርጓል!", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("የመጫኛ ሊንኩን ኮፒ አድርግ", fontSize = 11.sp)
                            }
                        }
                    }

                    2 -> {
                        // Web Browser / PWA Add to Home Screen
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                                border = BorderStroke(1.dp, Color(0xFF90CAF9)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "በድረ-ገጽ (Web) ውስጥ ወደ ስልክ መነሻ ገጽ መጫኛ",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color(0xFF0D47A1)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "በማንኛውም ስልክ Chrome፣ Safari ወይም Samsung Internet አሳሽ በመጠቀም አፑን በቀጥታ መጫን ይችላሉ።",
                                        fontSize = 11.sp,
                                        color = Color(0xFF1565C0)
                                    )
                                }
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                PwaStepRow(num = "1", text = "በአሳሽዎ (Browser) ላይ የላይኛውን ባለ 3 ነጥብ (⋮) ወይም የShare ምልክቱን ይጫኑ")
                                PwaStepRow(num = "2", text = "'Install app' ወይም 'Add to Home screen (ወደ መነሻ ገጽ ጨምር)' የሚለውን ይምረጡ")
                                PwaStepRow(num = "3", text = "የ'አርሶ አደር' አርማ ልክ እንደ ተራ አፕሊኬሽን በስልክዎ መነሻ ገጽ ላይ ይቀመጣል")
                            }

                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webAppUrl))
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("በድረ-ገጽ (Web Browser) ውስጥ ክፈት", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF01875F)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("btn_continue_in_app")
            ) {
                Text("በመተግበሪያው ውስጥ ቀጥል", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("አሁን ይቆይ / በኋላ ጫን", color = Color.Gray)
            }
        }
    )
}

@Composable
private fun InstallBadge(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = text, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun PwaStepRow(num: String, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(Color(0xFF1565C0), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = num, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}
