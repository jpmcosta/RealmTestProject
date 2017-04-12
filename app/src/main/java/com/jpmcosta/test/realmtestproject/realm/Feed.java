package com.jpmcosta.test.realmtestproject.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Feed extends RealmObject {

    public static Feed create(Long id) {
        Feed feed = new Feed();
        feed.id = id;
        feed.items = new RealmList<>();
        return feed;
    }

    @PrimaryKey
    public Long id;

    public RealmList<Item> items;


    public void addItem(Item item) {
        items.add(item);
        item.parentFeed = this;
    }
}