package com.babajisoft.sample.helper;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.babajisoft.sample.R;

import java.util.ArrayList;


/**
 * Created by s5 on 10/8/16.
 */
public class SendSmsHelper {
    public static BroadcastReceiver broadcastReceiver;

    public static void sendSMS(final Context context, String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        // PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,new Intent(SENT), 0);
        // PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,new Intent(DELIVERED), 0);

/*
        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                new Intent(context, SmsSentReceiver.class), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(context, SmsDeliveredReceiver.class), 0);*/
      /*  broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        ToastHelper.showToast(context, context.getResources().getString(R.string.SMS_SENT), 1);
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        ToastHelper.showToast(context, context.getResources().getString(R.string.SMS_NOT_SENT), 1);
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        ToastHelper.showToast(context, context.getResources().getString(R.string.SMS_NOT_SENT), 1);
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        ToastHelper.showToast(context, context.getResources().getString(R.string.SMS_NOT_SENT), 1);
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        ToastHelper.showToast(context, context.getResources().getString(R.string.SMS_NOT_SENT), 1);
                        break;
                }
            }
        };
        //---when the SMS has been sent---
        context.registerReceiver(broadcastReceiver, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        ToastHelper.showToast(context,context.getResources().getString(R.string.SMS_DELIVERD), 1);
                        break;
                    case Activity.RESULT_CANCELED:
                        ToastHelper.showToast(context, context.getResources().getString(R.string.SMS_NOT_DELIVERD), 1);
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
       // sms.sendMultipartTextMessage();
        sms.sendMultipartTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }
    public static void unRegisterReceiver(Context context){
      //  if(broadcastReceiver!=null)
       //   context.unregisterReceiver(broadcastReceiver);
    }*/


        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                new Intent(context, SmsSentReceiver.class), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(context, SmsDeliveredReceiver.class), 0);
        try {
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> mSMSMessage = sms.divideMessage(message);
            for (int i = 0; i < mSMSMessage.size(); i++) {
                sentPendingIntents.add(i, sentPI);

                deliveredPendingIntents.add(i, deliveredPI);
            }
            sms.sendMultipartTextMessage(phoneNumber, null, mSMSMessage,
                    sentPendingIntents, deliveredPendingIntents);

        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(context, "SMS sending failed...",
                    Toast.LENGTH_SHORT).show();
        }
    }
}