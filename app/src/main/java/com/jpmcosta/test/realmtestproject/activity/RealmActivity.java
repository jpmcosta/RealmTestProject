package com.jpmcosta.test.realmtestproject.activity;

import android.app.Activity;
import android.os.Bundle;

import com.jpmcosta.test.realmtestproject.RealmTestProject;

import io.realm.Realm;

abstract public class RealmActivity extends Activity {

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getInstance(((RealmTestProject) getApplication()).realmConfiguration);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    public Realm getRealm() {
        return mRealm;
    }
}