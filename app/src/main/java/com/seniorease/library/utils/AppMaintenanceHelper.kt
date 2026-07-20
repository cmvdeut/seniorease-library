package com.seniorease.library.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.seniorease.library.BuildConfig
import java.io.File

object AppMaintenanceHelper {

    private val SAFE_CACHE_FILE_NAMES = setOf(
        ExportFileNames.PDF_CACHE,
    )

    fun openPlayStoreListing(context: Context): Boolean {
        val appId = BuildConfig.APPLICATION_ID
        val marketUri = "market://details?id=$appId".toUri()
        val webUri = "https://play.google.com/store/apps/details?id=$appId".toUri()
        return try {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, marketUri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            true
        } catch (_: ActivityNotFoundException) {
            try {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, webUri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                true
            } catch (_: ActivityNotFoundException) {
                false
            }
        }
    }

    /** Opens Google Play promo-code redeem screen with the pasted code. */
    fun openPromoCodeRedeem(context: Context, code: String): Boolean {
        val cleaned = code.trim().replace(" ", "").replace("\n", "")
        if (cleaned.isEmpty()) return false
        val redeemUri = "https://play.google.com/redeem?code=$cleaned".toUri()
        return try {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, redeemUri)
                    .setPackage("com.android.vending")
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            true
        } catch (_: ActivityNotFoundException) {
            try {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, redeemUri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                true
            } catch (_: ActivityNotFoundException) {
                false
            }
        }
    }

    /** Ensures cache folders exist (repairs after a bad clear on older app versions). */
    fun ensureCacheDirectories(context: Context) {
        try {
            context.cacheDir.mkdirs()
            context.externalCacheDir?.mkdirs()
        } catch (_: Exception) {
            // Ignore — getCacheDir() will retry when needed
        }
    }

    /**
     * Removes temporary files only. Never deletes the cache folder itself (that can crash
     * Coil, CameraX and ML Kit while the app is running).
     */
    fun clearAppCache(context: Context): Boolean {
        return try {
            deleteKnownTempFiles(context.cacheDir)
            context.externalCacheDir?.let { deleteKnownTempFiles(it) }
            true
        } catch (_: Exception) {
            false
        }
    }

    private fun deleteKnownTempFiles(dir: File) {
        dir.listFiles()?.forEach { file ->
            when {
                file.name in SAFE_CACHE_FILE_NAMES -> file.deleteRecursively()
                file.isFile && file.name.endsWith("export.pdf") -> file.delete()
                file.isDirectory && file.name in IMAGE_CACHE_DIR_NAMES -> file.deleteRecursively()
            }
        }
    }

    /** Coil / OkHttp disk cache directory names — safe to remove. */
    private val IMAGE_CACHE_DIR_NAMES = setOf(
        "image_cache",
        "coil",
        "coil_image_cache",
    )
}
