package com.seniorease.library.ui

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.seniorease.library.utils.LanguageHelper

/** Forces Compose [stringResource] to use the language from [LanguageHelper]. */
@Composable
fun LocaleProvider(content: @Composable () -> Unit) {
    val baseContext = LocalContext.current
    val savedLanguage = LanguageHelper.getSavedLanguage(baseContext)
    val locale = remember(savedLanguage) { LanguageHelper.getLocale(baseContext) }
    val localizedContext = remember(locale) {
        wrapWithLocale(baseContext, locale)
    }
    val configuration = remember(locale) {
        Configuration(baseContext.resources.configuration).apply {
            setLocale(locale)
        }
    }
    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalConfiguration provides configuration,
    ) {
        content()
    }
}

private fun wrapWithLocale(context: Context, locale: java.util.Locale): Context {
    val config = Configuration(context.resources.configuration).apply {
        setLocale(locale)
    }
    return context.createConfigurationContext(config)
}
