package com.jpmcosta.test.realmtestproject.adapter

import com.jpmcosta.test.realmtestproject.realm.obj.Feed

class FeedResultsAdapter : ResultsListAdapter<Feed>() {

    override fun onCreateObjText(obj: Feed): String =
        obj.run { "feed#$id, $seqNum, $type, ${groups?.size}, ${items?.size}" }
}
