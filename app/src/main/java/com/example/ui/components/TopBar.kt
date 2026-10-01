package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.ArsoScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArsoTopBar(
    currentScreen: ArsoScreen,
    isOfflineSimulated: Boolean,
    onNavigateBack: () -> Unit,
    onToggleOffline: () -> Unit,
    onVoiceGuideClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = currentScreen.titleAmharic,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        navigationIcon = {
            if (currentScreen != ArsoScreen.HOME) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("top_bar_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "ወደ ኋላ ተመለስ",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        },
        actions = {
            // Voice Assistant Trigger
            IconButton(
                onClick = onVoiceGuideClick,
                modifier = Modifier.testTag("voice_guide_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Hearing,
                    contentDescription = "በድምፅ ያዳምጡ",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Offline Mode Toggle & Indicator Chip
            Surface(
                onClick = onToggleOffline,
                shape = RoundedCornerShape(16.dp),
                color = if (isOfflineSimulated) Color(0xFFFDE8E8) else Color(0xFFE8F5E9),
                modifier = Modifier
                    .padding(end = 12.dp)
                    .testTag("offline_toggle_chip")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .background(
                                color = if (isOfflineSimulated) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isOfflineSimulated) "ኦፍላይን" else "ኦፍላይን ዝግጁ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isOfflineSimulated) Color(0xFFC62828) else Color(0xFF1B5E20)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}
