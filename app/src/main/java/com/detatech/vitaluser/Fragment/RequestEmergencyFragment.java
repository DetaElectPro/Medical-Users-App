package com.detatech.vitaluser.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

import com.detatech.vitaluser.Activities.EmergencyServicesActivity;
import com.detatech.vitaluser.R;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Arbab on 8/24/2019.
 */

public class RequestEmergencyFragment extends Fragment {
    private View mMainView;
    Utilities utils = new Utilities();
    CustomDialog customDialog;
    private String token, user_id, selectedType;

    Button request;
    EditText ed_hospital, ed_price, ed_address, available_bed;
    Spinner typeSpinner;

    SweetAlertDialog pDialog, pDialog2;

    String[] type = {"ICU Bed", "Mechanical Ventilation", "Hemodialysis"};

    public RequestEmergencyFragment() {
    }

    public static RequestEmergencyFragment newInstance() {
        RequestEmergencyFragment fragment = new RequestEmergencyFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_request_doctor, container, false);

        mMainView = inflater.inflate(R.layout.fragment_request_emergency, container, false);

//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add Request");
//        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        initViews();
        token = SharedPreferenceUtil.getStringValue(getActivity(), "access_token");
        user_id = SharedPreferenceUtil.getStringValue(getActivity(), "user_id");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedType = type[position];
//                if (selectedType.equals("Bed")){
//                    available_bed
//                }else if(selectedType.equals("Tools")){
//
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_request();
            }
        });

        return mMainView;
    }

    private void initViews() {
        request = mMainView.findViewById(R.id.request_btn);
        ed_hospital = mMainView.findViewById(R.id.name);
        ed_price = mMainView.findViewById(R.id.price);
        ed_address = mMainView.findViewById(R.id.address);
        available_bed = mMainView.findViewById(R.id.bed);

        typeSpinner = mMainView.findViewById(R.id.type_spinner);
    }

    private void add_request() {
        if (new InternetConnection().isInternetOn(getActivity())) {
//            customDialog = new CustomDialog(getActivity());
//            customDialog.setCancelable(false);
//            customDialog.show();

            pDialog2 = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog2.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog2.setTitleText("Loading");
            pDialog2.setCancelable(false);
            pDialog2.show();

            JSONObject object = new JSONObject();
            try {
                object.put("name", ed_hospital.getText().toString());
                object.put("address", ed_address.getText().toString());
                object.put("price_per_day", ed_price.getText().toString());
                object.put("type", typeSpinner.getSelectedItem().toString());
                object.put("available", available_bed.getText().toString());
                object.put("user_id", user_id);

                utils.print("EmergencyActivity Request", "" + object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.emergency_services, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pDialog2.dismiss();
                    utils.print("EmergencyActivity Response", response.toString());

                    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.setTitleText("Successful!!");
                    pDialog.setContentText(getString(R.string.add_request));
                    pDialog.setCancelable(false);
                    pDialog.show();

                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            pDialog.dismiss();
                            goToMainActivity();
                        }
                    });

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

//                    Log.w("Emergency error", String.valueOf(error));
//                    Log.w("Emergency Network error", String.valueOf(error.networkResponse));

                    if (error instanceof NoConnectionError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof NetworkError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof TimeoutError) {
                        add_request();
                    }

                }
            })
//                    ;
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
//                    headers.put("Accept", "application/json");
//                    headers.put("Accept", "multipart/form-data");
//                    headers.put("Content-Type", "multipart/form-data");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        }
    }

    public void goToMainActivity() {
        Intent mainIntent = new Intent(getActivity(), EmergencyServicesActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        getActivity().finish();
    }

    public void displayMessage(String toastString) {
        utils.print("displayMessage", "" + toastString);
        Snackbar.make(mMainView.findViewById(android.R.id.content), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

}
