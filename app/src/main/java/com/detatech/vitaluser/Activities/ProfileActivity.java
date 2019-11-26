package com.detatech.vitaluser.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.android.volley.AuthFailureError;
//import com.android.volley.NetworkError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
//import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
//import com.android.volley.TimeoutError;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.detatech.vitaluser.R;
import com.detatech.vitaluser.Utils.InternetConnection;
import com.detatech.vitaluser.Utils.SharedPreferenceUtil;
import com.detatech.vitaluser.Utils.URLHelper;
import com.detatech.vitaluser.Utils.Utilities;
import com.detatech.vitaluser.Utils.XuberApplication;
import com.squareup.picasso.Picasso;

import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    TextView name, speciality, register_number, address, phone;

    //    public Context context = ProfileActivity.this;
    public Activity activity = ProfileActivity.this;
    private Context context;

    //    ConnectionHelper helper;
//    CustomDialog customDialog;
    //    Boolean isInternet;
    Utilities utils = new Utilities();

    private String token, employee_id, base64Image;

    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 3;
    private BottomNavigationView bottomNavigationView;

    private Context mContext = ProfileActivity.this;
    SweetAlertDialog pDialog, pDialog2;
    private CircleImageView img;
    private Button selectImg, uploadImg;
    //    private Uri filepath;
    private String filepath;
    private File base64Pdf, myFile;

    final int IMAGE_REQUEST_CODE = 999;
    final int REQUEST_FILE_CODE = 888;
    private static final int STORAGE_PERMISSION_CODE = 123;

    GalleryPhoto galleryPhoto;
    String photoPath;

    private Bitmap bitmap;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        findViewById();


        token = SharedPreferenceUtil.getStringValue(ProfileActivity.this, "access_token");
        employee_id = SharedPreferenceUtil.getStringValue(ProfileActivity.this, "employee_id");

//        setupBottomNavigationView();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        deleteCache(ProfileActivity.this);
        login();

//        displayImage();

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        Bitmap bitmap = BitmapFactory.decodeFile(baseImage2);
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] imageBytes = baos.toByteArray();
//        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);


//        Glide.with(ProfileActivity.this)
//                                .load(Base64.decode(baseImage2, Base64.DEFAULT))
//                                .into(img);

//        Picasso.get()
//                .load(String.valueOf(Decode(baseImage2)))
//                .into(img);
        requestStoragePermission();

    }

    private void findViewById() {
        name = (TextView) findViewById(R.id.profile_tv_name);
        img = (CircleImageView) findViewById(R.id.imagview);
        speciality = (TextView) findViewById(R.id.profile_specialties);
        register_number = (TextView) findViewById(R.id.profile_register_number);
        phone = (TextView) findViewById(R.id.profile_phone);
        address = (TextView) findViewById(R.id.profile_address);

        selectImg = (Button) findViewById(R.id.select_img);
        uploadImg = (Button) findViewById(R.id.upload_img);

        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, IMAGE_REQUEST_CODE);
//                getGallarey();
                chooseFile();
            }
        });

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                uploadPdf();
//                uploadMultipart();
                uploadPDF();
//                uploadImage();
            }
        });

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

    private void uploadPDF() {
        pDialog2 = new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog2.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog2.setTitleText("Loading");
        pDialog2.setCancelable(false);
        pDialog2.show();
//        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, URLHelper.cv + employee_id, new Response.Listener<String>() {
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, URLHelper.profile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ProfileActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                pDialog2.dismiss();

                try {
                    final JSONObject jsonObject =new JSONObject(response);


                    pDialog = new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.setTitleText("Image Uploaded Successfully!!");
                    pDialog.setContentText(getString(R.string.add_request));
                    pDialog.setCancelable(false);
                    pDialog.show();

                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            pDialog.dismiss();

                            try {
//                            Picasso.get()
//                                    .load(jsonObject.getString("image"))
//                                    .placeholder(R.drawable.ic_person_light_blue_700_48dp)
//                                    .error(R.drawable.ic_person_light_blue_700_48dp)
//                                    .into(img);

                                RequestOptions options = new RequestOptions()
//                                .centerCrop()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                                        .skipMemoryCache(true)
                                        .placeholder(R.drawable.ic_person_light_blue_700_48dp)
                                        .error(R.drawable.ic_person_light_blue_700_48dp);



                                Glide.with(ProfileActivity.this).load(jsonObject.getString("image")).apply(options).into(img);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                    displayImage();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.error.VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
//            ;

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
//                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        smr.addFile("image", filepath);
//        smr.addStringParam("userid", userid);
//        smr.addStringParam("caption", caption);
//        MyApplication.getInstance().addToRequestQueue(smr);

        //I used this because it was sending the file twice to the server
        smr.setRetryPolicy(
                new DefaultRetryPolicy(
                        0,
                        -1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(smr);
        mRequestQueue.start();

//        requestQueue.add(smr);

    }

//    private byte[] getFileData() {
//        int size = (int) pdf.length();
//        byte[] bytes = new byte[size];
//        byte[] tmpBuff = new byte[size];
//
//        try (FileInputStream inputStream = new FileInputStream(pdf)) {
//            int read = inputStream.read(bytes, 0, size);
//            if (read < size) {
//                int remain = size - read;
//                while (remain > 0) {
//                    read = inputStream.read(tmpBuff, 0, remain);
//                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
//                    remain -= read;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return bytes;
//    }

    private void getGallarey() {
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        Intent in = galleryPhoto.openGalleryIntent();
        startActivityForResult(in, IMAGE_REQUEST_CODE);
    }

    private void chooseFile() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/image");
            try {
                startActivityForResult(intent, REQUEST_FILE_CODE);

            } catch (ActivityNotFoundException e) {
                utils.print("Choose File", e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showFileChooserIntent() {
        Intent fileManagerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        //Choose any file
        fileManagerIntent.setType("*/*");
        startActivityForResult(fileManagerIntent, REQUEST_FILE_CODE);

    }


    private void login() {
        if (new InternetConnection().isInternetOn(ProfileActivity.this)) {
            pDialog2 = new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog2.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog2.setTitleText("Loading");
            pDialog2.setCancelable(false);
            pDialog2.show();
//            JSONObject object = new JSONObject();
//            try {
//                object.put("phone", phone.getText().toString());
//                object.put("password", password.getText().toString());
//
//                utils.print("Login Request", "" + object);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URLHelper.profile, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    pDialog2.dismiss();
                    utils.print("Login Response", jsonObject.toString());
//                    for (int i = 0; i < response.length(); i++) {

                    try {
//                            JSONObject jsonObject = response.getJSONObject(i);

                        JSONObject employ = jsonObject.getJSONObject("employ");
                        JSONObject medicalBoard = jsonObject.getJSONObject("medical_board");

                        name.setText(jsonObject.getString("name"));
                        speciality.setText(employ.getString("job_title"));
                        employee_id = (employ.getString("id"));
                        phone.setText(jsonObject.getString("phone"));
                        register_number.setText(medicalBoard.getString("registration_number"));
                        address.setText(employ.getString("address"));

                        RequestOptions options = new RequestOptions()
//                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                                .skipMemoryCache(true)
                                .placeholder(R.drawable.ic_person_light_blue_700_48dp)
                                .error(R.drawable.ic_person_light_blue_700_48dp);



                        Glide.with(ProfileActivity.this).load(jsonObject.getString("image")).apply(options).into(img);

                        /*Picasso.get()
                                .load(jsonObject.getString("image"))
                                .placeholder(R.drawable.ic_person_light_blue_700_48dp)
                                .error(R.drawable.ic_person_light_blue_700_48dp)
                                .into(img);
*/
                      /*  Glide.with(ProfileActivity.this)
                                .load((jsonObject.getString("image")))
                                .into(img);*/
//                        img.setBackground(getResources().getDrawable(R.drawable.circle));
//                        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

//                        SharedPreferenceUtil.storeStringValue(context, "access_token", response.optString("access_token"));
//
//                        JSONObject user = response.getJSONObject("user");
//
//                        SharedPreferenceUtil.storeStringValue(context, "user_id", user.optString("id"));
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

//                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog2.dismiss();
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
            })
//                    ;
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        }

    }

    private void uploadImage() {
        if (new InternetConnection().isInternetOn(ProfileActivity.this)) {
            pDialog2 = new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog2.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog2.setTitleText("Loading");
            pDialog2.setCancelable(false);
            pDialog2.show();

            JSONObject object = new JSONObject();
            try {

//                String imgdata = imgToString(bitmap);
//                params.put("image" + k, Encode(images.getString("image" + k, "")));
                object.put("image", Encode(photoPath));

                utils.print("Upload Image Request", "" + object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.profile + "/" + user_id, object, new Response.Listener<JSONObject>() {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.profile, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pDialog2.dismiss();

                    utils.print("Upload Image Response", response.toString());

                    pDialog = new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.setTitleText("Image Uploaded Successfully!!");
                    pDialog.setContentText(getString(R.string.add_request));
                    pDialog.setCancelable(false);
                    pDialog.show();
//
//                    Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(img);
                    try {
//                        JSONObject jsonObject = response.getJSONObject("data");
//
                        base64Image = response.getString("image");
//                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
//                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                                Decode(base64Image);

//                        Glide.with(ProfileActivity.this)
//                                .load(Decode(base64Image))
//                                .into(img);

                        /*RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.ic_person_light_blue_700_48dp)
                                .error(R.drawable.ic_person_light_blue_700_48dp);



                        Glide.with(ProfileActivity.this).load(base64Image).apply(options).into(img);*/

                        Picasso.get()
                                .load(base64Image)
                                .placeholder(R.drawable.ic_person_light_blue_700_48dp)
                                .error(R.drawable.ic_person_light_blue_700_48dp)
                                .into(img);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//

                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            pDialog.dismiss();
//                    displayImage();
                            goToMainActivity();
                        }
                    });

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog2.dismiss();
                    String json = null;
                    String Message;
                    NetworkResponse response = error.networkResponse;
                    utils.print("Upload Image", "" + error.toString());
                    utils.print("Upload Image Error", "" + error.networkResponse);

                    if (error instanceof NoConnectionError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof NetworkError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof TimeoutError) {
                        uploadImage();
                    }

                }
            }) {
                //                    ;
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> parms = new HashMap<String, String>();
//                    String imgdata = imgToString(bitmap);
//                    parms.put("image", imgdata);
//                    return parms;
//                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        }

    }

    private void uploadPdf() {

        if (new InternetConnection().isInternetOn(ProfileActivity.this)) {
            pDialog2 = new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog2.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog2.setTitleText("Loading");
            pDialog2.setCancelable(false);
            pDialog2.show();

            JSONObject object = new JSONObject();
            try {

//                String imgdata = imgToString(bitmap);
//                params.put("image" + k, Encode(images.getString("image" + k, "")));
//                object.put("cv", Encode(photoPath));
                object.put("cv", Encode2(filepath));

                utils.print("Upload PDF Request", "" + object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URLHelper.profile + "/" + user_id, null, new Response.Listener<JSONObject>() {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.cv + employee_id, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {


                    utils.print("Upload PDF Response", response.toString());

//                    pDialog = new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE);
//                    pDialog.setTitleText("Image Uploaded Successfully!!");
//                    pDialog.setContentText(getString(R.string.add_request));
//                    pDialog.setCancelable(false);
//                    pDialog.show();
//
//                    Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(img);
//
//
//                    JSONObject jsonObject = response.getJSONObject("data");
//
//                    base64Image = jsonObject.getString("image");

//                    try {
//                        JSONObject jsonObject = response.getJSONObject("data");
//                        base64Image = jsonObject.getString("image");

//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        Bitmap bitmap = BitmapFactory.decodeFile(base64Image);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                        byte[] imageBytes = baos.toByteArray();
//                        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

//                        Picasso.get()
//                                .load(jsonObject.getString("image"))
//                                .into(img);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

//                    displayImage();
                    pDialog2.dismiss();
                    goToMainActivity();


//                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            pDialog.dismiss();
//
//                            goToMainActivity();
//                        }
//                    });

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog2.dismiss();
                    String json = null;
                    String Message;
                    NetworkResponse response = error.networkResponse;
                    utils.print("Upload PDF", "" + error);
                    utils.print("Upload PDF", "" + error.networkResponse);

                    if (error instanceof NoConnectionError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof NetworkError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof TimeoutError) {
                        uploadPdf();
                    }

                }
            }) {
                //                    ;
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> parms = new HashMap<String, String>();
//                    String imgdata = imgToString(bitmap);
//                    parms.put("image", imgdata);
//                    return parms;
//                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        }
    }

    //    String encodeFileToBase64Binary = encodeFileToBase64Binary(yourFile);

    public static String convertFileToByteArray(File f) {
        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 11];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();

            Log.e("Byte array", ">" + byteArray);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

//    private String encodeFileToBase64Binary(File yourFile) {
//        int size = (int) yourFile.length();
//        byte[] bytes = new byte[size];
//        try {
//            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(yourFile));
//            buf.read(bytes, 0, bytes.length);
//            buf.close();
//        } catch (FileNotFoundException e) {
//// TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//// TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        String encoded = Base64.encodeToString(bytes, Base64.NO_WRAP);
//        return encoded;
//    }

//    private String encodeFileToBase64Binary2(File file){
//        String encodedfile = null;
//        try {
//            FileInputStream fileInputStreamReader = new FileInputStream(file);
//            byte[] bytes = new byte[(int)file.length()];
//            fileInputStreamReader.read(bytes);
//            encodedfile = Base64.encodeToString(bytes,Base64.DEFAULT);
////            String encodedString = new String(Base64.encodeBase64(bytes));
//
//            encodedfile = Base64.encodeBase64(bytes).toString();
//            String safeString = encodedfile.replace('+','-').replace('/','_');
//            utils.print("encodedfile", encodedfile);
//            utils.print("encodedfile", safeString);
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return encodedfile;
//    }

//    public void ImagesAqar(int id, String image) {
//        SharedPreferences.Editor userprofile = images.edit();
//        userprofile.putString("image" + id, image);
//        userprofile.apply();
////        if (userprofile.commit()){
////            //        images.a
////            count.setText(String.valueOf(id));
////            Log.e( "abdalmola: " ,id+ "   " + image);
////            String x= images.getString("image"+ id,"");
////            Toast.makeText(getActivity(), x, Toast.LENGTH_LONG).show();
////            Log.e("abdalmola: GET",  "image"+id  +""+ x);
////        }
////        else {
////            Log.e( "abdalmola: " ,"hhhhhhhhhhhhhhhhhh error");
////        }
////
//////        Encode(sheardprefernces.getString("image"+ id,""));
//
//        count_image++;
//
//    }

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

    public String Encode2(String path) {
        Log.e("abdalmola: ", "Start" + path);
        try {
//            Bitmap bitmap = ImageLoader.init().from(path).requestSize(256, 256).getBitmap();
//            Bitmap bitmap = ImageLoader.init().from(path).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            path = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.e("abdalmola: ", "END");
            Log.e("abdalmola: ", path);
        } catch (Exception e) {
            Log.e("abdalmola: ", path + "" + e.getMessage());
        }


        return path;

    }

    public Bitmap Decode(String image) {

        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedByte;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == IMAGE_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
////                Intent intent = new Intent(new Intent(Intent.ACTION_PICK));
////                intent.setType("image/*");
////                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////                startActivityForResult(i, IMAGE_REQUEST_CODE);
//                startActivityForResult(galleryPhoto.openGalleryIntent(), IMAGE_REQUEST_CODE);
////                startActivityForResult(Intent.createChooser(intent, "select image"), IMAGE_REQUEST_CODE);
//
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null || requestCode == REQUEST_FILE_CODE) {
        if (requestCode == REQUEST_FILE_CODE && resultCode == RESULT_OK && data != null ) {
//            if (resultCode != RESULT_CANCELED) {
            Uri uri = data.getData();

//            assert uri != null;
            String uriString = getRealPathFromURIPath(uri, ProfileActivity.this);
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

    private String imgToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgbytes = byteArrayOutputStream.toByteArray();
        String encodeimg = Base64.encodeToString(imgbytes, Base64.DEFAULT);
        return encodeimg;
    }

    public void goToMainActivity() {
        Intent mainIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }

    public void displayMessage(String toastString) {
        utils.print("displayMessage", "" + toastString);
        Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    //    public void uploadMultipart() {
//        //getting name for the image
////        String name = editText.getText().toString().trim();
//
//        //getting the actual path of the image
//        String path = getPath(filePath);
//
//        //Uploading code
//        try {
//            String uploadId = UUID.randomUUID().toString();
//
//            //Creating a multi part request
//            new MultipartUploadRequest(ProfileActivity.this, uploadId, URLHelper.profile)
////            new MultipartUploadRequest(this, uploadId, URLHelper.profile + "/" + user_id)
//                    .addHeader("Content-Type", "application/json")
//                    .addHeader("Authorization", "Bearer " + token)
//                    .addFileToUpload(path, "image") //Adding file
////                    .addParameter("name", name) //Adding text parameter to the request
//                    .setNotificationConfig(new UploadNotificationConfig())
//                    .setMaxRetries(2)
//                    .startUpload(); //Starting the upload
//
//        } catch (Exception exc) {
//            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void showFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST_CODE);
//    }
//
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    //
//
//    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

//    @Override
//    public void onBackPressed() {
//        Log.d("CDA", "onBackPressed Called");
//        Intent setIntent = new Intent(Intent.ACTION_MAIN);
//        setIntent.addCategory(Intent.CATEGORY_HOME);
//        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(setIntent);
//    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }

}
