package com.detatech.vitaluser.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.detatech.vitaluser.Activities.CancelRequestsDetailActivity;
import com.detatech.vitaluser.Models.AdminRequests;
import com.detatech.vitaluser.R;

import java.util.List;

/**
 * Created by Arbab on 8/8/2019.
 */

public class CancelRequestAdapter extends RecyclerView.Adapter<CancelRequestAdapter.AdminRequestViewHolder> {

    RequestOptions options;
    private List<AdminRequests> mAdminRequestData;
    private Context context;
    private LayoutInflater inflater;
    private Bundle mBundle;

    public CancelRequestAdapter(Context context, List<AdminRequests> feedsList) {

        this.context = context;
        this.mAdminRequestData = feedsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


//        options = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }


    @NonNull
    @Override
    public AdminRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.cancel_request_singleitem_recyclerview, parent, false);
        final AdminRequestViewHolder viewHolder = new AdminRequestViewHolder(view);

        viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CancelRequestsDetailActivity.class);
                // sending data process

                i.putExtra("medical_name", mAdminRequestData.get(viewHolder.getAdapterPosition()).getMedical_name());
                i.putExtra("medical_name_specialties", mAdminRequestData.get(viewHolder.getAdapterPosition()).getMedical_name_specialties());
//                i.putExtra("address", mAdminRequestData.get(viewHolder.getAdapterPosition()).getAddress());
                i.putExtra("price", mAdminRequestData.get(viewHolder.getAdapterPosition()).getPrice());
                i.putExtra("start_time", mAdminRequestData.get(viewHolder.getAdapterPosition()).getStart_time());
                i.putExtra("end_time", mAdminRequestData.get(viewHolder.getAdapterPosition()).getEnd_time());
                i.putExtra("request_admin_name", mAdminRequestData.get(viewHolder.getAdapterPosition()).getRequest_admin_name());
                i.putExtra("request_admin_phone", mAdminRequestData.get(viewHolder.getAdapterPosition()).getRequest_admin_phone());
                i.putExtra("request_id", mAdminRequestData.get(viewHolder.getAdapterPosition()).getRequest_id());
                i.putExtra("request_status", mAdminRequestData.get(viewHolder.getAdapterPosition()).getRequest_status());

                context.startActivity(i);

            }
        });

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final AdminRequestViewHolder holder, int position) {
        final AdminRequests feeds = mAdminRequestData.get(position);
        //Pass the values of feeds object to Views

        holder.medical_name.setText(feeds.getMedical_name());
        holder.medical_name_specialties.setText(feeds.getMedical_name_specialties());
//        holder.address.setText(feeds.getAddress());
        holder.price.setText(feeds.getPrice());
        holder.start_time.setText(feeds.getStart_time());
        holder.end_time.setText(feeds.getEnd_time());
        holder.request_admin_name.setText(feeds.getRequest_admin_name());
        holder.request_admin_phone.setText(feeds.getRequest_admin_phone());


//        holder.mLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent mIntent = new Intent(context, SponsorsDetailActivity.class);
//                mIntent.putExtra("title", holder.title.getText().toString());
//                mIntent.putExtra("content", holder.content.getText().toString());
////                Bundle extras = new Bundle();
////                mIntent.putExtra("imageview", );
////                holder.imageview.getDrawable();
////                mIntent.putExtra("imageview", (Parcelable) holder.imageview.getDrawable());
//                mIntent.putExtra("date", holder.date.getText().toString());
//               context.startActivity(mIntent);
//                fragmentJump(feeds);

//                AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                ShowRequestsDetailFragment myFragment = new ShowRequestsDetailFragment();
//                ShowRequestsDetailFragment myFragment = ShowRequestsDetailFragment.newInstance("fragment1");

        /*mBundle = new Bundle();
        mBundle.putString("medical_name", feeds.getMedical_name());
        mBundle.putString("medical_name_specialties", feeds.getMedical_name_specialties());
        mBundle.putString("address", feeds.getAddress());
        mBundle.putString("price", feeds.getPrice());
        mBundle.putString("start_time", feeds.getStart_time());
        mBundle.putString("end_time", feeds.getEnd_time());
        mBundle.putString("request_admin_name", feeds.getRequest_admin_name());
        mBundle.putString("request_admin_phone", feeds.getRequest_admin_phone());*/
//                myFragment.setArguments(mBundle);
//                fragmentManager.beginTransaction().replace(unique_id_here, fragment).commit();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.viewPager, myFragment).addToBackStack(null).commit();
//            }
//        });

    }

//    public void switchContent(int id, Fragment fragment) {
//        if (context == null)
//            return;
//        if (context instanceof MainActivity) {
//            MainActivity mainActivity = (MainActivity) context;
//            Fragment frag = fragment;
//            mainActivity.switchContent(id, frag);
//        }
//    }

//    private void fragmentJump(AdminRequests feeds) {
//        mFragment = new ShowRequestsDetailFragment();
//        mBundle = new Bundle();
//        mBundle.putString("medical_name", feeds.getMedical_name());
//        mBundle.putString("medical_name_specialties", feeds.getMedical_name_specialties());
//        mBundle.putString("address", feeds.getAddress());
//        mBundle.putString("price", feeds.getPrice());
//        mBundle.putString("start_time", feeds.getStart_time());
//        mBundle.putString("end_time", feeds.getEnd_time());
//        mBundle.putString("request_admin_name", feeds.getRequest_admin_name());
//        mBundle.putString("request_admin_phone", feeds.getRequest_admin_phone());
//        mFragment.setArguments(mBundle);
//        switchContent(R.id.viewPager, mFragment);
//    }

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
        return mAdminRequestData.size();
    }

    public class AdminRequestViewHolder extends RecyclerView.ViewHolder {

        TextView medical_name;
        TextView medical_name_specialties;
//        TextView address;
        TextView price;
        TextView start_time;
        TextView end_time;
        TextView request_admin_name;
        TextView request_admin_phone;
        RelativeLayout mLayout;
//        FrameLayout mLayout;

        public AdminRequestViewHolder(View itemView) {
            super(itemView);
//            mIcon = itemView.findViewById(R.id.sponsors_thumbnail);
//        mSender = itemView.findViewById(R.id.tvEmailSender);
            medical_name = itemView.findViewById(R.id.medical_name);
            medical_name_specialties = itemView.findViewById(R.id.medical_name_specialties);
//            address = itemView.findViewById(R.id.address);
            price = itemView.findViewById(R.id.price);
            start_time = itemView.findViewById(R.id.start_time);
            end_time = itemView.findViewById(R.id.end_time);
            request_admin_name = itemView.findViewById(R.id.request_admin_name);
            request_admin_phone = itemView.findViewById(R.id.request_admin_phone);
            mLayout = itemView.findViewById(R.id.sponsors_layout);


        }
    }

}
