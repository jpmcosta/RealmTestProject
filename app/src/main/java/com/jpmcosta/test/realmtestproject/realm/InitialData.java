package com.jpmcosta.test.realmtestproject.realm;

import androidx.annotation.NonNull;

import com.jpmcosta.test.realmtestproject.realm.obj.Item;

import io.realm.Realm;

public class InitialData implements Realm.Transaction {

    private static final long ITEM_COUNT = 100_000;


    @Override
    public void execute(@NonNull Realm realm) {
        for (long i = 0; i < ITEM_COUNT; i++) {
            realm.copyToRealm(Item.create(i));
        }
    }
}
