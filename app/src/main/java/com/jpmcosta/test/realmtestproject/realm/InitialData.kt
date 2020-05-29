package com.jpmcosta.test.realmtestproject.realm

import com.jpmcosta.test.realmtestproject.realm.obj.Item.Companion.create
import io.realm.Realm

class InitialData : Realm.Transaction {

    companion object {

        private const val ITEM_COUNT: Long = 100000
    }


    override fun execute(realm: Realm) {
        for (i in 0 until ITEM_COUNT) {
            realm.copyToRealm(create(i))
        }
    }
}