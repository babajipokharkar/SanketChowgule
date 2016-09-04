package com.babajisoft.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.babajisoft.sample.adapter.VotersAdapter;
import com.babajisoft.sample.dto.PersonInfoDTO;
import com.babajisoft.sample.helper.Databasehelper;


import java.io.IOException;
import java.util.ArrayList;

import static android.R.attr.name;

public class MainActivity extends AppCompatActivity {
  LinearLayout searchLayout,smsLayout,importLayout,exportLayout,aboutUsLayout,votingLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchLayout=(LinearLayout)findViewById(R.id.seach_layout);
        smsLayout=(LinearLayout)findViewById(R.id.message_layout);
        importLayout=(LinearLayout)findViewById(R.id.import_layout);
        exportLayout=(LinearLayout)findViewById(R.id.export_layout);
        aboutUsLayout=(LinearLayout)findViewById(R.id.aboutus_layout);
        votingLayout=(LinearLayout)findViewById(R.id.voting_layout);

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
        smsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,MessageActivity.class);
                startActivity(intent);
            }
        });

        aboutUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AboutUsActivity.class);
                startActivity(intent);
            }
        });

        votingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,VotingActivity.class);
                startActivity(intent);
            }
        });
        importLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ImportActivity.class);
                startActivity(intent);
            }
        });
        exportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ExportActivity.class);
                startActivity(intent);
            }
        });

    }

}
