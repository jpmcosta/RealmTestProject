package com.jpmcosta.test.realmtestproject.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jpmcosta.test.realmtestproject.R;
import com.jpmcosta.test.realmtestproject.adapter.FilterListAdapter;
import com.jpmcosta.test.realmtestproject.realm.App;
import com.jpmcosta.test.realmtestproject.realm.Feed;
import com.jpmcosta.test.realmtestproject.realm.Filter;
import com.jpmcosta.test.realmtestproject.realm.Item;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends RealmActivity {

    private static final int APP_COUNT = 300;

    private static final int FEED_COUNT = 10;

    @BindView(android.R.id.list)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.fab_filter)
    protected FloatingActionButton mFilterFab;

    @BindView(R.id.fab_item)
    protected FloatingActionButton mItemFab;

    private Random mRandom = new Random();

    private long mLastItemId = -1;

    private long mLastFilterId = -1;

    private List<App> mApps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupRealm();

        setupContentView();
    }

    private void setupRealm() {
        Long lastFilterId = (Long) getRealm().where(Filter.class).max("id");
        if (lastFilterId != null) {
            mLastFilterId = lastFilterId;
        }

        Long lastItemId = (Long) getRealm().where(Item.class).max("id");
        if (lastItemId != null) {
            mLastItemId = lastItemId;
        }

        if (getRealm().where(App.class).findFirst() == null) {
            getRealm().executeTransaction(new Realm.Transaction() {

                @Override
                public void execute(Realm realm) {
                    // Create and add FEED_COUNT Feeds for each App.

                    for (int i = 0; i < APP_COUNT; i++) {
                        App app = realm.copyToRealm(App.create((long) i));

                        for (int j = 0; j < FEED_COUNT; j++) {
                            Feed feed = realm.copyToRealm(Feed.create((long) (i * FEED_COUNT + j)));

                            app.feeds.add(feed);
                        }
                    }

                    // Create a temporary Filter that will be used to create all other Filters.
                    // It will have id = 0.
                    realm.copyToRealm(Filter.create(++mLastFilterId));
                }
            });
        }

        mApps = getRealm().where(App.class).findAll();
    }

    private void setupContentView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupRecyclerView();

        setupFilterFab();

        setupItemFab();
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

    private void setupFilterFab() {
        mFilterFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getRealm().executeTransaction(new Realm.Transaction() {

                    @Override
                    public void execute(Realm realm) {
                        // Update temporary Filter, before "committing changes".

                        Filter tempFilter = realm.where(Filter.class).equalTo("id", 0).findFirst();
                        tempFilter.reset();

                        for (int i = 0; i < APP_COUNT; i++) {
                            if (mRandom.nextBoolean()) {
                                App app = mApps.get(mRandom.nextInt(APP_COUNT));
                                if (!tempFilter.apps.contains(app)) {
                                    tempFilter.apps.add(app);
                                }
                            }
                        }
                    }
                });

                getRealm().executeTransaction(new Realm.Transaction() {

                    @Override
                    public void execute(Realm realm) {
                        // "Commit changes", create a new Filter based on tempFilter.

                        Filter tempFilter = realm.where(Filter.class).equalTo("id", 0).findFirst();
                        Filter newFilter = realm.copyToRealm(Filter.create(++mLastFilterId));
                        newFilter.copy(tempFilter);
                    }
                });
            }
        });
    }

    private void setupItemFab() {
        mItemFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getRealm().executeTransaction(new Realm.Transaction() {

                    @Override
                    public void execute(Realm realm) {
                        // Create and add a new Item for each Feed.

                        RealmResults<Feed> feeds = realm.where(Feed.class).findAll();
                        for (Feed feed : feeds) {
                            Item item = realm.copyToRealm(Item.create(++mLastItemId));
                            item.app = mApps.get(mRandom.nextInt(APP_COUNT));
                            item.parentFeed = feed;

                            feed.items.add(item);
                        }
                    }
                });
            }
        });
    }
}
