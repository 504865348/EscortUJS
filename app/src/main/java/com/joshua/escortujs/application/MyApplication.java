package com.joshua.escortujs.application;

import android.app.Application;

import org.xutils.BuildConfig;
import org.xutils.x;

/**
 * ============================================================
 * <p/>
 * 版 权 ： 吴奇俊  (c) 2016
 * <p/>
 * 作 者 : 吴奇俊
 * <p/>
 * 版 本 ： 1.0
 * <p/>
 * 创建日期 ： 2016/9/16 10:26
 * <p/>
 * 描 述 ：
 * <p/>
 * ============================================================
 **/
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}
