package com.jpmcosta.test.realmtestproject;

import android.app.Application;

import io.realm.Realm;

public class RealmTestProject extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }
}
