package com.jpmcosta.test.realmtestproject;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.jpmcosta.test.realmtestproject.realm.App;
import com.jpmcosta.test.realmtestproject.realm.Item;
import com.jpmcosta.test.realmtestproject.realm.Session;
import com.jpmcosta.test.realmtestproject.service.AppUpdateIntentService;

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
                        realm.copyToRealm(Session.create(0L));
                        for (int i = 0; i < 10; i++) {
                            realm.copyToRealm(Item.create((long) i));
                        }
                    }
                })
                .deleteRealmIfMigrationNeeded()
                .build();

        startService(new Intent(this, AppUpdateIntentService.class));
    }
}
