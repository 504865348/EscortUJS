package com.joshua.escortujs.receiver;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class ExitReceiver extends BroadcastReceiver {
    public ExitReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context != null) {
            if (context instanceof Activity) {
                ((Activity) context).finish();
            } else if (context instanceof Service) {
                ((Service) context).stopSelf();
            }
        }
    }
}
