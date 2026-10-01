package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.ArsoTopBar
import com.example.ui.components.AudioGuideDialog
import com.example.ui.components.ReceiptDialog
import com.example.ui.screens.GpsTrackingScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LandCalculatorScreen
import com.example.ui.screens.MarketplaceScreen
import com.example.ui.screens.OnboardingProfileScreen
import com.example.ui.screens.PaymentScreen
import com.example.ui.screens.QueueTokenScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ArsoAderViewModel
import com.example.ui.viewmodel.ArsoScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ArsoAderApp()
            }
        }
    }
}

@Composable
fun ArsoAderApp(viewModel: ArsoAderViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val farmerProfile by viewModel.farmerProfile.collectAsStateWithLifecycle()
    val activeToken by viewModel.activeToken.collectAsStateWithLifecycle()
    val payments by viewModel.payments.collectAsStateWithLifecycle()
    val crops by viewModel.crops.collectAsStateWithLifecycle()
    val shipmentState by viewModel.shipmentState.collectAsStateWithLifecycle()
    val isOffline by viewModel.isOfflineSimulated.collectAsStateWithLifecycle()
    val voiceGuideMessage by viewModel.voiceGuideMessage.collectAsStateWithLifecycle()
    val recentReceipt by viewModel.recentReceipt.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUserMessage()
        }
    }

    // Android back button handling
    BackHandler(enabled = currentScreen != ArsoScreen.HOME) {
        viewModel.navigateTo(ArsoScreen.HOME)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ArsoTopBar(
                currentScreen = currentScreen,
                isOfflineSimulated = isOffline,
                onNavigateBack = { viewModel.navigateTo(ArsoScreen.HOME) },
                onToggleOffline = { viewModel.toggleOfflineSimulated() },
                onVoiceGuideClick = {
                    val defaultMsg = when (currentScreen) {
                        ArsoScreen.HOME -> "እንኳን ወደ አርሶ አደር መተግበሪያ በደህና መጡ! ማዳበሪያ ለመውሰድ፣ ክፍያ ለመክፈል ወይም ሰብል ለመሸጥ ከታች ያሉትን ቁልፎች ይጫኑ።"
                        ArsoScreen.PROFILE -> "እዚህ ገፅ ላይ ሙሉ ስምዎን፣ መታወቂያዎንና የመሬትዎን ልክ ያስመዝግቡ። መረጃው አንዴ ከተሞላ በስልክዎ ውስጥ ይቀመጣል።"
                        ArsoScreen.QUEUE_TOKEN -> "የማዳበሪያ መውሰጃ ተራ ቁጥርዎ እና የቀበሌ መጋዘን መረጃው እዚህ ይገኛል።"
                        ArsoScreen.PAYMENT -> "የማዳበሪያ፣ የመሬት ግብር ወይም የምርጥ ዘር ክፍያዎን በቴሌብር ወይም በሲቢኢ ብር ከቤትዎ ሆነው መክፈል ይችላሉ።"
                        ArsoScreen.LAND_CALCULATOR -> "የመሬትዎን ስፋት በሄክታር ወይም በጥማድ በማስገባት የሚያስፈልገዎትን የማዳበሪያ መጠን ያሰሉ።"
                        ArsoScreen.GPS_TRACKING -> "የማዳበሪያ ጭነቱ ከጅቡቲ ወደብ ተነስቶ ወደ ቀበሌዎ መጋዘን የደረሰበትን መንገድ በጂፒኤስ ይከታተሉ።"
                        ArsoScreen.MARKETPLACE -> "ያመረቱትን ሰብል ያለ ምንም ደላላ በቀጥታ ለሸማቾች ለመሸጥ እዚህ ይመዝገቡ።"
                    }
                    viewModel.showVoiceGuide(defaultMsg)
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.testTag("arso_bottom_nav")
            ) {
                NavigationBarItem(
                    selected = currentScreen == ArsoScreen.HOME,
                    onClick = { viewModel.navigateTo(ArsoScreen.HOME) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "መነሻ") },
                    label = { Text("መነሻ", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = currentScreen == ArsoScreen.QUEUE_TOKEN,
                    onClick = { viewModel.navigateTo(ArsoScreen.QUEUE_TOKEN) },
                    icon = { Icon(Icons.Default.Assignment, contentDescription = "ተራዬ") },
                    label = { Text("ተራዬ", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = currentScreen == ArsoScreen.PAYMENT,
                    onClick = { viewModel.navigateTo(ArsoScreen.PAYMENT) },
                    icon = { Icon(Icons.Default.Payment, contentDescription = "ክፍያ") },
                    label = { Text("ክፍያ", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = currentScreen == ArsoScreen.GPS_TRACKING,
                    onClick = { viewModel.navigateTo(ArsoScreen.GPS_TRACKING) },
                    icon = { Icon(Icons.Default.LocalShipping, contentDescription = "ጂፒኤስ") },
                    label = { Text("ጂፒኤስ", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = currentScreen == ArsoScreen.MARKETPLACE,
                    onClick = { viewModel.navigateTo(ArsoScreen.MARKETPLACE) },
                    icon = { Icon(Icons.Default.Storefront, contentDescription = "ገበያ") },
                    label = { Text("ገበያ", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = currentScreen == ArsoScreen.PROFILE,
                    onClick = { viewModel.navigateTo(ArsoScreen.PROFILE) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "መገለጫ") },
                    label = { Text("መገለጫ", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                ArsoScreen.HOME -> HomeScreen(
                    farmerProfile = farmerProfile,
                    activeToken = activeToken,
                    isOffline = isOffline,
                    onNavigate = { viewModel.navigateTo(it) },
                    onVoiceClick = { viewModel.showVoiceGuide(it) }
                )
                ArsoScreen.PROFILE -> OnboardingProfileScreen(
                    currentProfile = farmerProfile,
                    onSaveProfile = { fullName, phone, nationality, region, zone, woreda, kebele, nationalId, kebeleId, landHectares, landTimad, taxBirr, primaryCrops ->
                        viewModel.saveFarmerProfile(
                            fullName, phone, nationality, region, zone, woreda, kebele, nationalId, kebeleId, landHectares, landTimad, taxBirr, primaryCrops
                        )
                    },
                    onVoiceClick = { viewModel.showVoiceGuide(it) }
                )
                ArsoScreen.QUEUE_TOKEN -> QueueTokenScreen(
                    profile = farmerProfile,
                    token = activeToken,
                    isOffline = isOffline,
                    onRequestNewToken = { hectares, name, kebele ->
                        viewModel.requestNewQueueToken(hectares, name, kebele)
                    },
                    onVoiceClick = { viewModel.showVoiceGuide(it) }
                )
                ArsoScreen.PAYMENT -> PaymentScreen(
                    profile = farmerProfile,
                    pastPayments = payments,
                    onProcessPayment = { type, amount, provider, phone, notes ->
                        viewModel.processPayment(type, amount, provider, phone, notes)
                    },
                    onVoiceClick = { viewModel.showVoiceGuide(it) }
                )
                ArsoScreen.LAND_CALCULATOR -> LandCalculatorScreen(
                    currentProfile = farmerProfile,
                    onSaveToProfile = { hectares, timad ->
                        val current = farmerProfile
                        if (current != null) {
                            viewModel.saveFarmerProfile(
                                current.fullName,
                                current.phoneNumber,
                                current.nationality,
                                current.region,
                                current.zone,
                                current.woreda,
                                current.kebele,
                                current.nationalId,
                                current.kebeleId,
                                hectares,
                                timad,
                                current.annualTaxBirr,
                                current.primaryCrops
                            )
                        }
                    },
                    onVoiceClick = { viewModel.showVoiceGuide(it) }
                )
                ArsoScreen.GPS_TRACKING -> GpsTrackingScreen(
                    shipment = shipmentState,
                    onVoiceClick = { viewModel.showVoiceGuide(it) }
                )
                ArsoScreen.MARKETPLACE -> MarketplaceScreen(
                    profile = farmerProfile,
                    crops = crops,
                    onAddCrop = { cropName, sellerName, sellerPhone, location, quantity, price ->
                        viewModel.addMarketCrop(cropName, sellerName, sellerPhone, location, quantity, price)
                    },
                    onVoiceClick = { viewModel.showVoiceGuide(it) }
                )
            }

            // Audio Guide Dialog
            voiceGuideMessage?.let { msg ->
                AudioGuideDialog(
                    message = msg,
                    onDismiss = { viewModel.dismissVoiceGuide() }
                )
            }

            // Digital Receipt Dialog
            recentReceipt?.let { rec ->
                ReceiptDialog(
                    record = rec,
                    onDismiss = { viewModel.dismissReceipt() }
                )
            }
        }
    }
}
