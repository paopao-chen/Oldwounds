package com.example.oldwounds.activity;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.oldwounds.MainActivity;
import com.example.oldwounds.R;
import com.example.oldwounds.domain.User;
import com.example.oldwounds.utils.ToastUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_userName;
    private EditText et_psw;
    private TextView tv_forget;
    private CheckBox cb_remberUser;
    private Button btn_login;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        et_userName = findViewById(R.id.et_userName);
        et_psw = findViewById(R.id.et_psw);
        tv_forget = findViewById(R.id.tv_forget);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        //设置监听事件
        tv_forget.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                //跳转到注册界面
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.tv_forget:
                //跳转到忘记密码界面
                 startActivity(new Intent(this,ForgetActivity.class));
                break;
            case R.id.btn_login:
                /**
                 * 检验用户名密码是否正确
                 * 从数据库中加载信息
                 * 跳转到主页面
                 */
                final String userName = et_userName.getText().toString().trim();
                String psw = et_psw.getText().toString().trim();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(psw)) {
                    ToastUtil.showShort("用户名或密码为空");
                } else {
                    User user = new User();
                    user.setUsername(userName);
                    user.setPassword(psw);
                    user.login(new SaveListener<Object>() {
                        @Override
                        public void done(Object o, BmobException e) {
                            if (e == null) {
                                SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                                editor.putString("userName", userName);
                                editor.putBoolean("isLogin", true);
                                editor.apply();
                                // 直接用sp记录一下userName等信息，其他地方从本地获取数据即可
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                ToastUtil.showShort("用户名或密码错误");
                            }
                        }
                    });
                }
                /*if (userName.equals("123") && psw.equals("123")) {
                    //如果登录成功，存储用户名和登录的状态（下次直接登录，不需要用户名密码）
                    SharedPreferences.Editor editor = getSharedPreferences("login",MODE_PRIVATE).edit();
                    editor.putString("userName",userName);
                    editor.putBoolean("isLogin",true);
                    editor.apply();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {

                }*/
                break;
        }
    }
}
