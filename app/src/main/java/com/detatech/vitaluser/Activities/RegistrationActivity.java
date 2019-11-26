package com.detatech.vitaluser.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import com.bumptech.glide.Glide;
import com.detatech.vitaluser.R;
import com.detatech.vitaluser.Utils.ConnectionHelper;
import com.detatech.vitaluser.Utils.CustomDialog;
import com.detatech.vitaluser.Utils.InternetConnection;
import com.detatech.vitaluser.Utils.SharedPreferenceUtil;
import com.detatech.vitaluser.Utils.URLHelper;
import com.detatech.vitaluser.Utils.Utilities;
import com.detatech.vitaluser.Utils.XuberApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    public Context context = RegistrationActivity.this;
    public Activity activity = RegistrationActivity.this;

    ConnectionHelper helper;
    CustomDialog customDialog;
    Boolean isInternet;
    Utilities utils = new Utilities();

    private String token;

    Button register, login;
    EditText fullName, phone, password;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        findViewById();

//        isInternet = helper.isConnectingToInternet();
        token = SharedPreferenceUtil.getStringValue(context, "access_token");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pattern ps = Pattern.compile(".*[0-9].*");
                Matcher ful_Name = ps.matcher(fullName.getText().toString());

                if (fullName.getText().toString().equals("") || fullName.getText().toString().equalsIgnoreCase(getString(R.string.full_name))) {
                    displayMessage(getString(R.string.full_name_empty));
                } else if (ful_Name.matches()) {
                    displayMessage(getString(R.string.full_name_no_number));
                } else if (phone.getText().toString().equals("") || phone.getText().toString().equalsIgnoreCase(getString(R.string.phone_number))) {
                    displayMessage(getString(R.string.mobile_number_empty));
                } else if (phone.length() < 10 || phone.length() > 14) {
                    displayMessage(getString(R.string.mobile_number_validation));
                }else if (password.getText().toString().equals("") || password.getText().toString().equalsIgnoreCase(getString(R.string.password_txt))) {
                    displayMessage(getString(R.string.password_validation));
                } else if (password.length() < 6) {
                    displayMessage(getString(R.string.password_size));
                } else {
                    if (new InternetConnection().isInternetOn(RegistrationActivity.this)) {
                        register();
                    } else {
                        displayMessage(getString(R.string.something_went_wrong_net));
                    }
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(activity, Login1Activity.class);
                startActivity(registerIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        Glide.with(this).load(R.drawable.new_logo).into(imageView);

    }

    private void findViewById() {

        fullName = (EditText)findViewById(R.id.register_fullName);
        phone = (EditText)findViewById(R.id.register_phone);
        password = (EditText)findViewById(R.id.register_password);
        register = (Button) findViewById(R.id.register_registerBtn);
        login = (Button) findViewById(R.id.register_loginBtn);
        imageView = (ImageView) findViewById(R.id.logo);

    }

    private void register() {
        if (new InternetConnection().isInternetOn(RegistrationActivity.this)) {
            customDialog = new CustomDialog(activity);
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("name", fullName.getText().toString());
                object.put("phone", phone.getText().toString());
                object.put("password", password.getText().toString());
                object.put("role_id", 2);

                utils.print("Register Activity Request", "" + object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.register, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    customDialog.dismiss();
                    utils.print("Register Response", response.toString());
                    try {
                        SharedPreferenceUtil.storeStringValue(context, "access_token", response.optString("access_token"));

                        JSONObject user = response.getJSONObject("user");
                        if (user.getInt("status") == 1){
                            goToLogin2Activity();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    customDialog.dismiss();
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
                        register();
                    }

                }
            })
//                    ;
            {
//                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
//                    headers.put("Accept", "application/json");
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };
            XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        }
    }

    private void login() {
        if (new InternetConnection().isInternetOn(RegistrationActivity.this)) {
            customDialog = new CustomDialog(activity);
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("name", fullName.getText().toString());
                object.put("phone", phone.getText().toString());
                object.put("password", password.getText().toString());
                object.put("role_id", 2);

                utils.print("Login Request", "" + object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.login, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    customDialog.dismiss();
                    utils.print("Login Response", response.toString());
                    SharedPreferenceUtil.storeStringValue(context, "access_token", response.optString("access_token"));
                    goToLogin2Activity();
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    customDialog.dismiss();
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
                        register();
                    }

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

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

    public void displayMessage(String toastString) {
        utils.print("displayMessage", "" + toastString);
        Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }
}
