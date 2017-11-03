package com.jpmcosta.test.realmtestproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jpmcosta.test.realmtestproject.R;
import com.jpmcosta.test.realmtestproject.adapter.ItemListAdapter;
import com.jpmcosta.test.realmtestproject.realm.App;
import com.jpmcosta.test.realmtestproject.realm.Item;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class MainActivity extends RealmActivity {

    @BindView(android.R.id.list)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.fab)
    protected FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupContentView();
    }

    private void setupContentView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupRecyclerView();

        setupFab();
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupFab() {
        mFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateActivity.class));
            }
        });

        mFab.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                final App app = getRealm().where(App.class).findFirst();

                getRealm().executeTransaction(new Realm.Transaction() {

                    @Override
                    public void execute(@NonNull Realm realm) {
                        Random random = new Random();
                        final Item item = realm.copyToRealm(Item.create(random.nextInt()));
                        app.items.add(item);
                    }
                });
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        App app = getRealm().where(App.class).findFirst();
        ItemListAdapter mAdapter = new ItemListAdapter(app.items);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mRecyclerView.setAdapter(null);
    }
}