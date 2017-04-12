package com.jpmcosta.test.realmtestproject.activity;

import android.app.Activity;
import android.os.Bundle;

import io.realm.Realm;

public class RealmActivity extends Activity {

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
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