package com.example.oldwounds.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.oldwounds.R;
import com.example.oldwounds.utils.TimeCount;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_phone;
    EditText et_psw;
    EditText et_ackPsw;
    EditText et_authCode;
    Button btn_getAuthCode;
    Button btn_register;

    private TimeCount mTimeCount;//计时器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        //初始化控件
        et_phone = findViewById(R.id.et_phone);
        et_psw = findViewById(R.id.et_psw);
        et_ackPsw = findViewById(R.id.et_ackPsw);
        et_authCode = findViewById(R.id.et_authCode);
        btn_getAuthCode = findViewById(R.id.btn_getAuthCode);
        btn_register = findViewById(R.id.btn_register);
        //设置监听事件
        btn_getAuthCode.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        mTimeCount = new TimeCount(60000, 1000,btn_getAuthCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getAuthCode:
                /**
                 * 检验手机号是否正确（正则表达式）
                 * 调用定时器定时改变
                 */
                mTimeCount.start();
                break;
            case R.id.btn_register:
                /**
                 * 检验输入的是否正确（验证码，密码）
                 * 跳转到登录界面
                 */
                startActivity(new Intent(this,LoginActivity.class));
                break;
            default:
                break;
        }
    }
}
