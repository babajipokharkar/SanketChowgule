package com.babajisoft.sanketc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


import static android.R.attr.name;

public class MainActivity extends AppCompatActivity {
  LinearLayout searchLayout,smsLayout,importLayout,exportLayout,aboutUsLayout,votingLayout;
    Tracker mTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppController application = (AppController) getApplication();
        mTracker = application.getDefaultTracker();

        Log.i("MainActivity", "Enter in MainActivity: " + name);
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("MainActivity")
                .setAction("Successfull on Dashboard")
                .build());
// Using StandardExceptionParser to get an Exception description.


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
