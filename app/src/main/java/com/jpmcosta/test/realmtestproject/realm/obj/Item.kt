package com.jpmcosta.test.realmtestproject.realm.obj

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

open class Item : RealmObject() {

    @PrimaryKey
    open var id: Long = 0

    @Index
    open var seqNum: Long = Long.MIN_VALUE

    open var label: Label? = null

    open lateinit var subItems: RealmList<SubItem>
}