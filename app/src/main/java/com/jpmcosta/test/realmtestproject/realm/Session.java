package com.jpmcosta.test.realmtestproject.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Session extends RealmObject {

    public static Session create(Long id) {
        Session app = new Session();
        app.id = id;
        return app;
    }

    @PrimaryKey
    public Long id;

    public int appVersion;
}