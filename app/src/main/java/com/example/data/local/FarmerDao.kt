package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ExpenseRecord
import com.example.data.model.FarmerProfile
import com.example.data.model.FertilizerQueueToken
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

    // Direct Marketplace Crops
    @Query("SELECT * FROM market_crops ORDER BY postDate DESC")
    fun getAllCrops(): Flow<List<MarketCropItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrop(crop: MarketCropItem)

    @Delete
    suspend fun deleteCrop(crop: MarketCropItem)
}
