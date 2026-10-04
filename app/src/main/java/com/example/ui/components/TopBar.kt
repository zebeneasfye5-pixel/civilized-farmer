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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Language
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.locale.AppLanguage
import com.example.ui.locale.AppStrings
import com.example.ui.viewmodel.ArsoScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArsoTopBar(
    currentScreen: ArsoScreen,
    currentLanguage: AppLanguage,
    isOfflineSimulated: Boolean,
    onNavigateBack: () -> Unit,
    onToggleOffline: () -> Unit,
    onVoiceGuideClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onInstallClick: () -> Unit = {}
) {
    val strings = AppStrings.get(currentLanguage)

    val title = when (currentScreen) {
        ArsoScreen.HOME -> strings.titleHome
        ArsoScreen.PROFILE -> strings.titleProfile
        ArsoScreen.LAND_ADMIN_OFFICE -> strings.titleLandOffice
        ArsoScreen.INPUT_SUPPLIERS -> strings.titleInputs
        ArsoScreen.QUEUE_TOKEN -> strings.titleQueueToken
        ArsoScreen.PAYMENT -> strings.titlePayment
        ArsoScreen.LAND_CALCULATOR -> strings.titleLandCalc
        ArsoScreen.GPS_TRACKING -> strings.titleGps
        ArsoScreen.MARKETPLACE -> strings.titleMarketplace
        ArsoScreen.SYSTEM_INTEGRATION_HUB -> "የሲስተም ማገናኛና ፕሌይ ስቶር"
    }

    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
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
                        contentDescription = strings.back,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        },
        actions = {
            // Direct Phone Install / Web Action Button (ስልክ ላይ ጫን)
            Surface(
                onClick = onInstallClick,
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF01875F),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .testTag("topbar_install_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "ስልክ ላይ ጫን",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "ጫን",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Language Switcher Button (ቀያሪ ቁልፍ)
            Surface(
                onClick = onLanguageClick,
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .testTag("language_switcher_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = strings.selectLanguage,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = currentLanguage.shortBadge,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Voice Assistant Trigger
            IconButton(
                onClick = onVoiceGuideClick,
                modifier = Modifier.testTag("voice_guide_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Hearing,
                    contentDescription = strings.listen,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Offline Mode Toggle & Indicator Chip
            Surface(
                onClick = onToggleOffline,
                shape = RoundedCornerShape(16.dp),
                color = if (isOfflineSimulated) Color(0xFFFDE8E8) else Color(0xFFE8F5E9),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .testTag("offline_toggle_chip")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = if (isOfflineSimulated) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isOfflineSimulated) strings.offline else strings.offlineReady,
                        fontSize = 10.sp,
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
