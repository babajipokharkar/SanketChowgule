package com.babajisoft.sample;

import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.babajisoft.sample.adapter.VotersAdapter;
import com.babajisoft.sample.dto.PersonInfoDTO;
import com.babajisoft.sample.helper.Databasehelper;

import java.io.IOException;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ImageButton searchButton;
    EditText input_lastname,input_layout_name,input_layout_middlename;
    String lastName,name,middleName;
    Databasehelper myDbHelper ;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView=(ListView)findViewById(R.id.listView);
        searchButton=(ImageButton)findViewById(R.id.searchButton);
        input_lastname=(EditText)findViewById(R.id.input_lastname);
        input_layout_name=(EditText)findViewById(R.id.input_name);
        input_layout_middlename=(EditText)findViewById(R.id.middle_name);

        getSupportActionBar().setHomeButtonEnabled(true);

        myDbHelper = new Databasehelper(this);
        try {
            myDbHelper.openDatabase();
        }catch(SQLException sqle){

            throw sqle;
        }
        System.out.println("Data count from VoterInfo**********"+myDbHelper.getVoterInfo().size());
        final ArrayList<PersonInfoDTO> voterinfo=myDbHelper.getVoterInfo();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastName=input_lastname.getText().toString() ==null ? "" : input_lastname.getText().toString();
                name=input_layout_name.getText().toString() ==null ? "" : input_layout_name.getText().toString();
                middleName=input_layout_middlename.getText().toString() ==null ? "" : input_layout_middlename.getText().toString();
                ArrayList<PersonInfoDTO> votersSearchinfo=myDbHelper.getSearchedVotersInfo(lastName,name,middleName);
                if(votersSearchinfo!=null && votersSearchinfo.size() >=1){
                    VotersAdapter votersAdapter=new VotersAdapter(SearchActivity.this,R.layout.votorsinfolist_item,votersSearchinfo);
                    listView.setAdapter(votersAdapter);
                }
            }
        });
/*
        VotersAdapter votersAdapter=new VotersAdapter(this,R.layout.votorsinfolist_item,voterinfo);
        listView.setAdapter(votersAdapter);*/


    }
}
