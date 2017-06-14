package com.jpmcosta.test.realmtestproject.realm;

import io.realm.RealmObject;

public class RealmColor extends RealmObject {

    public static RealmColor create(int value) {
        RealmColor color = new RealmColor();
        color.value = value;
        return color;
    }

    public int value;
}