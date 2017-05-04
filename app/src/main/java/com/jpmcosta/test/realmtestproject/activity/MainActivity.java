package com.jpmcosta.test.realmtestproject.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.jpmcosta.test.realmtestproject.R;
import com.jpmcosta.test.realmtestproject.adapter.FilterListAdapter;
import com.jpmcosta.test.realmtestproject.realm.App;
import com.jpmcosta.test.realmtestproject.realm.Feed;
import com.jpmcosta.test.realmtestproject.realm.Filter;
import com.jpmcosta.test.realmtestproject.realm.Item;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends RealmActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int OBJECT_COUNT = 10;

    private static final int TEST_REPEAT_COUNT = 10;

    @BindView(android.R.id.list)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.fab)
    protected FloatingActionButton mFab;

    private Random mRandom = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupRealm();

        setupContentView();
    }

    private void setupRealm() {
        // Delete all.
        getRealm().executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });

        getRealm().executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                for (int i = 0; i < OBJECT_COUNT; i++) {
                    realm.copyToRealm(App.create((long) i));
                    realm.copyToRealm(Feed.create((long) i));
                    realm.copyToRealm(Item.create((long) i));
                }

                App app0 = realm.where(App.class).equalTo("id", 0).findFirst();

                for (int i = 0; i < 3; i++) {
                    Filter filter = realm.copyToRealm(Filter.create((long) i));
                    filter.apps.add(app0);
                }
            }
        });
    }


    private void setupContentView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupRecyclerView();

        setupFab();
    }

    private void setupRecyclerView() {
        FilterListAdapter filterListAdapter = new FilterListAdapter(getRealm().where(Filter.class).findAll());
        filterListAdapter.setOnClickListenerE(new FilterListAdapter.OnFilterClickListener() {

            @Override
            public void onFilterClick(final Filter filter) {
                getRealm().executeTransaction(new Realm.Transaction() {

                    @Override
                    public void execute(Realm realm) {
                        filter.isEnabled = !filter.isEnabled;
                    }
                });
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(filterListAdapter);
    }

    private void setupFab() {
        mFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RealmResults<App> apps = getRealm().where(App.class).findAll();
                RealmResults<Feed> feeds = getRealm().where(Feed.class).findAll();
                RealmResults<Item> items = getRealm().where(Item.class).findAll();

                for (int i = 1; i <= TEST_REPEAT_COUNT; i++) {
                    test(i, apps, feeds, items);
                }
            }
        });
    }

    private void test(int testNumber, final RealmResults<App> apps, final RealmResults<Feed> feeds,
                      final RealmResults<Item> items) {
        getRealm().executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                for (int i = 0; i < OBJECT_COUNT; i++) {
                    App app = apps.get(i);
                    app.feeds.clear();

                    for (int j = 0; j < OBJECT_COUNT; j++) {
                        Feed feed = feeds.get(j);
                        feed.items.clear();

                        for (int k = 0; k < OBJECT_COUNT; k++) {
                            Item item = items.get(k);
                            item.app = apps.get(mRandom.nextInt(OBJECT_COUNT));
                            item.parentFeed = feeds.get(mRandom.nextInt(OBJECT_COUNT));

                            if (mRandom.nextBoolean()) {
                                feed.items.add(item);
                            }
                        }

                        if (mRandom.nextBoolean()) {
                            app.feeds.add(feed);
                        }
                    }
                }
            }
        });

        Log.i(LOG_TAG, "--------------------------------------------------");
        Log.i(LOG_TAG, "[test " + testNumber + "]");

        for (App app : apps) {
            Log.i(LOG_TAG, app.toString());
            for (Feed feed : app.feeds) {
                Log.i(LOG_TAG, "                   " + feed.toString());
            }
        }

        for (Feed feed : feeds) {
            Log.i(LOG_TAG, feed.toString());
            for (Item item : feed.items) {
                Log.i(LOG_TAG, "                   " + item.toString());
            }
        }

        for (Item item : items) {
            Log.i(LOG_TAG, item.toString());
            Log.i(LOG_TAG, "                   " + item.app.toString());
            Log.i(LOG_TAG, "                   " + item.parentFeed.toString());
        }

        Long time = SystemClock.elapsedRealtime();

        getRealm().executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                Filter filter = realm.where(Filter.class).equalTo("id", 0).findFirst();
                filter.isEnabled = !filter.isEnabled;
            }
        });

        Log.i(LOG_TAG, "[test " + testNumber + "] time: " + (SystemClock.elapsedRealtime() - time));
        Log.i(LOG_TAG, "--------------------------------------------------");
    }
}