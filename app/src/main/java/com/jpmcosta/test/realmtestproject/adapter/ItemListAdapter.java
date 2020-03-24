package com.jpmcosta.test.realmtestproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jpmcosta.test.realmtestproject.R;
import com.jpmcosta.test.realmtestproject.realm.obj.Item;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemHolder> {

    private RealmResults<Item> mItems;
    private RealmChangeListener<RealmResults<Item>> mItemsChangeListener =
            new RealmChangeListener<RealmResults<Item>>() {

                @Override
                public void onChange(@NonNull RealmResults<Item> items) {
                    notifyDataSetChanged();
                }
            };
    private OnItemClickListener mOnItemClickListener;

    public ItemListAdapter(RealmResults<Item> items) {
        mItems = items;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mItems.addChangeListener(mItemsChangeListener);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

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
    public void onViewRecycled(ItemHolder holder) {
        super.onViewRecycled(holder);
        holder.unbind();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    public interface OnItemClickListener {

        void onItemClick(Item item);
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        private Item mItem;


        ItemHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mItem);
                    }
                }
            });
        }

        void bind(Item item) {
            mItem = item;
            final String name = "Item$" + item.id;
            final boolean isBookmarked = item.isBookmarked;
            onBind(name, isBookmarked);
        }

        void unbind() {
            mItem = null;
        }

        private void onBind(String name, boolean isBookmarked) {
            final String text = name + (isBookmarked ? " - bookmarked" : "");
            ((TextView) itemView).setText(text);
        }
    }
}