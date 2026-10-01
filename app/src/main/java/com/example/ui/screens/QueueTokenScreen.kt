package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FarmerProfile
import com.example.data.model.FertilizerQueueToken

@Composable
fun QueueTokenScreen(
    profile: FarmerProfile?,
    token: FertilizerQueueToken?,
    isOffline: Boolean,
    onRequestNewToken: (landHectares: Double, farmerName: String, kebele: String) -> Unit,
    onVoiceClick: (String) -> Unit
) {
    val queueNum = token?.queueNumber ?: 48
    val tokenCode = token?.tokenCode ?: "ET-AGR-7281"
    val scheduledDate = token?.scheduledDate ?: "ነሐሴ 25 / ዛሬ"
    val timeSlot = token?.timeSlot ?: "ከጧቱ 3:00 - 5:30"
    val depot = token?.kebeleDepot ?: "${profile?.kebele ?: "አዴት"} ቀበሌ ህ/ስራ መጋዘን"
    val npsb = token?.npsbBags ?: 4
    val urea = token?.ureaBags ?: 3

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("queue_screen_content"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Status Card: Your Queue Number
        item {
            ElevatedCard(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = Color(0xFFE8F5E9)
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Status Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF2E7D32)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ዛሬ ተራዎ ነው!",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "የእርስዎ መውሰጃ ተራ ቁጥር",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1B5E20)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "#$queueNum",
                        fontSize = 56.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1B5E20)
                    )

                    Text(
                        text = "በመጋዘን አሁን እየተስተናገደ ያለው፦ #45 (3 ሰው ብቻ ይቀራል)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2E7D32)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Digital Token Code Badge (For offline presentation)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC8E6C9)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "የይለፍ ዲጂታል ቁጥር (Token)",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = tokenCode,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF1B5E20)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.QrCode2,
                                contentDescription = "QR Code",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Audio Button
                    OutlinedButton(
                        onClick = {
                            onVoiceClick(
                                "ክቡር አርሶ አደር ተራ ቁጥርዎ $queueNum ነው። ዛሬ ከጧቱ $timeSlot በማዳበሪያ መጋዘን ተገኝተው $npsb ኩንታል NPSB እና $urea ኩንታል ዩሪያ መውሰድ ይችላሉ።"
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.VolumeUp, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ተራዎን በድምፅ ያዳምጡ", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Details Breakdown Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "የግብአት መውሰጃ ዝርዝር መረጃ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    DetailItem(
                        icon = Icons.Default.CalendarToday,
                        title = "የተመደበበት ቀን",
                        value = scheduledDate
                    )

                    DetailItem(
                        icon = Icons.Default.AccessTime,
                        title = "የመውሰጃ ሰዓት",
                        value = timeSlot
                    )

                    DetailItem(
                        icon = Icons.Default.LocationOn,
                        title = "የማከፋፈያ መጋዘን ጣቢያ",
                        value = depot
                    )

                    DetailItem(
                        icon = Icons.Default.AssignmentTurnedIn,
                        title = "የተፈቀደ የማዳበሪያ መጠን",
                        value = "$npsb ኩንታል NPSB + $urea ኩንታል ዩሪያ"
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "የመጋዘን ሃላፊ፡ አቶ ደስታ አያሌው",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "ስልክ፦ 0918123456",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        Button(
                            onClick = {
                                onVoiceClick("የቀበሌ ማዳበሪያ መጋዘን ሃላፊ አቶ ደስታ አያሌው ናቸው። ስልካቸው 0918123456 ነው።")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("ይደውሉ", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Offline Presentation Advice
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFFF57F17),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "በመጋዘኑ ኢንተርኔት ባይኖርም ይህንን ስክሪን ወይም የይለፍ ቁጥሩን $tokenCode በማሳየት ማዳበሪያዎን መውሰድ ይችላሉ።",
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = Color(0xFF5D4037)
                    )
                }
            }
        }

        // Request new / refreshed turn button
        item {
            OutlinedButton(
                onClick = {
                    val hectares = profile?.landSizeHectares ?: 2.0
                    val name = profile?.fullName ?: "አበበ ታደሰ"
                    val kebele = profile?.kebele ?: "አዴት"
                    onRequestNewToken(hectares, name, kebele)
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("request_new_token_button")
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("አዲስ ተራ ቁጥር ጠይቅ / አድስ", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun DetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(Color(0xFFE8F5E9), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, fontSize = 11.sp, color = Color.Gray)
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
