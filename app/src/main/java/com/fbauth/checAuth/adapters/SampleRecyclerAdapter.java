package com.fbauth.checAuth.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fbauth.checAuth.R;
import com.fbauth.checAuth.fragment.AuthFragment;
import com.fbauth.checAuth.models.SampleModel;

import java.util.List;

/**
 * Created by labattula on 19/01/17.
 */

public class SampleRecyclerAdapter extends RecyclerView.Adapter<SampleRecyclerAdapter.sampleViewHolder> {

    private List<SampleModel> sampleModelList;

    private AuthFragment.recyclerItemClickListener listener;

    public void setSampleModelList(List<SampleModel> modelList, AuthFragment.recyclerItemClickListener listener) {
        this.sampleModelList = modelList;
        this.listener = listener;
    }

    @Override
    public sampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_layout, parent, false);
        return new sampleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(sampleViewHolder holder, int position) {
        final SampleModel model = sampleModelList.get(position);
        holder.sampleTextView.setText(model.getModelString());
        holder.sampleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.clickModel(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sampleModelList.size();
    }


    class sampleViewHolder extends RecyclerView.ViewHolder {

        public TextView sampleTextView;

        public sampleViewHolder(View itemView) {
            super(itemView);

            sampleTextView = (TextView) itemView.findViewById(R.id.sampleTxt);
        }
    }
}
