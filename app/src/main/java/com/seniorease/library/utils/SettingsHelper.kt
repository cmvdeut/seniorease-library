package com.seniorease.library.utils

import android.content.Context
import android.content.SharedPreferences

object SettingsHelper {
    private const val PREFS_NAME = "app_prefs"
    private const val KEY_LARGE_TEXT = "large_text_enabled"
    private const val KEY_HIGH_CONTRAST = "high_contrast_enabled"
    
    /**
     * Haal de grote tekst instelling op
     */
    fun isLargeTextEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_LARGE_TEXT, false)
    }
    
    /**
     * Sla de grote tekst instelling op
     */
    fun setLargeTextEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_LARGE_TEXT, enabled).apply()
    }
    
    /**
     * Haal de hoog contrast instelling op
     */
    fun isHighContrastEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_HIGH_CONTRAST, false)
    }
    
    /**
     * Sla de hoog contrast instelling op
     */
    fun setHighContrastEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_HIGH_CONTRAST, enabled).apply()
    }
}
