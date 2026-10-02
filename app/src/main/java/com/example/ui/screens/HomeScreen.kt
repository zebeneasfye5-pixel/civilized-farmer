package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.FarmerProfile
import com.example.data.model.FertilizerQueueToken
import com.example.ui.viewmodel.ArsoScreen
import java.io.File

@Composable
fun HomeScreen(
    farmerProfile: FarmerProfile?,
    activeToken: FertilizerQueueToken?,
    isOffline: Boolean,
    onNavigate: (ArsoScreen) -> Unit,
    onVoiceClick: (String) -> Unit
) {
    val farmerName = farmerProfile?.fullName?.ifEmpty { "ክቡር አርሶ አደር" } ?: "ክቡር አርሶ አደር"
    val kebeleName = farmerProfile?.kebele?.ifEmpty { "አዴት ቀበሌ" } ?: "አዴት ቀበሌ"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen_content"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome and Hero Banner
        item {
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                    ) {
                        // Hero visual image
                        val heroFile = File("/app/src/main/res/drawable/farm_hero_banner_1790816767382.jpg")
                        if (heroFile.exists()) {
                            AsyncImage(
                                model = heroFile,
                                contentDescription = "የኢትዮጵያ እርሻ መልክዓ ምድር",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFF1B5E20), Color(0xFF2E7D32), Color(0xFF81C784))
                                        )
                                    )
                            )
                        }

                        // Gradient overlay for readability
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f))
                                    )
                                )
                        )

                        // Title text over hero
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "እንኳን ደህና መጡ!",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "$farmerName ($kebeleName)",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Audio explanation button for illiterate or elderly farmers
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Agriculture,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "የአፈር ማዳበሪያና ግብአት ማዕከል",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Surface(
                            onClick = {
                                onVoiceClick(
                                    "እንኳን ወደ አርሶ አደር መተግበሪያ በደህና መጡ! በዚህ መተግበሪያ የማዳበሪያ መውሰጃ ተራዎትን ማወቅ፣ በስልክዎ መክፈል፣ ማዳበሪያው የደረሰበትን በጂፒኤስ መከታተል እና ሰብልዎን ያለ ደላላ መሸጥ ይችላሉ።"
                                )
                            },
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "ያዳምጡ",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Queue Banner (Primary requirement: ተራቸውን የሚያውቁበት)
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8F5E9)
                ),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF2E7D32)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(ArsoScreen.QUEUE_TOKEN) }
                    .testTag("home_active_queue_banner")
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
                                    .size(36.dp)
                                    .background(Color(0xFF2E7D32), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.QrCode,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "የማዳበሪያ መውሰጃ ተራዎ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color(0xFF1B5E20)
                                )
                                Text(
                                    text = "የተመደበው ቀን፦ ${activeToken?.scheduledDate ?: "ነሐሴ 25 / ዛሬ"}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF2E7D32)
                                )
                            }
                        }

                        // Queue badge number
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF2E7D32)
                        ) {
                            Text(
                                text = "ተራ #${activeToken?.queueNumber ?: 48}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "የተፈቀደ ኮታ፦ ${activeToken?.npsbBags ?: 4} ኩንታል NPSB + ${activeToken?.ureaBags ?: 3} ኩንታል ዩሪያ",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1B5E20)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "ቦታ፦ ${activeToken?.kebeleDepot ?: "አዴት ቀበሌ ህ/ስራ መጋዘን"}",
                            fontSize = 12.sp,
                            color = Color(0xFF388E3C),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "ሙሉውን እይ →",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                    }
                }
            }
        }

        // Land Administration Office Banner
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(ArsoScreen.LAND_ADMIN_OFFICE) }
                    .testTag("home_land_admin_banner")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
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
                                text = "የግብርናና መሬት አስተዳደር ጽ/ቤት",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "የአርሶ አደር ብዛት (ሴት/ወንድ ድምር) እና የወጪ ገቢ ሂሳብ",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Text(
                        text = "ክፈት →",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Section Title: ዋና ዋና አገልግሎቶች
        item {
            Text(
                text = "የአርሶ አደር ዋና ዋና አገልግሎቶች",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // 6 Big Action Tiles
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionTile(
                        title = "የማዳበሪያ መውሰጃ ተራ",
                        subtitle = "ተራ ቁጥርና ቀን ማወቂያ",
                        icon = Icons.Default.Assignment,
                        containerColor = Color(0xFFE8F5E9),
                        contentColor = Color(0xFF1B5E20),
                        testTag = "tile_queue_token",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(ArsoScreen.QUEUE_TOKEN) }
                    )

                    ActionTile(
                        title = "የግብአት ክፍያ በቤትዎ",
                        subtitle = "በቴሌብርና ሲቢኢ ይክፈሉ",
                        icon = Icons.Default.Payment,
                        containerColor = Color(0xFFFFF3E0),
                        contentColor = Color(0xFFE65100),
                        testTag = "tile_payment",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(ArsoScreen.PAYMENT) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionTile(
                        title = "የመሬት ልክ መመዝገቢያ",
                        subtitle = "የመሬት ስፋትና ማዳበሪያ ስሌት",
                        icon = Icons.Default.Calculate,
                        containerColor = Color(0xFFE1F5FE),
                        contentColor = Color(0xFF0277BD),
                        testTag = "tile_land_calc",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(ArsoScreen.LAND_CALCULATOR) }
                    )

                    ActionTile(
                        title = "የጭነት ጂፒኤስ ክትትል",
                        subtitle = "ከጅቡቲ ወደብ እስከ ቀበሌ መጋዘን",
                        icon = Icons.Default.LocalShipping,
                        containerColor = Color(0xFFF3E5F5),
                        contentColor = Color(0xFF7B1FA2),
                        testTag = "tile_gps_tracking",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(ArsoScreen.GPS_TRACKING) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionTile(
                        title = "ያለ ደላላ የሰብል ገበያ",
                        subtitle = "ምርትዎን በቀጥታ ለተጠቃሚው",
                        icon = Icons.Default.Storefront,
                        containerColor = Color(0xFFFFFDE7),
                        contentColor = Color(0xFFF57F17),
                        testTag = "tile_marketplace",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(ArsoScreen.MARKETPLACE) }
                    )

                    ActionTile(
                        title = "የአርሶ አደር መገለጫ",
                        subtitle = "መታወቂያ፣ አድራሻና ግብር",
                        icon = Icons.Default.Person,
                        containerColor = Color(0xFFEFEBE9),
                        contentColor = Color(0xFF4E342E),
                        testTag = "tile_profile",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(ArsoScreen.PROFILE) }
                    )
                }
            }
        }

        // Quick Hotline & Extension Agent Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ),
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
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "የግብርና ባለሙያ የስልክ መስመር 8028",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "ነፃ የግብርና የምክርና መረጃ መስመር",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Button(
                        onClick = {
                            onVoiceClick("የግብርና ባለሙያ የስልክ መስመር 8028 ነፃ መስመር ነው። ስለ ማዳበሪያ አጠቃቀም እና ምርጥ ዘር ጥያቄ ካለዎት መደወል ይችላሉ።")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("መመሪያ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    testTag: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .height(130.dp)
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(contentColor.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = contentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = contentColor.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
