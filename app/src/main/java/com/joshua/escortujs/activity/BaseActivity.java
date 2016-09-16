package com.joshua.escortujs.activity;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;

import com.joshua.escortujs.receiver.ExitReceiver;

import org.xutils.x;


public class BaseActivity extends Activity {

    private ExitReceiver exitReceiver;
    public static String EXIT_APP_ACTION = "com.joshua.exit";


    /**
     * 注册退出广播
     */
    private void registerExitReceiver() {
        IntentFilter exitFilter = new IntentFilter();
        exitFilter.addAction(EXIT_APP_ACTION);
        registerReceiver(exitReceiver, exitFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        exitReceiver = new ExitReceiver();
        registerExitReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterExitReceiver();
    }

    /**
     * 注销退出广播
     */
    private void unRegisterExitReceiver() {
        unregisterReceiver(exitReceiver);
    }

}
