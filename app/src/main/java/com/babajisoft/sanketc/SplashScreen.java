package com.babajisoft.sanketc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.babajisoft.sanketc.helper.Databasehelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.IOException;

import static android.R.attr.name;

public class SplashScreen extends AppCompatActivity {
Button goButton,bdaybtn;
    Databasehelper myDbHelper ;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        goButton=(Button)findViewById(R.id.gobutton);
        bdaybtn=(Button)findViewById(R.id.Birthdaybutton);
        myDbHelper = new Databasehelper(SplashScreen.this);
        AppController application = (AppController) getApplication();
        mTracker = application.getDefaultTracker();


        Log.i("SplashScreen", "Enter in MainActivity: " + name);
        mTracker.setScreenName("SplashScreen" +name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        if(!myDbHelper.checkDataBase()){
            new ExtractDatabase().execute();
        }
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Splashscreen")
                        .setAction("GoButton Click")
                        .build());
                Intent intent=new Intent(SplashScreen.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        bdaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Splashscreen")
                        .setAction("Birthday Click")
                        .build());
                Intent intent=new Intent(SplashScreen.this,BirthdayActivity.class);
                startActivity(intent);
            }
        });
    }

    ProgressDialog progressDialog;
    private class ExtractDatabase extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(SplashScreen.this);
            progressDialog.setMessage("Please wait a moment...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {

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
            // System.out.println("Data Delete from MyTable"+myDbHelper.deleteRecords());
            System.out.println("Data count from MyTable"+myDbHelper.getCount());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(progressDialog != null){
                progressDialog.dismiss();
            }
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Splashscreen")
                    .setAction("DataBase Copied")
                    .build());
            System.out.println("Done......");
        }
    }
}
