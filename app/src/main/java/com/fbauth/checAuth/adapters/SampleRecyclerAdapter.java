package com.fbauth.checAuth.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fbauth.checAuth.R;
import com.fbauth.checAuth.fragment.AuthFragment;
import com.fbauth.checAuth.models.DeviceModel;

import java.util.List;

/**
 * Created by labattula on 19/01/17.
 */

public class SampleRecyclerAdapter extends RecyclerView.Adapter<SampleRecyclerAdapter.sampleViewHolder> {

    private List<DeviceModel> deviceModelList;

    private AuthFragment.recyclerItemClickListener listener;

    public void setSampleModelList(List<DeviceModel> modelList, AuthFragment.recyclerItemClickListener listener) {
        this.deviceModelList = modelList;
        this.listener = listener;
    }

    @Override
    public sampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_layout, parent, false);
        return new sampleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(sampleViewHolder holder, int position) {
        final DeviceModel model = deviceModelList.get(position);
        holder.sampleTextView.setText(model.getModelString());

        int cal = position % 5;
        switch (cal) {
            case 0:
                holder.sampleImageView.setImageResource(R.drawable.sea);
                break;
            case 1:
                holder.sampleImageView.setImageResource(R.drawable.sky);
                break;
            case 2:
                holder.sampleImageView.setImageResource(R.drawable.phone);
                break;
            case 3:
                holder.sampleImageView.setImageResource(R.drawable.sea_shore);
                break;
            case 4:
                holder.sampleImageView.setImageResource(R.drawable.motel);
                break;
        }

        holder.sampleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.clickModel(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceModelList.size();
    }


    class sampleViewHolder extends RecyclerView.ViewHolder {

        TextView sampleTextView;
        ImageView sampleImageView;

        sampleViewHolder(View itemView) {
            super(itemView);

            sampleTextView = (TextView) itemView.findViewById(R.id.sampleTxt);
            sampleImageView = (ImageView) itemView.findViewById(R.id.sampleImg);
        }
    }
}
