package com.detatech.vitaluser.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.detatech.vitaluser.Adapters.Tab2Adapter;
import com.detatech.vitaluser.Fragment.ActiveEmergencyFragment;
import com.detatech.vitaluser.Fragment.ShowEmergencyFragment;
import com.detatech.vitaluser.Helpers.BottomNavigationViewHelper;
import com.detatech.vitaluser.R;
import com.detatech.vitaluser.Utils.CustomDialog;
import com.detatech.vitaluser.Utils.Utilities;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class EmergencyServicesActivity extends AppCompatActivity {

    public Context context = EmergencyServicesActivity.this;
    public Activity activity = EmergencyServicesActivity.this;

    private static final String TAG = "EmergencyActivity";
    private static final int ACTIVITY_NUM = 1;
    private BottomNavigationView bottomNavigationView;

    private Spinner typeSpinner;
    private Button request;
    private EditText ed_hospital,ed_price, ed_address, available_bed;

    CustomDialog customDialog;
    Boolean isInternet;
    Utilities utils = new Utilities();

    private String token;

    private Context mContext = EmergencyServicesActivity.this;

    private Tab2Adapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.request_doctor2,
            R.drawable.icu
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_services);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        // Hide ActionBar
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }

//        setupBottomNavigationView();

        adapter = new Tab2Adapter(getSupportFragmentManager(), this);
//        adapter.addFragment(new RequestEmergencyFragment(), "Request");
        adapter.addFragment(new ShowEmergencyFragment(), "Show");
        adapter.addFragment(new ActiveEmergencyFragment(), "Active");
//        adapter.addFragment(new ShowMyEmergencyRequestsFragment(), "My Requests");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        highLightCurrentTab(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private void highLightCurrentTab(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(i));
        }
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(adapter.getSelectedTabView(position));
    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(context, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
