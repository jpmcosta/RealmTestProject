package com.jpmcosta.test.realmtestproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jpmcosta.test.realmtestproject.R;
import com.jpmcosta.test.realmtestproject.realm.Filter;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class FilterListAdapter extends RecyclerView.Adapter<FilterListAdapter.FilterHolder> {

    private RealmResults<Filter> mFilters;

    private OnFilterClickListener mOnFilterClickListener;


    public FilterListAdapter(RealmResults<Filter> filters) {
        mFilters = filters;
        mFilters.addChangeListener(new RealmChangeListener<RealmResults<Filter>>() {

            @Override
            public void onChange(RealmResults<Filter> filters) {
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(FilterHolder holder, int position) {
        Filter filter = mFilters.get(position);
        holder.bind(filter);
    }

    @Override
    public void onViewRecycled(FilterHolder holder) {
        holder.unbind();
    }

    @Override
    public int getItemCount() {
        return mFilters.size();
    }

    public void setOnClickListenerE(OnFilterClickListener listener) {
        mOnFilterClickListener = listener;
    }


    public class FilterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Filter filter;


        public FilterHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(Filter filter) {
            this.filter = filter;
            onBind(filter.id, filter.isEnabled);
        }

        public void onBind(long id, boolean isEnabled) {
            String name = "Filter " + id + (isEnabled ? " enabled!" : " disabled");
            ((TextView) itemView).setText(name);
        }

        public void unbind() {
            filter = null;
        }

        @Override
        public void onClick(View v) {
            if (mOnFilterClickListener != null) {
                mOnFilterClickListener.onFilterClick(filter);
            }
        }
    }


    public interface OnFilterClickListener {

        void onFilterClick(Filter filter);
    }
}
