package com.jpmcosta.test.realmtestproject.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jpmcosta.test.realmtestproject.R;
import com.jpmcosta.test.realmtestproject.adapter.ItemListAdapter;
import com.jpmcosta.test.realmtestproject.realm.Feed;
import com.jpmcosta.test.realmtestproject.realm.Item;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends RealmActivity {

    @BindView(android.R.id.list)
    RecyclerView mRecyclerView;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupRecyclerView();

        setupFab();
    }

    private void setupRecyclerView() {
        ItemListAdapter adapter = new ItemListAdapter(getBookmarkedItems());
        adapter.setOnItemClickListener(new ItemListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(final Item item) {
                getRealm().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        item.isBookmarked = !item.isBookmarked;
                    }
                });
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }

    private void setupFab() {
        mFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final RealmResults<Item> items = getRealm().where(Item.class).findAll();
                getRealm().executeTransaction(new Realm.Transaction() {

                    @Override
                    public void execute(Realm realm) {
                        for (Item item : items) {
                            item.isBookmarked = true;
                        }
                    }
                });
            }
        });
    }

    private RealmResults<Item> getBookmarkedItems() {
        ensureItems();
        return getRealm().where(Item.class).equalTo("isBookmarked", true).findAll();
    }

    private void ensureItems() {
        RealmResults items = getRealm().where(Item.class).findAll();
        if (items.size() == 0) {
            getRealm().executeTransaction(new Realm.Transaction() {

                @Override
                public void execute(Realm realm) {
                    Feed feed = realm.copyToRealm(Feed.create(0L));

                    for (int i = 0; i < 10; i++) {
                        Item item = realm.copyToRealm(Item.create((long) i));
                        feed.addItem(item);
                    }
                }
            });
        }
    }
}
