package com.fbauth.checAuth.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fbauth.checAuth.R;
import com.fbauth.checAuth.models.SampleModelHistory;

import java.util.ArrayList;

/**
 * Created by labattula on 03/02/17.
 */

public class SDetailsHistoryAdapter extends RecyclerView.Adapter<SDetailsHistoryAdapter.SDViewHolder> {

    ArrayList<SampleModelHistory> historyArrayList = new ArrayList<>();

    public SDetailsHistoryAdapter() {

    }

    public void setHistoryArrayList(ArrayList<SampleModelHistory> list) {
        this.historyArrayList = list;
    }

    @Override
    public SDViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_history_item, parent, false);
        return new SDViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SDViewHolder holder, int position) {
        if (historyArrayList != null && historyArrayList.size() > 0) {
            SampleModelHistory history = historyArrayList.get(position);
            bindView(holder, history);
        }
    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }

    private void bindView(SDViewHolder holder, SampleModelHistory history) {
        if (history != null) {
            if (history.getUser() != null) {
                holder.userView.setText(history.getUser());
            }
            if (history.getStarttime() != null) {
                holder.startView.setText(history.getStarttime());
            }
            if (history.getEndtime() != null) {
                holder.endView.setText(history.getEndtime());
            }
        }
    }

    class SDViewHolder extends RecyclerView.ViewHolder {

        TextView userView;
        TextView startView;
        TextView endView;
        ImageView userImg;

        public SDViewHolder(View itemView) {
            super(itemView);
            userView = (TextView) itemView.findViewById(R.id.history_user);
            startView = (TextView) itemView.findViewById(R.id.history_start);
            endView = (TextView) itemView.findViewById(R.id.history_end);
            userImg = (ImageView) itemView.findViewById(R.id.history_user_img);
        }
    }
}
