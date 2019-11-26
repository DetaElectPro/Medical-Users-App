package com.detatech.vitaluser.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.detatech.vitaluser.R;
import com.detatech.vitaluser.Utils.InternetConnection;
import com.detatech.vitaluser.Utils.SharedPreferenceUtil;
import com.detatech.vitaluser.Utils.URLHelper;
import com.detatech.vitaluser.Utils.Utilities;
import com.detatech.vitaluser.Utils.XuberApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class Login2Activity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public Context context = Login2Activity.this;
    public Activity activity = Login2Activity.this;
    private static final String TAG = Login2Activity.class.getSimpleName();

    private String filepath;
    private File myFile;

    //    ConnectionHelper helper;
//    CustomDialog customDialog;
    //    Boolean isInternet;
    Utilities utils = new Utilities();

    SweetAlertDialog pDialog;

    private String token, path, filename, attachedFile;
    final Calendar myCalendar = Calendar.getInstance();
    Button login, fileBrowseBtn, uploadBtn;
    EditText job_title, graduation_date, birth_date, experience, address, pdfName;
    TextView fileName;

    //Pdf request code
    private int PICK_PDF_REQUEST = 1;

    private static final int REQUEST_FILE_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    private Uri filePath;
    Uri fileUri;
    private File file;

    Base64OutputStream output64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        // Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        findViewById();
        showFileChooser();

        token = SharedPreferenceUtil.getStringValue(context, "access_token");

        checkUserToken();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateGraduateLabel();
            }

        };

        graduation_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Login2Activity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateBirthLabel();
            }
        };

        birth_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Login2Activity.this, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                uploadPDF();
            }
        });

        fileBrowseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPDF();
            }
        });

        /*fileBrowseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if app has permission to access the external storage.
                if (EasyPermissions.hasPermissions(Login2Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showFileChooserIntent();

                } else {
                    //If permission is not present request for the same.
                    EasyPermissions.requestPermissions(Login2Activity.this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }

            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (file != null) {
//                    UploadAsyncTask uploadAsyncTask = new UploadAsyncTask(Login2Activity.this);
//                    uploadAsyncTask.execute();
//
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "Please select a file first", Toast.LENGTH_LONG).show();
//
//                }
            }
        });*/

    }

    private void updateGraduateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        graduation_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateBirthLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        birth_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void findViewById() {

        job_title = (EditText) findViewById(R.id.login2_job_title);
        graduation_date = (EditText) findViewById(R.id.login2_graduation_date);
        birth_date = (EditText) findViewById(R.id.login2_birth_of_date);
        address = (EditText) findViewById(R.id.login2_address);
        experience = (EditText) findViewById(R.id.login2_experience);
        login = (Button) findViewById(R.id.login2_loginBtn);

        fileBrowseBtn = findViewById(R.id.login2_choose_fileBtn);
        uploadBtn = findViewById(R.id.login2_upload_fileBtn);
//        pdfName = findViewById(R.id.tv_file_name);
        fileName = findViewById(R.id.tv_file_name);

    }

    private void login() {
        if (new InternetConnection().isInternetOn(Login2Activity.this)) {
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
            JSONObject object = new JSONObject();
            try {
                object.put("job_title", job_title.getText().toString());
                object.put("graduation_date", graduation_date.getText().toString());
                object.put("birth_of_date", birth_date.getText().toString());
                object.put("address", address.getText().toString());
                object.put("years_of_experience", experience.getText().toString());
//                object.put("cv", attachedFile);

                utils.print("Login 2 Request", "" + object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.employee, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pDialog.dismiss();
                    utils.print("Employee Response", response.toString());
                    try {
//                        SharedPreferenceUtil.storeStringValue(context, "access_token", response.optString("access_token"));

//                        JSONObject user = response.getJSONObject("user");
                        if (response.getInt("status") == 1){
                            goToLogin1Activity();
                        }else if(response.getInt("status") == 2){
                            goToLogin3Activity();
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
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
//                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Content-Type", "application/json");
//                    headers.put("Content-Type", "multipart/form-data");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        }

    }

    private void uploadPDF() {

        pDialog = new SweetAlertDialog(Login2Activity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        SimpleMultiPartRequest smr =  new SimpleMultiPartRequest(Request.Method.POST, URLHelper.employee, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(Login2Activity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();

                utils.print("Employee Response", response.toString());

//                pDialog = new SweetAlertDialog(Login2Activity.this, SweetAlertDialog.SUCCESS_TYPE);
//                pDialog.setTitleText("PDF Uploaded Successfully!!");
//                pDialog.setContentText(getString(R.string.pdf_success));
//                pDialog.setCancelable(false);
//                pDialog.show();

//                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        pDialog.dismiss();
//                    displayImage();
                try {
//                        SharedPreferenceUtil.storeStringValue(context, "access_token", response.optString("access_token"));

                        JSONObject user = new JSONObject(response);
                    if (user.getInt("status") == 1){
                        goToLogin1Activity();
                    }else if(user.getInt("status") == 2){
                        goToLogin3Activity();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                    }
//                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.error.VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
//                ;

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        smr.addFile("cv", filepath);
        smr.addStringParam("job_title", job_title.getText().toString());
        smr.addStringParam("graduation_date", graduation_date.getText().toString());
        smr.addStringParam("birth_of_date", birth_date.getText().toString());
        smr.addStringParam("address", address.getText().toString());
        smr.addStringParam("years_of_experience", experience.getText().toString());
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

    public void goToLogin1Activity() {
        Intent mainIntent = new Intent(activity, Login1Activity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        activity.finish();
    }

    public void goToLogin3Activity() {
        Intent mainIntent = new Intent(activity, Login3Activity.class);
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

    private void checkUserToken() {
        if (new InternetConnection().isInternetOn(Login2Activity.this)) {
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.check_token, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pDialog.dismiss();
                    utils.print("Check Token Response", response.toString());

                    try {
                        if (!response.getBoolean("status")) {
                            goToLogin1Activity();
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
                        checkUserToken();
                    }

                }
            })
//                    ;
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
//                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        }
    }

//    public void uploadMultipart() {
//        //getting name for the image
////        String name = fileName.getText().toString().trim();
//
//        //getting the actual path of the image
////        String path = FilePath.getPath(this, filePath);
//
//        if (path == null) {
//
//            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
//        } else {
//            //Uploading code
//
//            try {
////                String uploadId = UUID.randomUUID().toString();
//
//                //Creating a multi part request
//                new MultipartUploadRequest(this, URLHelper.employee)
//                        .addFileToUpload(path, "cv") //Adding file
////                        .addHeader("Accept","application/json")
////                        .addParameter("name", name) //Adding text parameter to the request
//                        .addParameter("job_title", job_title.getText().toString()) //Adding text parameter to the request
//                        .addParameter("graduation_date", graduation_date.getText().toString()) //Adding text parameter to the request
//                        .addParameter("birth_of_date", birth_date.getText().toString()) //Adding text parameter to the request
//                        .addParameter("address", address.getText().toString()) //Adding text parameter to the request
//                        .addParameter("years_of_experience", experience.getText().toString()) //Adding text parameter to the request
////                object.put("job_title", job_title.getText().toString());
////                object.put("graduation_date", graduation_date.getText().toString());
////                object.put("birth_of_date", birth_date.getText().toString());
////                object.put("address", address.getText().toString());
////                object.put("years_of_experience", experience.getText().toString());
//                        .setNotificationConfig(new UploadNotificationConfig())
//                        .setMaxRetries(2)
//                        .startUpload(); //Starting the upload
//
//            } catch (Exception exc) {
//                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    /*//method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }*/

    //handling the image chooser activity result
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            path = FilePath.getPath(this, filePath);
            assert path != null;
            filename = path.substring(path.lastIndexOf("/") + 1);
            *//*pdfName.setText(filename);

            InputStream inputStream = null;//You can get an inputStream using any IO API
//            URL url;
//            File file = new File(url.toURI());

            try {
                inputStream = new FileInputStream(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
//            inputStream = new FileInputStream(file.getAbsolutePath());
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output64 = new Base64OutputStream(output, Base64.DEFAULT);
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output64.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                output64.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            attachedFile = output.toString();*//*

            Uri selectedPdf = data.getData();


            try {

                InputStream iStream = getContentResolver().openInputStream(selectedPdf);
                byte[] inputData = getBytes(iStream);

                long fileSizeInBytes = inputData.length;
                long fileSizeInKB = fileSizeInBytes / 1024;
                long fileSizeInMB = fileSizeInKB / 1024;

                Parse

//                ParseFile resumes  = new ParseFile("image.pdf",
//                        inputData);

            } catch (IOException e) {
                e.printStackTrace();

            }

        }
    }*/

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    /*//Requesting permission
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
    }*/

//    private void upload(){
//        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, URLHelper.employee,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("Response", response);
////                        Toast.makeText(getApplicationContext(), R.string.alert_comment_sukses, Toast.LENGTH_LONG).show();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//
//        smr.addStringParam("param string", " data text");
//        smr.addFile("param file", path);
//    }

/*    public String Encode(String path) {
        Log.e("abdalmola: ", "Start" + path);
        try {
            Bitmap bitmap = ImageLoader.init().from(path).requestSize(256, 256).getBitmap();
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

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_FILE_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
//            assert uri != null;
            String uriString = getRealPathFromURIPath(uri, Login2Activity.this);
            myFile = new File(uriString);
            filepath = myFile.getAbsolutePath();
//            previewFile(fileUri);
            hideFileChooser();

        }
    }

    private void previewFile(Uri uri) {
        String filePath = getRealPathFromURIPath(uri, Login2Activity.this);
        file = new File(filePath);
        Log.d(TAG, "Filename " + file.getName());
        fileName.setText(file.getName());

        ContentResolver cR = this.getContentResolver();
        String mime = cR.getType(uri);

        //Show preview if the uploaded file is an image.
        /*if (mime != null && mime.contains("image")) {
            BitmapFactory.Options options = new BitmapFactory.Options();

            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            previewImage.setImageBitmap(bitmap);
        } else {
            previewImage.setImageResource(R.drawable.ic_file);
        }*/

//        hideFileChooser();
    }

    /**
     * Shows an intent which has options from which user can choose the file like File manager, Gallery etc
     */
    private void showFileChooserIntent() {
        Intent fileManagerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        //Choose any file
        fileManagerIntent.setType("*/*");
        startActivityForResult(fileManagerIntent, REQUEST_FILE_CODE);

    }

    private void chooseFile() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.setType("*/*");
            intent.setType("application/pdf");
            try {
                startActivityForResult(intent, REQUEST_FILE_CODE);

            } catch (ActivityNotFoundException e) {
                utils.print("Choose File", e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Returns the actual path of the file in the file system
     *
     * @param contentURI
     * @param activity
     * @return
     */
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, Login2Activity.this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        chooseFile();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }

    /**
     * Hides the Choose file button and displays the file preview, file name and upload button
     */
    private void hideFileChooser() {
        fileBrowseBtn.setVisibility(View.GONE);
        uploadBtn.setVisibility(View.VISIBLE);
        fileName.setVisibility(View.VISIBLE);
//        previewImage.setVisibility(View.VISIBLE);
    }

    /**
     *  Displays Choose file button and Hides the file preview, file name and upload button
     */
    private void showFileChooser() {
        fileBrowseBtn.setVisibility(View.VISIBLE);
        uploadBtn.setVisibility(View.GONE);
        fileName.setVisibility(View.GONE);
//        previewImage.setVisibility(View.GONE);

    }

    /**
     * Background network task to handle file upload.
     */
//    private class UploadAsyncTask extends AsyncTask<Void, Integer, String> {
//
//        HttpClient httpClient = new DefaultHttpClient();
//        private Context context;
//        private Exception exception;
//        private ProgressDialog progressDialog;
//
//        private UploadAsyncTask(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            HttpResponse httpResponse = null;
//            HttpEntity httpEntity = null;
//            String responseString = null;
//
//            try {
//                HttpPost httpPost = new HttpPost(URLHelper.employee);
//                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
//
//                // Add the file to be uploaded
//                multipartEntityBuilder.addPart("cv", new FileBody(file));
////                multipartEntityBuilder.addBinaryBody("cv", file);
//                multipartEntityBuilder.addTextBody("job_title", job_title.getText().toString());
//                multipartEntityBuilder.addTextBody("graduation_date", graduation_date.getText().toString());
//                multipartEntityBuilder.addTextBody("birth_of_date", birth_date.getText().toString());
//                multipartEntityBuilder.addTextBody("address", address.getText().toString());
//                multipartEntityBuilder.addTextBody("years_of_experience", experience.getText().toString());
//
////                    object.put("job_title", job_title.getText().toString());
////                    object.put("graduation_date", graduation_date.getText().toString());
////                    object.put("birth_of_date", birth_date.getText().toString());
////                    object.put("address", address.getText().toString());
////                    object.put("years_of_experience", experience.getText().toString());
////                    object.put("cv", attachedFile);
//
//                // Progress listener - updates task's progress
//                MyHttpEntity.ProgressListener progressListener =
//                        new MyHttpEntity.ProgressListener() {
//                            @Override
//                            public void transferred(float progress) {
//                                publishProgress((int) progress);
//                            }
//                        };
//
//                // POST
//                httpPost.setEntity(new MyHttpEntity(multipartEntityBuilder.build(),
//                        progressListener));
//
//                httpPost.addHeader("Accept", "application/json");
//                httpPost.addHeader("Accept", "multipart/form-data");
//                httpPost.addHeader("Content-Type", "application/json");
//                httpPost.addHeader("Authorization", "bearer " + token);
//
//
//                httpResponse = httpClient.execute(httpPost);
//                httpEntity = httpResponse.getEntity();
//
//                int statusCode = httpResponse.getStatusLine().getStatusCode();
//                if (statusCode == 200) {
//                    // Server response
//                    responseString = EntityUtils.toString(httpEntity);
//                    utils.print("Employee Response", httpResponse.toString());
//                } else {
//                    responseString = "Error occurred! Http Status Code: "
//                            + statusCode;
//
//                    utils.print("Employee Response", httpResponse.toString());
//                }
//            } catch (UnsupportedEncodingException | ClientProtocolException e) {
//                e.printStackTrace();
//                utils.print("Employee Response", e.getMessage());
//                Log.e("Employee Response", e.getMessage());
//                this.exception = e;
//            } catch (IOException e) {
//                utils.print("Employee Response", e.getMessage());
//                e.printStackTrace();
//            }
//
//            return responseString;
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//            // Init and show dialog
//            this.progressDialog = new ProgressDialog(this.context);
//            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            this.progressDialog.setCancelable(false);
//            this.progressDialog.show();
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            // Close dialog
//            this.progressDialog.dismiss();
//            Toast.makeText(getApplicationContext(),
//                    result, Toast.LENGTH_LONG).show();
//            utils.print("Employee Response", result);
//            showFileChooser();
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... progress) {
//            // Update process
//            this.progressDialog.setProgress((int) progress[0]);
//        }
//    }

//    String encodeFileToBase64Binary = encodeFileToBase64Binary(yourFile);

    private String encodeFileToBase64Binary(File yourFile) {
        int size = (int) yourFile.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(yourFile));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        String encoded = Base64.encodeToString(bytes,Base64.NO_WRAP);
        return encoded;
    }

}
