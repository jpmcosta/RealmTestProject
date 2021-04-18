package com.jpmcosta.test.realmtestproject.realm

import com.jpmcosta.test.realmtestproject.realm.obj.Item
import io.realm.Realm

class RealmInitialData : Realm.Transaction {

    override fun execute(realm: Realm) {
        for (index in 1..10L) {
            realm.insert(Item().apply { id = index })
        }
    }
}