package com.jpmcosta.test.realmtestproject.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jpmcosta.test.realmtestproject.RealmTestProject;
import com.jpmcosta.test.realmtestproject.realm.App;
import com.jpmcosta.test.realmtestproject.realm.Item;

import java.util.Random;

import io.realm.Realm;

public class CreateIntentService extends IntentService {

    public CreateIntentService() {
        super(CreateIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Realm realm = Realm.getInstance(((RealmTestProject) getApplication()).realmConfiguration);

        final App app = realm.where(App.class).findFirst();
        if (app == null) {
            return;
        }

        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(@NonNull Realm realm) {
                Random random = new Random();

                if (app.items.isEmpty()) {
                    for (int i = 0; i < 10; i++) {
                        final Item item = realm.copyToRealm(Item.create(random.nextInt()));
                        app.items.add(0, item);
                    }
                } else {
                    app.items.clear();
                }
            }
        });
    }
}
