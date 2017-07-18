package com.palanceli.ime.androidimesample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by palance on 2017/7/18.
 */

public class IMChangedReceiver extends BroadcastReceiver {
    public IMChangedReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent){
        Toast.makeText(context, "接收到IMChanged广播", Toast.LENGTH_SHORT).show();
    }
}
