package com.jpmcosta.test.realmtestproject.realm;

import io.realm.RealmObject;

public class Item extends RealmObject {

    public static Item create(int value) {
        Item item = new Item();
        item.value = value;
        return item;
    }

    public int value;
}