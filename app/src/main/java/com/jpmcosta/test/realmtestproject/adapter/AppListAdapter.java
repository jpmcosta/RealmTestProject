package com.jpmcosta.test.realmtestproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jpmcosta.test.realmtestproject.R;
import com.jpmcosta.test.realmtestproject.realm.App;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppHolder> {

    private RealmResults<App> mApps;

    private OnAppClickListener mOnAppClickListener;


    public AppListAdapter(RealmResults<App> apps) {
        mApps = apps;
        mApps.addChangeListener(new RealmChangeListener<RealmResults<App>>() {

            @Override
            public void onChange(RealmResults<App> apps) {
                notifyDataSetChanged();
            }
        });
    }


    public void setOnAppClickListener(OnAppClickListener listener) {
        mOnAppClickListener = listener;
    }

    @Override
    public AppHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_app, parent, false));
    }

    @Override
    public void onBindViewHolder(AppHolder holder, int position) {
        App app = mApps.get(position);
        holder.bind(app);
    }

    @Override
    public void onViewRecycled(AppHolder holder) {
        holder.unbind();
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }


    public class AppHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private App mApp;


        public AppHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(App app) {
            mApp = app;

            onBind(app.name, app.color.value);
        }

        public void unbind() {
            mApp = null;
        }

        protected void onBind(String name, int color) {
            ((TextView) itemView).setText(name);
            ((TextView) itemView).setTextColor(color);
        }

        @Override
        public void onClick(View v) {
            if (mOnAppClickListener != null && mApp != null) {
                mOnAppClickListener.onAppClick(mApp);
            }
        }
    }


    public interface OnAppClickListener {

        void onAppClick(App app);
    }
}
