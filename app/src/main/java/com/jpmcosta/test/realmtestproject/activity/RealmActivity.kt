package com.jpmcosta.test.realmtestproject.activity

import android.app.Activity
import androidx.annotation.CallSuper
import com.jpmcosta.test.realmtestproject.RealmTestProject
import io.realm.Realm
import io.realm.RealmAsyncTask
import io.realm.RealmConfiguration

abstract class RealmActivity : Activity() {

    protected val realmConfiguration: RealmConfiguration
        get() = (application as RealmTestProject).realmConfiguration

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
                }
            }
        }


    override fun onStart() {
        super.onStart()

        val realmConfiguration = realmConfiguration

        // Ensure Realm file is created for compact to run.
        Realm.getInstance(realmConfiguration).close()

        realmAsyncTask = Realm.getInstanceAsync(realmConfiguration, realmCallback)
        Realm.compactRealm(realmConfiguration) // This is causing an issue.
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