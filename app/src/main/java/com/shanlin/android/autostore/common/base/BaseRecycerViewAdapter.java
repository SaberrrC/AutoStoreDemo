package com.shanlin.android.autostore.common.base;

/**
 * Created by cuieney on 16/3/9.
 */

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseRecycerViewAdapter<T,V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {
    protected OnItemClickListener mClickListener;
    public static Context context;
    public List<T> list;
    public LayoutInflater inflater;

    public BaseRecycerViewAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        this.addAll(list);
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {
        return getCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(V holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(position,view,holder);
                }
            }
        });
        getBindViewHolder(holder, position);
    }

    public abstract V getCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void getBindViewHolder(V holder, int position);

    public void addAll(Collection<? extends T> collection) {
        addAll(list.size(), collection);
    }

    public void addAll(int position, Collection<? extends T> collection) {
        list.addAll(position, collection);
        notifyItemRangeInserted(position, collection.size());
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    protected View getItemView(@LayoutRes int layoutResId, ViewGroup parent) {
        return inflater.inflate(layoutResId, parent, false);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view, RecyclerView.ViewHolder vh);
    }


    public static class BaseHolder extends RecyclerView.ViewHolder{
        private final SparseArray<View> views;

        public BaseHolder(View itemView) {
            super(itemView);
            this.views = new SparseArray<>();
        }

        public <T extends View> T getView(@IdRes int viewId) {
            View view = views.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                views.put(viewId, view);
            }
            return (T) view;
        }

        public BaseHolder setText(@IdRes int viewId, CharSequence value) {
            TextView view = getView(viewId);
            view.setText(value);
            return this;
        }

    }
}


