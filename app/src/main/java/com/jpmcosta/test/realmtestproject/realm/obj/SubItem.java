package com.jpmcosta.test.realmtestproject.realm.obj;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

import static com.jpmcosta.test.realmtestproject.util.Const.STRING_EMPTY;

@SuppressWarnings("WeakerAccess")
public class SubItem extends RealmObject {

    public static SubItem create(Long id) {
        SubItem subItem = new SubItem();
        subItem.id = id;
        return subItem;
    }


    @PrimaryKey
    public Long id;

    @Index
    public String name = STRING_EMPTY;
}