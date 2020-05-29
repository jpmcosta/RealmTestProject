package com.jpmcosta.test.realmtestproject.activity

import android.app.Activity
import androidx.annotation.CallSuper
import com.jpmcosta.test.realmtestproject.RealmTestProject
import io.realm.Realm
import io.realm.RealmAsyncTask

abstract class RealmActivity : Activity() {

    private var realmAsyncTask: RealmAsyncTask? = null

    private val realmCallback = object : Realm.Callback() {

        override fun onSuccess(realm: Realm) {
            this@RealmActivity.realm = realm
        }
    }

    var realm: Realm? = null
        private set(realm) {
            realmAsyncTask?.cancel()

            val previousRealm = field
            if (previousRealm != realm) {
                if (previousRealm != null) {
                    onStopRealm()
                    previousRealm.close()
                }
                field = realm
                if (realm != null) {
                    onStartRealm(realm)
                } else {
                    onStopRealm()
                }
            }
        }


    override fun onStart() {
        super.onStart()

        realmAsyncTask = Realm.getInstanceAsync((application as RealmTestProject).realmConfiguration, realmCallback)
    }

    override fun onStop() {
        super.onStop()

        realm = null
    }

    @CallSuper
    open fun onStartRealm(realm: Realm) {
        // Sub-classes may override.
    }

    @CallSuper
    open fun onStopRealm() {
        // Sub-classes may override.
    }
}