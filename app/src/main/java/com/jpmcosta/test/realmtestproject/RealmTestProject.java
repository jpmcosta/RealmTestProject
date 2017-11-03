package com.jpmcosta.test.realmtestproject;

import android.app.Application;
import android.support.annotation.NonNull;

import com.jpmcosta.test.realmtestproject.realm.App;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmTestProject extends Application {

    public RealmConfiguration realmConfiguration;


    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        realmConfiguration = new RealmConfiguration.Builder()
                .initialData(new Realm.Transaction() {

                    @Override
                    public void execute(@NonNull Realm realm) {
                        realm.copyToRealm(App.create(0L));
                    }
                })
                .deleteRealmIfMigrationNeeded()
                .build();
    }
}
