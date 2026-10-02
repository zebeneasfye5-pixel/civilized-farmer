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
    val photoUri: String = "", // Custom photo URI if selected
    val avatarPreset: String = "man_1", // "man_1", "woman_1", "man_2", "woman_2"

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
    val receiptOfficialNumber: String = "KB-REC-001", // ህጋዊ የደረሰኝ ቁጥር
    val paymentTitle: String, // "የአፈር ማዳበሪያ (NPSB & ዩሪያ)", "የመሬት ግብር", "የተሻሻለ ምርጥ ዘር", "የይዞታ ማረጋገጫ ካርታ", "የመስኖ ውሃ አገልግሎት"
    val paymentType: String, // Legacy compatibility
    val categoryKey: String = "FERTILIZER", // FERTILIZER, LAND_TAX, SEED, LAND_TITLING, IRRIGATION
    val amountBirr: Double,
    val provider: String,    // "Telebirr (ቴሌብር)", "CBE Birr (ሲቢኢ)", "Coopay (ኮኦፕ)", "የባንክ ቀጥታ ዝውውር"
    val status: String = "የተከፈለ",      // "የተከፈለ", "በመጠባበቅ ላይ"
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
    val expenseTitle: String, // e.g. "ለግብርና ሚኒስቴር የማዳበሪያ ግዢ ክፍያ", "የጭነት ትራንስፖርትና ነዳጅ ወጪ"
    val category: String,     // "የማዳበሪያ ግዢ", "ትራንስፖርትና ሎጀስቲክስ", "የመጋዘን ኪራይና ጥበቃ", "የመስኖ መሰረተ ልማት"
    val amountBirr: Double,
    val kebele: String = "አዴት 01 ቀበሌ",
    val paidTo: String,       // ተከፋይ አካል (ለምሳሌ፦ "የኢትዮጵያ ግብርና ስራዎች ኮርፖሬሽን")
    val voucherNumber: String, // የክፍያ ማዘዣ ቫውቸር ቁጥር e.g. "PV-2026-084"
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
