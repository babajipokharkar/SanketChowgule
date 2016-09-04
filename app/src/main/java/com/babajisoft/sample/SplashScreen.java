package com.babajisoft.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.babajisoft.sample.helper.Databasehelper;

import java.io.IOException;

public class SplashScreen extends AppCompatActivity {
Button goButton;
    Databasehelper myDbHelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        goButton=(Button)findViewById(R.id.gobutton);
        myDbHelper = new Databasehelper(SplashScreen.this);

        if(!myDbHelper.checkDataBase()){
            new ExtractDatabase().execute();
        }
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SplashScreen.this,LoginActivity.class);
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
            System.out.println("Done......");
        }
    }
}
