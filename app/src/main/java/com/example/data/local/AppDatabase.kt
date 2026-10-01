package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.FarmerProfile
import com.example.data.model.FertilizerQueueToken
import com.example.data.model.MarketCropItem
import com.example.data.model.PaymentRecord

@Database(
    entities = [
        FarmerProfile::class,
        FertilizerQueueToken::class,
        PaymentRecord::class,
        MarketCropItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun farmerDao(): FarmerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "arso_ader_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
