package com.seniorease.library.utils

import android.content.Context
import android.content.SharedPreferences

object UnlockHelper {
    private const val PREFS_NAME = "app_prefs"
    private const val KEY_UNLOCKED = "app_unlocked"
    private const val KEY_UNLOCK_CODE = "app_unlock_code"
    
    // Vaste unlock code (kan later worden aangepast naar dynamische code)
    private const val VALID_UNLOCK_CODE = "SENIOREASE2025"
    
    /**
     * Check of de app is unlocked
     */
    fun isUnlocked(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_UNLOCKED, false)
    }
    
    /**
     * Unlock de app met een code
     */
    fun unlockWithCode(context: Context, code: String): Boolean {
        if (code.trim().uppercase() == VALID_UNLOCK_CODE) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().putBoolean(KEY_UNLOCKED, true).commit()
            return true
        }
        return false
    }
    
    /**
     * Get de unlock code (voor weergave na betaling)
     */
    fun getUnlockCode(): String {
        return VALID_UNLOCK_CODE
    }
    
    /**
     * Unlock de app direct (na verificatie via API)
     */
    fun unlockDirectly(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_UNLOCKED, true).commit()
        return true
    }
    
    /**
     * Reset unlock status (voor testen)
     */
    fun resetUnlock(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_UNLOCKED, false).commit()
    }
}
