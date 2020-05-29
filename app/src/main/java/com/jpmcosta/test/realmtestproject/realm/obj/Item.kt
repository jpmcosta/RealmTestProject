package com.jpmcosta.test.realmtestproject.realm.obj

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Item : RealmObject() {

    companion object {

        @JvmStatic
        fun create(id: Long, subItems: RealmList<SubItem>? = null): Item {
            val item = Item()
            item.id = id
            item.subItems = subItems ?: RealmList()
            return item
        }
    }


    @PrimaryKey
    open var id: Long = 0

    open lateinit var subItems: RealmList<SubItem>
}