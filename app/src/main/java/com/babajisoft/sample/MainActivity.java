package com.babajisoft.sample;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.babajisoft.sample.helper.Databasehelper;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
  LinearLayout searchLayout,smsLayout,importLayout,exportLayout,aboutUsLayout,votingLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchLayout=(LinearLayout)findViewById(R.id.seach_layout);
        smsLayout=(LinearLayout)findViewById(R.id.message_layout);
        importLayout=(LinearLayout)findViewById(R.id.import_layout);
        exportLayout=(LinearLayout)findViewById(R.id.export_layout);
        aboutUsLayout=(LinearLayout)findViewById(R.id.aboutus_layout);
        votingLayout=(LinearLayout)findViewById(R.id.voting_layout);
        Databasehelper myDbHelper ;
        myDbHelper = new Databasehelper(this);
        try {
            myDbHelper.createDatabase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDatabase();
        }catch(SQLException sqle){

            throw sqle;
        }
        System.out.println("Data Delete from MyTable"+myDbHelper.deleteRecords());
        System.out.println("Data count from MyTable"+myDbHelper.getCount());
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

    }
}
