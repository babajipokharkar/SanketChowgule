package com.babajisoft.sanketc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainSplashScreen extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_splash_screen);

        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(5*1000);

                    sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                    /*if(sharedPreferences.getBoolean("IsLogin",false)){
                        startActivity(new Intent(getBaseContext(),MainActivity.class));
                    }else {*/
                        Intent i = new Intent(getBaseContext(), SplashScreen.class);
                        startActivity(i);
                    //}

                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
