package com.detatech.vitaluser.Activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.detatech.vitaluser.R;

public class Main2Activity extends AppCompatActivity {
//implements View.OnClickListener
    MaterialRippleLayout mrl_requests, mrl_icu, mrl_ambulance, mrl_profile;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mrl_requests = (MaterialRippleLayout) findViewById(R.id.mrl_requests);
        mrl_icu = (MaterialRippleLayout) findViewById(R.id.mrl_icu);
        mrl_ambulance = (MaterialRippleLayout) findViewById(R.id.mrl_ambulance);
        mrl_profile = (MaterialRippleLayout) findViewById(R.id.mrl_profile);
        imageView = (ImageView) findViewById(R.id.logo);

//        Glide.with(this).load(getImage(logo)).into(imageView);
        Glide.with(this).load(R.drawable.new_logo).into(imageView);


        mrl_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent requests = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(requests);
            }
        });

        mrl_icu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent icu = new Intent(Main2Activity.this, EmergencyServicesActivity.class);
                startActivity(icu);
            }
        });

        mrl_ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ambulance = new Intent(Main2Activity.this, AmbulanceActivity.class);
                startActivity(ambulance);
            }
        });

        mrl_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(Main2Activity.this, ProfileActivity.class);
                startActivity(profile);
            }
        });

    }

    public int getImage(String imageName) {

        return this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.mrl_requests:
//                Intent requests = new Intent(Main2Activity.this, MainActivity.class);
//                startActivity(requests);
//                break;
//
//            case R.id.mrl_icu:
//                Intent icu = new Intent(Main2Activity.this, EmergencyServicesActivity.class);
//                startActivity(icu);
//                break;
//
//            case R.id.mrl_ambulance:
//                Intent ambulance = new Intent(Main2Activity.this, AmbulanceActivity.class);
//                startActivity(ambulance);
//                break;
//
//            case R.id.mrl_profile:
//                Intent profile = new Intent(Main2Activity.this, ProfileActivity.class);
//                startActivity(profile);
//                break;
//
//        }
//    }
}
