package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.AgriculturalInputItem
import com.example.data.model.CreatorEarningsSummary
import com.example.data.model.CreatorPayoutRecord
import com.example.data.model.ExpenseRecord
import com.example.data.model.FarmerProfile
import com.example.data.model.FertilizerQueueToken
import com.example.data.model.InputOrderItem
import com.example.data.model.MarketCropItem
import com.example.data.model.PaymentRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface FarmerDao {

    // Farmer Profiles
    @Query("SELECT * FROM farmer_profiles WHERE id = 'primary_farmer' LIMIT 1")
    fun getFarmerProfile(): Flow<FarmerProfile?>

    @Query("SELECT * FROM farmer_profiles WHERE id = 'primary_farmer' LIMIT 1")
    suspend fun getFarmerProfileSync(): FarmerProfile?

    @Query("SELECT * FROM farmer_profiles ORDER BY fullName ASC")
    fun getAllFarmerProfiles(): Flow<List<FarmerProfile>>

    @Query("SELECT * FROM farmer_profiles WHERE kebele = :kebele ORDER BY fullName ASC")
    fun getFarmersByKebele(kebele: String): Flow<List<FarmerProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: FarmerProfile)

    // Fertilizer Queue Token
    @Query("SELECT * FROM queue_tokens ORDER BY generatedDate DESC LIMIT 1")
    fun getActiveQueueToken(): Flow<FertilizerQueueToken?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQueueToken(token: FertilizerQueueToken)

    // Income Payments
    @Query("SELECT * FROM payment_records ORDER BY timestamp DESC")
    fun getAllPayments(): Flow<List<PaymentRecord>>

    @Query("SELECT * FROM payment_records WHERE kebele = :kebele ORDER BY timestamp DESC")
    fun getPaymentsByKebele(kebele: String): Flow<List<PaymentRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentRecord)

    // Expenses (የወጪ መዝገብ)
    @Query("SELECT * FROM expense_records ORDER BY timestamp DESC")
    fun getAllExpenses(): Flow<List<ExpenseRecord>>

    @Query("SELECT * FROM expense_records WHERE kebele = :kebele ORDER BY timestamp DESC")
    fun getExpensesByKebele(kebele: String): Flow<List<ExpenseRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseRecord)

    // Direct Marketplace Crops (ያለ ደላላ የሰብል ገበያ)
    @Query("SELECT COUNT(*) FROM market_crops")
    suspend fun getCropsCount(): Int

    @Query("SELECT * FROM market_crops ORDER BY postDate DESC")
    fun getAllCrops(): Flow<List<MarketCropItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrop(crop: MarketCropItem)

    @Delete
    suspend fun deleteCrop(crop: MarketCropItem)

    // Agricultural Inputs (የግብዓት መግዣ)
    @Query("SELECT COUNT(*) FROM agricultural_inputs")
    suspend fun getInputsCount(): Int

    @Query("SELECT * FROM agricultural_inputs ORDER BY id ASC")
    fun getAllInputs(): Flow<List<AgriculturalInputItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInput(input: AgriculturalInputItem)

    // Input Orders (የትዕዛዝ መዝገብ)
    @Query("SELECT * FROM input_orders ORDER BY orderDate DESC")
    fun getAllInputOrders(): Flow<List<InputOrderItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInputOrder(order: InputOrderItem)

    // Secret Creator Monetization & Ethiopian Payouts
    @Query("SELECT * FROM creator_earnings WHERE id = 'creator_main' LIMIT 1")
    fun getCreatorEarnings(): Flow<CreatorEarningsSummary?>

    @Query("SELECT * FROM creator_earnings WHERE id = 'creator_main' LIMIT 1")
    suspend fun getCreatorEarningsSync(): CreatorEarningsSummary?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateEarnings(earnings: CreatorEarningsSummary)

    @Query("SELECT * FROM creator_payouts ORDER BY timestamp DESC")
    fun getAllCreatorPayouts(): Flow<List<CreatorPayoutRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayoutRecord(payout: CreatorPayoutRecord)
}
