package com.detatech.vitaluser.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.detatech.vitaluser.Activities.AmbulanceActivity;
import com.detatech.vitaluser.Activities.EmergencyServicesActivity;
import com.detatech.vitaluser.Activities.MainActivity;
import com.detatech.vitaluser.Activities.ProfileActivity;
import com.detatech.vitaluser.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Arbab on 8/12/2019.
 */

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(true);
        bottomNavigationViewEx.enableShiftingMode(true);
        bottomNavigationViewEx.setTextVisibility(true);
    }

    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationViewEx view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.request_doctor:
                        Intent intent1 = new Intent(context, MainActivity.class);//ACTIVITY_NUM = 0
                        context.startActivity(intent1);
                        callingActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        break;

                    case R.id.icu:
                        Intent intent2 = new Intent(context, EmergencyServicesActivity.class);//ACTIVITY_NUM = 1
                        context.startActivity(intent2);
                        callingActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        break;

                    case R.id.ambulance:
                        Intent intent3 = new Intent(context, AmbulanceActivity.class);//ACTIVITY_NUM = 2
                        context.startActivity(intent3);
                        callingActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        break;

                    case R.id.profile:
                        Intent intent4 = new Intent(context, ProfileActivity.class);//ACTIVITY_NUM = 2
                        context.startActivity(intent4);
                        callingActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        break;

                }

                return false;
            }
        });
    }
}
