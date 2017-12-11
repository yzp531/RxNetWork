package com.rxnetwork.sample;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * by y on 2017/2/27
 */

class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainAdapterHolder> {

    private List<ListModel> listModels = null;

    MainAdapter(List<ListModel> listModels) {
        this.listModels = listModels;
    }


    @Override
    public MainAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainAdapterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(MainAdapterHolder holder, int position) {
        if (listModels == null) {
            return;
        }
        Glide
                .with(holder.imageView.getContext())
                .load(listModels.get(position).getTitleImage())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .into(holder.imageView);
        holder.textView.setText(listModels.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return listModels == null ? 0 : listModels.size();
    }

    void addAll(List<ListModel> data) {
        if (listModels != null) {
            listModels.addAll(data);
            notifyDataSetChanged();
        }
    }

    void clear() {
        if (listModels != null) {
            listModels.clear();
            notifyDataSetChanged();
        }
    }

    class MainAdapterHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView imageView;
        private AppCompatTextView textView;

        MainAdapterHolder(View itemView) {
            super(itemView);
            imageView = (AppCompatImageView) itemView.findViewById(R.id.list_image);
            textView = (AppCompatTextView) itemView.findViewById(R.id.list_tv);
        }
    }
}
