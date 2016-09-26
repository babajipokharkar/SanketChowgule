package com.babajisoft.sanketc;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.SQLException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.babajisoft.sanketc.dto.PersonInfoDTO;
import com.babajisoft.sanketc.helper.Databasehelper;
import com.babajisoft.sanketc.helper.ToastHelper;
import com.babajisoft.sanketc.utils.CommonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExportActivity extends AppCompatActivity {

    private Databasehelper myDbHelper;
    ImageButton uploaddata;
    private PopupWindow pwindo;
    private Button loginButtonpPopup;
    EditText username,password;
    Dialog dialog;
    String DeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        ActionBar actionBar=getSupportActionBar();
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        uploaddata=(ImageButton)findViewById(R.id.uploaddata);
        myDbHelper = new Databasehelper(ExportActivity.this);
        try {
            myDbHelper.openDatabase();
        }catch(SQLException sqle){
            throw sqle;
        }
        TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        DeviceId = mngr.getDeviceId();
        uploaddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // new GetSerchedResult().execute();
                /*final Dialog dialog = new Dialog(ExportActivity.this);

                dialog.setContentView(R.layout.login_for_export);
                dialog.setTitle("Please login to Export Data.");
                Button btnSave          = (Button) dialog.findViewById(R.id.loginButtonExport);

                dialog.show();*/

                initpopup();
            }

        });

      // JSONArray jsonarray= myDbHelper.getJsonFromLocal();
      //  System.out.println("JSON Array:"+jsonarray);
    }
    ProgressDialog progressDialog;

    private class GetSerchedResult extends AsyncTask<ArrayList<PersonInfoDTO>, Void, JSONArray> {
        String userName,Password;

        public GetSerchedResult(String username, String password){
            this.userName = username;
            this.Password = password;
        }

        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(ExportActivity.this);
            progressDialog.setMessage("Please wait a moment...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected JSONArray doInBackground(ArrayList<PersonInfoDTO>... params) {
            return myDbHelper.getJsonFromLocal();
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            System.out.println("JSON:"+result.toString());
            try {
                if (result.length() >= 1) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("user_id", userName);
                    jsonObject.put("password", Password);
                    jsonObject.put("imei_no", DeviceId);

                    makeJsonObjectRequest(jsonObject.put("myTable", (Object) result));
                }else{
                    ToastHelper.showToast(getApplicationContext(),"No Data for Update",Toast.LENGTH_LONG);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*      if (result != null) {
                if (result != null) {
                    votersAdapter = new VotersAdapter(SearchActivity.this, R.layout.votorsinfolist_item, result);
                    listView.setAdapter(votersAdapter);
                }
            }*/
        }
    }

    private void initpopup(){

        dialog = new Dialog(ExportActivity.this);

        dialog.setContentView(R.layout.login_for_export);
        dialog.setTitle("Please login to Export Data.");
        Button btnSave          = (Button) dialog.findViewById(R.id.loginButtonExport);
        username = (EditText)dialog.findViewById(R.id.edt_username_export);
        password = (EditText) dialog.findViewById(R.id.edt_password_export);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetSerchedResult(username.getText().toString().trim(),password.getText().toString().trim()).execute();

                //ToastHelper.showToast(ExportActivity.this,username.getText().toString()+""+password.getText().toString(),Toast.LENGTH_SHORT);
            }
        });

        dialog.show();
    }

    private void makeJsonObjectRequest(JSONObject json) {
        //  showpDialog();
        //UpdateRecords
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                CommonUtils.MAIN_URL+"UpdateRecords", json, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if(progressDialog != null){
                    progressDialog.dismiss();
                }
                try {
                    if(response.get("status_code").toString().equalsIgnoreCase("500")) {
                        myDbHelper.UpdateFlag();
                        Log.d("JSON", response.toString());
                        dialog.dismiss();
                        ToastHelper.showToast(getApplicationContext(), "Done", Toast.LENGTH_LONG);
                    }else if(response.get("status_code").toString().equalsIgnoreCase("400")){
                        ToastHelper.showToast(getApplicationContext(), response.get("message").toString(), Toast.LENGTH_LONG);
                    }else if(response.get("status_code").toString().equalsIgnoreCase("401")){
                        ToastHelper.showToast(getApplicationContext(), response.get("message").toString(), Toast.LENGTH_LONG);
                    }else{
                        ToastHelper.showToast(getApplicationContext(), "Somethings went Wrong...", Toast.LENGTH_LONG);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

               /* try {
                    // Parsing json object response
                   *//* // response will be a json object
                    String name = response.getString("name");
                    String email = response.getString("email");
                    JSONObject phone = response.getJSONObject("phone");
                    String home = phone.getString("home");
                    String mobile = phone.getString("mobile");

                    jsonResponse = "";
                    jsonResponse += "Name: " + name + "\n\n";
                    jsonResponse += "Email: " + email + "\n\n";
                    jsonResponse += "Home: " + home + "\n\n";
                    jsonResponse += "Mobile: " + mobile + "\n\n";

                    txtResponse.setText(jsonResponse);*//*

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
             //   hidepDialog();*/
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(progressDialog != null){
                    progressDialog.dismiss();
                }
                VolleyLog.d("", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                // hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}
