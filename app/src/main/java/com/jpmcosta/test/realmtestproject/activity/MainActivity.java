package com.jpmcosta.test.realmtestproject.activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jpmcosta.test.realmtestproject.R;
import com.jpmcosta.test.realmtestproject.adapter.AppListAdapter;
import com.jpmcosta.test.realmtestproject.realm.App;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;

public class MainActivity extends RealmActivity {

    private static final int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.BLACK, Color.YELLOW};

    @BindView(android.R.id.list)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.fab)
    protected FloatingActionButton mFab;

    private App mApp;

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
                int color = colors[mRandom.nextInt(colors.length)];
                realm.copyToRealm(App.create(0L, "My awesome app", color));
            }
        });

        mApp = getRealm().where(App.class).findFirst();
        mApp.addChangeListener(new RealmChangeListener<RealmModel>() {

            @Override
            public void onChange(RealmModel element) {
                updateFabColor();
            }
        });
    }

    private void setupContentView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupRecyclerView();

        setupFab();

        updateFabColor();
    }

    private void setupRecyclerView() {
        AppListAdapter appListAdapter = new AppListAdapter(getRealm().where(App.class).findAll());
        appListAdapter.setOnAppClickListener(new AppListAdapter.OnAppClickListener() {

            @Override
            public void onAppClick(final App app) {
                getRealm().executeTransaction(new Realm.Transaction() {

                    @Override
                    public void execute(Realm realm) {
                        app.color.value = colors[mRandom.nextInt(colors.length)];
                    }
                });
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(appListAdapter);
    }

    private void setupFab() {
        mFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateFabColor();
            }
        });
    }

    private void updateFabColor() {
        mFab.setBackgroundTintList(ColorStateList.valueOf(mApp.color.value));
    }
}