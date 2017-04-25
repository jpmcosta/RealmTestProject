package com.jpmcosta.test.realmtestproject.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Filter extends RealmObject {

    public static Filter create(Long id) {
        Filter feed = new Filter();
        feed.id = id;
        feed.apps = new RealmList<>();
        return feed;
    }

    @PrimaryKey
    public Long id;

    public RealmList<App> apps;

    public boolean isEnabled = false;


    public void reset() {
        isEnabled = false;
        apps.clear();
    }

    public void copy(Filter from) {
        isEnabled = from.isEnabled;
        for (App app : from.apps) {
            apps.add(app);
        }
    }
}