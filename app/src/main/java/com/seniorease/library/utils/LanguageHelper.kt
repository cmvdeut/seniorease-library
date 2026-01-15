package com.seniorease.library.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LanguageHelper {
    private const val PREFS_NAME = "app_prefs"
    private const val KEY_LANGUAGE = "app_language"
    
    // Taal opties
    const val LANGUAGE_SYSTEM = "system"
    const val LANGUAGE_DUTCH = "nl"
    const val LANGUAGE_ENGLISH = "en"
    
    /**
     * Haal de opgeslagen taal voorkeur op
     */
    fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, LANGUAGE_SYSTEM) ?: LANGUAGE_SYSTEM
    }
    
    /**
     * Sla de taal voorkeur op (persistent, direct opgeslagen)
     */
    fun saveLanguage(context: Context, language: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, language).commit() // commit() voor directe opslag
    }
    
    /**
     * Bepaal de te gebruiken locale op basis van voorkeur
     */
    fun getLocale(context: Context): Locale {
        val savedLanguage = getSavedLanguage(context)
        return when (savedLanguage) {
            LANGUAGE_DUTCH -> Locale("nl", "NL")
            LANGUAGE_ENGLISH -> Locale("en", "US")
            else -> {
                // Gebruik systeemtaal
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    context.resources.configuration.locales[0]
                } else {
                    @Suppress("DEPRECATION")
                    context.resources.configuration.locale
                }
            }
        }
    }
    
}

