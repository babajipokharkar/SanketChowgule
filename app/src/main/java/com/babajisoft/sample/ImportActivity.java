package com.babajisoft.sample;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.SQLException;
import android.graphics.Color;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.babajisoft.sample.dto.PersonInfoDTO;
import com.babajisoft.sample.helper.Databasehelper;
import com.babajisoft.sample.helper.ToastHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class ImportActivity extends AppCompatActivity {
ImageButton importbtn;
    ProgressDialog progressDialog;
    private Databasehelper myDbHelper;
    EditText username,password;
    Dialog dialog;
    String DeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        ActionBar actionBar=getSupportActionBar();
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        myDbHelper = new Databasehelper(ImportActivity.this);
        TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        DeviceId = mngr.getDeviceId();
        try {
            myDbHelper.openDatabase();
        }catch(SQLException sqle){
            throw sqle;
        }
        importbtn=(ImageButton)findViewById(R.id.importbtn);
        importbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //makeJsonObjectRequest();
                initpopup();
            }
        });




// Access the RequestQueue through your singleton class.
        //MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);


    }
    private void initpopup(){

        dialog = new Dialog(ImportActivity.this);

        dialog.setContentView(R.layout.login_for_export);
        dialog.setTitle("Please login to Export Data.");
        Button btnSave          = (Button) dialog.findViewById(R.id.loginButtonExport);
        username = (EditText)dialog.findViewById(R.id.edt_username_export);
        password = (EditText) dialog.findViewById(R.id.edt_password_export);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new ExportActivity.GetSerchedResult(username.getText().toString().trim(),password.getText().toString().trim()).execute();
                makeJsonObjectRequest(username.getText().toString().trim(),password.getText().toString().trim());
               // ToastHelper.showToast(ImportActivity.this,username.getText().toString()+""+password.getText().toString(),Toast.LENGTH_SHORT);
            }
        });

        dialog.show();

    }

    /**
     * Method to make json object request where json response starts wtih {
     * */
    private void makeJsonObjectRequest(String userID, String pass) {

      //  showpDialog();
        progressDialog=new ProgressDialog(ImportActivity.this);
        progressDialog.setMessage("Please wait a moment...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("user_id", userID);
            jsonObject.put("password", pass);
            jsonObject.put("imei_no", "12345678");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "http://www.vinayakabbamore.com/VoterSystem/api/getUpdatedUsers", jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("JSON", response.toString());
                if(progressDialog!= null){
                    progressDialog.dismiss();
                }
                //parsJson(response);
                CheckResponse(response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(progressDialog!=null)
                progressDialog.dismiss();
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

    public void CheckResponse(JSONObject response){
        try {
            if(response.get("status_code").toString().equalsIgnoreCase("500")) {
                myDbHelper.UpdateFlag();
                Log.d("JSON", response.toString());
                dialog.dismiss();
                ToastHelper.showToast(getApplicationContext(), "Done", Toast.LENGTH_LONG);
                parsJson(response);
            }else if(response.get("status_code").toString().equalsIgnoreCase("400")){
                ToastHelper.showToast(getApplicationContext(), response.get("message").toString(), Toast.LENGTH_LONG);
            }else if(response.get("status_code").toString().equalsIgnoreCase("401")){
                ToastHelper.showToast(getApplicationContext(), response.get("message").toString(), Toast.LENGTH_LONG);
            }else{
                ToastHelper.showToast(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void parsJson(JSONObject responce){
        JSONObject jsonObj = responce;
        JSONArray myTableArray = null;
        try {
            myTableArray = jsonObj.getJSONArray("myTable");
        }catch (Exception e){
            e.printStackTrace();
        }
        ArrayList<PersonInfoDTO> personArray = new ArrayList<PersonInfoDTO>();
        if(myTableArray != null) {

            for (int i = 0; i < myTableArray.length(); i++) {
                try {
                    JSONObject cursor = myTableArray.getJSONObject(i);
                    PersonInfoDTO personInfoDTO = new PersonInfoDTO();
                    personInfoDTO.setFamilycode(cursor.getInt("familycode")==0 ? 0 :cursor.getInt("familycode"));
                    personInfoDTO.setVibhagNo(Integer.parseInt(cursor.getString("vno"))==0 ? 0 : Integer.parseInt(cursor.getString("vno")));
                    personInfoDTO.setFullName(cursor.getString("fullname")==null ? "" :cursor.getString("fullname"));
                    personInfoDTO.setPartNo(Integer.parseInt(cursor.getString("yadibhag"))==0 ? 0 :Integer.parseInt(cursor.getString("yadibhag")));
                    personInfoDTO.setAgeSex(cursor.getString("sexage")==null ? "" : cursor.getString("sexage"));
                    personInfoDTO.setVoterNo(Integer.parseInt(cursor.getString("vno")));
                    personInfoDTO.setSectionNo(Integer.parseInt(cursor.getString("sectionno"))==0 ? 0 :Integer.parseInt(cursor.getString("sectionno")));
                    personInfoDTO.setHno(cursor.getString("hno")==null ? "" : cursor.getString("hno"));
                    personInfoDTO.setMobileNo(cursor.getString("mobile")==null ? "" : cursor.getString("mobile"));
                    personInfoDTO.setCardNo(cursor.getString("cardno")==null ? "" :cursor.getString("cardno"));
                    personInfoDTO.setEmail(cursor.getString("email")==null ? "" :cursor.getString("email"));
                    personInfoDTO.setDob(cursor.getString("dob")==null ? "" :cursor.getString("dob"));
                    personInfoDTO.setAliveDead(cursor.getString("alivedead")==null ? "" :cursor.getString("alivedead"));
                    personInfoDTO.setVotedone(Integer.parseInt(cursor.getString("voting"))==0 ? 0 : Integer.parseInt(cursor.getString("voting")));
                    personInfoDTO.setId(Integer.parseInt(cursor.getString("_id"))==0 ? 0 : Integer.parseInt(cursor.getString("_id")));
                    //personInfoDTO.setVotedone(0);

                    personInfoDTO.setNewAddr(cursor.getString("newadd")==null ? "" : cursor.getString("newadd"));
                    personInfoDTO.setJat(cursor.getString("jat")==null ? "" : cursor.getString("jat"));
                    personInfoDTO.setLastName(cursor.getString("surname")==null ? "" : cursor.getString("surname"));
                    personInfoDTO.setFirstName(cursor.getString("Name")==null ? "" : cursor.getString("Name"));
                    personInfoDTO.setMiddleName(cursor.getString("middle")==null ? "" : cursor.getString("middle"));
                    personArray.add(personInfoDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
          myDbHelper.updateRecord(personArray);
        }

    }
}
