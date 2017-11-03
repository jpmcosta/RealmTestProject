package com.jpmcosta.test.realmtestproject.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class App extends RealmObject {

    public static App create(Long id) {
        App app = new App();
        app.id = id;
        app.items = new RealmList<>();
        return app;
    }

    @PrimaryKey
    public Long id;

    public RealmList<Item> items;
}