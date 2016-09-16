package com.joshua.escortujs.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.joshua.escortujs.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


@ContentView(R.layout.activity_select)
public class SelectActivity extends BaseActivity {
    @ViewInject(R.id.btn_fetch_package)
    Button btn_fetch_package;
    @ViewInject(R.id.btn_deliver_package)
    Button btn_deliver_package;
    @ViewInject(R.id.tv_result)
    TextView tv_result;
    private final static int FETCH_PACKAGE_CODE = 1;
    private final static int DELIVER_PACKAGE_CODE = 2;
    private final static int RESULT_CODE_OK = 3;
    private final static int RESULT_CODE_FAIL = 4;
    private Intent intent;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FETCH_PACKAGE_CODE:
                    intent.putExtra("comeFrom",FETCH_PACKAGE_CODE);
                    startActivityForResult(intent, FETCH_PACKAGE_CODE);
                    break;
                case DELIVER_PACKAGE_CODE:
                    intent.putExtra("comeFrom",DELIVER_PACKAGE_CODE);
                    startActivityForResult(intent, DELIVER_PACKAGE_CODE);
                    break;
            }
        }
    };
    private StringBuffer url;
    private SharedPreferences sp;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GetCacheData();

    }
    /**
     * 获取用户信息缓存
     */
    private void GetCacheData() {
        sp = getSharedPreferences("config", MODE_PRIVATE);
        username=sp.getString("username","");
    }
    @Event(value = {R.id.btn_deliver_package, R.id.btn_fetch_package})
    private void ButtonOnClick(View view) {
        intent = new Intent();
        intent.setClass(SelectActivity.this, ScanActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        switch (view.getId()) {
            case R.id.btn_deliver_package:
                intent.putExtra("comeFrom",DELIVER_PACKAGE_CODE);
                startActivityForResult(intent, DELIVER_PACKAGE_CODE);
                break;
            case R.id.btn_fetch_package:
                intent.putExtra("comeFrom",FETCH_PACKAGE_CODE);
                startActivityForResult(intent, FETCH_PACKAGE_CODE);
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FETCH_PACKAGE_CODE:
                if (resultCode == RESULT_CODE_OK) {
                    url = new StringBuffer(data.getStringExtra("url"));
                    url.append("/identity/" + FETCH_PACKAGE_CODE);
                    url.append("/username/" + username);
                    getServer(url.toString(), FETCH_PACKAGE_CODE);
                } else if (resultCode == RESULT_CODE_FAIL) {
                    tv_result.setTextColor(Color.RED);
                    tv_result.setText("扫码失败");
                }
                break;
            case DELIVER_PACKAGE_CODE:
                if (resultCode == RESULT_CODE_OK) {
                    url = new StringBuffer(data.getStringExtra("url"));
                    url.append("/identity/" + DELIVER_PACKAGE_CODE);
                    url.append("/username/" + username);
                    getServer(url.toString(), DELIVER_PACKAGE_CODE);
                } else if (resultCode == RESULT_CODE_FAIL) {
                    tv_result.setTextColor(Color.RED);
                    tv_result.setText("扫码失败\n" + "二维码非法！");
                }
                break;


        }
    }

    public void getServer(String url, final int identity) {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    String order_state=jsonObject.getString("order_state");
                    switch (order_state) {
                        case "1":
                            tv_result.setTextColor(Color.BLACK);
                            tv_result.setText("取件成功\n" + "2s后返回取件扫码界面");
                            handler.sendEmptyMessageDelayed(identity, 2000);
                            break;
                        case "2":
                            tv_result.setTextColor(Color.BLACK);
                            tv_result.setText("派件成功\n" + "2s后返回派件扫码界面");
                            handler.sendEmptyMessageDelayed(identity, 2000);
                            break;
                        case "3":
                            tv_result.setTextColor(Color.RED);
                            tv_result.setText("扫码失败\n" + "请不要重复扫码！");
                            break;
                        case "4":
                            tv_result.setTextColor(Color.RED);
                            tv_result.setText("扫码失败\n" + "请先取件再派件！");
                            break;
                        case "5":
                            tv_result.setTextColor(Color.RED);
                            tv_result.setText("扫码失败\n" + "该件已经配送！");
                            break;
                        default:
                            tv_result.setTextColor(Color.RED);
                            tv_result.setText("扫码失败\n" + "二维码非法！");
                            break;
                    }
                } catch (JSONException e) {
                    tv_result.setTextColor(Color.RED);
                    tv_result.setText("扫码失败\n" + "二维码非法！\n" + "错误信息:" + e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                tv_result.setTextColor(Color.RED);
                tv_result.setText("扫码失败\n"+"错误信息:"+ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ){

            final AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("退出");
            builder.setMessage("是否退出江大镖局");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent("com.joshua.exit");
                    sendBroadcast(intent);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
