package com.jpmcosta.test.realmtestproject.realm.obj

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Feed : RealmObject() {

    companion object {

        @JvmStatic
        fun create(id: Long, groups: RealmList<Group>? = null): Feed {
            val feed = Feed()
            feed.id = id
            feed.groups = groups ?: RealmList()
            return feed
        }
    }


    @PrimaryKey
    open var id: Long = 0

    open lateinit var groups: RealmList<Group>
}