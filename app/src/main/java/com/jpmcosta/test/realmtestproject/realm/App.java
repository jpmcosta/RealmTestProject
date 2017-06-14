package com.jpmcosta.test.realmtestproject.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class App extends RealmObject {

    public static App create(Long id, String name, int color) {
        App app = new App();
        app.name = name;
        app.id = id;
        app.color = RealmColor.create(color);
        return app;
    }

    @PrimaryKey
    public Long id;

    public String name;

    public RealmColor color;
}