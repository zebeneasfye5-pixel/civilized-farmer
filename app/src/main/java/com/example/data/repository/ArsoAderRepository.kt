package com.example.data.repository

import com.example.data.local.FarmerDao
import com.example.data.model.FarmerProfile
import com.example.data.model.FertilizerQueueToken
import com.example.data.model.FertilizerShipment
import com.example.data.model.MarketCropItem
import com.example.data.model.PaymentRecord
import com.example.data.model.ShipmentCheckpoint
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ArsoAderRepository(private val dao: FarmerDao) {

    val farmerProfile: Flow<FarmerProfile?> = dao.getFarmerProfile()
    val activeToken: Flow<FertilizerQueueToken?> = dao.getActiveQueueToken()
    val allPayments: Flow<List<PaymentRecord>> = dao.getAllPayments()
    val allCrops: Flow<List<MarketCropItem>> = dao.getAllCrops()

    suspend fun saveFarmerProfile(profile: FarmerProfile) {
        dao.insertOrUpdateProfile(profile)
        // Automatically calculate and assign/update fertilizer quota token based on land size!
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
        type: String,
        amount: Double,
        provider: String,
        phone: String,
        notes: String
    ): PaymentRecord {
        val record = PaymentRecord(
            referenceId = "PAY-${(100000..999999).random()}",
            paymentType = type,
            amountBirr = amount,
            provider = provider,
            status = "የተከፈለ",
            payerPhone = phone,
            receiptNotes = notes
        )
        dao.insertPayment(record)
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

    suspend fun seedInitialDataIfEmpty() {
        val currentProfile = dao.getFarmerProfileSync()
        if (currentProfile == null) {
            val sampleProfile = FarmerProfile(
                id = "primary_farmer",
                fullName = "አበበ ታደሰ ወርቁ",
                phoneNumber = "0911234567",
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
                isRegistered = true
            )
            dao.insertOrUpdateProfile(sampleProfile)

            val initialToken = FertilizerQueueToken(
                tokenCode = "ET-AGR-7281",
                queueNumber = 48,
                farmerName = sampleProfile.fullName,
                kebeleDepot = "አዴት 01 ቀበሌ የግብርና ህ/ስ/ማህበር መጋዘን",
                scheduledDate = "ነሐሴ 25 / ዛሬ",
                timeSlot = "ከጧቱ 3:00 - 5:30",
                npsbBags = 4,
                ureaBags = 3,
                status = "ዛሬ ተራዎ ነው",
                isTurnActive = true
            )
            dao.insertQueueToken(initialToken)

            val samplePayment = PaymentRecord(
                referenceId = "TLB-948201",
                paymentType = "የአፈር ማዳበሪያ ክፍያ (4 ኩንታል NPSB + 3 ኩንታል ዩሪያ)",
                amountBirr = 28650.0,
                provider = "Telebirr (ቴሌብር)",
                status = "የተከፈለ",
                payerPhone = "0911234567",
                receiptNotes = "ክፍያው በስኬት ተፈፅሟል፤ በመጋዘን ደረሰኙን ያሳዩ"
            )
            dao.insertPayment(samplePayment)

            // Seed direct farmer crops for the marketplace
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
