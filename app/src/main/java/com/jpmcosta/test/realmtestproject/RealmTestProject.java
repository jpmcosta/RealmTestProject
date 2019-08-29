package com.jpmcosta.test.realmtestproject;

import android.app.Application;

import com.jpmcosta.test.realmtestproject.realm.InitialData;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmTestProject extends Application {

    public RealmConfiguration realmConfiguration;


    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        realmConfiguration = new RealmConfiguration.Builder()
                .initialData(new InitialData())
                .deleteRealmIfMigrationNeeded()
                .build();
    }
}
