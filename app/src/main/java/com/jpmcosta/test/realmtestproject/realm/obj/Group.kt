package com.jpmcosta.test.realmtestproject.realm.obj

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Group : RealmObject() {

    companion object {

        @JvmStatic
        fun create(id: Long, items: RealmList<Item>? = null, feed: Feed? = null): Group {
            val group = Group()
            group.id = id
            group.items = items
            group.feed = feed
            return group
        }
    }


    @PrimaryKey
    open var id: Long = 0

    open var items: RealmList<Item>? = null

    open var feed: Feed? = null
}