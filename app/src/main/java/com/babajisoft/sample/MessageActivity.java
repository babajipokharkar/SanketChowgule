package com.babajisoft.sample;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.babajisoft.sample.helper.SendSmsHelper;
import com.babajisoft.sample.helper.ToastHelper;

public class MessageActivity extends AppCompatActivity {

    EditText mobileno,message;
    Button sendSms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ActionBar actionBar=getSupportActionBar();
        mobileno=(EditText)findViewById(R.id.mobileno);
        message=(EditText)findViewById(R.id.message);
        sendSms=(Button)findViewById(R.id.sendSms);
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validCellPhone(mobileno.getText().toString().trim())) {
                    SendSmsHelper smsHelper = new SendSmsHelper();
                    smsHelper.sendSMS(MessageActivity.this, mobileno.getText().toString().trim(), message.getText().toString().trim());
                }else{
                    ToastHelper.showToast(getApplicationContext(),"Please enter valid mobile number", Toast.LENGTH_LONG);
                }
            }
        });
    }
    public boolean validCellPhone(String number)
    {
        if(number.length() == 10 ||number.length() == 0){
            return true;
        }else {
            return false;
        }

    }
}
