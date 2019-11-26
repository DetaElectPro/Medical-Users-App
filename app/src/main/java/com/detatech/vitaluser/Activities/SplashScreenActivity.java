package com.detatech.vitaluser.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.TrailingCircularDotsLoader;
import com.detatech.vitaluser.R;
import com.detatech.vitaluser.Utils.ConnectionHelper;
import com.detatech.vitaluser.Utils.SharedPreferenceUtil;

public class SplashScreenActivity extends AppCompatActivity {
    public Activity activity = SplashScreenActivity.this;
    public Context context = SplashScreenActivity.this;

    ConnectionHelper helper;
    Boolean isInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    if (helper.isConnectingToInternet()) {
                        sleep(2 * 1000);
                        loader_animation();
                        if (!SharedPreferenceUtil.getStringValue(context, "loggedIn").equalsIgnoreCase(getString(R.string.True))) {
                            sleep(1 * 1000);
                            loader_animation();
                            GoToLogin1Activity();
//                        startActivity(new Intent(SplashScreenActivity.this, Login1Activity.class));
                        } else {
                            sleep(1 * 1000);
                            loader_animation();
                            GoToLogin2Activity();
//                        startActivity(new Intent(SplashScreenActivity.this, Login2Activity.class));
                        }
                        finish();
                    }
                } catch (Exception e) {

                }
            }
        };
        thread.start();
    }

    public void loader_animation() {
        TrailingCircularDotsLoader trailingCircularDotsLoader = new TrailingCircularDotsLoader(
                this,
                8,
                ContextCompat.getColor(this, android.R.color.holo_green_light),
                100,
                5);
        trailingCircularDotsLoader.setAnimDuration(900);
        trailingCircularDotsLoader.setAnimDelay(100);


    }

    public void GoToLogin2Activity() {
        Intent mainIntent = new Intent(activity, Login2Activity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }

    public void GoToLogin1Activity() {
        Intent mainIntent = new Intent(activity, Login1Activity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }

    public void displayMessage(String toastString) {
        Log.e("displayMessage", "" + toastString);
        Toast.makeText(activity, toastString, Toast.LENGTH_SHORT).show();
    }

}
