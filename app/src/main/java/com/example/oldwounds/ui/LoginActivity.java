package com.example.oldwounds.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.oldwounds.MainActivity;
import com.example.oldwounds.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_userName;
    EditText et_psw;
    TextView tv_register;
    TextView tv_forget;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        et_userName = findViewById(R.id.et_userName);
        et_psw = findViewById(R.id.et_psw);
        tv_register = findViewById(R.id.tv_register);
        tv_forget = findViewById(R.id.tv_forget);
        btn_login = findViewById(R.id.btn_login);

        //设置监听事件
        tv_register.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
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
                String userName = et_userName.getText().toString().trim();
                String psw = et_psw.getText().toString().trim();
                if (userName.equals("123") && psw.equals("123")) {
                    //如果登录成功，存储用户名和登录的状态（下次直接登录，不需要用户名密码）
                    SharedPreferences.Editor editor = getSharedPreferences("login",MODE_PRIVATE).edit();
                    editor.putString("userName",userName);
                    editor.putBoolean("isLogin",true);
                    editor.apply();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {

                }
                break;
        }
    }
}
