package com.detatech.vitaluser.Activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.detatech.vitaluser.R;
import com.detatech.vitaluser.Utils.ConnectionHelper;
import com.detatech.vitaluser.Utils.CustomDialog;
import com.detatech.vitaluser.Utils.InternetConnection;
import com.detatech.vitaluser.Utils.SharedPreferenceUtil;
import com.detatech.vitaluser.Utils.URLHelper;
import com.detatech.vitaluser.Utils.Utilities;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EmergencyDetailActivity extends AppCompatActivity {

    private View mMainView;
    private String token;

    TextView hospital_name;
    TextView available;
    TextView address;
    TextView price;
    TextView type;
    ImageView image;
    TextView report;
    TextView needing;

    ConnectionHelper helper;
    CustomDialog customDialog;
    Boolean isInternet;
    Utilities utils = new Utilities();
    String request_id;
    Button accept_request, cancel_request;

    String str_price;

    Button selectImg;
//    TextView needing;

    SweetAlertDialog pDialog, pDialog2;

    private String filepath;
    private File myFile;

    GalleryPhoto galleryPhoto;
    String photoPath;

    final int IMAGE_REQUEST_CODE = 999;
    private static final int STORAGE_PERMISSION_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_detail);

        // Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initViews();
        token = SharedPreferenceUtil.getStringValue(this, "access_token");

//        checkUserToken();

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            hospital_name.setText(mBundle.getString("hospital_name"));
            available.setText(mBundle.getString("available"));
            address.setText(mBundle.getString("address"));
            price.setText(mBundle.getString("price"));
            str_price = (mBundle.getString("price"));
            type.setText(mBundle.getString("type"));

//            type.setText(mBundle.getString("needing"));

            request_id = mBundle.getString("id");

//            report.setText(mBundle.getString("report"));

//            Glide.with(this).load(mBundle.getString("image")).into(image);

        }

        accept_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept_request();
            }
        });

//        cancel_request.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cancel_request();
//            }
//        });

        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, IMAGE_REQUEST_CODE);
//                getGallarey();
                chooseFile();
            }
        });

    }

    private void cancel_request() {

    }

    private void accept_request() {
        if (new InternetConnection().isInternetOn(EmergencyDetailActivity.this)) {
            pDialog = new SweetAlertDialog(EmergencyDetailActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
            /*JSONObject object = new JSONObject();
            try {

//                object.put("image", Encode(photoPath));
                object.put("report", report.getText().toString());
                object.put("emergency_id", request_id);
                object.put("needing", needing.getText().toString());
                object.put("price", str_price);
                object.put("status", 1);

                utils.print("Show Active Request", "" + object);
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

            SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, URLHelper.accept_emergency_services, new Response.Listener<String>() {
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.accept_emergency_services, object, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(String response) {
                    utils.print("Show Active Request", response.toString());
                    pDialog.dismiss();

                    pDialog2 = new SweetAlertDialog(EmergencyDetailActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                    pDialog2.setTitleText("Successful!!");
                    pDialog2.setContentText(getString(R.string.accept_request));
                    pDialog2.setCancelable(false);
                    pDialog2.show();

                    pDialog2.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            pDialog2.dismiss();
                            goToEmergencyServicesActivity();
                        }
                    });
//                    pDialog.dismiss();
                    utils.print("EmergencyActivity Response", response.toString());

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                    String json = null;
                    String Message;
                    NetworkResponse response = error.networkResponse;
                    utils.print("MyTest", "" + error);
                    utils.print("MyTestError", "" + error.networkResponse);
                    Log.i("Emergency Detail", "Request body: " + error.getMessage());
                    Log.i("Emergency Detail1", "Request body: " + error.toString());
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

            smr.addFile("image", filepath);
            smr.addStringParam("report", report.getText().toString());
            smr.addStringParam("emergency_id", request_id);
            smr.addStringParam("needing", needing.getText().toString());
            smr.addStringParam("price", str_price);
            smr.addStringParam("status", "1");
//
//            try {
//                Log.i("Emergency Detail3", "Request body: " + new String(Arrays.toString(smr.getBody())));
//            } catch (AuthFailureError authFailureError) {
//                authFailureError.printStackTrace();
//            }

            smr.setRetryPolicy(
                    new DefaultRetryPolicy(
                            0,
                            -1,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
            RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());

//            VolleyLog.DEBUG = true;
            mRequestQueue.add(smr);
            mRequestQueue.start();

//            XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        }
    }

    public void goToEmergencyServicesActivity() {
        Intent mainIntent = new Intent(EmergencyDetailActivity.this, EmergencyServicesActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        finish();
    }

    private void initViews() {
        hospital_name = (TextView) findViewById(R.id.hospital_name);
        available = (TextView) findViewById(R.id.available);
        address = (TextView) findViewById(R.id.address);
        price = (TextView) findViewById(R.id.price);
        type = (TextView) findViewById(R.id.type);

        report = (TextView) findViewById(R.id.report);
        needing = (TextView) findViewById(R.id.needing);
        image = (ImageView) findViewById(R.id.image);

        needing = (TextView) findViewById(R.id.needing);

        selectImg = (Button) findViewById(R.id.select_img);

        accept_request = (Button) findViewById(R.id.accept_request);
//        cancel_request = (Button) findViewById(R.id.cancel_request);

    }

    private void chooseFile() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            try {
                startActivityForResult(intent, IMAGE_REQUEST_CODE);

            } catch (ActivityNotFoundException e) {
                utils.print("Choose Image", e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void displayMessage(String toastString) {
        utils.print("displayMessage", "" + toastString);
        Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null || requestCode == REQUEST_FILE_CODE) {
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
//            if (resultCode != RESULT_CANCELED) {
            Uri uri = data.getData();

//            assert uri != null;
            String uriString = getRealPathFromURIPath(uri, EmergencyDetailActivity.this);
//            String uriString = uri.toString();
            myFile = new File(uriString);
            filepath = myFile.getAbsolutePath();

//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            Cursor cursor = getContentResolver().query(filepath, filePathColumn, null, null, null);
//            cursor.moveToFirst();
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//            try {
//                InputStream inputStream = getContentResolver().openInputStream(filepath);
//                bitmap = BitmapFactory.decodeStream(inputStream);
//                img.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//                }

            /*galleryPhoto.setPhotoUri(data.getData());
            photoPath = galleryPhoto.getPath();*/

//            try {
//                Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(256, 256).getBitmap();
//                img.setImageBitmap(bitmap);
//                ImagesAqar(count_image, photoPath);
//                    selectedImage = photoPath;
//            filePath = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                imageView.setImageBitmap(bitmap);

//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        String realPath = "";
        if (cursor == null) {
            realPath = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            realPath = cursor.getString(idx);
        }
        if (cursor != null) {
            cursor.close();
        }

        return realPath;
    }

    private void getGallarey() {
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        Intent in = galleryPhoto.openGalleryIntent();
        startActivityForResult(in, IMAGE_REQUEST_CODE);
    }

    public String Encode(String path) {
        Log.e("abdalmola: ", "Start" + path);
        try {
//            Bitmap bitmap = ImageLoader.init().from(path).requestSize(256, 256).getBitmap();
            Bitmap bitmap = ImageLoader.init().from(path).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            path = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.e("abdalmola: ", "END");
            Log.e("abdalmola: ", path);
        } catch (Exception e) {
            Log.e("abdalmola: ", path + "" + e.getMessage());
        }


        return path;

    }
}
