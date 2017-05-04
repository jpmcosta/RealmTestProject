package com.jpmcosta.test.realmtestproject.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Filter extends RealmObject {

    public static Filter create(Long id) {
        Filter filter = new Filter();
        filter.id = id;
        filter.apps = new RealmList<>();
        return filter;
    }

    @PrimaryKey
    public Long id;

    public RealmList<App> apps;

    public boolean isEnabled = false;
}