package com.seniorease.library

import android.app.Application
import com.seniorease.library.utils.AppMaintenanceHelper

class BiblitoheekApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppMaintenanceHelper.ensureCacheDirectories(this)
    }
}
