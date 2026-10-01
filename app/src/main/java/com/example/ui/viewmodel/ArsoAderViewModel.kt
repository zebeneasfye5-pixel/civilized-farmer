package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.FarmerProfile
import com.example.data.model.FertilizerQueueToken
import com.example.data.model.FertilizerShipment
import com.example.data.model.MarketCropItem
import com.example.data.model.PaymentRecord
import com.example.data.repository.ArsoAderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ArsoScreen(val titleAmharic: String, val subtitleAmharic: String) {
    HOME("አርሶ አደር", "የግብርና ግብአት እና ማዳበሪያ ዲጂታል ማዕከል"),
    PROFILE("የአርሶ አደር መገለጫ", "ሙሉ የህይወት ታሪክ፣ መታወቂያ እና የመሬት መረጃ"),
    QUEUE_TOKEN("የማዳበሪያ መውሰጃ ተራ", "የተራ ቁጥር፣ ቀን እና የቀበሌ መጋዘን መረጃ"),
    PAYMENT("የግብአት ክፍያ", "በቴሌብር እና በሲቢኢ ከቤትዎ ሆነው ይክፈሉ"),
    LAND_CALCULATOR("የመሬት ልክ መመዝገቢያ", "የመሬት ስፋትና የሚያስፈልግ የማዳበሪያ መጠን ስሌት"),
    GPS_TRACKING("የጭነት ጂፒኤስ ክትትል", "ማዳበሪያው ከውጭ እስከ ቀበሌ መጋዘን የደረሰበት መንገድ"),
    MARKETPLACE("ያለ ደላላ የሰብል ገበያ", "ምርትዎን በቀጥታ ለተጠቃሚው ያቅርቡ")
}

class ArsoAderViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ArsoAderRepository

    val currentScreen = MutableStateFlow(ArsoScreen.HOME)

    // Voice assistant / Audio guide message state
    private val _voiceGuideMessage = MutableStateFlow<String?>(null)
    val voiceGuideMessage: StateFlow<String?> = _voiceGuideMessage.asStateFlow()

    // Offline simulation flag
    private val _isOfflineSimulated = MutableStateFlow(false)
    val isOfflineSimulated: StateFlow<Boolean> = _isOfflineSimulated.asStateFlow()

    // Last completed payment receipt for popup dialog
    private val _recentReceipt = MutableStateFlow<PaymentRecord?>(null)
    val recentReceipt: StateFlow<PaymentRecord?> = _recentReceipt.asStateFlow()

    // GPS shipment data
    private val _shipmentState = MutableStateFlow<FertilizerShipment?>(null)
    val shipmentState: StateFlow<FertilizerShipment?> = _shipmentState.asStateFlow()

    // Farmer Profile Flow
    val farmerProfile: StateFlow<FarmerProfile?>

    // Active Token Flow
    val activeToken: StateFlow<FertilizerQueueToken?>

    // Payments Flow
    val payments: StateFlow<List<PaymentRecord>>

    // Crops Flow
    val crops: StateFlow<List<MarketCropItem>>

    // Message snackbar
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = ArsoAderRepository(database.farmerDao())

        farmerProfile = repository.farmerProfile.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        activeToken = repository.activeToken.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        payments = repository.allPayments.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        crops = repository.allCrops.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
            _shipmentState.value = repository.getFertilizerShipmentData()
        }
    }

    fun navigateTo(screen: ArsoScreen) {
        currentScreen.value = screen
    }

    fun toggleOfflineSimulated() {
        _isOfflineSimulated.value = !_isOfflineSimulated.value
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    fun showVoiceGuide(message: String) {
        _voiceGuideMessage.value = message
    }

    fun dismissVoiceGuide() {
        _voiceGuideMessage.value = null
    }

    fun dismissReceipt() {
        _recentReceipt.value = null
    }

    fun saveFarmerProfile(
        fullName: String,
        phoneNumber: String,
        nationality: String,
        region: String,
        zone: String,
        woreda: String,
        kebele: String,
        nationalId: String,
        kebeleId: String,
        landSizeHectares: Double,
        landSizeTimad: Double,
        annualTaxBirr: Double,
        primaryCrops: String
    ) {
        viewModelScope.launch {
            val updated = FarmerProfile(
                id = "primary_farmer",
                fullName = fullName,
                phoneNumber = phoneNumber,
                nationality = nationality.ifEmpty { "ኢትዮጵያዊ" },
                region = region,
                zone = zone,
                woreda = woreda,
                kebele = kebele,
                nationalId = nationalId,
                kebeleId = kebeleId,
                landSizeHectares = landSizeHectares,
                landSizeTimad = landSizeTimad,
                annualTaxBirr = annualTaxBirr,
                primaryCrops = primaryCrops,
                isRegistered = true
            )
            repository.saveFarmerProfile(updated)
            _userMessage.value = "የአርሶ አደር መረጃዎ በስኬት ተመዝግቧል!"
            currentScreen.value = ArsoScreen.HOME
        }
    }

    fun processPayment(
        type: String,
        amount: Double,
        provider: String,
        phone: String,
        notes: String
    ) {
        viewModelScope.launch {
            val record = repository.recordPayment(type, amount, provider, phone, notes)
            _recentReceipt.value = record
            _userMessage.value = "$provider ክፍያዎ በስኬት ተጠናቋል! ደረሰኝ ቁጥር፦ ${record.referenceId}"
        }
    }

    fun addMarketCrop(
        cropName: String,
        sellerName: String,
        sellerPhone: String,
        location: String,
        quantityQuintals: Double,
        pricePerQuintal: Double
    ) {
        viewModelScope.launch {
            repository.addMarketCrop(
                cropName = cropName,
                sellerName = sellerName,
                sellerPhone = sellerPhone,
                location = location,
                quantityQuintals = quantityQuintals,
                pricePerQuintal = pricePerQuintal
            )
            _userMessage.value = "የምርት መረጃዎ ያለ ደላላ በቀጥታ ወደ ገበያው ገብቷል!"
        }
    }

    fun requestNewQueueToken(landHectares: Double, farmerName: String, kebele: String) {
        viewModelScope.launch {
            repository.requestNewQueueToken(landHectares, farmerName, kebele)
            _userMessage.value = "አዲስ የማዳበሪያ መውሰጃ ተራ ቁጥር ተመድቦልዎታል!"
        }
    }
}
