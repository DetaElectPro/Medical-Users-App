package com.detatech.vitaluser.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//import com.android.volley.AuthFailureError;
//import com.android.volley.NetworkError;
//import com.android.volley.NetworkResponse;
//import com.android.volley.NoConnectionError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.TimeoutError;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import com.bumptech.glide.Glide;
import com.detatech.vitaluser.R;
import com.detatech.vitaluser.Utils.ConnectionHelper;
import com.detatech.vitaluser.Utils.InternetConnection;
import com.detatech.vitaluser.Utils.SharedPreferenceUtil;
import com.detatech.vitaluser.Utils.URLHelper;
import com.detatech.vitaluser.Utils.Utilities;
import com.detatech.vitaluser.Utils.XuberApplication;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Login1Activity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;
    private View view;

    public Context context = Login1Activity.this;
    public Activity activity = Login1Activity.this;

    ConnectionHelper helper;
//    CustomDialog customDialog;

    SweetAlertDialog pDialog;

    Boolean isInternet;
    Utilities utils = new Utilities();

    private String token;

    Button login, register;
    EditText phone, password;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);

        // Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        findViewById();

        if (!checkPermission()) {

            requestPermission();

        }

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        isInternet = helper.isConnectingToInternet();

//        isInternet = helper.isConnectingToInternet();
        token = SharedPreferenceUtil.getStringValue(context, "access_token");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (phone.getText().toString().equals("") || phone.getText().toString().equalsIgnoreCase(getString(R.string.phone_number))) {
                    displayMessage(getString(R.string.mobile_number_empty));
                } else if (phone.length() < 10 || phone.length() > 14) {
                    displayMessage(getString(R.string.mobile_number_validation));
                } else if (password.getText().toString().equals("") || password.getText().toString().equalsIgnoreCase(getString(R.string.password_txt))) {
                    displayMessage(getString(R.string.password_validation));
                } else if (password.length() < 6) {
                    displayMessage(getString(R.string.password_size));
                } else {
                    if (new InternetConnection().isInternetOn(Login1Activity.this)) {
                        login();
                    } else {
                        displayMessage(getString(R.string.something_went_wrong_net));
                    }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(activity, RegistrationActivity.class);
                startActivity(registerIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        Glide.with(this).load(R.drawable.new_logo).into(imageView);

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && readAccepted && writeAccepted)
                        Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    else {

                        Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Login1Activity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private void findViewById() {

        phone = (EditText) findViewById(R.id.login_phone);
        password = (EditText) findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.login_loginBtn);
        register = (Button) findViewById(R.id.login_registerBtn);
        imageView = (ImageView) findViewById(R.id.logo);
    }

    private void login() {
        if (new InternetConnection().isInternetOn(Login1Activity.this)) {
//            customDialog = new CustomDialog(activity);
//            customDialog.setCancelable(false);
//            customDialog.show();
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("phone", phone.getText().toString());
                object.put("password", password.getText().toString());

                utils.print("Login Request", "" + object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.login, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pDialog.dismiss();
                    utils.print("Login Response", response.toString());
                    try {
                        SharedPreferenceUtil.storeStringValue(context, "access_token", response.optString("access_token"));
                        JSONObject user = response.getJSONObject("user");
                        SharedPreferenceUtil.storeStringValue(context, "user_id", user.optString("id"));

                        if (user.getInt("status") == 1) {
                            goToLogin2Activity();
                        } else if (user.getInt("status") == 2) {
                            goToLogin3Activity();
                        } else if (user.getInt("status") == 3) {
                            goToMainActivity();
                        } else if (response.getString("error").equals(true)) {
//                        if (response.getJSONObject("error").equals(true)) {
                            Toast.makeText(Login1Activity.this, "Phone or Password is incorrect", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    String json = null;
                    String Message;
                    NetworkResponse response = error.networkResponse;
                    utils.print("MyTest", "" + error);
                    utils.print("MyTestError", "" + error.networkResponse);

                    if (error instanceof NoConnectionError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof NetworkError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof TimeoutError) {
                        login();
                    }

                }
            });
//            {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap<String, String> headers = new HashMap<String, String>();
//                    headers.put("X-Requested-With", "XMLHttpRequest");
//                    headers.put("Authorization", "Bearer " + token);
//                    return headers;
//                }
//            };

            XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        }

    }

    public void goToLogin2Activity() {
        Intent mainIntent = new Intent(activity, Login2Activity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
//        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        activity.finish();
    }

    public void goToLogin3Activity() {
        Intent mainIntent = new Intent(activity, Login3Activity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        activity.finish();
    }

    public void goToMainActivity() {
        Intent mainIntent = new Intent(activity, Main2Activity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        activity.finish();
    }

    public void displayMessage(String toastString) {
        utils.print("displayMessage", "" + toastString);
        Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

}
