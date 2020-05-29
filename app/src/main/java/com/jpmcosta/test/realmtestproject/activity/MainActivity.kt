package com.jpmcosta.test.realmtestproject.activity

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jpmcosta.test.realmtestproject.R
import com.jpmcosta.test.realmtestproject.RealmTestProject
import com.jpmcosta.test.realmtestproject.adapter.ItemListAdapter
import com.jpmcosta.test.realmtestproject.realm.obj.Item
import com.jpmcosta.test.realmtestproject.util.Const
import io.realm.Realm
import io.realm.Sort
import java.util.concurrent.ThreadLocalRandom
import kotlin.concurrent.thread

class MainActivity : RealmActivity() {

    companion object {

        private val TAG = MainActivity::class.java.simpleName
    }

    @BindView(android.R.id.list)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.fab_query)
    lateinit var fabQuery: FloatingActionButton

    private var itemListAdapter: ItemListAdapter? = null

    private var firstCreatedAt: Long = 0

    private var lastCreatedAt: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupContentView()

        firstCreatedAt = realm.where(Item::class.java).min("createdAt")?.toLong() ?: 0L
        lastCreatedAt = realm.where(Item::class.java).max("createdAt")?.toLong() ?: 0L
    }

    private fun setupContentView() {
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setupRecyclerView()
        setupFabQuery()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)

        itemListAdapter = ItemListAdapter(realm.where(Item::class.java).findAll()).apply {
            setOnItemClickListener(object : ItemListAdapter.OnItemClickListener {

                override fun onItemClick(item: Item) {
                    realm.executeTransaction {
                        item.isBookmarked = !item.isBookmarked
                    }
                }
            })
        }
    }

    private fun setupFabQuery() {
        fabQuery.setOnClickListener {
            thread {
                Realm.getInstance((application as RealmTestProject).realmConfiguration).use { realm ->
                    for (i in 0..9) {
                        val createdAt = ThreadLocalRandom.current().nextLong(firstCreatedAt, lastCreatedAt)
                        val item = realm.where(Item::class.java)
                                .equalTo("key", "A")
                                .equalTo("createdAt", createdAt)
                                .equalTo("removedAt", Const.NO_TIME)
                                .sort("random", Sort.DESCENDING)
                                .findFirst()
                        val itemId = item?.id ?: Const.NO_ID
                        Log.i(TAG, "item.id = $itemId")
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        recyclerView.adapter = itemListAdapter
    }

    override fun onStop() {
        super.onStop()

        recyclerView.adapter = null
    }
}