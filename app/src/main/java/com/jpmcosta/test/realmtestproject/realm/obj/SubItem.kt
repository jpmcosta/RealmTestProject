package com.jpmcosta.test.realmtestproject.realm.obj

import com.jpmcosta.test.realmtestproject.util.Const
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import java.util.concurrent.ThreadLocalRandom

open class SubItem : RealmObject() {

    companion object {

        private val SUB_ITEM_KEYS = arrayOf("A", "B", "C", "D")


        @JvmStatic
        fun create(id: Long): SubItem {
            val subItem = SubItem()
            subItem.id = id
            subItem.isBookmarked = false
            subItem.random = ThreadLocalRandom.current().nextLong()
            subItem.key = SUB_ITEM_KEYS[(id % SUB_ITEM_KEYS.size).toInt()]
            subItem.createdAt = System.currentTimeMillis()
            return subItem
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