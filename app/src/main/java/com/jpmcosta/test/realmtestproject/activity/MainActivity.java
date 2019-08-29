package com.jpmcosta.test.realmtestproject.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jpmcosta.test.realmtestproject.R;
import com.jpmcosta.test.realmtestproject.adapter.ItemListAdapter;
import com.jpmcosta.test.realmtestproject.realm.obj.Item;
import com.jpmcosta.test.realmtestproject.realm.obj.ItemPage;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.jpmcosta.test.realmtestproject.util.Const.NO_TIME;

public class MainActivity extends RealmActivity {

    @BindView(android.R.id.list)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.result)
    protected TextView mResultTextView;

    @BindView(R.id.fab_move)
    protected FloatingActionButton mFabMove;

    @BindView(R.id.fab_query)
    protected FloatingActionButton mFabQuery;

    private ItemListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupContentView();
    }

    private void setupContentView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupRecyclerView();

        setupFabMove();

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
                        // Do nothing.
                    }
                });
            }
        });
    }

    private void setupFabMove() {
        mFabMove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Realm realm = getRealm();
                realm.executeTransactionAsync(new Realm.Transaction() {

                    @SuppressWarnings("ConstantConditions")
                    @Override
                    public void execute(Realm realm) {
                        postText(mResultTextView, "Move transaction has started");

                        final Item item = realm.where(Item.class).contains("subItem.name", "A").findAll().last();
                        if (item == null) return;

                        final RealmResults<ItemPage> itemPages = item.itemPages;

                        ItemPage previousItemPage = null;
                        if (itemPages != null && !itemPages.isEmpty()) {
                            previousItemPage = itemPages.first();
                            for (ItemPage itemPage : itemPages) {
                                itemPage.items.remove(item);
                            }
                        }

                        ItemPage newItemPage = null;
                        if (previousItemPage != null) {
                            newItemPage = realm.where(ItemPage.class)
                                    .equalTo("id", previousItemPage.id + 1)
                                    .findFirst();
                        }
                        if (newItemPage == null) {
                            newItemPage = realm.where(ItemPage.class).sort("id").findFirst();
                        }

                        if (newItemPage != null) {
                            newItemPage.items.add(newItemPage.items.size() / 2, item);
                            postText(mResultTextView, "Last Item with 'A' moved to ItemPage with id " + newItemPage.id);
                        } else {
                            postText(mResultTextView, "No items pages available");
                        }
                    }
                });
            }
        });
    }

    private void setupFabQuery() {
        mFabQuery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Realm realm = getRealm();
                realm.executeTransactionAsync(new Realm.Transaction() {

                    @Override
                    public void execute(Realm realm) {
                        postText(mResultTextView, "Query transaction has started");

                        final ArrayList<Long> elapsedTimes = new ArrayList<>();
                        final RealmResults<ItemPage> itemPages = realm.where(ItemPage.class).sort("id").findAll();
                        for (ItemPage itemPage : itemPages) {
                            final RealmQuery<Item> query = itemPage.items.where()
                                    .contains("subItem.name", "A")
                                    .equalTo("removedAt", NO_TIME);
                            final long startTime = SystemClock.elapsedRealtime();
                            final RealmResults<Item> items = query.findAll();
                            final long elapsedTime = SystemClock.elapsedRealtime() - startTime;
                            elapsedTimes.add(elapsedTime);

                            for (Item item : items) {
                                item.isBookmarked = !item.isBookmarked;
                            }
                        }

                        postText(mResultTextView, "findAll (ms): " + elapsedTimes.toString());
                    }
                });
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

    private void postText(@Nonnull final TextView textView, @Nonnull final String text) {
        textView.post(new Runnable() {

            @Override
            public void run() {
                textView.setText(text);
            }
        });
    }
}