package com.detatech.vitaluser.Fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.android.volley.AuthFailureError;
//import com.android.volley.NetworkError;
//import com.android.volley.NetworkResponse;
//import com.android.volley.NoConnectionError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.TimeoutError;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonArrayRequest;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;

import com.detatech.vitaluser.Adapters.HistoryRequestAdapter;
import com.detatech.vitaluser.Models.AdminRequests;
import com.detatech.vitaluser.R;
import com.detatech.vitaluser.Utils.ConnectionHelper;
import com.detatech.vitaluser.Utils.CustomDialog;
import com.detatech.vitaluser.Utils.SharedPreferenceUtil;
import com.detatech.vitaluser.Utils.URLHelper;
import com.detatech.vitaluser.Utils.Utilities;
import com.detatech.vitaluser.Utils.XuberApplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Arbab on 8/19/2019.
 */

public class HistoryRequestsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    public Context context = getContext();
    public Activity activity = getActivity();

    ConnectionHelper helper;
    CustomDialog customDialog;
    Boolean isInternet;
    Utilities utils = new Utilities();

    private View mMainView;
    private String token;

    RecyclerView recyclerView;
    List<AdminRequests> userRequestsListList = new ArrayList<AdminRequests>();
    HistoryRequestAdapter adapter;
    JSONObject jsonObject;

    SweetAlertDialog pDialog;

    SwipeRefreshLayout mySwipeRefreshLayout;

    public HistoryRequestsFragment() {
    }

    public static HistoryRequestsFragment newInstance() {
        HistoryRequestsFragment fragment = new HistoryRequestsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_show_requests, container, false);

        mMainView = inflater.inflate(R.layout.fragment_history_requests, container, false);

        mySwipeRefreshLayout = (SwipeRefreshLayout) mMainView.findViewById(R.id.swipe_container);
        mySwipeRefreshLayout.setOnRefreshListener(this);
        mySwipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));

//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Show Active Requests");
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        deleteCache(getContext());
        initViews();
        token = SharedPreferenceUtil.getStringValue(getActivity(), "access_token");

        adapter = new HistoryRequestAdapter(getActivity(), userRequestsListList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(adapter);

        showRequests();

        return mMainView;
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    private void initViews() {
        recyclerView = (RecyclerView) mMainView.findViewById(R.id.recyclerview);
    }

    private void showRequests() {
        mySwipeRefreshLayout.setRefreshing(false);

        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

//        customDialog = new CustomDialog(getActivity());
//        customDialog.setCancelable(false);
//        customDialog.show();
//            JSONObject object = new JSONObject();
//            try {
//                object.put("phone", phone.getText().toString());
//                object.put("password", password.getText().toString());
//
//                utils.print("Login 2 CheckToken Request", "" + object);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, URLHelper.get_admin_requests, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pDialog.dismiss();
                utils.print("Show Requests Response", response.toString());
                         userRequestsListList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
//                        if (Locale.getDefault().getLanguage().equals("ar")) {
                        jsonObject = response.getJSONObject(i);
                        if (jsonObject.getString("status").equals("6")) {
//                        SharedPreferenceUtil.storeStringValue(context, "request_id", jsonObject.optString("id"));
//                        SharedPreferenceUtil.storeStringValue(context, "request_status", jsonObject.optString("status"));
                            JSONObject userObject = jsonObject.getJSONObject("user");
                            JSONObject specialtiesObject = jsonObject.getJSONObject("specialties");
                            AdminRequests userRequests = new AdminRequests();
                            userRequests.setMedical_name(jsonObject.getString("name"));
                            userRequests.setMedical_name_specialties(specialtiesObject.getString("name"));
                            userRequests.setPrice(jsonObject.getString("price"));
                            userRequests.setRequest_id(jsonObject.getString("id"));
                            userRequests.setStart_time(jsonObject.getString("start_time"));
                            userRequests.setEnd_time(jsonObject.getString("end_time"));
                            userRequests.setRequest_admin_name(userObject.getString("name"));
                            userRequests.setRequest_admin_phone(userObject.getString("phone"));

                            userRequestsListList.add(userRequests);
//                            customDialog.dismiss();
                        }
                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
                        pDialog.dismiss();
                    } finally {
                        //Notify adapter about data changes
                        adapter.notifyItemChanged(i);
                    }
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
                    showRequests();
                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void displayMessage(String toastString) {
        utils.print("displayMessage", "" + toastString);
        Snackbar.make(getActivity().getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    @Override
    public void onRefresh() {
        showRequests();
    }
}
