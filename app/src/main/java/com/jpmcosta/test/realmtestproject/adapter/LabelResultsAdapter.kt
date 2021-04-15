package com.jpmcosta.test.realmtestproject.adapter

import com.jpmcosta.test.realmtestproject.realm.obj.Label

class LabelResultsAdapter : ResultsListAdapter<Label>() {

    override fun onCreateObjText(obj: Label): String = obj.run { "label$id, $isA, $isB" }
}