package com.jpmcosta.test.realmtestproject.adapter

import com.jpmcosta.test.realmtestproject.realm.obj.SubItem

class SubItemResultsAdapter : ResultsListAdapter<SubItem>() {

    override fun onCreateObjText(obj: SubItem): String = obj.run { "subItem#$id, $seqNum" }
}