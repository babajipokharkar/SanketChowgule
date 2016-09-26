package com.babajisoft.sanketc.helper;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by s5 on 10/8/16.
 */
public class ToastHelper {
    public static void showToast(Context context,String messageString,int toastLength){
        Toast toast= Toast.makeText(context,messageString,toastLength);
        if(context!=null){
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
