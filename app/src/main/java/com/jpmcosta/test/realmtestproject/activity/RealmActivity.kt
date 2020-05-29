package com.jpmcosta.test.realmtestproject.activity

import android.app.Activity
import android.os.Bundle
import com.jpmcosta.test.realmtestproject.RealmTestProject
import io.realm.Realm

abstract class RealmActivity : Activity() {

    lateinit var realm: Realm
        private set


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        realm = Realm.getInstance((application as RealmTestProject).realmConfiguration)
    }

    override fun onDestroy() {
        super.onDestroy()

        realm.close()
    }
}