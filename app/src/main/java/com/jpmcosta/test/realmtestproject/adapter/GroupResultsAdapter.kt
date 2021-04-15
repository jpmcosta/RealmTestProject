package com.jpmcosta.test.realmtestproject.adapter

import com.jpmcosta.test.realmtestproject.realm.obj.Group

class GroupResultsAdapter : ResultsListAdapter<Group>() {

    override fun onCreateObjText(obj: Group): String = obj.run { "group$id, $seqNum ${items?.size}, ${feed?.id}" }
}