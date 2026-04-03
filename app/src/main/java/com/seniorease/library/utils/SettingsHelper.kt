package com.seniorease.library.utils

import android.content.Context
import android.content.SharedPreferences

object SettingsHelper {
    private const val PREFS_NAME = "app_prefs"
    private const val KEY_LARGE_TEXT = "large_text_enabled"
    private const val KEY_HIGH_CONTRAST = "high_contrast_enabled"
    private const val KEY_ITEMS_ADDED_TOTAL = "items_added_total"
    private const val KEY_REVIEW_REQUESTED = "review_requested"
    private const val KEY_THEME_MODE = "theme_mode"
    private const val KEY_ONBOARDING_DONE = "onboarding_done"

    const val THEME_SYSTEM = "system"
    const val THEME_LIGHT = "light"
    const val THEME_DARK = "dark"
    
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

    fun incrementItemsAdded(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val newCount = prefs.getInt(KEY_ITEMS_ADDED_TOTAL, 0) + 1
        prefs.edit().putInt(KEY_ITEMS_ADDED_TOTAL, newCount).apply()
        return newCount
    }

    fun hasReviewBeenRequested(context: Context): Boolean {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_REVIEW_REQUESTED, false)
    }

    fun markReviewRequested(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_REVIEW_REQUESTED, true).apply()
    }

    fun getThemeMode(context: Context): String {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_THEME_MODE, THEME_SYSTEM) ?: THEME_SYSTEM
    }

    fun setThemeMode(context: Context, mode: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_THEME_MODE, mode).apply()
    }

    fun isOnboardingDone(context: Context): Boolean {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_ONBOARDING_DONE, false)
    }

    fun markOnboardingDone(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_ONBOARDING_DONE, true).apply()
    }
}
