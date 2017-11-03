package com.jpmcosta.test.realmtestproject.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jpmcosta.test.realmtestproject.R;
import com.jpmcosta.test.realmtestproject.realm.Item;

import io.realm.RealmChangeListener;
import io.realm.RealmList;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemHolder> {

    private static final String LOG_TAG = ItemListAdapter.class.getSimpleName();

    private RealmList<Item> mItems;

    private RealmChangeListener<RealmList<Item>> mItemsChangeListener =
            new RealmChangeListener<RealmList<Item>>() {

                @Override
                public void onChange(@NonNull RealmList<Item> items) {
                    Log.i(LOG_TAG, "onChange() called " + items.size());
                    notifyDataSetChanged();
                }
            };


    public ItemListAdapter(RealmList<Item> items) {
        mItems = items;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mItems.addChangeListener(mItemsChangeListener);
        Log.i(LOG_TAG, "add listener " + mItems.size());
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        Log.i(LOG_TAG, "remove listener");
        mItems.removeChangeListener(mItemsChangeListener);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.adapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Item item = mItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        public ItemHolder(View itemView) {
            super(itemView);
        }

        public void bind(Item item) {
            onBind(item.value);
        }

        private void onBind(int value) {
            ((TextView) itemView).setText("Item: " + value);
        }
    }
}
