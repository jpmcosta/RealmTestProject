package com.jpmcosta.test.realmtestproject.realm;

import androidx.annotation.NonNull;

import com.jpmcosta.test.realmtestproject.realm.obj.Item;
import com.jpmcosta.test.realmtestproject.realm.obj.ItemPage;
import com.jpmcosta.test.realmtestproject.realm.obj.SubItem;

import io.realm.Realm;

public class InitialData implements Realm.Transaction {

    private static final long ITEM_COUNT = 50000;


    @Override
    public void execute(@NonNull Realm realm) {
        ItemPage itemPage = null;
        for (long i = 0; i < ITEM_COUNT; i++) {
            final SubItem subItem = realm.copyToRealm(SubItem.create(i));
            subItem.name = Character.toString((char) (65 + i % 5)) + i;

            final Item item = realm.copyToRealm(Item.create(i));
            item.subItem = subItem;
            if (i % 4 == 0) {
                item.removedAt = System.currentTimeMillis();
            }

            if (i % (ITEM_COUNT / 10) == 0) {
                itemPage = realm.copyToRealm(ItemPage.create(i / (ITEM_COUNT / 10)));
            }
            if (itemPage != null) {
                itemPage.items.add(item);
            }
        }
    }
}
