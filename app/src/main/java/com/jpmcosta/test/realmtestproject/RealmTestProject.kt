package com.jpmcosta.test.realmtestproject

import android.app.Application
import com.jpmcosta.test.realmtestproject.realm.InitialData
import io.realm.Realm
import io.realm.RealmConfiguration

class RealmTestProject : Application() {

    lateinit var realmConfiguration: RealmConfiguration


    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        realmConfiguration = RealmConfiguration.Builder()
                .initialData(InitialData())
                .deleteRealmIfMigrationNeeded()
                .build()
    }
}