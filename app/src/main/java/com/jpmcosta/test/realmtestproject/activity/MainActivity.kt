package com.jpmcosta.test.realmtestproject.activity

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jpmcosta.test.realmtestproject.R
import com.jpmcosta.test.realmtestproject.adapter.ItemResultsAdapter
import com.jpmcosta.test.realmtestproject.databinding.ActivityMainBinding
import com.jpmcosta.test.realmtestproject.realm.obj.Item
import io.realm.Realm
import io.realm.Sort


class MainActivity : RealmActivity() {

    lateinit var binding: ActivityMainBinding

    private val fab: FloatingActionButton get() = binding.fab

    private val recyclerView: RecyclerView get() = binding.list

    private val itemResultsAdapter = ItemResultsAdapter()


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

            createItem(realm)
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = itemResultsAdapter
    }

    override fun onStartRealm(realm: Realm) {
        super.onStartRealm(realm)

        val text = getString(R.string.realm_started, realm.toString())
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()

        updateItems(realm)
    }

    private fun updateItems(realm: Realm) {
        val items = realm.where(Item::class.java).sort("id", Sort.DESCENDING).findAllAsync()
        itemResultsAdapter.updateResults(results = items)
    }

    private fun createItem(realm: Realm) {
        realm.executeTransactionAsync { realm ->
            val itemId = (realm.where(Item::class.java).max("id")?.toLong() ?: -1L) + 1
            realm.insert(Item().apply { id = itemId })
        }
    }
}