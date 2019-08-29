package com.jpmcosta.test.realmtestproject.realm.obj;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.Index;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

import static com.jpmcosta.test.realmtestproject.util.Const.NO_TIME;

@SuppressWarnings("WeakerAccess")
public class Item extends RealmObject {

    public static Item create(Long id) {
        Item item = new Item();
        item.id = id;
        return item;
    }


    @PrimaryKey
    public Long id;

    public SubItem subItem = null;

    public Boolean isBookmarked = false;

    @Index // Removing this index seems to speed up findAll() in MainActivity (line 150).
    public long removedAt = NO_TIME;

    @LinkingObjects("items")
    public final RealmResults<ItemPage> itemPages = null;
}