package com.detatech.vitaluser.Activities;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
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
import com.detatech.vitaluser.R;
import com.detatech.vitaluser.Utils.ConnectionHelper;
import com.detatech.vitaluser.Utils.CustomDialog;
import com.detatech.vitaluser.Utils.SharedPreferenceUtil;
import com.detatech.vitaluser.Utils.Utilities;

public class ActiveRequestsDetailActivity extends AppCompatActivity {

    private View mMainView;
    private String token, request_id, request_status;

    TextView medical_name;
    TextView medical_name_specialties;
    TextView address;
    TextView price;
    TextView start_time;
    TextView end_time;
    TextView request_admin_name;
    TextView request_admin_phone;

    ConnectionHelper helper;
    CustomDialog customDialog;
    Boolean isInternet;
    Utilities utils = new Utilities();

//    Button accept_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_requests_detail);
        initViews();
        token = SharedPreferenceUtil.getStringValue(ActiveRequestsDetailActivity.this, "access_token");
//        token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xNjUuMjIuODYuMTkzXC9NZWRpY2FsXC9wdWJsaWNcL2FwaVwvbG9naW4iLCJpYXQiOjE1NjQ5MzMyMTUsImV4cCI6MTU2NjE0MjgxNSwibmJmIjoxNTY0OTMzMjE1LCJqdGkiOiJJQzNYZkRZZ05WWmJJMHRmIiwic3ViIjo0NCwicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSJ9.vX8XCQ3WeJvMedhcOHeq-v1e2JOaZ7RB1Zgz2KoZDPY";
        checkUserToken();

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
//            int myInt = bundle.getInt(key, defaultValue);
            medical_name.setText(mBundle.getString("medical_name"));
            medical_name_specialties.setText(mBundle.getString("medical_name_specialties"));
            address.setText(mBundle.getString("address"));
            price.setText(mBundle.getString("price"));
            start_time.setText(mBundle.getString("start_time"));
            end_time.setText(mBundle.getString("end_time"));
            request_admin_name.setText(mBundle.getString("request_admin_name"));
            request_admin_phone.setText(mBundle.getString("request_admin_phone"));

            request_id = (mBundle.getString("request_id"));
            request_status = (mBundle.getString("request_status"));
        }

//        accept_request.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                accept_request2();
//            }
//        });

//        Bundle mBundle = getIntent().getExtras();
//        if (mBundle != null) {
//            mEmailTitle.setText(mBundle.getString("title"));
//            mSource.setText(mBundle.getString("source"));
//            mEmailDetails.setText(mBundle.getString("details"));
//            mEmailTime.setText(mBundle.getString("time"));
//            mImage.setImageResource(mBundle.getInt("icon"));

//            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

//            Glide.with(this).load(mBundle.getString("icon")).apply(requestOptions).into(mImage);
//        }

    }

    private void accept_request() {
//        displayMessage(getString(R.string.accept_order));
        Toast.makeText(getApplicationContext(),"Order Accepted Successfully", Toast.LENGTH_SHORT).show();
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        this.finish();

    }

    private void checkUserToken() {

    }

    private void initViews() {
        medical_name = (TextView) findViewById(R.id.medical_name);
        medical_name_specialties = (TextView) findViewById(R.id.medical_name_specialties);
        address = (TextView) findViewById(R.id.address);
        price = (TextView) findViewById(R.id.price);
        start_time = (TextView) findViewById(R.id.start_time);
        end_time = (TextView) findViewById(R.id.end_time);
        request_admin_name = (TextView) findViewById(R.id.request_admin_name);
        request_admin_phone = (TextView) findViewById(R.id.request_admin_phone);
//        accept_request = (Button) findViewById(R.id.accept_request);
    }

    /*private void accept_request2() {
        if (new InternetConnection().isInternetOn(ActiveRequestsDetailActivity.this)) {
            customDialog = new CustomDialog(ActiveRequestsDetailActivity.this);
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("id", request_id);
                object.put("status", 2);
//                object.put("password", password.getText().toString());

                utils.print("Login Request", "" + object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.accept_request, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    customDialog.dismiss();
                    utils.print("Login Response", response.toString());
                    try {
                        if (response.getBoolean("accept") && response.getBoolean("request")){
                            accept_request();
                        }
//                            goToLogin2Activity();
//                        }
//                        SharedPreferenceUtil.storeStringValue(ShowRequestsDetailActivity.this, "access_token", response.optString("access_token"));
//
//                        JSONObject user = response.getJSONObject("user");
//
//                        if (user.getInt("status") == 1){
//                            goToLogin2Activity();
//                        }else if(user.getInt("status") == 2){
//                            goToLogin3Activity();
//                        } else if(user.getInt("status") == 3){
//                            goToMainActivity();
//                        }

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
                        accept_request();
                    }

                }
            })
//                    ;
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
//                    headers.put("Accept", "application/json");
                    headers.put("Content-Type", "application/json");
//                    headers.put("Content-Type", "multipart/form-data");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        }

    }*/

    public void displayMessage(String toastString) {
        utils.print("displayMessage", "" + toastString);
        Snackbar.make(ActiveRequestsDetailActivity.this.getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }
}
