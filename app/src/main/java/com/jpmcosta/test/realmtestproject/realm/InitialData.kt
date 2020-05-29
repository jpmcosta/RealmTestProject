package com.jpmcosta.test.realmtestproject.realm

import android.util.Log
import com.jpmcosta.test.realmtestproject.realm.obj.Feed
import com.jpmcosta.test.realmtestproject.realm.obj.Group
import com.jpmcosta.test.realmtestproject.realm.obj.Item
import com.jpmcosta.test.realmtestproject.realm.obj.SubItem
import com.jpmcosta.test.realmtestproject.util.Const
import io.realm.Realm
import io.realm.RealmList
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.max

class InitialData : Realm.Transaction {

    companion object {

        private val TAG = InitialData::class.java.simpleName

        private const val FEED_COUNT = 30

        private const val GROUP_COUNT = 30000

        private const val ITEM_COUNT = 120000

        private const val SUB_ITEM_COUNT = 250000
    }

    private var feedId = Const.NO_ID

    private var groupId = Const.NO_ID

    private var itemId = Const.NO_ID

    private var subItemId = Const.NO_ID

    private val random = ThreadLocalRandom.current()


    override fun execute(realm: Realm) {
        do {
            val feedGroups = realm.createGroups(maxGroupPerFeed = 10000, maxItemPerGroup = 8, maxSubItemPerItem = 4,
                    maxSubFeedCount = 10)
            val feed = Feed.create(++feedId, groups = feedGroups)
            realm.copyToRealm(feed)

            val feedCount = realm.where(Feed::class.java).count()
            val groupCount = realm.where(Group::class.java).count()
            val itemCount = realm.where(Item::class.java).count()
            val subItemCount = realm.where(SubItem::class.java).count()
            Log.i(TAG, "feeds: $feedCount; groups: $groupCount; items: $itemCount; subItems: $subItemCount")

        } while (feedId < FEED_COUNT || groupId < GROUP_COUNT || itemId < ITEM_COUNT || subItemId < SUB_ITEM_COUNT)
    }


    private fun Realm.createGroups(maxGroupPerFeed: Int, maxItemPerGroup: Int, maxSubItemPerItem: Int,
                                   maxSubFeedCount: Int): RealmList<Group> {
        val groups = RealmList<Group>()
        val randomMaxSubFeedCount = if (maxSubFeedCount == 0) 0 else random.nextInt(0, maxSubFeedCount)
        var subFeedCount = 0
        repeat(random.nextInt(1, maxGroupPerFeed)) {
            val itemCount = random.nextInt(0, maxItemPerGroup)
            if (0 < itemCount || randomMaxSubFeedCount <= subFeedCount++) {
                val groupItems = RealmList<Item>()
                repeat(max(1, itemCount)) {
                    val itemSubItems = RealmList<SubItem>()
                    repeat(random.nextInt(1, maxSubItemPerItem)) {
                        val subItem = copyToRealm(SubItem.create(++subItemId))
                        itemSubItems.add(subItem)
                    }
                    val item = copyToRealm(Item.create(++itemId, itemSubItems))
                    groupItems.add(item)
                }
                val group = copyToRealm(Group.create(++groupId, groupItems))
                groups.add(group)
            } else {
                val groupFeedGroups = createGroups(maxGroupPerFeed = 20, maxItemPerGroup = 4, maxSubItemPerItem = 2,
                        maxSubFeedCount = 0)
                val groupFeed = copyToRealm(Feed.create(++feedId, groups = groupFeedGroups))
                val group = copyToRealm(Group.create(++groupId, feed = groupFeed))
                groups.add(group)
            }
        }
        return groups
    }
}