package com.example.data.repository

import com.example.data.local.FarmerDao
import com.example.data.model.AgriculturalInputItem
import com.example.data.model.CreatorEarningsSummary
import com.example.data.model.CreatorPayoutRecord
import com.example.data.model.ExpenseRecord
import com.example.data.model.FarmerProfile
import com.example.data.model.FertilizerQueueToken
import com.example.data.model.FertilizerShipment
import com.example.data.model.InputOrderItem
import com.example.data.model.MarketCropItem
import com.example.data.model.PaymentRecord
import com.example.data.model.ShipmentCheckpoint
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ArsoAderRepository(private val dao: FarmerDao) {

    val farmerProfile: Flow<FarmerProfile?> = dao.getFarmerProfile()
    val allFarmerProfiles: Flow<List<FarmerProfile>> = dao.getAllFarmerProfiles()
    val activeToken: Flow<FertilizerQueueToken?> = dao.getActiveQueueToken()
    val allPayments: Flow<List<PaymentRecord>> = dao.getAllPayments()
    val allExpenses: Flow<List<ExpenseRecord>> = dao.getAllExpenses()
    val allCrops: Flow<List<MarketCropItem>> = dao.getAllCrops()
    val allInputs: Flow<List<AgriculturalInputItem>> = dao.getAllInputs()
    val allInputOrders: Flow<List<InputOrderItem>> = dao.getAllInputOrders()

    // Secret Creator Monetization Flows (Hidden from regular users)
    val creatorEarnings: Flow<CreatorEarningsSummary?> = dao.getCreatorEarnings()
    val allCreatorPayouts: Flow<List<CreatorPayoutRecord>> = dao.getAllCreatorPayouts()

    suspend fun recordRegistrationEarning() {
        val current = dao.getCreatorEarningsSync() ?: CreatorEarningsSummary()
        val updated = current.copy(
            totalRegistrations = current.totalRegistrations + 1,
            availableBalanceBirr = current.availableBalanceBirr + current.regCommissionRateBirr,
            lastUpdated = System.currentTimeMillis()
        )
        dao.insertOrUpdateEarnings(updated)
    }

    suspend fun recordShareEarning() {
        val current = dao.getCreatorEarningsSync() ?: CreatorEarningsSummary()
        val updated = current.copy(
            totalShares = current.totalShares + 1,
            availableBalanceBirr = current.availableBalanceBirr + current.shareCommissionRateBirr,
            lastUpdated = System.currentTimeMillis()
        )
        dao.insertOrUpdateEarnings(updated)
    }

    suspend fun recordLikeEarning() {
        val current = dao.getCreatorEarningsSync() ?: CreatorEarningsSummary()
        val updated = current.copy(
            totalLikesAndImpressions = current.totalLikesAndImpressions + 1,
            availableBalanceBirr = current.availableBalanceBirr + current.likeCommissionRateBirr,
            lastUpdated = System.currentTimeMillis()
        )
        dao.insertOrUpdateEarnings(updated)
    }

    suspend fun updateCreatorPayoutDetails(method: String, accountNumber: String, accountName: String) {
        val current = dao.getCreatorEarningsSync() ?: CreatorEarningsSummary()
        val updated = current.copy(
            payoutMethod = method,
            payoutAccountNumber = accountNumber,
            payoutAccountName = accountName,
            lastUpdated = System.currentTimeMillis()
        )
        dao.insertOrUpdateEarnings(updated)
    }

    suspend fun processCreatorPayout(amount: Double, method: String, targetAccount: String, recipientName: String): CreatorPayoutRecord {
        val current = dao.getCreatorEarningsSync() ?: CreatorEarningsSummary()
        val withdrawAmount = amount.coerceAtMost(current.availableBalanceBirr).coerceAtLeast(10.0)
        val updated = current.copy(
            availableBalanceBirr = (current.availableBalanceBirr - withdrawAmount).coerceAtLeast(0.0),
            totalWithdrawnBirr = current.totalWithdrawnBirr + withdrawAmount,
            lastUpdated = System.currentTimeMillis()
        )
        dao.insertOrUpdateEarnings(updated)

        val txRef = "ET-PAY-${(100000..999999).random()}"
        val record = CreatorPayoutRecord(
            payoutId = txRef,
            amountBirr = withdrawAmount,
            method = method,
            targetAccount = targetAccount,
            recipientName = recipientName,
            status = "የተከፈለ (Transferred)",
            referenceNumber = "TX-${(10000000..99999999).random()}",
            timestamp = System.currentTimeMillis()
        )
        dao.insertPayoutRecord(record)
        return record
    }

    suspend fun saveFarmerProfile(profile: FarmerProfile) {
        dao.insertOrUpdateProfile(profile)
        // Automatically credit creator with registration commission
        recordRegistrationEarning()
        val npsbCount = (profile.landSizeHectares * 1.5).toInt().coerceAtLeast(1)
        val ureaCount = (profile.landSizeHectares * 1.5).toInt().coerceAtLeast(1)
        val defaultToken = FertilizerQueueToken(
            tokenCode = "ET-AGR-${(1000..9999).random()}",
            queueNumber = 42,
            farmerName = profile.fullName.ifEmpty { "ክቡር አርሶ አደር" },
            kebeleDepot = "${profile.kebele} የግብርና ግብአት ማከፋፈያ ጣቢያ",
            scheduledDate = getFormattedDateTomorrow(),
            timeSlot = "ከጧቱ 2:30 - 5:00",
            npsbBags = npsbCount,
            ureaBags = ureaCount,
            status = "ዛሬ ተራዎ ነው",
            isTurnActive = true
        )
        dao.insertQueueToken(defaultToken)
    }

    suspend fun requestNewQueueToken(landHectares: Double, farmerName: String, kebele: String) {
        val npsbCount = (landHectares * 1.5).toInt().coerceAtLeast(1)
        val ureaCount = (landHectares * 1.5).toInt().coerceAtLeast(1)
        val token = FertilizerQueueToken(
            tokenCode = "ET-AGR-${(1000..9999).random()}",
            queueNumber = (25..85).random(),
            farmerName = farmerName.ifEmpty { "ክቡር አርሶ አደር" },
            kebeleDepot = "$kebele የግብርና ግብአት ማከፋፈያ ጣቢያ",
            scheduledDate = getFormattedDateTomorrow(),
            timeSlot = "ከጧቱ 3:00 - 6:00",
            npsbBags = npsbCount,
            ureaBags = ureaCount,
            status = "ይጠብቁ",
            isTurnActive = false
        )
        dao.insertQueueToken(token)
    }

    suspend fun recordPayment(
        paymentTitle: String,
        categoryKey: String,
        amount: Double,
        provider: String,
        phone: String,
        farmerId: String,
        farmerName: String,
        kebele: String,
        notes: String
    ): PaymentRecord {
        val nextNum = (1000..9999).random()
        val record = PaymentRecord(
            referenceId = "PAY-${(100000..999999).random()}",
            receiptOfficialNumber = "KB-REC-2026-$nextNum",
            paymentTitle = paymentTitle,
            paymentType = paymentTitle,
            categoryKey = categoryKey,
            amountBirr = amount,
            provider = provider,
            status = "የተከፈለ",
            payerPhone = phone,
            farmerId = farmerId,
            farmerName = farmerName,
            kebele = kebele,
            receiptNotes = notes
        )
        dao.insertPayment(record)
        return record
    }

    suspend fun recordExpense(
        title: String,
        category: String,
        amount: Double,
        kebele: String,
        paidTo: String,
        notes: String
    ): ExpenseRecord {
        val voucherNum = "PV-2026-${(100..999).random()}"
        val record = ExpenseRecord(
            expenseTitle = title,
            category = category,
            amountBirr = amount,
            kebele = kebele,
            paidTo = paidTo,
            voucherNumber = voucherNum,
            notes = notes
        )
        dao.insertExpense(record)
        return record
    }

    suspend fun addMarketCrop(
        cropName: String,
        sellerName: String,
        sellerPhone: String,
        location: String,
        quantityQuintals: Double,
        pricePerQuintal: Double
    ) {
        val crop = MarketCropItem(
            cropName = cropName,
            sellerName = sellerName,
            sellerPhone = sellerPhone,
            location = location,
            quantityQuintals = quantityQuintals,
            pricePerQuintal = pricePerQuintal
        )
        dao.insertCrop(crop)
    }

    suspend fun deleteMarketCrop(crop: MarketCropItem) {
        dao.deleteCrop(crop)
    }

    suspend fun orderInput(
        inputName: String,
        category: String,
        quantity: Int,
        totalBirr: Double,
        farmerName: String,
        farmerPhone: String,
        pickupDepot: String
    ): InputOrderItem {
        val order = InputOrderItem(
            orderNumber = "ORD-2026-${(1000..9999).random()}",
            inputName = inputName,
            category = category,
            quantity = quantity,
            totalBirr = totalBirr,
            farmerName = farmerName,
            farmerPhone = farmerPhone,
            pickupDepot = pickupDepot
        )
        dao.insertInputOrder(order)
        return order
    }

    suspend fun seedInitialDataIfEmpty() {
        val currentProfile = dao.getFarmerProfileSync()
        if (currentProfile == null) {
            val primaryFarmer = FarmerProfile(
                id = "primary_farmer",
                fullName = "አበበ ታደሰ ወርቁ",
                phoneNumber = "0911234567",
                gender = "ወንድ",
                nationality = "ኢትዮጵያዊ",
                region = "አማራ",
                zone = "ምዕራብ ጎጃም",
                woreda = "ይስማላ / መርዓዊ",
                kebele = "አዴት 01 ቀበሌ",
                nationalId = "FAN-84930219",
                kebeleId = "KB-AD-0428",
                landSizeHectares = 2.5,
                landSizeTimad = 10.0,
                annualTaxBirr = 450.0,
                primaryCrops = "ማኛ ጤፍ፣ ነጭ ስንዴ፣ ቀይ በቆሎ",
                bankName = "የኢትዮጵያ ንግድ ባንክ (CBE)",
                bankAccountNumber = "1000284910294",
                bankAccountHolder = "አበበ ታደሰ ወርቁ",
                avatarPreset = "man_1",
                isRegistered = true
            )
            dao.insertOrUpdateProfile(primaryFarmer)

            val otherFarmers = listOf(
                FarmerProfile(
                    id = "farmer_f1",
                    fullName = "ወ/ሮ ፋንቱ ተሾመ መንገሻ",
                    phoneNumber = "0921456789",
                    gender = "ሴት",
                    nationality = "ኢትዮጵያዊ",
                    region = "አማራ",
                    zone = "ምዕራብ ጎጃም",
                    woreda = "ይስማላ / መርዓዊ",
                    kebele = "አዴት 01 ቀበሌ",
                    nationalId = "FAN-93821045",
                    kebeleId = "KB-AD-0112",
                    landSizeHectares = 1.8,
                    landSizeTimad = 7.2,
                    annualTaxBirr = 340.0,
                    primaryCrops = "ጤፍ፣ ቦሎቄ፣ አተር",
                    bankName = "አዋሽ ባንክ (Awash Bank)",
                    bankAccountNumber = "01320492817200",
                    bankAccountHolder = "ፋንቱ ተሾመ",
                    avatarPreset = "woman_1",
                    isRegistered = true
                ),
                FarmerProfile(
                    id = "farmer_m2",
                    fullName = "አቶ ገብረማርያም ከበደ አያሌው",
                    phoneNumber = "0932567890",
                    gender = "ወንድ",
                    nationality = "ኢትዮጵያዊ",
                    region = "አማራ",
                    zone = "ምዕራብ ጎጃም",
                    woreda = "ይስማላ / መርዓዊ",
                    kebele = "አዴት 01 ቀበሌ",
                    nationalId = "FAN-71049283",
                    kebeleId = "KB-AD-0235",
                    landSizeHectares = 3.2,
                    landSizeTimad = 12.8,
                    annualTaxBirr = 580.0,
                    primaryCrops = "ነጭ ስንዴ፣ ገብስ፣ ዳጉሳ",
                    bankName = "የኦሮሚያ ህ/ስራ ባንክ (Coop Bank)",
                    bankAccountNumber = "1002948201948",
                    bankAccountHolder = "ገብረማርያም ከበደ",
                    avatarPreset = "man_2",
                    isRegistered = true
                ),
                FarmerProfile(
                    id = "farmer_f2",
                    fullName = "ወ/ሮ ብርቱካን አስፋው ኃይሌ",
                    phoneNumber = "0943678901",
                    gender = "ሴት",
                    nationality = "ኢትዮጵያዊ",
                    region = "አማራ",
                    zone = "ምዕራብ ጎጃም",
                    woreda = "ይስማላ / መርዓዊ",
                    kebele = "አዴት 01 ቀበሌ",
                    nationalId = "FAN-64920194",
                    kebeleId = "KB-AD-0389",
                    landSizeHectares = 2.0,
                    landSizeTimad = 8.0,
                    annualTaxBirr = 380.0,
                    primaryCrops = "በቆሎ፣ ማሽላ፣ ባቄላ",
                    bankName = "የኢትዮጵያ ንግድ ባንክ (CBE)",
                    bankAccountNumber = "1000392019482",
                    bankAccountHolder = "ብርቱካን አስፋው",
                    avatarPreset = "woman_2",
                    isRegistered = true
                ),
                FarmerProfile(
                    id = "farmer_m3",
                    fullName = "አቶ ደሳለኝ ሞገስ ታምራት",
                    phoneNumber = "0954789012",
                    gender = "ወንድ",
                    nationality = "ኢትዮጵያዊ",
                    region = "አማራ",
                    zone = "ምዕራብ ጎጃም",
                    woreda = "ይስማላ / መርዓዊ",
                    kebele = "አዴት 01 ቀበሌ",
                    nationalId = "FAN-52019482",
                    kebeleId = "KB-AD-0541",
                    landSizeHectares = 2.4,
                    landSizeTimad = 9.6,
                    annualTaxBirr = 440.0,
                    primaryCrops = "ማኛ ጤፍ፣ በቆሎ",
                    bankName = "ዳሸን ባንክ (Dashen Bank)",
                    bankAccountNumber = "5192049281029",
                    bankAccountHolder = "ደሳለኝ ሞገስ",
                    avatarPreset = "man_1",
                    isRegistered = true
                ),
                FarmerProfile(
                    id = "farmer_f3",
                    fullName = "ወ/ሮ አልማዝ በላይነህ ዘለቀ",
                    phoneNumber = "0965890123",
                    gender = "ሴት",
                    nationality = "ኢትዮጵያዊ",
                    region = "አማራ",
                    zone = "ምዕራብ ጎጃም",
                    woreda = "ይስማላ / መርዓዊ",
                    kebele = "አዴት 01 ቀበሌ",
                    nationalId = "FAN-41092837",
                    kebeleId = "KB-AD-0672",
                    landSizeHectares = 1.5,
                    landSizeTimad = 6.0,
                    annualTaxBirr = 300.0,
                    primaryCrops = "ጤፍ፣ ስንዴ፣ ሽንብራ",
                    bankName = "አባይ ባንክ (Abay Bank)",
                    bankAccountNumber = "2094820192841",
                    bankAccountHolder = "አልማዝ በላይነህ",
                    avatarPreset = "woman_1",
                    isRegistered = true
                )
            )

            for (farmer in otherFarmers) {
                dao.insertOrUpdateProfile(farmer)
            }

            val initialToken = FertilizerQueueToken(
                tokenCode = "ET-AGR-7281",
                queueNumber = 48,
                farmerName = primaryFarmer.fullName,
                kebeleDepot = "አዴት 01 ቀበሌ የግብርና ህ/ስ/ማህበር መጋዘን",
                scheduledDate = "ነሐሴ 25 / ዛሬ",
                timeSlot = "ከጧቱ 3:00 - 5:30",
                npsbBags = 4,
                ureaBags = 3,
                status = "ዛሬ ተራዎ ነው",
                isTurnActive = true
            )
            dao.insertQueueToken(initialToken)

            val payments = listOf(
                PaymentRecord(
                    referenceId = "TLB-948201",
                    receiptOfficialNumber = "KB-REC-2026-0041",
                    paymentTitle = "የአፈር ማዳበሪያ ክፍያ (4 ኩንታል NPSB + 3 ኩንታል ዩሪያ)",
                    paymentType = "የአፈር ማዳበሪያ",
                    categoryKey = "FERTILIZER",
                    amountBirr = 28650.0,
                    provider = "Telebirr (ቴሌብር)",
                    status = "የተከፈለ",
                    payerPhone = "0911234567",
                    farmerId = "primary_farmer",
                    farmerName = "አበበ ታደሰ ወርቁ",
                    kebele = "አዴት 01 ቀበሌ",
                    receiptNotes = "ክፍያው በስኬት ተፈፅሟል፤ በመጋዘን ደረሰኙን ያሳዩ"
                ),
                PaymentRecord(
                    referenceId = "CBE-829104",
                    receiptOfficialNumber = "KB-REC-2026-0042",
                    paymentTitle = "ዓመታዊ የመሬት መጠቀሚያ ግብር",
                    paymentType = "የመሬት ግብር",
                    categoryKey = "LAND_TAX",
                    amountBirr = 450.0,
                    provider = "CBE Birr (ሲቢኢ)",
                    status = "የተከፈለ",
                    payerPhone = "0911234567",
                    farmerId = "primary_farmer",
                    farmerName = "አበበ ታደሰ ወርቁ",
                    kebele = "አዴት 01 ቀበሌ",
                    receiptNotes = "የ2016/2017 ዓ.ም የመሬት ግብር የተከፈለ"
                ),
                PaymentRecord(
                    referenceId = "AWB-619283",
                    receiptOfficialNumber = "KB-REC-2026-0043",
                    paymentTitle = "የተሻሻለ ምርጥ ዘር (ማኛ ጤፍ 50 ኪ.ግ)",
                    paymentType = "የተሻሻለ ምርጥ ዘር",
                    categoryKey = "SEED",
                    amountBirr = 5200.0,
                    provider = "Telebirr (ቴሌብር)",
                    status = "የተከፈለ",
                    payerPhone = "0921456789",
                    farmerId = "farmer_f1",
                    farmerName = "ወ/ሮ ፋንቱ ተሾመ መንገሻ",
                    kebele = "አዴት 01 ቀበሌ",
                    receiptNotes = "ከቀበሌ ምርጥ ዘር ማዕከል የተረከቡ"
                ),
                PaymentRecord(
                    referenceId = "CPY-510294",
                    receiptOfficialNumber = "KB-REC-2026-0044",
                    paymentTitle = "የገጠር መሬት ይዞታ ማረጋገጫ ካርታ (የደብተር ክፍያ)",
                    paymentType = "የይዞታ ማረጋገጫ ካርታ",
                    categoryKey = "LAND_TITLING",
                    amountBirr = 600.0,
                    provider = "Coopay (ኮኦፕ)",
                    status = "የተከፈለ",
                    payerPhone = "0932567890",
                    farmerId = "farmer_m2",
                    farmerName = "አቶ ገብረማርያም ከበደ አያሌው",
                    kebele = "አዴት 01 ቀበሌ",
                    receiptNotes = "የቀበሌ መሬት አስተዳደር ይዞታ ካርታ የተሰጠ"
                ),
                PaymentRecord(
                    referenceId = "TLB-492019",
                    receiptOfficialNumber = "KB-REC-2026-0045",
                    paymentTitle = "የቀበሌ የጋራ መስኖ ውሃ አገልግሎት ክፍያ",
                    paymentType = "የመስኖ ውሃ ክፍያ",
                    categoryKey = "IRRIGATION",
                    amountBirr = 350.0,
                    provider = "Telebirr (ቴሌብር)",
                    status = "የተከፈለ",
                    payerPhone = "0943678901",
                    farmerId = "farmer_f2",
                    farmerName = "ወ/ሮ ብርቱካን አስፋው ኃይሌ",
                    kebele = "አዴት 01 ቀበሌ",
                    receiptNotes = "የደረቅ ወቅት የመስኖ ውሃ ድርሻ ክፍያ"
                )
            )

            for (payment in payments) {
                dao.insertPayment(payment)
            }

            val expenses = listOf(
                ExpenseRecord(
                    expenseTitle = "ለግብርና ሚኒስቴር የአፈር ማዳበሪያ ግዢ የተላለፈ",
                    category = "የማዳበሪያ ግዢ",
                    amountBirr = 22400.0,
                    kebele = "አዴት 01 ቀበሌ",
                    paidTo = "የኢትዮጵያ ግብርና ስራዎች ኮርፖሬሽን",
                    voucherNumber = "PV-2026-0012",
                    notes = "ለአዴት ቀበሌ ማዳበሪያ ኮታ ማሟያ የተፈፀመ"
                ),
                ExpenseRecord(
                    expenseTitle = "የማዳበሪያና ምርጥ ዘር ከዞን መጋዘን የጭነት ትራንስፖርት",
                    category = "ትራንስፖርትና ሎጀስቲክስ",
                    amountBirr = 3800.0,
                    kebele = "አዴት 01 ቀበሌ",
                    paidTo = "ጎጃም የትራንስፖርት ማህበር",
                    voucherNumber = "PV-2026-0013",
                    notes = "ከባህር ዳር ማከፋፈያ ወደ አዴት ቀበሌ ህ/ስራ ማህበር"
                ),
                ExpenseRecord(
                    expenseTitle = "የቀበሌ ግብአት መጋዘን ኪራይና የጥበቃ አበል",
                    category = "የመጋዘን ኪራይና ጥበቃ",
                    amountBirr = 2500.0,
                    kebele = "አዴት 01 ቀበሌ",
                    paidTo = "የቀበሌው ጥበቃ ቡድን",
                    voucherNumber = "PV-2026-0014",
                    notes = "የነሐሴ ወር የመጋዘን ጥበቃ"
                ),
                ExpenseRecord(
                    expenseTitle = "የአዴት ማዕከላዊ መስኖ ቦይ ጥገናና ጽዳት",
                    category = "የመስኖ መሰረተ ልማት",
                    amountBirr = 1800.0,
                    kebele = "አዴት 01 ቀበሌ",
                    paidTo = "የቀበሌ የመስኖ ተጠቃሚዎች ኮሚቴ",
                    voucherNumber = "PV-2026-0015",
                    notes = "የመስኖ ቦይ ደለል ማጽጃና ማስተካከያ"
                )
            )

            for (expense in expenses) {
                dao.insertExpense(expense)
            }
        }

        // Ensure marketplace crops are seeded if empty
        if (dao.getCropsCount() == 0) {
            val crops = listOf(
                MarketCropItem(
                    cropName = "ማኛ ጤፍ (የመጀመሪያ ደረጃ)",
                    sellerName = "አበበ ታደሰ (እኔ)",
                    sellerPhone = "0911234567",
                    location = "ምዕራብ ጎጃም፣ አዴት",
                    quantityQuintals = 20.0,
                    pricePerQuintal = 9800.0
                ),
                MarketCropItem(
                    cropName = "ነጭ ስንዴ (ኦርጋኒክ)",
                    sellerName = "ገብረማርያም ከበደ",
                    sellerPhone = "0923456789",
                    location = "ምስራቅ ጎጃም፣ ደብረ ማርቆስ",
                    quantityQuintals = 35.0,
                    pricePerQuintal = 6200.0
                ),
                MarketCropItem(
                    cropName = "ቀይ ቦሎቄ እና ባቄላ",
                    sellerName = "ወ/ሮ ፋንቱ ተሾመ",
                    sellerPhone = "0934567890",
                    location = "ደቡብ ወሎ፣ ደሴ ዙሪያ",
                    quantityQuintals = 15.0,
                    pricePerQuintal = 5400.0
                ),
                MarketCropItem(
                    cropName = "የተመረጠ የበቆሎ ምርት",
                    sellerName = "ሙሉጌታ አስፋው",
                    sellerPhone = "0945678901",
                    location = "ምዕራብ ሸዋ፣ አምቦ",
                    quantityQuintals = 50.0,
                    pricePerQuintal = 4100.0
                )
            )
            for (crop in crops) {
                dao.insertCrop(crop)
            }
        }

        // Ensure Agricultural Inputs (ምርጥ ዘር፣ ማዳበሪያ እና የግብርና መሳሪያዎች) are seeded if empty
        if (dao.getInputsCount() == 0) {
            val inputs = listOf(
                AgriculturalInputItem(
                    name = "ማኛ ጤፍ የተሻሻለ ምርጥ ዘር (DZ-Cr-387)",
                    category = "ምርጥ ዘር",
                    description = "በሄክታር እስከ 28 ኩንታል ምርት የሚሰጥ፣ ነቀዝንና በሽታን የሚቋቋም የተረጋገጠ ምርጥ ዘር",
                    priceBirr = 5200.0,
                    unit = "በኩንታል (100 ኪ.ግ)",
                    availableStock = 120,
                    supplierName = "የኢትዮጵያ የግብርና ስራዎች ኮርፖሬሽን",
                    supplierPhone = "0115512345",
                    depotLocation = "የአዴት ማዕከላዊ ግብአት መጋዘን"
                ),
                AgriculturalInputItem(
                    name = "የኩክሳ ነጭ ስንዴ ምርጥ ዘር (Kekeba Wheat)",
                    category = "ምርጥ ዘር",
                    description = "ዋግ በሽታን የሚቋቋም ከፍተኛ ምርታማነት ያለው የተረጋገጠ የስንዴ ዝርያ",
                    priceBirr = 4800.0,
                    unit = "በኩንታል (100 ኪ.ግ)",
                    availableStock = 95,
                    supplierName = "የአማራ ምርጥ ዘር ድርጅት",
                    supplierPhone = "0582201948",
                    depotLocation = "አዴት 01 ቀበሌ ማከፋፈያ"
                ),
                AgriculturalInputItem(
                    name = "የቦቆሎ ምርጥ ዘር (BH-661 Hybrid Maize)",
                    category = "ምርጥ ዘር",
                    description = "በቆላማና ወይናደጋ በፍጥነት የሚደርስ በሄክታር እስከ 80 ኩንታል የሚያስገኝ ድቅል ዝርያ",
                    priceBirr = 3900.0,
                    unit = "በጆንያ (50 ኪ.ግ)",
                    availableStock = 150,
                    supplierName = "የኢትዮጵያ ግብርና ስራዎች ኮርፖሬሽን",
                    supplierPhone = "0115512345",
                    depotLocation = "የአዴት ማዕከላዊ ግብአት መጋዘን"
                ),
                AgriculturalInputItem(
                    name = "NPSB አፈር ማዳበሪያ (NPS + Boron)",
                    category = "አፈር ማዳበሪያ",
                    description = "ናይትሮጅን፣ ፎስፈረስ፣ ሰልፈርና ቦሮን ያካተተ ለአፈራችን ተስማሚ ማዳበሪያ",
                    priceBirr = 4200.0,
                    unit = "በኩንታል (100 ኪ.ግ)",
                    availableStock = 450,
                    supplierName = "የግብርና ሚኒስቴር ማዳበሪያ ማከፋፈያ",
                    supplierPhone = "0115512345",
                    depotLocation = "የቀበሌ ህ/ስ/ማህበር መጋዘን"
                ),
                AgriculturalInputItem(
                    name = "ዩሪያ (Urea 46% Nitrogen) ማዳበሪያ",
                    category = "አፈር ማዳበሪያ",
                    description = "ለሰብል እድገትና አረንጓዴነት ወሳኝ የሆነ የመጀመሪያ ደረጃ የናይትሮጅን ማዳበሪያ",
                    priceBirr = 3950.0,
                    unit = "በኩንታል (100 ኪ.ግ)",
                    availableStock = 380,
                    supplierName = "የግብርና ሚኒስቴር ማዳበሪያ ማከፋፈያ",
                    supplierPhone = "0115512345",
                    depotLocation = "የቀበሌ ህ/ስ/ማህበር መጋዘን"
                ),
                AgriculturalInputItem(
                    name = "ኦርጋኒክ ባዮ-ማዳበሪያ (Bio-Fertilizer)",
                    category = "አፈር ማዳበሪያ",
                    description = "የአፈርን ለምነትና እርጥበት የሚጠብቅ ተፈጥሯዊ ባዮ-ማዳበሪያ",
                    priceBirr = 1800.0,
                    unit = "በኩንታል (100 ኪ.ግ)",
                    availableStock = 80,
                    supplierName = "የአካባቢ ጥበቃና ግብርና ማህበር",
                    supplierPhone = "0918765432",
                    depotLocation = "አዴት 01 ቀበሌ ማከፋፈያ"
                ),
                AgriculturalInputItem(
                    name = "የእጅ ኬሚካልና ፀረ-ተባይ መርጫ (16L Knapsack Sprayer)",
                    category = "የግብርና መሳሪያዎች",
                    description = "ጠንካራ ፕላስቲክ፣ ከፍተኛ ጫና የሚፈጥር እጀታና የሚስተካከል አፍንጫ ያለው መርጫ",
                    priceBirr = 2800.0,
                    unit = "በፍሬ (1 ማሽን)",
                    availableStock = 60,
                    supplierName = "የአግሮ ቴክኖሎጂ መሳሪያዎች አቅራቢ",
                    supplierPhone = "0911987654",
                    depotLocation = "የአዴት ማዕከላዊ ግብአት መጋዘን"
                ),
                AgriculturalInputItem(
                    name = "የውሃ መሳቢያ ሞተር ፓምፕ (3 ኢንች ናፍጣ ፓምፕ)",
                    category = "የግብርና መሳሪያዎች",
                    description = "ለመስኖ እርሻ ከፍተኛ መጠን ያለው ውሃ ከወንዝ ወይም ከጉድጓድ የሚስብ ኃይለኛ ፓምፕ",
                    priceBirr = 38500.0,
                    unit = "በፍሬ (ሙሉ ጥቅል)",
                    availableStock = 25,
                    supplierName = "የኢትዮጵያ ግብርና መካናይዜሽን",
                    supplierPhone = "0115512345",
                    depotLocation = "ባህር ዳር ማከፋፈያ ማዕከል"
                ),
                AgriculturalInputItem(
                    name = "አነስተኛ የጤፍና ስንዴ ማጨጃ ማሽን (Mini Reaper)",
                    category = "የግብርና መሳሪያዎች",
                    description = "በሰዓት እስከ 1 ሄክታር ሰብል የሚያጭድ፣ ጉልበትና ጊዜን የሚቆጥብ ዘመናዊ ማሽን",
                    priceBirr = 65000.0,
                    unit = "በፍሬ",
                    availableStock = 12,
                    supplierName = "የግብርና መካናይዜሽን ዳይሬክቶሬት",
                    supplierPhone = "0115512345",
                    depotLocation = "ባህር ዳር ማከፋፈያ ማዕከል"
                ),
                AgriculturalInputItem(
                    name = "የአርሶ አደር መከላከያ ጓንት፣ ቦት ጫማና ጭምብል",
                    category = "የግብርና መሳሪያዎች",
                    description = "ፀረ-ተባይ በሚረጭበትና አረም በሚታረምበት ጊዜ ለአደጋ መከላከያ የሚሆን ሙሉ ጥቅል",
                    priceBirr = 1400.0,
                    unit = "በጥቅል (Set)",
                    availableStock = 200,
                    supplierName = "የቀበሌ ግብርና ጽ/ቤት",
                    supplierPhone = "0918123456",
                    depotLocation = "የቀበሌ ህ/ስ/ማህበር መጋዘን"
                )
            )

            for (input in inputs) {
                dao.insertInput(input)
            }
        }

        // Initialize Creator Monetization Summary & Payout history if not yet seeded
        if (dao.getCreatorEarningsSync() == null) {
            dao.insertOrUpdateEarnings(
                CreatorEarningsSummary(
                    id = "creator_main",
                    ownerEmail = "zebeneasfye5@gmail.com",
                    ownerName = "Zebene Asfye",
                    payoutMethod = "Telebirr",
                    payoutAccountNumber = "0921458976",
                    payoutAccountName = "Zebene Asfye",
                    totalRegistrations = 184,
                    totalShares = 96,
                    totalLikesAndImpressions = 1420,
                    ethioTelecomVasEarnedBirr = 4250.0,
                    regCommissionRateBirr = 25.0,
                    shareCommissionRateBirr = 5.0,
                    likeCommissionRateBirr = 1.0,
                    availableBalanceBirr = 9850.0,
                    totalWithdrawnBirr = 6500.0
                )
            )

            dao.insertPayoutRecord(
                CreatorPayoutRecord(
                    payoutId = "ET-PAY-49120",
                    amountBirr = 4000.0,
                    method = "Telebirr (ቴሌብር)",
                    targetAccount = "0921458976",
                    recipientName = "Zebene Asfye",
                    status = "የተከፈለ (Transferred)",
                    referenceNumber = "TX-99824102",
                    timestamp = System.currentTimeMillis() - 86400000L * 3
                )
            )

            dao.insertPayoutRecord(
                CreatorPayoutRecord(
                    payoutId = "ET-PAY-38291",
                    amountBirr = 2500.0,
                    method = "የኢትዮጵያ ንግድ ባንክ (CBE)",
                    targetAccount = "1000284910294",
                    recipientName = "Zebene Asfye",
                    status = "የተከፈለ (Transferred)",
                    referenceNumber = "TX-88291044",
                    timestamp = System.currentTimeMillis() - 86400000L * 7
                )
            )
        }
    }

    fun getFertilizerShipmentData(): FertilizerShipment {
        val checkpoints = listOf(
            ShipmentCheckpoint(
                name = "Djibouti Port",
                amharicName = "ጅቡቲ ወደብ (የመርከብ ጭነት)",
                isCompleted = true,
                isCurrent = false,
                timestamp = "ሰኔ 12 ቀን 2016",
                description = "ማዳበሪያው ከውጭ በመርከብ ደርሶ ወደ ከባድ ተሽከርካሪዎች ተጭኗል",
                latitude = 11.588,
                longitude = 43.145
            ),
            ShipmentCheckpoint(
                name = "Galafi Border",
                amharicName = "ጋላፊ የጉምሩክ ፍተሻ ጣቢያ",
                isCompleted = true,
                isCurrent = false,
                timestamp = "ሰኔ 14 ቀን 2016",
                description = "የድንበር ጉምሩክና የጥራት ፍተሻ በስኬት አልፏል",
                latitude = 11.716,
                longitude = 41.838
            ),
            ShipmentCheckpoint(
                name = "Mojo Dry Port Transit",
                amharicName = "ሞጆ ደረቅ ወደብ እና አዋሽ",
                isCompleted = true,
                isCurrent = false,
                timestamp = "ሰኔ 16 ቀን 2016",
                description = "ወደ ማእከላዊ ማከፋፈያ ማዕከል ደርሶ ወደ ክልል ተሽከርካሪዎች ተሸጋሽጓል",
                latitude = 8.598,
                longitude = 39.124
            ),
            ShipmentCheckpoint(
                name = "Regional Distribution Warehouse",
                amharicName = "የዞን ማከፋፈያ መጋዘን (ባህር ዳር)",
                isCompleted = true,
                isCurrent = false,
                timestamp = "ትናንት ከቀኑ 10:30",
                description = "ለወረዳዎችና ቀበሌዎች የሚላከው ኮታ ተመድቦ ተሽከርካሪው ተነስቷል",
                latitude = 11.574,
                longitude = 37.361
            ),
            ShipmentCheckpoint(
                name = "Local Kebele Union Depot",
                amharicName = "የአዴት ቀበሌ ህብረት ስራ ማህበር መጋዘን",
                isCompleted = false,
                isCurrent = true,
                timestamp = "ዛሬ ከቀኑ 7:00 (በመንገድ ላይ)",
                description = "ከባድ ጭነት መኪናው በ 45 ኪ/ሜ/ሰ ፍጥነት ወደ ቀበሌው መጋዘን እየተጓዘ ነው",
                latitude = 11.272,
                longitude = 37.491
            )
        )

        return FertilizerShipment(
            shipmentId = "ET-TRANS-89240",
            fertilizerType = "NPSB እና ዩሪያ (Urea) ከፍተኛ ጥራት ያለው",
            totalQuintals = 450,
            driverName = "አቶ ታደሰ ገብሬ (ዋና አሽከርካሪ)",
            driverPhone = "0918765432",
            truckPlateNumber = "ኮድ 3 - A49210 ኢት",
            origin = "ጅቡቲ ወደብ (Djibouti Port Terminal)",
            destinationDepot = "አዴት 01 ቀበሌ የግብርና ህብረት ስራ ማህበር መጋዘን",
            estimatedArrival = "ዛሬ ከቀኑ 9:00 ሰዓት (2 ሰዓት ይቀራል)",
            currentProgressPercentage = 0.88f,
            currentSpeedKmH = 48,
            checkpoints = checkpoints
        )
    }

    private fun getFormattedDateTomorrow(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(Date(System.currentTimeMillis() + 86400000L))
    }
}
