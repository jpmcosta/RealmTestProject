package com.jpmcosta.test.realmtestproject.realm.obj

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Item : RealmObject() {

    @PrimaryKey
    open var id: Long = 0
}