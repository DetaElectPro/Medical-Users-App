package com.detatech.vitaluser.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.detatech.vitaluser.Activities.AcceptEmergencyDetailActivity;
import com.detatech.vitaluser.Models.Emergency2;
import com.detatech.vitaluser.R;

import java.util.List;

/**
 * Created by Arbab on 8/24/2019.
 */

public class EmergencyAcceptAdapter extends RecyclerView.Adapter<EmergencyAcceptAdapter.EmergencyRequestViewHolder> {

    RequestOptions options;
    private List<Emergency2> mEmergencyRequestData;
    private Context context;
    private LayoutInflater inflater;
    private Bundle mBundle;

    public EmergencyAcceptAdapter(Context context, List<Emergency2> feedsList) {

        this.context = context;
        this.mEmergencyRequestData = feedsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        options = new RequestOptions().centerCrop().placeholder(R.drawable.loading).error(R.drawable.loading);

    }


    @NonNull
    @Override
    public EmergencyAcceptAdapter.EmergencyRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.emergency_accept_singleitem_recyclerview, parent, false);
        final EmergencyAcceptAdapter.EmergencyRequestViewHolder viewHolder = new EmergencyAcceptAdapter.EmergencyRequestViewHolder(view);

        viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AcceptEmergencyDetailActivity.class);
                // sending data process

                i.putExtra("hospital_name", mEmergencyRequestData.get(viewHolder.getAdapterPosition()).getHospital_name());
                i.putExtra("price", mEmergencyRequestData.get(viewHolder.getAdapterPosition()).getPrice());
                i.putExtra("type", mEmergencyRequestData.get(viewHolder.getAdapterPosition()).getType());
                i.putExtra("address", mEmergencyRequestData.get(viewHolder.getAdapterPosition()).getAddress());
                i.putExtra("available", mEmergencyRequestData.get(viewHolder.getAdapterPosition()).getAvailable());


                i.putExtra("needing", mEmergencyRequestData.get(viewHolder.getAdapterPosition()).getNeeding());

                i.putExtra("id", mEmergencyRequestData.get(viewHolder.getAdapterPosition()).getId());

                i.putExtra("report", mEmergencyRequestData.get(viewHolder.getAdapterPosition()).getReport());
                i.putExtra("image", mEmergencyRequestData.get(viewHolder.getAdapterPosition()).getImage());

                context.startActivity(i);

            }
        });

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final EmergencyAcceptAdapter.EmergencyRequestViewHolder holder, int position) {
        final Emergency2 feeds = mEmergencyRequestData.get(position);
        //Pass the values of feeds object to Views

        holder.hospital_name.setText(feeds.getHospital_name());
        holder.type.setText(feeds.getType());
        holder.address.setText(feeds.getAddress());
        holder.price.setText(feeds.getPrice());
        holder.available.setText(feeds.getAvailable());

        holder.needing.setText(feeds.getNeeding());

        holder.report.setText(feeds.getAvailable());

        Glide.with(context).load(feeds.getImage()).apply(options).into(holder.image);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mEmergencyRequestData.size();
    }

    public class EmergencyRequestViewHolder extends RecyclerView.ViewHolder {

        TextView hospital_name;
        TextView type;
        TextView address;
        TextView price;
        TextView available;
        TextView needing;

        TextView report;
        ImageView image;

        RelativeLayout mLayout;

        public EmergencyRequestViewHolder(View itemView) {
            super(itemView);

            hospital_name = itemView.findViewById(R.id.hospital_name);
            type = itemView.findViewById(R.id.type);
            address = itemView.findViewById(R.id.address);
            price = itemView.findViewById(R.id.price);
            available = itemView.findViewById(R.id.available);

            report = itemView.findViewById(R.id.report);
            image = itemView.findViewById(R.id.image);
            needing = itemView.findViewById(R.id.needing);

            mLayout = itemView.findViewById(R.id.sponsors_layout);

        }
    }
}
