package com.tunesworks.todolin

import android.app.Application
import com.squareup.otto.Bus
import io.realm.Realm
import io.realm.RealmConfiguration

class ToDolin: Application() {
    companion object {
        val bus = Bus()
    }
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