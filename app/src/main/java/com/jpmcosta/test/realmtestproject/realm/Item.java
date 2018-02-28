package com.jpmcosta.test.realmtestproject.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject {

    public static Item create(Long id) {
        Item item = new Item();
        item.id = id;
        return item;
    }

    @PrimaryKey
    public Long id;

    public boolean isBookmarked = false;
}