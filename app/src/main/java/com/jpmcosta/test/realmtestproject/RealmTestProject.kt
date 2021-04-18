package com.jpmcosta.test.realmtestproject

import android.app.Application
import android.os.SystemClock
import com.jpmcosta.test.realmtestproject.realm.RealmInitialData
import io.realm.Realm
import io.realm.RealmConfiguration

class RealmTestProject : Application() {

    lateinit var realmConfiguration: RealmConfiguration


    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        realmConfiguration = RealmConfiguration.Builder()
            .schemaVersion(SystemClock.elapsedRealtimeNanos()) // Ensure Realm file is recreated.
            .deleteRealmIfMigrationNeeded()
            .initialData(RealmInitialData())
            .build()
    }
}