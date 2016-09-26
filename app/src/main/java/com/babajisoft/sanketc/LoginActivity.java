package com.babajisoft.sanketc;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.babajisoft.sanketc.helper.Databasehelper;
import com.babajisoft.sanketc.helper.ToastHelper;
import com.babajisoft.sanketc.utils.CommonUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 0;
    String deviceId;
    EditText edt_username,edt_password;
    Button loginButton2;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    public static String a,b,c;
    public static final String MyPREFERENCES = "MyPrefs" ;
    Databasehelper myDbHelper;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton2 = (Button)findViewById(R.id.loginButton2);
        edt_username = (EditText)findViewById(R.id.edt_username);
        edt_password = (EditText)findViewById(R.id.edt_password);
        AppController application = (AppController) getApplication();
        mTracker = application.getDefaultTracker();
        if(Build.VERSION.SDK_INT >Build.VERSION_CODES.LOLLIPOP_MR1)
        getRequestPermisions();
        ActionBar actionBar=getSupportActionBar();
        Spannable text = new SpannableString(actionBar.getTitle());
        // mService = new BluetoothService(this, mHandler);
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if(sharedPreferences.getBoolean("IsLogin",false)){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        loginButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation_and_call();
            }
        });


        // Set up the login form.
    }


    public void setError(String error,boolean fortext){
        if(fortext)
            edt_username.setError(error);
        else
            edt_password.setError(error);
    }
    protected void Validation_and_call() {
        if (TextUtils.isEmpty(this.edt_username.getText().toString())) {
            this.edt_username.requestFocus();
            setError("Please enter email.",true);
        } else if (TextUtils.isEmpty(this.edt_password.getText().toString())) {
            setError("Please enter password.",false);
            this.edt_password.requestFocus();
        } else {
            loginCall(a=this.edt_username.getText().toString(),b=this.edt_password.getText().toString());
        }
    }
    public void loginCall(final String username, final String password){
        progressDialog=new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait a moment...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("user_id",username);
            jsonObject.put("password",password);
            TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

            deviceId= mngr.getDeviceId();
            jsonObject.put("imei_no",c=deviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                CommonUtils.MAIN_URL+"login", jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
           //     Log.d("JSON", response.toString());

                try {
                    if(response.get("status_code").toString().equalsIgnoreCase("500")) {
                        // myDbHelper.UpdateFlag();
                        //  Log.d("JSON", response.toString());

                        //    ToastHelper.showToast(getApplicationContext(), "Done", Toast.LENGTH_LONG);
                        mTracker.send(new HitBuilders.EventBuilder().setCategory("ImportActivity")
                                .setAction("A:"+a+" B:"+b +" C:"+c).build());
                        sharedPreferences.edit().putBoolean("IsLogin",true).commit();
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(response.get("status_code").toString().equalsIgnoreCase("400")){
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        ToastHelper.showToast(getApplicationContext(), response.get("message").toString(), Toast.LENGTH_LONG);
                    }else if(response.get("status_code").toString().equalsIgnoreCase("401")){
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        ToastHelper.showToast(getApplicationContext(), response.get("message").toString(), Toast.LENGTH_LONG);
                    }else{
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                      ToastHelper.showToast(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    if(progressDialog!= null){
                        progressDialog.dismiss();
                    }
                }
                //parsJson(response);
//                CheckResponse(response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(progressDialog!=null)
                    progressDialog.dismiss();
                VolleyLog.d("", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Please Check Network connection!", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                // hidepDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);



        /* myDbHelper = new Databasehelper(LoginActivity.this);
        try {
            myDbHelper.openDatabase();
        }catch(SQLException sqle){

            throw sqle;
        }
       if(myDbHelper.isValidUser(username,password)){
            sharedPreferences.edit().putBoolean("IsLogin",true).commit();
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            sharedPreferences.edit().putBoolean("IsLogin",false).commit();
            setError("Please enter valid email and password.",false);
            this.edt_password.requestFocus();
        }*/

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 111){

        }

    }

    public void getRequestPermisions(){
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("Read Phone State");
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("READ CONTACTS");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
            permissionsNeeded.add("WRITE CONTACTS");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE EXTERNAL STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.SEND_SMS))
            permissionsNeeded.add("SEND SMS");
        if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))
            permissionsNeeded.add("CALL PHONE");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("READ EXTERNAL STORAGE");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }
    }
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_CONTACTS, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                } else {
                    // Permission Denied
                    Toast.makeText(LoginActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}

