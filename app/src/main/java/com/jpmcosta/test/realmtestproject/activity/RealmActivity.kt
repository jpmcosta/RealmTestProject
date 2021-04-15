package com.jpmcosta.test.realmtestproject.activity

import android.app.Activity
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.annotation.CallSuper
import com.jpmcosta.test.realmtestproject.R
import com.jpmcosta.test.realmtestproject.RealmTestProject
import io.realm.Realm
import io.realm.RealmAsyncTask

abstract class RealmActivity : Activity() {

    companion object {

        private val LOG_TAG = RealmActivity::class.java.simpleName
    }

    private var realmAsyncTask: RealmAsyncTask? = null

    private var realmStartTime: Long = 0L

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

        realmStartTime = SystemClock.elapsedRealtime()
        realmAsyncTask = Realm.getInstanceAsync((application as RealmTestProject).realmConfiguration, realmCallback)
    }

    override fun onStop() {
        super.onStop()

        realm = null
    }

    @CallSuper
    open fun onStartRealm(realm: Realm) {
        val startupTimeSeconds = (SystemClock.elapsedRealtime() - realmStartTime) / 1000f
        val startupTimeText = getString(R.string.realm_startup_time_seconds, startupTimeSeconds)
        Log.i(LOG_TAG, startupTimeText)
        Toast.makeText(this, startupTimeText, Toast.LENGTH_LONG).show()
    }

    @CallSuper
    open fun onStopRealm() {
        // Sub-classes may override.
    }
}