package com.example.anshuman_hp.codeathon;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Anshuman-HP on 30-07-2017.
 */

public class recyclerHolder extends RecyclerView.ViewHolder {
    TextView message;
    public recyclerHolder(View itemView) {
        super(itemView);
        message=(TextView)itemView.findViewById(R.id.message);
    }
}
