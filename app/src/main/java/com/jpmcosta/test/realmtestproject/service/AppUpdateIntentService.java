package com.jpmcosta.test.realmtestproject.service;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.jpmcosta.test.realmtestproject.RealmTestProject;
import com.jpmcosta.test.realmtestproject.realm.App;

import io.realm.Realm;

public class AppUpdateIntentService extends IntentService {

    public AppUpdateIntentService() {
        super(AppUpdateIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Realm realm = Realm.getInstance(((RealmTestProject) getApplication()).realmConfiguration);

        while (true) {
            final App app = realm.where(App.class).findFirst();
            realm.executeTransaction(new Realm.Transaction() {

                @Override
                public void execute(Realm realm) {
                    app.version++;
                }
            });
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
