package com.jpmcosta.test.realmtestproject.adapter

import com.jpmcosta.test.realmtestproject.realm.obj.Feed
import io.realm.RealmChangeListener

class FeedAdapter : GroupListAdapter() {

    private val feedChangeListener = RealmChangeListener<Feed> { feed ->
        onFeedChanged(feed)
    }

    var feed: Feed? = null
        set(feed) {
            if (field != feed) {
                field?.removeChangeListener(feedChangeListener)
                field = feed
                if (feed != null) {
                    feed.addChangeListener(feedChangeListener)
                    onFeedChanged(feed)
                }
            }
        }


    private fun onFeedChanged(feed: Feed) {
        groups = feed.groups
    }
}