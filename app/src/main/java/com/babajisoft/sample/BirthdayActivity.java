package com.babajisoft.sample;

import android.app.ProgressDialog;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.babajisoft.sample.adapter.VotersAdapter;
import com.babajisoft.sample.dto.PersonInfoDTO;
import com.babajisoft.sample.helper.Databasehelper;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.birthday_activity);

        listView = (ListView)findViewById(R.id.bdaylistView);
        myDbHelper = new Databasehelper(this);
        try {
            myDbHelper.openDatabase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        currentDate = df.format(c.getTime());
        CurrentDay = c.get(Calendar.DAY_OF_MONTH);
        currenMonth = c.get(Calendar.MONTH);

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
                }else{

                }
            }
        }
    }
}
