package com.babajisoft.sample;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.babajisoft.sample.helper.ToastHelper;

public class AboutUsActivity extends AppCompatActivity {

    TextView DevelopedByTV,MarketedByTV,RightsTV;
    Button callButton1, callButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ActionBar actionBar=getSupportActionBar();
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        DevelopedByTV = (TextView)findViewById(R.id.textView16);
        MarketedByTV = (TextView)findViewById(R.id.textView14);
        RightsTV = (TextView)findViewById(R.id.textView10);
        callButton2 = (Button)findViewById(R.id.callButton2);
        callButton1 = (Button)findViewById(R.id.callButton1);

        callButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidMobile(callButton1.getText().toString().trim())) {
                    call(callButton1.getText().toString().trim());
                } else {
                    ToastHelper.showToast(getApplicationContext(), "Please Check mobile number", Toast.LENGTH_SHORT);
                }
            }
        });
        callButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidMobile(callButton2.getText().toString().trim())) {
                    call(callButton2.getText().toString().trim());
                } else {
                    ToastHelper.showToast(getApplicationContext(), "Please Check mobile number", Toast.LENGTH_SHORT);
                }
            }
        });

        String mystring1 = new String("Developed By");
        SpannableString content = new SpannableString(mystring1);
        content.setSpan(new UnderlineSpan(), 0, mystring1.length(), 0);
        DevelopedByTV.setText(content);

        String mystring2 = new String("Marketed By");
        SpannableString content1 = new SpannableString(mystring2);
        content1.setSpan(new UnderlineSpan(), 0, mystring2.length(), 0);
        MarketedByTV.setText(content1);

        String mystring3 = new String("All Rights Reserved for Vinayak More");
        SpannableString content3 = new SpannableString(mystring3);
        content3.setSpan(new UnderlineSpan(), 0, mystring3.length(), 0);
        RightsTV.setText(content3);

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
}
