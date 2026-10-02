package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
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
import com.example.ui.screens.LandAdminOfficeScreen
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
    val allFarmerProfiles by viewModel.allFarmerProfiles.collectAsStateWithLifecycle()
    val activeToken by viewModel.activeToken.collectAsStateWithLifecycle()
    val payments by viewModel.payments.collectAsStateWithLifecycle()
    val expenses by viewModel.expenses.collectAsStateWithLifecycle()
    val crops by viewModel.crops.collectAsStateWithLifecycle()
    val shipmentState by viewModel.shipmentState.collectAsStateWithLifecycle()
    val kebeleStatistics by viewModel.kebeleStatistics.collectAsStateWithLifecycle()
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
                        ArsoScreen.PROFILE -> "እዚህ ማህደር ላይ ፎቶዎን፣ ጾታዎን፣ ስምዎን፣ አድራሻዎን፣ የባንክ አካውንትዎን እና የመሬትዎን ልክ ያስመዝግቡ።"
                        ArsoScreen.LAND_ADMIN_OFFICE -> "ይህ የቀበሌ ግብርና እና መሬት አስተዳደር ጽ/ቤት ዳሽቦርድ ነው። የተመዘገቡ አርሶ አደሮች ብዛት፣ የሴትና ወንድ ስሌት፣ የተሰበሰበ ገቢ እና የተፈፀመ ወጪ እዚህ ይታያል።"
                        ArsoScreen.QUEUE_TOKEN -> "የማዳበሪያ መውሰጃ ተራ ቁጥርዎ እና የቀበሌ መጋዘን መረጃው እዚህ ይገኛል።"
                        ArsoScreen.PAYMENT -> "የማዳበሪያ፣ የመሬት ግብር፣ የምርጥ ዘር ወይም የመስኖ ክፍያዎን በሞባይል ከቤትዎ ሆነው ይክፈሉ፤ ህጋዊ ደረሰኝ ወዲያው ይሰጥዎታል።"
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
                    label = { Text("መነሻ", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = currentScreen == ArsoScreen.LAND_ADMIN_OFFICE,
                    onClick = { viewModel.navigateTo(ArsoScreen.LAND_ADMIN_OFFICE) },
                    icon = { Icon(Icons.Default.AccountBalance, contentDescription = "ጽ/ቤት") },
                    label = { Text("ጽ/ቤት", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = currentScreen == ArsoScreen.QUEUE_TOKEN,
                    onClick = { viewModel.navigateTo(ArsoScreen.QUEUE_TOKEN) },
                    icon = { Icon(Icons.Default.Assignment, contentDescription = "ተራዬ") },
                    label = { Text("ተራዬ", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = currentScreen == ArsoScreen.PAYMENT,
                    onClick = { viewModel.navigateTo(ArsoScreen.PAYMENT) },
                    icon = { Icon(Icons.Default.Payment, contentDescription = "ክፍያ") },
                    label = { Text("ክፍያ", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = currentScreen == ArsoScreen.GPS_TRACKING,
                    onClick = { viewModel.navigateTo(ArsoScreen.GPS_TRACKING) },
                    icon = { Icon(Icons.Default.LocalShipping, contentDescription = "ጂፒኤስ") },
                    label = { Text("ጂፒኤስ", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = currentScreen == ArsoScreen.PROFILE,
                    onClick = { viewModel.navigateTo(ArsoScreen.PROFILE) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "ማህደር") },
                    label = { Text("ማህደር", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) }
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
                ArsoScreen.LAND_ADMIN_OFFICE -> LandAdminOfficeScreen(
                    statistics = kebeleStatistics,
                    farmers = allFarmerProfiles,
                    payments = payments,
                    expenses = expenses,
                    onRecordExpense = { title, category, amount, kebele, paidTo, notes ->
                        viewModel.recordOfficeExpense(title, category, amount, kebele, paidTo, notes)
                    },
                    onViewReceipt = { viewModel.showReceipt(it) },
                    onVoiceClick = { viewModel.showVoiceGuide(it) }
                )
                ArsoScreen.PROFILE -> OnboardingProfileScreen(
                    currentProfile = farmerProfile,
                    onSaveProfile = { fullName, phone, gender, nationality, region, zone, woreda, kebele, nationalId, kebeleId, landHectares, landTimad, taxBirr, primaryCrops, bankName, bankAccountNumber, bankAccountHolder, avatarPreset, photoUri ->
                        viewModel.saveFarmerProfile(
                            fullName = fullName,
                            phoneNumber = phone,
                            gender = gender,
                            nationality = nationality,
                            region = region,
                            zone = zone,
                            woreda = woreda,
                            kebele = kebele,
                            nationalId = nationalId,
                            kebeleId = kebeleId,
                            landSizeHectares = landHectares,
                            landSizeTimad = landTimad,
                            annualTaxBirr = taxBirr,
                            primaryCrops = primaryCrops,
                            bankName = bankName,
                            bankAccountNumber = bankAccountNumber,
                            bankAccountHolder = bankAccountHolder,
                            avatarPreset = avatarPreset,
                            photoUri = photoUri
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
                    onProcessPayment = { title, categoryKey, amount, provider, phone, notes ->
                        val farmerName = farmerProfile?.fullName ?: "አበበ ታደሰ"
                        val kebele = farmerProfile?.kebele ?: "አዴት 01 ቀበሌ"
                        viewModel.processPayment(
                            paymentTitle = title,
                            categoryKey = categoryKey,
                            amount = amount,
                            provider = provider,
                            phone = phone,
                            farmerId = farmerProfile?.id ?: "primary_farmer",
                            farmerName = farmerName,
                            kebele = kebele,
                            notes = notes
                        )
                    },
                    onViewReceipt = { viewModel.showReceipt(it) },
                    onVoiceClick = { viewModel.showVoiceGuide(it) }
                )
                ArsoScreen.LAND_CALCULATOR -> LandCalculatorScreen(
                    currentProfile = farmerProfile,
                    onSaveToProfile = { hectares, timad ->
                        val current = farmerProfile
                        if (current != null) {
                            viewModel.saveFarmerProfile(
                                fullName = current.fullName,
                                phoneNumber = current.phoneNumber,
                                gender = current.gender,
                                nationality = current.nationality,
                                region = current.region,
                                zone = current.zone,
                                woreda = current.woreda,
                                kebele = current.kebele,
                                nationalId = current.nationalId,
                                kebeleId = current.kebeleId,
                                landSizeHectares = hectares,
                                landSizeTimad = timad,
                                annualTaxBirr = current.annualTaxBirr,
                                primaryCrops = current.primaryCrops,
                                bankName = current.bankName,
                                bankAccountNumber = current.bankAccountNumber,
                                bankAccountHolder = current.bankAccountHolder,
                                avatarPreset = current.avatarPreset,
                                photoUri = current.photoUri
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
