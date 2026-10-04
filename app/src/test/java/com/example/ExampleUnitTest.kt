package com.example

import com.example.data.model.FarmerProfile
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testKebeleFarmerDemographicCalculations() {
        val farmers = listOf(
            FarmerProfile(id = "1", fullName = "አበበ", gender = "ወንድ", landSizeHectares = 2.0),
            FarmerProfile(id = "2", fullName = "ፋንቱ", gender = "ሴት", landSizeHectares = 1.5),
            FarmerProfile(id = "3", fullName = "ከበደ", gender = "ወንድ", landSizeHectares = 3.0),
            FarmerProfile(id = "4", fullName = "አልማዝ", gender = "ሴት", landSizeHectares = 1.5)
        )

        val total = farmers.size
        val maleCount = farmers.count { it.gender == "ወንድ" }
        val femaleCount = farmers.count { it.gender == "ሴት" }
        val totalHectares = farmers.sumOf { it.landSizeHectares }

        assertEquals(4, total)
        assertEquals(2, maleCount)
        assertEquals(2, femaleCount)
        assertEquals(8.0, totalHectares, 0.01)
    }

    @Test
    fun testIncomeAndExpenseNetBalance() {
        val totalRevenue = 35250.0
        val totalExpense = 30500.0
        val netBalance = totalRevenue - totalExpense

        assertEquals(4750.0, netBalance, 0.01)
    }

    @Test
    fun testMultilingualTranslations() {
        val amharic = com.example.ui.locale.AppStrings.get(com.example.ui.locale.AppLanguage.AMHARIC)
        assertEquals("አርሶ አደር", amharic.appTitle)
        assertEquals("መነሻ", amharic.navHome)

        val oromo = com.example.ui.locale.AppStrings.get(com.example.ui.locale.AppLanguage.OROMO)
        assertEquals("Qonnaan Bulaa", oromo.appTitle)
        assertEquals("Fuula Duraa", oromo.navHome)

        val tigrinya = com.example.ui.locale.AppStrings.get(com.example.ui.locale.AppLanguage.TIGRINYA)
        assertEquals("ሓረስታይ", tigrinya.appTitle)
        assertEquals("መበገሲ", tigrinya.navHome)

        val somali = com.example.ui.locale.AppStrings.get(com.example.ui.locale.AppLanguage.SOMALI)
        assertEquals("Beeraley", somali.appTitle)
        assertEquals("Bogga Hore", somali.navHome)

        val sidama = com.example.ui.locale.AppStrings.get(com.example.ui.locale.AppLanguage.SIDAMA)
        assertEquals("Kalaasicho", sidama.appTitle)
        assertEquals("Hanafo", sidama.navHome)

        val wolaytta = com.example.ui.locale.AppStrings.get(com.example.ui.locale.AppLanguage.WOLAYTTA)
        assertEquals("Goshshanchcha", wolaytta.appTitle)
        assertEquals("Doomiyuwaa", wolaytta.navHome)

        val english = com.example.ui.locale.AppStrings.get(com.example.ui.locale.AppLanguage.ENGLISH)
        assertEquals("Farmer Hub", english.appTitle)
        assertEquals("Home", english.navHome)
    }
}
