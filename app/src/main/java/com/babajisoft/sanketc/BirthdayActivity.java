package com.babajisoft.sanketc;

import android.app.ProgressDialog;
import android.database.SQLException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.babajisoft.sanketc.adapter.VotersAdapter;
import com.babajisoft.sanketc.dto.PersonInfoDTO;
import com.babajisoft.sanketc.helper.Databasehelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by babaji on 6/9/16.
 */

public class BirthdayActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<PersonInfoDTO> votersSearchinfo;
    VotersAdapter votersAdapter;
    Databasehelper myDbHelper;
    String currentDate;
    int CurrentDay,currenMonth;
    TextView NoBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.birthday_activity);

        listView = (ListView)findViewById(R.id.bdaylistView);
        NoBirthday = (TextView)findViewById(R.id.Nobirthday);
        myDbHelper = new Databasehelper(this);
        try {
            myDbHelper.openDatabase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        ActionBar actionBar=getSupportActionBar();
        Spannable text = new SpannableString(actionBar.getTitle());
        // mService = new BluetoothService(this, mHandler);
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        currentDate = df.format(c.getTime());
        CurrentDay = c.get(Calendar.DAY_OF_MONTH);
        currenMonth = 9;//c.get(Calendar.MONTH)+1;

        /*final Calendar c1 = Calendar.getInstance();
        //mYear = c.get(Calendar.YEAR)-18;
        currenMonth = c1.get(Calendar.MONTH)+1;
        CurrentDay = c1.get(Calendar.DAY_OF_MONTH);*/

        new GetSerchedResult().execute();

    }

    ProgressDialog progressDialog;
    private class GetSerchedResult extends AsyncTask<ArrayList<PersonInfoDTO>, Void, ArrayList<PersonInfoDTO>> {
        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(BirthdayActivity.this);
            progressDialog.setMessage("Please wait a moment...");
            progressDialog.show();
        }
        @Override
        protected ArrayList<PersonInfoDTO> doInBackground(ArrayList<PersonInfoDTO>... params) {
            return votersSearchinfo = myDbHelper.getBirthdayVoterInfo(currentDate,CurrentDay,currenMonth);
        }

        @Override
        protected void onPostExecute(ArrayList<PersonInfoDTO> result) {
            if(progressDialog != null){
                progressDialog.dismiss();
            }
            if (result != null) {
                if (result.size()>0 ) {
                    votersAdapter = new VotersAdapter(BirthdayActivity.this, R.layout.votorsinfolist_item, result);
                    listView.setAdapter(votersAdapter);
                    NoBirthday.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }else{
                    NoBirthday.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
            }
        }
    }
}
