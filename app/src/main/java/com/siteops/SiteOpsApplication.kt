package com.siteops

import android.app.Application
import androidx.room.Room
import com.siteops.data.local.SiteOpsDatabase

class SiteOpsApplication : Application() {
    
    // Singleton instance for simplicity in this utility build
    companion object {
        lateinit var database: SiteOpsDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            this,
            SiteOpsDatabase::class.java,
            "siteops-db"
        ).build()
    }
}
