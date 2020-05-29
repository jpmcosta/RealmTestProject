package com.jpmcosta.test.realmtestproject.realm.obj

import com.jpmcosta.test.realmtestproject.util.Const
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import java.util.concurrent.ThreadLocalRandom

open class Item : RealmObject() {

    companion object {

        private val ITEM_KEYS = arrayOf("A", "B", "C", "D")


        @JvmStatic
        fun create(id: Long): Item {
            val item = Item()
            item.id = id
            item.isBookmarked = false
            item.random = ThreadLocalRandom.current().nextLong()
            item.key = ITEM_KEYS[(id % ITEM_KEYS.size).toInt()]
            item.createdAt = System.currentTimeMillis()
            return item
        }
    }


    @PrimaryKey
    open var id: Long = 0

    open var isBookmarked: Boolean = false

    @Index
    open var random: Long = 0

    @Index
    open var key: String? = null

    @Index
    open var createdAt: Long = 0

    @Index
    open var removedAt = Const.NO_TIME
}