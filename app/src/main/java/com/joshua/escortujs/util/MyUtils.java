package com.joshua.escortujs.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.SmsManager;
import android.text.format.Formatter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * ============================================================
 * <p/>
 * 版 权 ： 吴奇俊  (c) 2016
 * <p/>
 * 作 者 : 吴奇俊
 * <p/>
 * 版 本 ： 1.0
 * <p/>
 * 创建日期 ： 2016/8/2 17:29
 * <p/>
 * 描 述 ：个人工具类
 * <p/>
 * ============================================================
 **/
public class MyUtils extends Activity {
    public static String TAG="com.joshua.log";
    private static int SD_TOTAL_MEMORY = 0;//获取SD卡全部内存
    private static int SD_REMAIN_MEMORY = 1;//获取SD卡剩余内存

    /**
     * 发送短信
     * <uses-permission android:name="android.permission.SEND_SMS"/>
     *
     * @param address 地址
     * @param content 内容
     */
    public static void sendSms(String address, String content) {
        SmsManager manager = SmsManager.getDefault();
        ArrayList<String> smsList = manager.divideMessage(content);
        for (String sms : smsList) {
            manager.sendTextMessage(address, null, sms, null, null);
        }
    }

    /**
     * 拨打电话
     * <uses-permission android:name="android.permission.CALL_PHONE"/>
     *
     * @param number 号码
     * @return 用于调用拨号界面的intent
     */
    public static Intent callNumber(String number) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        return intent;
    }

    /**
     * 判断SD卡片是否挂载
     *
     * @return boolean
     */
    public static boolean isSDCardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡容量
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
     *
     * @param activity 上下文
     * @param choice   SD_TOTAL_MEMORY=0：全部容量
     *                 SD_REMAIN_MEMORY=1：剩余容量
     * @return 容量
     */
    public static String getSDcardMemory(Activity activity, int choice) {
        File sdCard = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(sdCard.getPath());
        long blockSize;
        long totalBlocks;
        long availableBlocks;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = statFs.getBlockSizeLong();
            availableBlocks = statFs.getAvailableBlocksLong();
            totalBlocks = statFs.getBlockCountLong();
        } else {
            blockSize = statFs.getBlockSize();
            totalBlocks = statFs.getBlockCount();
            availableBlocks = statFs.getAvailableBlocks();
        }
        if (choice == SD_TOTAL_MEMORY) {
            return Formatter.formatFileSize(activity, totalBlocks * blockSize);

        } else if (choice == SD_REMAIN_MEMORY) {
            return Formatter.formatFileSize(activity, availableBlocks * blockSize);
        }
        return "获取SD卡容量失败";
    }

    /**
     * 将String写入文件中
     *
     * @param string string内容
     * @param file   目标文件
     * @throws IOException
     */
    public static void String2File(String string, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(string.getBytes());
    }

    /**
     * 从url连接中获取文件名称
     *
     * @param url 网络地址
     * @return 文件名称
     */
    public static String getFileNameFromURL(String url) {
        int index = url.lastIndexOf("/");
        return url.substring(index + 1);
    }

    /**
     * 将输入流转换成字符串
     *
     * @param is     输入流
     * @param decode 编码方式
     * @return 字符串
     */
    public static String Stream2String(InputStream is, String decode) {
        byte[] b = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            while ((len = is.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            String text = new String(bos.toByteArray(), decode);
            return text;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
