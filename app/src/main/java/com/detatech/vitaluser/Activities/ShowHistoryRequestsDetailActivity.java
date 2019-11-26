package com.detatech.vitaluser.Activities;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.detatech.vitaluser.R;
import com.detatech.vitaluser.Utils.ConnectionHelper;
import com.detatech.vitaluser.Utils.CustomDialog;
import com.detatech.vitaluser.Utils.SharedPreferenceUtil;
import com.detatech.vitaluser.Utils.Utilities;

public class ShowHistoryRequestsDetailActivity extends AppCompatActivity {

    private View mMainView;
    private String token, id;

    TextView medical_name;
    TextView medical_name_specialties;
    TextView address;
    TextView price;
    TextView start_time;
    TextView end_time;
    TextView request_admin_name;
    TextView request_admin_phone;
//    RatingBar simpleRatingBar;
//    Float ratingBar;
//    int intRatingBar;
//    EditText note, recommendations;

    ConnectionHelper helper;
    CustomDialog customDialog;
    Boolean isInternet;
    Utilities utils = new Utilities();

//    Button finish_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history_requests_detail);

        initViews();
        token = SharedPreferenceUtil.getStringValue(this, "access_token");

//        checkUserToken();

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            medical_name.setText(mBundle.getString("medical_name"));
            medical_name_specialties.setText(mBundle.getString("medical_name_specialties"));
            address.setText(mBundle.getString("address"));
            price.setText(mBundle.getString("price"));
            start_time.setText(mBundle.getString("start_time"));
            end_time.setText(mBundle.getString("end_time"));
            request_admin_name.setText(mBundle.getString("request_admin_name"));
            request_admin_phone.setText(mBundle.getString("request_admin_phone"));
            id = (mBundle.getString("id"));
        }

//        ratingBar = simpleRatingBar.getRating();
//        intRatingBar = ratingBar.intValue();
//        simpleRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                ratingBar = simpleRatingBar.getRating(rating);
//            }
//        });

//        finish_request.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish_request2();
//            }
//        });
    }

    private void finish_request() {
//        displayMessage(getString(R.string.accept_order));
        Toast.makeText(getApplicationContext(), "Order Accepted Successfully", Toast.LENGTH_SHORT).show();
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        this.finish();

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
//        simpleRatingBar = (RatingBar) findViewById(R.id.simpleRatingBar);
//        finish_request = (Button) findViewById(R.id.accept_request);

//        note = (EditText) findViewById(R.id.note);
//        recommendations = (EditText) findViewById(R.id.recommendations);
    }

//    private void finish_request2() {
//        if (new InternetConnection().isInternetOn(ShowHistoryRequestsDetailActivity.this)) {
//            customDialog = new CustomDialog(ShowHistoryRequestsDetailActivity.this);
//            customDialog.setCancelable(false);
//            customDialog.show();
//            JSONObject object = new JSONObject();
//            try {
//                object.put("id", id);
//                object.put("status", 6);
//                object.put("notes", note.getText().toString());
//                object.put("recommendation", recommendations.getText().toString());
//                object.put("rating", intRatingBar);
//
//                utils.print("Show Active Request", "" + object);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.accept_request, object, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    customDialog.dismiss();
//                    utils.print("Show Active Request", response.toString());
//                    //                        SharedPreferenceUtil.storeStringValue(ShowRequestsDetailActivity.this, "access_token", response.optString("access_token"));
////                    utils.print("Login Response", response.toString());
////                        JSONObject user = response.getJSONObject("user");
//                    finish_request();
////                        if (user.getInt("status") == 1){
////                            goToLogin2Activity();
////                        }else if(user.getInt("status") == 2){
////                            goToLogin3Activity();
////                        } else if(user.getInt("status") == 3){
////                            goToMainActivity();
////                        }
//
//                }
//
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    customDialog.dismiss();
//                    String json = null;
//                    String Message;
//                    NetworkResponse response = error.networkResponse;
//                    utils.print("MyTest", "" + error);
//                    utils.print("MyTestError", "" + error.networkResponse);
//
//                    if (error instanceof NoConnectionError) {
//                        displayMessage(getString(R.string.oops_connect_your_internet));
//                    } else if (error instanceof NetworkError) {
//                        displayMessage(getString(R.string.oops_connect_your_internet));
//                    } else if (error instanceof TimeoutError) {
//                        finish_request2();
//                    }
//
//                }
//            })
////                    ;
//            {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap<String, String> headers = new HashMap<String, String>();
////                    headers.put("Accept", "application/json");
//                    headers.put("Content-Type", "application/json");
////                    headers.put("Content-Type", "multipart/form-data");
//                    headers.put("Authorization", "Bearer " + token);
//                    return headers;
//                }
//            };
//
//            XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);
//
//        }
//
//    }

    public void displayMessage(String toastString) {
        utils.print("displayMessage", "" + toastString);
        Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }
}
