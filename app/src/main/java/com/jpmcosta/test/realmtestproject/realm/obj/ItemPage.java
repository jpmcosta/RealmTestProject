package com.jpmcosta.test.realmtestproject.realm.obj;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@SuppressWarnings("WeakerAccess")
public class ItemPage extends RealmObject {

    public static ItemPage create(Long id) {
        ItemPage itemPage = new ItemPage();
        itemPage.id = id;
        itemPage.items = new RealmList<>();
        return itemPage;
    }


    @PrimaryKey
    public Long id;

    public RealmList<Item> items;
}