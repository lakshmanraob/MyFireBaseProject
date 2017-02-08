package com.fbauth.checAuth.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fbauth.checAuth.R;
import com.fbauth.checAuth.models.DeviceUsageHistory;
import com.fbauth.checAuth.models.Profile;
import com.fbauth.checAuth.utils.UtilsDate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by labattula on 03/02/17.
 */

public class SDetailsHistoryAdapter extends RecyclerView.Adapter<SDetailsHistoryAdapter.SDViewHolder> {

    ArrayList<DeviceUsageHistory> historyArrayList = new ArrayList<>();

    public SDetailsHistoryAdapter() {

    }

    public void setHistoryArrayList(ArrayList<DeviceUsageHistory> list) {
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
            DeviceUsageHistory history = historyArrayList.get(position);
            bindView(holder, history);
        }
    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }

    private void bindView(final SDViewHolder holder, final DeviceUsageHistory history) {
        if (history != null) {
            if (history.getProfileId() != null) {
                //holder.userView.setText(history.getProfileId());
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(history.getProfileId());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, Object> profileMap = (HashMap<String, Object>) dataSnapshot.getValue();
                        Profile profile = new Profile((HashMap<String, String>) profileMap.get("profile"));
                        holder.userView.setText(profile.getUserName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if (history.getStartTime() != null) {
                String startDate = UtilsDate.getDate(history.getStartTime());
                holder.startView.setText(startDate);
            }
            if (history.getEndTime() != null) {
                String endDate = UtilsDate.getDate(history.getEndTime());
                holder.endView.setText(endDate);
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
