package com.jpmcosta.test.realmtestproject.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jpmcosta.test.realmtestproject.R;
import com.jpmcosta.test.realmtestproject.adapter.ItemListAdapter;
import com.jpmcosta.test.realmtestproject.realm.App;
import com.jpmcosta.test.realmtestproject.realm.Item;
import com.jpmcosta.test.realmtestproject.realm.Session;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;

public class MainActivity extends RealmActivity {

    @BindView(R.id.session)
    protected TextView sessionTextView;

    @BindView(android.R.id.list)
    protected RecyclerView mRecyclerView;

    private App mApp;

    private Session mSession;

    private RealmChangeListener<App> mAppChangeListener =
            new RealmChangeListener<App>() {

                @Override
                public void onChange(final App app) {
                    getRealm().executeTransaction(new Realm.Transaction() {

                        @Override
                        public void execute(Realm realm) {
                            mSession.appVersion = app.version;
                        }
                    });
                }
            };

    private RealmChangeListener<Session> mSessionChangeListener =
            new RealmChangeListener<Session>() {

                @Override
                public void onChange(final Session session) {
                    getRealm().executeTransaction(new Realm.Transaction() {

                        @Override
                        public void execute(Realm realm) {
                            sessionTextView.setText("Session appVersion: " + session.appVersion);
                        }
                    });
                }
            };

    private ItemListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupContentView();
    }

    private void setupContentView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupListeners();

        setupRecyclerView();
    }

    private void setupListeners() {
        mApp = getRealm().where(App.class).findFirst();
        mSession = getRealm().where(Session.class).findFirst();
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

    @Override
    protected void onStart() {
        super.onStart();

        mApp.addChangeListener(mAppChangeListener);
        mSession.addChangeListener(mSessionChangeListener);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mApp.removeChangeListener(mAppChangeListener);
        mSession.removeChangeListener(mSessionChangeListener);
        mRecyclerView.setAdapter(null);
    }
}