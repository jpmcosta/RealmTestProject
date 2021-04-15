package com.jpmcosta.test.realmtestproject

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class RealmTestProject : Application() {

    lateinit var realmConfiguration: RealmConfiguration


    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        realmConfiguration = RealmConfiguration.Builder().build()
    }
}