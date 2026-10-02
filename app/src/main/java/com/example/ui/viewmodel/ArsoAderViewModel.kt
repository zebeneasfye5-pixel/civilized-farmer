package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.ExpenseRecord
import com.example.data.model.FarmerProfile
import com.example.data.model.FertilizerQueueToken
import com.example.data.model.FertilizerShipment
import com.example.data.model.KebeleStatistics
import com.example.data.model.MarketCropItem
import com.example.data.model.PaymentRecord
import com.example.data.repository.ArsoAderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ArsoScreen(val titleAmharic: String, val subtitleAmharic: String) {
    HOME("አርሶ አደር", "የግብርና ግብአት እና ማዳበሪያ ዲጂታል ማዕከል"),
    PROFILE("የአርሶ አደር ማህደር", "ሙሉ የህይወት ታሪክ፣ ፎቶ፣ መታወቂያና የባንክ ሂሳብ"),
    LAND_ADMIN_OFFICE("የግብርናና መሬት አስተዳደር", "የአርሶ አደር ብዛት፣ ሴት ወንድ ስሌትና የወጪ ገቢ ሂሳብ"),
    QUEUE_TOKEN("የማዳበሪያ መውሰጃ ተራ", "የተራ ቁጥር፣ ቀን እና የቀበሌ መጋዘን መረጃ"),
    PAYMENT("የግብአትና ግብር ክፍያ", "በቴሌብርና ሲቢኢ ይክፈሉ፤ ዲጂታል ደረሰኝ ይውሰዱ"),
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

    // Single Farmer Profile Flow (Currently active farmer)
    val farmerProfile: StateFlow<FarmerProfile?>

    // All registered farmers in Kebele (For Land Administration Office)
    val allFarmerProfiles: StateFlow<List<FarmerProfile>>

    // Active Token Flow
    val activeToken: StateFlow<FertilizerQueueToken?>

    // Income Payments Flow
    val payments: StateFlow<List<PaymentRecord>>

    // Expense Records Flow
    val expenses: StateFlow<List<ExpenseRecord>>

    // Crops Flow
    val crops: StateFlow<List<MarketCropItem>>

    // Kebele Automatic Aggregated Statistics Flow (ሴት ወንድ ድምር፣ የመሬት ስፋት፣ ወጪና ገቢ)
    val kebeleStatistics: StateFlow<KebeleStatistics>

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

        allFarmerProfiles = repository.allFarmerProfiles.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
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

        expenses = repository.allExpenses.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        crops = repository.allCrops.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Automatically calculate Kebele Statistics reactive to database changes:
        kebeleStatistics = combine(
            repository.allFarmerProfiles,
            repository.allPayments,
            repository.allExpenses
        ) { farmersList, paymentsList, expensesList ->
            val totalCount = farmersList.size
            val maleCount = farmersList.count { it.gender.trim() == "ወንድ" }
            val femaleCount = farmersList.count { it.gender.trim() == "ሴት" }
            val malePct = if (totalCount > 0) (maleCount.toFloat() / totalCount) * 100f else 0f
            val femalePct = if (totalCount > 0) (femaleCount.toFloat() / totalCount) * 100f else 0f
            val totalHectares = farmersList.sumOf { it.landSizeHectares }

            val totalRevenue = paymentsList.sumOf { it.amountBirr }
            val totalExpense = expensesList.sumOf { it.amountBirr }
            val netBalance = totalRevenue - totalExpense

            val revByCategory = paymentsList.groupBy { it.paymentTitle }
                .mapValues { entry -> entry.value.sumOf { it.amountBirr } }

            val kebeleName = farmersList.firstOrNull()?.kebele ?: "አዴት 01 ቀበሌ"

            KebeleStatistics(
                kebeleName = kebeleName,
                totalFarmers = totalCount,
                maleFarmers = maleCount,
                femaleFarmers = femaleCount,
                malePercentage = malePct,
                femalePercentage = femalePct,
                totalLandHectares = totalHectares,
                totalRevenueBirr = totalRevenue,
                totalExpenseBirr = totalExpense,
                netBalanceBirr = netBalance,
                revenueByCategory = revByCategory
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = KebeleStatistics(
                kebeleName = "አዴት 01 ቀበሌ",
                totalFarmers = 0,
                maleFarmers = 0,
                femaleFarmers = 0,
                malePercentage = 0f,
                femalePercentage = 0f,
                totalLandHectares = 0.0,
                totalRevenueBirr = 0.0,
                totalExpenseBirr = 0.0,
                netBalanceBirr = 0.0,
                revenueByCategory = emptyMap()
            )
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

    fun showReceipt(record: PaymentRecord) {
        _recentReceipt.value = record
    }

    fun dismissReceipt() {
        _recentReceipt.value = null
    }

    fun saveFarmerProfile(
        id: String = "primary_farmer",
        fullName: String,
        phoneNumber: String,
        gender: String,
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
        primaryCrops: String,
        bankName: String,
        bankAccountNumber: String,
        bankAccountHolder: String,
        avatarPreset: String,
        photoUri: String
    ) {
        viewModelScope.launch {
            val updated = FarmerProfile(
                id = id,
                fullName = fullName,
                phoneNumber = phoneNumber,
                gender = gender,
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
                bankName = bankName,
                bankAccountNumber = bankAccountNumber,
                bankAccountHolder = bankAccountHolder.ifEmpty { fullName },
                avatarPreset = avatarPreset,
                photoUri = photoUri,
                isRegistered = true
            )
            repository.saveFarmerProfile(updated)
            _userMessage.value = "የአርሶ አደር መረጃና የባንክ ሂሳብ በስኬት ተመዝግቧል!"
            currentScreen.value = ArsoScreen.HOME
        }
    }

    fun processPayment(
        paymentTitle: String,
        categoryKey: String,
        amount: Double,
        provider: String,
        phone: String,
        farmerId: String,
        farmerName: String,
        kebele: String,
        notes: String
    ) {
        viewModelScope.launch {
            val record = repository.recordPayment(
                paymentTitle = paymentTitle,
                categoryKey = categoryKey,
                amount = amount,
                provider = provider,
                phone = phone,
                farmerId = farmerId,
                farmerName = farmerName,
                kebele = kebele,
                notes = notes
            )
            _recentReceipt.value = record
            _userMessage.value = "ክፍያው በስኬት ተፈፅሟል! ደረሰኝ ቁጥር፦ ${record.receiptOfficialNumber}"
        }
    }

    fun recordOfficeExpense(
        title: String,
        category: String,
        amount: Double,
        kebele: String,
        paidTo: String,
        notes: String
    ) {
        viewModelScope.launch {
            val exp = repository.recordExpense(title, category, amount, kebele, paidTo, notes)
            _userMessage.value = "የቢሮ ወጪ መዝገብ ${exp.voucherNumber} ተመዝግቧል!"
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
