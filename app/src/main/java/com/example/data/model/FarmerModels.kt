package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Farmer profile containing complete biographical, identification,
 * land, bank linkage, and photo dossier information.
 */
@Entity(tableName = "farmer_profiles")
data class FarmerProfile(
    @PrimaryKey
    val id: String = "primary_farmer",
    val fullName: String = "",
    val phoneNumber: String = "",
    val gender: String = "ወንድ", // "ወንድ" ወይም "ሴት" (System auto-calculates male/female ratio)
    val nationality: String = "ኢትዮጵያዊ",
    val region: String = "አማራ",
    val zone: String = "ምዕራብ ጎጃም",
    val woreda: String = "ይስማላ / መርዓዊ",
    val kebele: String = "አዴት 01 ቀበሌ",
    val nationalId: String = "", // ፋይዳ / ብሔራዊ መታወቂያ
    val kebeleId: String = "",   // የቀበሌ መታወቂያ ቁጥር
    val landSizeHectares: Double = 2.0, // የመሬት መጠን በሄክታር
    val landSizeTimad: Double = 8.0,    // በቃዳ/በጥማድ (1 ሄክታር = 4 ጥማድ)
    val annualTaxBirr: Double = 380.0,  // መክፈል ያለባቸው የግብር መጠን በብር
    val primaryCrops: String = "ነጭ ጤፍ፣ ስንዴ፣ በቆሎ",

    // Bank Account Linkage (የተያያዘ የባንክ ሂሳብ)
    val bankName: String = "የኢትዮጵያ ንግድ ባንክ (CBE)",
    val bankAccountNumber: String = "1000284910294",
    val bankAccountHolder: String = "",

    // Profile Photo in Dossier (ፎቶ በፕሮፋይል ማህደር)
    val photoUri: String = "",
    val avatarPreset: String = "man_1",

    val isRegistered: Boolean = false,
    val registeredDate: Long = System.currentTimeMillis()
)

/**
 * Fertilizer distribution turn/queue token entity
 */
@Entity(tableName = "queue_tokens")
data class FertilizerQueueToken(
    @PrimaryKey
    val tokenCode: String, // e.g. "ET-FERT-048"
    val queueNumber: Int,  // e.g. 48
    val farmerName: String,
    val kebeleDepot: String,
    val scheduledDate: String,
    val timeSlot: String,
    val npsbBags: Int, // NPSB ኩንታል
    val ureaBags: Int, // ዩሪያ ኩንታል
    val status: String, // "ይጠብቁ", "ዛሬ ተራዎ ነው", "ተጠናቋል"
    val isTurnActive: Boolean = false,
    val generatedDate: Long = System.currentTimeMillis()
)

/**
 * Payment record for inputs, land tax, titles, or irrigation.
 * System issues individual verified receipts for every title.
 */
@Entity(tableName = "payment_records")
data class PaymentRecord(
    @PrimaryKey
    val referenceId: String, // e.g. "TLB-7492038"
    val receiptOfficialNumber: String = "KB-REC-001",
    val paymentTitle: String,
    val paymentType: String,
    val categoryKey: String = "FERTILIZER",
    val amountBirr: Double,
    val provider: String,
    val status: String = "የተከፈለ",
    val payerPhone: String,
    val farmerId: String = "primary_farmer",
    val farmerName: String = "",
    val kebele: String = "አዴት 01 ቀበሌ",
    val timestamp: Long = System.currentTimeMillis(),
    val receiptNotes: String = ""
)

/**
 * Kebele Agricultural & Land Administration Office Expense Record (የወጪ መዝገብ)
 */
@Entity(tableName = "expense_records")
data class ExpenseRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val expenseTitle: String,
    val category: String,
    val amountBirr: Double,
    val kebele: String = "አዴት 01 ቀበሌ",
    val paidTo: String,
    val voucherNumber: String,
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String = ""
)

/**
 * Direct Farmer-to-Consumer crop listing without brokers (ያለ ደላላ)
 */
@Entity(tableName = "market_crops")
data class MarketCropItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cropName: String,     // "ማኛ ጤፍ", "ነጭ ስንዴ", "ቀይ በቆሎ", "ባቄላ"
    val sellerName: String,
    val sellerPhone: String,
    val location: String,     // ቀበሌ / ወረዳ
    val quantityQuintals: Double,
    val pricePerQuintal: Double,
    val isOrganic: Boolean = true,
    val postDate: Long = System.currentTimeMillis()
)

/**
 * Agricultural Input item (ምርጥ ዘር፣ ማዳበሪያ እና የግብርና መሳሪያዎች) available for farmers to order
 */
@Entity(tableName = "agricultural_inputs")
data class AgriculturalInputItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,           // e.g. "ማኛ ጤፍ የተሻሻለ ምርጥ ዘር"
    val category: String,       // "ምርጥ ዘር", "አፈር ማዳበሪያ", "የግብርና መሳሪያዎች"
    val description: String,
    val priceBirr: Double,
    val unit: String,           // "በኩንታል (100 ኪ.ግ)", "በጆንያ (50 ኪ.ግ)", "በፍሬ"
    val availableStock: Int,
    val supplierName: String,   // "የኢትዮጵያ ግብርና ስራዎች ኮርፖሬሽን"
    val supplierPhone: String,  // "0911234567"
    val depotLocation: String,  // "የአዴት ማዕከላዊ ግብአት መጋዘን"
    val isFeatured: Boolean = true
)

/**
 * Input order placed by farmer (የግብዓት ትዕዛዝ መዝገብ)
 */
@Entity(tableName = "input_orders")
data class InputOrderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderNumber: String,     // e.g. "ORD-2026-8492"
    val inputName: String,
    val category: String,
    val quantity: Int,
    val totalBirr: Double,
    val farmerName: String,
    val farmerPhone: String,
    val pickupDepot: String,
    val orderDate: Long = System.currentTimeMillis(),
    val status: String = "የተረጋገጠ (ክምችት ተይዟል)"
)

/**
 * Aggregated Kebele Land Administration Statistics calculated automatically
 */
data class KebeleStatistics(
    val kebeleName: String,
    val totalFarmers: Int,
    val maleFarmers: Int,
    val femaleFarmers: Int,
    val malePercentage: Float,
    val femalePercentage: Float,
    val totalLandHectares: Double,
    val totalRevenueBirr: Double,
    val totalExpenseBirr: Double,
    val netBalanceBirr: Double,
    val revenueByCategory: Map<String, Double>
)

/**
 * Real-time GPS shipment tracking for fertilizer transit from port to depot
 */
data class ShipmentCheckpoint(
    val name: String,
    val amharicName: String,
    val isCompleted: Boolean,
    val isCurrent: Boolean,
    val timestamp: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
)

data class FertilizerShipment(
    val shipmentId: String,
    val fertilizerType: String,
    val totalQuintals: Int,
    val driverName: String,
    val driverPhone: String,
    val truckPlateNumber: String,
    val origin: String,
    val destinationDepot: String,
    val estimatedArrival: String,
    val currentProgressPercentage: Float,
    val currentSpeedKmH: Int,
    val checkpoints: List<ShipmentCheckpoint>
)

/**
 * Secret Creator / Developer monetization model.
 * Tracks earnings from user registrations, shares, likes, and telecom VAS partnerships.
 * Hidden from regular users, accessible only by developer/owner via secret PIN.
 */
@Entity(tableName = "creator_earnings")
data class CreatorEarningsSummary(
    @PrimaryKey
    val id: String = "creator_main",
    val ownerEmail: String = "zebeneasfye5@gmail.com",
    val ownerName: String = "Zebene Asfye",
    val payoutMethod: String = "Telebirr", // "Telebirr" or "CBE"
    val payoutAccountNumber: String = "0921458976", // Telebirr or CBE account
    val payoutAccountName: String = "Zebene Asfye",
    val totalRegistrations: Int = 184,
    val totalShares: Int = 96,
    val totalLikesAndImpressions: Int = 1420,
    val ethioTelecomVasEarnedBirr: Double = 4250.0,
    val regCommissionRateBirr: Double = 25.0,  // 25 ETB per farmer registered
    val shareCommissionRateBirr: Double = 5.0, // 5 ETB per share/referral
    val likeCommissionRateBirr: Double = 1.0,   // 1 ETB per like/view
    val availableBalanceBirr: Double = 9850.0,
    val totalWithdrawnBirr: Double = 6500.0,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "creator_payouts")
data class CreatorPayoutRecord(
    @PrimaryKey
    val payoutId: String,
    val amountBirr: Double,
    val method: String, // "Telebirr", "CBE Birr", "CBE Bank"
    val targetAccount: String,
    val recipientName: String,
    val status: String, // "የተከፈለ (Transferred)", "በሂደት ላይ (Processing)"
    val referenceNumber: String,
    val timestamp: Long = System.currentTimeMillis()
)

