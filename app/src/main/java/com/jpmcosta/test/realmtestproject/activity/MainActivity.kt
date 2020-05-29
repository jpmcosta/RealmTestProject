package com.jpmcosta.test.realmtestproject.activity

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jpmcosta.test.realmtestproject.R
import com.jpmcosta.test.realmtestproject.adapter.FeedAdapter
import com.jpmcosta.test.realmtestproject.realm.obj.Feed
import io.realm.Realm

class MainActivity : RealmActivity() {

    @BindView(R.id.fab)
    lateinit var fab: FloatingActionButton

    @BindView(android.R.id.list)
    lateinit var recyclerView: RecyclerView

    private var feedAdapter = FeedAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupContentView()
    }

    private fun setupContentView() {
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        setupFab()

        setupRecyclerView()
    }

    private fun setupFab() {
        fab.setOnClickListener {
            Toast.makeText(this, "Hello, World!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()

        recyclerView.adapter = feedAdapter
    }

    override fun onStop() {
        super.onStop()

        feedAdapter.feed = null
        recyclerView.adapter = null
    }

    override fun onStartRealm(realm: Realm) {
        super.onStartRealm(realm)

        feedAdapter.feed = realm.where(Feed::class.java).findFirst()
    }

    override fun onStopRealm() {
        super.onStopRealm()

        feedAdapter.feed = null
    }
}