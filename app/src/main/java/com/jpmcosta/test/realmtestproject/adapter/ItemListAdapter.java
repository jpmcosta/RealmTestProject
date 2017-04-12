package com.jpmcosta.test.realmtestproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jpmcosta.test.realmtestproject.R;
import com.jpmcosta.test.realmtestproject.realm.Item;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemHolder> {

    private static String LOG_TAG = ItemListAdapter.class.getSimpleName();

    private OnItemClickListener mOnItemClickListener;

    private RealmResults<Item> mItems;


    public ItemListAdapter(RealmResults<Item> items) {
        mItems = items;
        mItems.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<Item>>() {

            @Override
            public void onChange(RealmResults<Item> collection, OrderedCollectionChangeSet changeSet) {
                if (changeSet == null) {
                    notifyDataSetChanged();
                    return;
                }

                Log.i(LOG_TAG, "start log ------------------------------------");

                // For deletions, the adapter has to be notified in reverse order.
                OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
                for (int i = deletions.length - 1; i >= 0; i--) {
                    OrderedCollectionChangeSet.Range range = deletions[i];
                    Log.i(LOG_TAG, "deletion: " + range.startIndex + ", " + range.length);
                    notifyItemRangeRemoved(range.startIndex, range.length);
                }

                OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
                for (OrderedCollectionChangeSet.Range range : insertions) {
                    Log.i(LOG_TAG, "insertion: " + range.startIndex + ", " + range.length);
                    notifyItemRangeInserted(range.startIndex, range.length);
                }

                OrderedCollectionChangeSet.Range[] changes = changeSet.getChangeRanges();
                for (OrderedCollectionChangeSet.Range range : changes) {
                    Log.i(LOG_TAG, "change: " + range.startIndex + ", " + range.length);
                    notifyItemRangeChanged(range.startIndex, range.length);
                }

                Log.i(LOG_TAG, "stop log ------------------------------------");
            }
        });
    }


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false));
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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Item mItem;


        public ItemHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(Item item) {
            mItem = item;

            onBind(item.name);
        }

        public void onBind(String name) {
            ((TextView) itemView).setText(name);
        }

        public void unbind() {
            mItem = null;
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(mItem);
            }
        }
    }

    public interface OnItemClickListener {

        void onItemClick(Item item);
    }
}
