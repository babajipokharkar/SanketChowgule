package com.babajisoft.sanketc.helper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;

import com.babajisoft.sanketc.R;

/**
 * Created by babaji on 2/9/16.
 */

public class SmsSentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                Toast.makeText(context,
                        R.string.SMS_SENT,
                        Toast.LENGTH_SHORT).show();

                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                Toast.makeText(context, R.string.SMS_NOT_SENT, Toast.LENGTH_SHORT)
                        .show();

                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                Toast.makeText(context, R.string.SMS_NOT_SENT, Toast.LENGTH_SHORT)
                        .show();

                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                Toast.makeText(context, R.string.SMS_NOT_SENT, Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                Toast.makeText(context, R.string.SMS_NOT_SENT, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}