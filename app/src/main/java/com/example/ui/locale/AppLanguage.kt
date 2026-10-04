package com.example.ui.locale

/**
 * Supported Ethiopian languages and working languages for agricultural accessibility.
 * Allows every Ethiopian nation and nationality to comfortably use the app in their native tongue.
 */
enum class AppLanguage(
    val code: String,
    val nativeName: String,
    val englishName: String,
    val regionOrDetail: String,
    val shortBadge: String
) {
    AMHARIC(
        code = "am",
        nativeName = "አማርኛ",
        englishName = "Amharic",
        regionOrDetail = "ሀገር አቀፍ / አማራ ክልል",
        shortBadge = "አማ"
    ),
    OROMO(
        code = "om",
        nativeName = "Afaan Oromoo",
        englishName = "Oromo",
        regionOrDetail = "Naannoo Oromiyaa",
        shortBadge = "OM"
    ),
    TIGRINYA(
        code = "ti",
        nativeName = "ትግርኛ",
        englishName = "Tigrinya",
        regionOrDetail = "ክልል ትግራይ",
        shortBadge = "ትግ"
    ),
    SOMALI(
        code = "so",
        nativeName = "Af-Soomaali",
        englishName = "Somali",
        regionOrDetail = "Deegaanka Soomaalida",
        shortBadge = "SO"
    ),
    SIDAMA(
        code = "sid",
        nativeName = "Sidaamu Afoo",
        englishName = "Sidama",
        regionOrDetail = "Sidaamu Dagoomi Qoqqowo",
        shortBadge = "SD"
    ),
    WOLAYTTA(
        code = "wal",
        nativeName = "Wolayttatto",
        englishName = "Wolaytta",
        regionOrDetail = "Wolaytta Dega",
        shortBadge = "WL"
    ),
    ENGLISH(
        code = "en",
        nativeName = "English",
        englishName = "English",
        regionOrDetail = "International / Business",
        shortBadge = "EN"
    )
}
