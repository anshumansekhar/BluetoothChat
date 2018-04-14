package com.example.anshuman_hp.codeathon;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Anshuman-HP on 30-07-2017.
 */

public class recyclerAdapter extends RecyclerView.Adapter<recyclerHolder> {
    public recyclerAdapter() {
    }

    @Override
    public recyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("d","OnCreateViewHolder");
        return new recyclerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message,parent,false));
    }

    @Override
    public void onBindViewHolder(recyclerHolder holder, int position) {
        holder.message.setText(ChatService.message.get(position));
        Log.e("d","OnBindViewHolder");
//        holder.message.setText("hii");
    }

    @Override
    public int getItemCount() {
        return ChatService.message.size();
//        return 10;
    }
}
