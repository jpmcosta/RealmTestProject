package com.jpmcosta.test.realmtestproject.realm.obj

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

open class Feed : RealmObject() {

    @PrimaryKey
    open var id: Long = 0

    @Index
    open var seqNum: Long = Long.MAX_VALUE

    @Index
    open var type: Int = 0

    open var groups: RealmList<Group>? = null

    open var items: RealmList<Item>? = null
}