package com.babajisoft.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.babajisoft.sample.adapter.VotersAdapter;
import com.babajisoft.sample.dto.PersonInfoDTO;
import com.babajisoft.sample.helper.Databasehelper;
import com.babajisoft.sample.helper.SendSmsHelper;
import com.babajisoft.sample.helper.ToastHelper;

import java.io.IOException;
import java.util.ArrayList;

import static android.support.v7.app.AlertDialog.*;

public class SearchActivity extends AppCompatActivity {
    ImageButton searchButton, editButton, btnsms, btncall, btnfamily;
    EditText input_lastname, input_layout_name, input_layout_middlename;
    String lastName, name, middleName;
    Databasehelper myDbHelper;
    ListView listView;
    ArrayList<PersonInfoDTO> votersSearchinfo;
    PersonInfoDTO selectedperson;
    ImageButton btninfo;
    VotersAdapter votersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView = (ListView) findViewById(R.id.listView);
        int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        ActionBar actionBar=getSupportActionBar();
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        searchButton = (ImageButton) findViewById(R.id.searchButton);
        btncall = (ImageButton) findViewById(R.id.btncall);
        btnsms = (ImageButton) findViewById(R.id.btnsms);
        input_lastname = (EditText) findViewById(R.id.input_lastname);
        input_layout_name = (EditText) findViewById(R.id.input_name);
        editButton = (ImageButton) findViewById(R.id.btnedit);
        btnfamily = (ImageButton) findViewById(R.id.btnfamily);
        input_layout_middlename = (EditText) findViewById(R.id.middle_name);
        btninfo = (ImageButton) findViewById(R.id.btninfo);
        getSupportActionBar().setHomeButtonEnabled(true);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedperson != null) {
                    Intent i = new Intent(SearchActivity.this, EditDetailsActivity.class);
                    i.putExtra("fullname", selectedperson.getFullName());
                    i.putExtra("voterno", selectedperson.getVoterNo());
                    i.putExtra("mobile", selectedperson.getMobileNo());
                    i.putExtra("email", selectedperson.getEmail());
                    String address;
                    if(selectedperson.getNewAddr() == null || selectedperson.getNewAddr().equalsIgnoreCase("")){
                        address = myDbHelper.getAddress(selectedperson.getSectionNo());
                    }else{
                        address = selectedperson.getNewAddr();
                    }
                    i.putExtra("address", address);
                    i.putExtra("dob", selectedperson.getDob());
                    i.putExtra("aliveDead", selectedperson.getAliveDead());
                    i.putExtra("sectionNo",selectedperson.getSectionNo());
                    i.putExtra("yadibhagNo", selectedperson.getPartNo());
                    startActivityForResult(i, 111, null);
                }
            }
        });
        myDbHelper = new Databasehelper(this);
        try {
            myDbHelper.openDatabase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        //    System.out.println("Data count from VoterInfo**********"+myDbHelper.getVoterInfo().size());
        //final ArrayList<PersonInfoDTO> voterinfo=myDbHelper.getVoterInfo();


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedperson = null;
                lastName = input_lastname.getText().toString() == null ? "" : input_lastname.getText().toString();
                name = input_layout_name.getText().toString() == null ? "" : input_layout_name.getText().toString();
                middleName = input_layout_middlename.getText().toString() == null ? "" : input_layout_middlename.getText().toString();
                if (lastName.equalsIgnoreCase("") && name.equalsIgnoreCase("") && middleName.equalsIgnoreCase("")) {
                    ToastHelper.showToast(SearchActivity.this, "Please enter Name", Toast.LENGTH_LONG);
                } else {
                   new GetSerchedResult().execute();
                }
            }
        });
        btnfamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedperson != null) {
                    votersSearchinfo = myDbHelper.getFamilyInfobyVoter(selectedperson);
                    if (votersSearchinfo != null) {
                        votersAdapter = new VotersAdapter(SearchActivity.this, R.layout.votorsinfolist_item, votersSearchinfo);
                        listView.invalidate();
                        listView.setAdapter(votersAdapter);
                    }
                } else {
                    ToastHelper.showToast(SearchActivity.this, "Please select Name", Toast.LENGTH_LONG);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectedperson = votersSearchinfo.get(position);
                // When clicked, show a toast with the TextView text
/*                Toast.makeText(getApplicationContext(), selectedperson.getFullName(),
                        Toast.LENGTH_SHORT).show();*/
            }
        });
        btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedperson != null) {
                    if (isValidMobile(selectedperson.getMobileNo())) {
                        call(selectedperson.getMobileNo());
                    } else {
                        ToastHelper.showToast(getApplicationContext(), "Please Check mobile number", Toast.LENGTH_SHORT);
                    }
                } else {
                    ToastHelper.showToast(getApplicationContext(), "Please Select person", Toast.LENGTH_SHORT);
                }
            }
        });
        btnsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedperson != null) {
                    if (isValidMobile(selectedperson.getMobileNo())) {
                     //   final String address = myDbHelper.getAddress(selectedperson.getSectionNo());
                        final String booth = myDbHelper.getBooth(selectedperson.getPartNo());

                            sendSMS(booth);

//                        SendSmsHelper.sendSMS(SearchActivity.this, selectedperson.getMobileNo(), "" + selectedperson.getFullName());

                    } else {
                        ToastHelper.showToast(getApplicationContext(), "Please Check mobile number", Toast.LENGTH_SHORT);
                    }
                } else {
                    ToastHelper.showToast(getApplicationContext(), "Please Select person", Toast.LENGTH_SHORT);
                }
            }
        });
        btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedperson != null) {
                    LayoutInflater inflater = getLayoutInflater();
                    View dialoglayout = inflater.inflate(R.layout.person_details_view, null);
                    TextView txtpartno = (TextView) dialoglayout.findViewById(R.id.txtpartno);
                    TextView txtFullname = (TextView) dialoglayout.findViewById(R.id.txtname);
                    TextView txtvoterno = (TextView) dialoglayout.findViewById(R.id.txtvoterno);
                    TextView txthno = (TextView) dialoglayout.findViewById(R.id.txthno);
                    TextView txtaddress = (TextView) dialoglayout.findViewById(R.id.txtaddress);
                    TextView txtBooth = (TextView) dialoglayout.findViewById(R.id.txtBooth);
                    TextView txtMo = (TextView) dialoglayout.findViewById(R.id.txtMo);
                    TextView txtId = (TextView) dialoglayout.findViewById(R.id.txtId);
                    String address;
                    if(selectedperson.getNewAddr()==null ||selectedperson.getNewAddr().equals("") ) {
                        address = myDbHelper.getAddress(selectedperson.getSectionNo());
                    }else{
                        address=selectedperson.getNewAddr();
                    }
                    final String booth = myDbHelper.getBooth(selectedperson.getPartNo());
                    txtFullname.setText(selectedperson.getFullName());
                    txtvoterno.setText(selectedperson.getVoterNo() + "");
                    txtpartno.setText(selectedperson.getPartNo() + "");
                    txthno.setText(selectedperson.getHno() + "");
                    txtMo.setText(selectedperson.getMobileNo() + "");
                    txtBooth.setText(booth + "");
                    txtId.setText(selectedperson.getCardNo() + "");
                    txtaddress.setText(address);
                    Builder builder = new Builder(SearchActivity.this);
                    builder.setView(dialoglayout).setPositiveButton("Send SMS", new OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (isValidMobile(selectedperson.getMobileNo())) {
                                sendSMS(booth);
                               /* SendSmsHelper.sendSMS(SearchActivity.this, selectedperson.getMobileNo(),selectedperson.getFullName()
                                        + "\n" + booth
                                        + "\n CNo:" + selectedperson.getCardNo()
                                        + "\n PNo:" + selectedperson.getPartNo()
                                        + "\n SrNo:" + selectedperson.getId());
*/
                            } else {
                                ToastHelper.showToast(getApplicationContext(), "Please Check mobile number", Toast.LENGTH_SHORT);
                            }
                        }
                    });
                    builder.show();
                    Toast.makeText(getApplicationContext(), selectedperson.getFullName(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Selecte Person", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // http://developer.android.com/intl/zh-TW/resources/tutorials/vi

/*
        VotersAdapter votersAdapter=new VotersAdapter(this,R.layout.votorsinfolist_item,voterinfo);
        listView.setAdapter(votersAdapter);*/


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 111) {
                if (selectedperson != null) {
                    int itemIndex = votersSearchinfo.indexOf(selectedperson);
                    int voterNo = selectedperson.getVoterNo();
                    selectedperson = myDbHelper.getPerticularVoterInfo(voterNo);
                    votersSearchinfo.set(itemIndex, selectedperson);
                }
            }
        } catch (Exception e) {

        }
    }

    private static boolean isValidMobile(String mobile) {
        return !TextUtils.isEmpty(mobile) && Patterns.PHONE.matcher(mobile).matches();
    }

    private void call(String mobile) {
        Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
        try {
            startActivity(in);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // SendSmsHelper.unRegisterReceiver(SearchActivity.this);
    }
    ProgressDialog progressDialog;

    private class GetSerchedResult extends AsyncTask<ArrayList<PersonInfoDTO>, Void, ArrayList<PersonInfoDTO>> {
        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(SearchActivity.this);
            progressDialog.setMessage("Please wait a moment...");
            progressDialog.show();
        }
        @Override
        protected ArrayList<PersonInfoDTO> doInBackground(ArrayList<PersonInfoDTO>... params) {
          return votersSearchinfo = myDbHelper.getSearchedVotersInfo(lastName, name, middleName);
        }

        @Override
        protected void onPostExecute(ArrayList<PersonInfoDTO> result) {
            if(progressDialog != null){
                progressDialog.dismiss();
            }
            if (result != null) {
                if (result != null) {
                    votersAdapter = new VotersAdapter(SearchActivity.this, R.layout.votorsinfolist_item, result);
                    listView.setAdapter(votersAdapter);
                }
            }
        }
    }


public void sendSMS(String booth){
    SendSmsHelper.sendSMS(SearchActivity.this, selectedperson.getMobileNo(),selectedperson.getFullName()
            + "\n" + booth
            + "\n CNo:" + selectedperson.getCardNo()
            + "\n PNo:" + selectedperson.getPartNo()
            + "\n SrNo:" + selectedperson.getId());
}
}

