package com.joshua.escortujs.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.et_username)
    EditText et_username;
    @ViewInject(R.id.et_password)
    EditText et_password;
    @ViewInject(R.id.btn_login)
    Button btn_login;
    @ViewInject(R.id.tv_result)
    TextView tv_result;
    @ViewInject(R.id.pb_login)
    ProgressBar pb_login;
    private static final int ON_LOGIN_SUCCESS = 1;
    private static final int ON_LOGIN_FAILED = 0;
    private SharedPreferences sp;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayCacheData();
    }

    /**
     * 获取用户信息缓存
     */
    private void DisplayCacheData() {
        sp = getSharedPreferences("config", MODE_PRIVATE);
        username=sp.getString("username","");
        password=sp.getString("password","");
        et_username.setText(username);
        et_password.setText(password);

    }

    /**
     * 响应回调
     * @param view 按钮
     */
    @Event(value = {R.id.btn_login})
    private void ButtonOnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                pb_login.setVisibility(View.VISIBLE);
                btn_login.setClickable(false);
                http_login();
                break;
        }

    }

    /**
     * 访问后台-登录
     */
    private void http_login() {
        username = et_username.getText().toString();
        password = et_password.getText().toString();
        String url = "http://express.vastsum.net/admin/code/login" + "/username/" + username + "/password/" + password;
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String login_result = jsonObject.getString("status");
                    switch (login_result) {
                        case ON_LOGIN_SUCCESS+"":
                            sp.edit().putString("username", username).apply();
                            sp.edit().putString("password", password).apply();
                            tv_result.setText("登录成功");
                            Intent intent=new Intent(LoginActivity.this,SelectActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case ON_LOGIN_FAILED+"":
                            tv_result.setTextColor(Color.RED);
                            tv_result.setText("用户名或密码错误");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                tv_result.setTextColor(Color.RED);
                tv_result.setText("网络错误");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                pb_login.setVisibility(View.INVISIBLE);
                btn_login.setClickable(true);
            }
        });

    }

}
