package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Agriculture
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.ArsoTopBar
import com.example.ui.components.AudioGuideDialog
import com.example.ui.components.DirectInstallDialog
import com.example.ui.components.LanguageSelectionDialog
import com.example.ui.components.ReceiptDialog
import com.example.ui.locale.AppStrings
import com.example.ui.screens.GpsTrackingScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InputSupplierScreen
import com.example.ui.screens.LandAdminOfficeScreen
import com.example.ui.screens.LandCalculatorScreen
import com.example.ui.screens.MarketplaceScreen
import com.example.ui.screens.OnboardingProfileScreen
import com.example.ui.screens.PaymentScreen
import com.example.ui.screens.QueueTokenScreen
import com.example.ui.screens.SystemIntegrationScreen
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
    val currentLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val farmerProfile by viewModel.farmerProfile.collectAsStateWithLifecycle()
    val allFarmerProfiles by viewModel.allFarmerProfiles.collectAsStateWithLifecycle()
    val activeToken by viewModel.activeToken.collectAsStateWithLifecycle()
    val payments by viewModel.payments.collectAsStateWithLifecycle()
    val expenses by viewModel.expenses.collectAsStateWithLifecycle()
    val crops by viewModel.crops.collectAsStateWithLifecycle()
    val inputs by viewModel.inputs.collectAsStateWithLifecycle()
    val inputOrders by viewModel.inputOrders.collectAsStateWithLifecycle()
    val shipmentState by viewModel.shipmentState.collectAsStateWithLifecycle()
    val kebeleStatistics by viewModel.kebeleStatistics.collectAsStateWithLifecycle()
    val isOffline by viewModel.isOfflineSimulated.collectAsStateWithLifecycle()
    val voiceGuideMessage by viewModel.voiceGuideMessage.collectAsStateWithLifecycle()
    val recentReceipt by viewModel.recentReceipt.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

    val strings = AppStrings.get(currentLanguage)
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showInstallDialog by remember { mutableStateOf(true) }
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

    // Centered responsive box for desktop/tablet web browsers and mobile phones
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)),
        contentAlignment = Alignment.TopCenter
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp),
            topBar = {
                ArsoTopBar(
                    currentScreen = currentScreen,
                    currentLanguage = currentLanguage,
                    isOfflineSimulated = isOffline,
                    onNavigateBack = { viewModel.navigateTo(ArsoScreen.HOME) },
                    onToggleOffline = { viewModel.toggleOfflineSimulated() },
                    onVoiceGuideClick = {
                        val defaultMsg = when (currentScreen) {
                            ArsoScreen.HOME -> strings.voiceHome
                            ArsoScreen.PROFILE -> strings.voiceProfile
                            ArsoScreen.LAND_ADMIN_OFFICE -> strings.voiceOffice
                            ArsoScreen.INPUT_SUPPLIERS -> strings.voiceInputs
                            ArsoScreen.QUEUE_TOKEN -> strings.voiceQueue
                            ArsoScreen.PAYMENT -> strings.voicePayment
                            ArsoScreen.LAND_CALCULATOR -> "${strings.titleLandCalc}: ${strings.subtitleLandCalc}"
                            ArsoScreen.GPS_TRACKING -> "${strings.titleGps}: ${strings.subtitleGps}"
                            ArsoScreen.MARKETPLACE -> strings.voiceMarket
                            ArsoScreen.SYSTEM_INTEGRATION_HUB -> "ይህ ገጽ አፕሊኬሽኑን በፕሌይ ስቶር ለማግኘት፣ በቀላሉ ወደ ስልክ ለመጫን እና አስፈላጊ የሲስተም ማገናኛዎችን ለመጠቀም የሚያስችል ነው።"
                        }
                        viewModel.showVoiceGuide(defaultMsg)
                    },
                    onLanguageClick = { showLanguageDialog = true },
                    onInstallClick = { showInstallDialog = true }
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
                    icon = { Icon(Icons.Default.Home, contentDescription = strings.navHome) },
                    label = { Text(strings.navHome, fontSize = 9.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = currentScreen == ArsoScreen.INPUT_SUPPLIERS,
                    onClick = { viewModel.navigateTo(ArsoScreen.INPUT_SUPPLIERS) },
                    icon = { Icon(Icons.Default.Agriculture, contentDescription = strings.navInputs) },
                    label = { Text(strings.navInputs, fontSize = 9.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = currentScreen == ArsoScreen.MARKETPLACE,
                    onClick = { viewModel.navigateTo(ArsoScreen.MARKETPLACE) },
                    icon = { Icon(Icons.Default.Storefront, contentDescription = strings.navMarket) },
                    label = { Text(strings.navMarket, fontSize = 9.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = currentScreen == ArsoScreen.QUEUE_TOKEN,
                    onClick = { viewModel.navigateTo(ArsoScreen.QUEUE_TOKEN) },
                    icon = { Icon(Icons.Default.Assignment, contentDescription = strings.navTurn) },
                    label = { Text(strings.navTurn, fontSize = 9.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = currentScreen == ArsoScreen.PAYMENT,
                    onClick = { viewModel.navigateTo(ArsoScreen.PAYMENT) },
                    icon = { Icon(Icons.Default.Payment, contentDescription = strings.navPayment) },
                    label = { Text(strings.navPayment, fontSize = 9.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = currentScreen == ArsoScreen.LAND_ADMIN_OFFICE,
                    onClick = { viewModel.navigateTo(ArsoScreen.LAND_ADMIN_OFFICE) },
                    icon = { Icon(Icons.Default.AccountBalance, contentDescription = strings.navOffice) },
                    label = { Text(strings.navOffice, fontSize = 9.sp, fontWeight = FontWeight.SemiBold) }
                )
                NavigationBarItem(
                    selected = currentScreen == ArsoScreen.PROFILE,
                    onClick = { viewModel.navigateTo(ArsoScreen.PROFILE) },
                    icon = { Icon(Icons.Default.Person, contentDescription = strings.navProfile) },
                    label = { Text(strings.navProfile, fontSize = 9.sp, fontWeight = FontWeight.SemiBold) }
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
                    currentLanguage = currentLanguage,
                    onNavigate = { viewModel.navigateTo(it) },
                    onVoiceClick = { viewModel.showVoiceGuide(it) },
                    onLanguageClick = { showLanguageDialog = true },
                    onInstallClick = { showInstallDialog = true }
                )
                ArsoScreen.INPUT_SUPPLIERS -> InputSupplierScreen(
                    profile = farmerProfile,
                    inputs = inputs,
                    orders = inputOrders,
                    onOrderInput = { input, quantity, farmerName, farmerPhone, pickupDepot ->
                        viewModel.orderAgriculturalInput(input, quantity, farmerName, farmerPhone, pickupDepot)
                    },
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
                ArsoScreen.SYSTEM_INTEGRATION_HUB -> SystemIntegrationScreen(
                    currentLanguage = currentLanguage,
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

            // Language Selection Dialog
            if (showLanguageDialog) {
                LanguageSelectionDialog(
                    currentLanguage = currentLanguage,
                    onLanguageSelected = { viewModel.setLanguage(it) },
                    onDismiss = { showLanguageDialog = false }
                )
            }

            // Direct Phone Install Dialog (Auto-opens on launch or when clicked)
            if (showInstallDialog) {
                DirectInstallDialog(
                    currentLanguage = currentLanguage,
                    onDismiss = { showInstallDialog = false }
                )
            }
        }
    }
}
}
