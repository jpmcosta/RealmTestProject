package com.jpmcosta.test.realmtestproject.adapter

import com.jpmcosta.test.realmtestproject.realm.obj.Item

class ItemResultsAdapter : ResultsListAdapter<Item>() {

    override fun onCreateObjText(obj: Item): String = obj.run { "item$id, $seqNum ${label?.id} ${subItems.size}" }
}