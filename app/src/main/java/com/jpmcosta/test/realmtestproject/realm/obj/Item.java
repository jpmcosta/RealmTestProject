package com.jpmcosta.test.realmtestproject.realm.obj;

import java.util.concurrent.ThreadLocalRandom;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

import static com.jpmcosta.test.realmtestproject.util.Const.NO_TIME;

@SuppressWarnings("WeakerAccess")
public class Item extends RealmObject {

    private static final String[] ITEM_KEYS = new String[]{"A", "B", "C", "D"};


    public static Item create(Long id) {
        Item item = new Item();
        item.id = id;
        item.isBookmarked = false;
        item.random = ThreadLocalRandom.current().nextLong();
        item.key = ITEM_KEYS[(int) (id % ITEM_KEYS.length)];
        item.createdAt = System.currentTimeMillis();
        return item;
    }


    @PrimaryKey
    public long id;

    public boolean isBookmarked;

    @Index
    public long random;

    @Index
    public String key;

    @Index
    public long createdAt;

    @Index
    public long removedAt = NO_TIME;
}