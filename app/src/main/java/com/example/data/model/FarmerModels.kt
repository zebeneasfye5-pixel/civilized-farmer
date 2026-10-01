package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Farmer profile containing complete biographical, identification,
 * land, and tax information.
 */
@Entity(tableName = "farmer_profiles")
data class FarmerProfile(
    @PrimaryKey
    val id: String = "primary_farmer",
    val fullName: String = "",
    val phoneNumber: String = "",
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
 * Payment record for inputs or agricultural taxes
 */
@Entity(tableName = "payment_records")
data class PaymentRecord(
    @PrimaryKey
    val referenceId: String, // e.g. "TLB-7492038"
    val paymentType: String, // "የአፈር ማዳበሪያ ክፍያ", "የመሬት ግብር", "የተሻሻለ ዘር"
    val amountBirr: Double,
    val provider: String,    // "Telebirr (ቴሌብር)", "CBE Birr (ሲቢኢ)", "Coopay (ኮኦፕ)"
    val status: String,      // "የተከፈለ", "በመጠባበቅ ላይ"
    val payerPhone: String,
    val timestamp: Long = System.currentTimeMillis(),
    val receiptNotes: String = ""
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
