package com.jpmcosta.test.realmtestproject.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jpmcosta.test.realmtestproject.R;
import com.jpmcosta.test.realmtestproject.RealmTestProject;
import com.jpmcosta.test.realmtestproject.adapter.ItemListAdapter;
import com.jpmcosta.test.realmtestproject.realm.obj.Item;

import java.util.concurrent.ThreadLocalRandom;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.Sort;

import static com.jpmcosta.test.realmtestproject.util.Const.NO_TIME;

public class MainActivity extends RealmActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(android.R.id.list)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.fab_query)
    protected FloatingActionButton mFabQuery;

    private ItemListAdapter mAdapter;

    private long firstCreatedAt;

    private long lastCreatedAt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupContentView();

        final Realm realm = getRealm();
        firstCreatedAt = realm.where(Item.class).min("createdAt").longValue();
        lastCreatedAt = realm.where(Item.class).max("createdAt").longValue();
    }

    private void setupContentView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupRecyclerView();

        setupFabQuery();
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new ItemListAdapter(getRealm().where(Item.class).findAll());
        mAdapter.setOnItemClickListener(new ItemListAdapter.OnItemClickListener() {

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
    }

    private void setupFabQuery() {
        mFabQuery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread() {

                    @Override
                    public void run() {
                        final Realm realm = Realm.getInstance(((RealmTestProject) getApplication()).realmConfiguration);
                        try {
                            for (int i = 0; i < 10; i++) {
                                final long createdAt =
                                        ThreadLocalRandom.current().nextLong(firstCreatedAt, lastCreatedAt);
                                final Item item = realm.where(Item.class)
                                        .equalTo("key", "A")
                                        .equalTo("createdAt", createdAt)
                                        .equalTo("removedAt", NO_TIME)
                                        .sort("random", Sort.DESCENDING)
                                        .findFirst();
                                final long itemId = item != null ? item.id : -1L;

                                Log.i(TAG, "item.id = " + itemId);
                            }
                        } finally {
                            realm.close();
                        }
                    }
                }.start();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mRecyclerView.setAdapter(null);
    }
}