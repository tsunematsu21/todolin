package com.tunesworks.todolin

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class ToDolin: Application() {
    override fun onCreate() {
        super.onCreate()

        // Set Realm Configuration
        val configBuilder = RealmConfiguration.Builder(applicationContext).apply {
            if (BuildConfig.DEBUG) {
                name("debug")
                deleteRealmIfMigrationNeeded()
            }
        }
        Realm.setDefaultConfiguration(configBuilder.build())
    }
}