package com.wmt.raj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wmt.raj.modal.Modal;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private List<Modal> dataModel;
    private Context context;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView email;
        TextView username;
        ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            this.email =  view.findViewById(R.id.email);
            this.username =  view.findViewById(R.id.username);
            this.iv = view.findViewById(R.id.iv);

        }
    }
    public CustomAdapter(List<Modal> data, Context c) {
        this.dataModel = data;
        this.context = c;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);
        // view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        TextView text_title = holder.email;
        TextView text_desc = holder.username;
        ImageView iv = holder.iv;
        text_title.setText(dataModel.get(position).getEmail());
        text_desc.setText(dataModel.get(position).getUsername());

        Glide.with(context)
                .load(dataModel.get(position).getImg())
                .into(iv);
    }
    @Override
    public int getItemCount() {
        return dataModel.size();
    }
}
