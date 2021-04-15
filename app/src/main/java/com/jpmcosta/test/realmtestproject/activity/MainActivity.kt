package com.jpmcosta.test.realmtestproject.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jpmcosta.test.realmtestproject.adapter.*
import com.jpmcosta.test.realmtestproject.databinding.ActivityMainBinding
import com.jpmcosta.test.realmtestproject.realm.obj.*
import io.realm.Realm

class MainActivity : RealmActivity() {

    lateinit var binding: ActivityMainBinding

    private val fab: FloatingActionButton get() = binding.fab

    private val recyclerView: RecyclerView get() = binding.list

    private val adapterSelector = AdapterSelector()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupContentView()
    }

    private fun setupContentView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFab()

        setupRecyclerView()
    }

    private fun setupFab() {
        fab.setOnClickListener {
            val realm = realm ?: return@setOnClickListener

            recyclerView.adapter = adapterSelector.next(realm)
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapterSelector.currentAdapter
    }

    override fun onStartRealm(realm: Realm) {
        super.onStartRealm(realm)

        adapterSelector.fill(realm)
    }

    override fun onStopRealm() {
        super.onStopRealm()

        adapterSelector.clear()
    }


    private class AdapterSelector {

        private var adapters: Array<ResultsListAdapter<*>> = arrayOf(FeedResultsAdapter(), GroupResultsAdapter(),
            ItemResultsAdapter(), SubItemResultsAdapter(), LabelResultsAdapter())

        private var index: Int = 0

        val currentAdapter: ResultsListAdapter<*> get() = adapters[index]


        fun fill(realm: Realm) {
            when (val current = currentAdapter) {
                is FeedResultsAdapter -> current.results = realm.where(Feed::class.java).findAllAsync()
                is GroupResultsAdapter -> current.results = realm.where(Group::class.java).findAllAsync()
                is ItemResultsAdapter -> current.results = realm.where(Item::class.java).findAllAsync()
                is SubItemResultsAdapter -> current.results = realm.where(SubItem::class.java).findAllAsync()
                is LabelResultsAdapter -> current.results = realm.where(Label::class.java).findAllAsync()
            }
        }

        fun clear() {
            currentAdapter.clearResults()
        }

        fun next(realm: Realm): ResultsListAdapter<*> {
            clear()
            if (adapters.size <= ++index) {
                index = 0
            }
            fill(realm)

            return currentAdapter
        }
    }
}