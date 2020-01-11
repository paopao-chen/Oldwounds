package com.example.oldwounds.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Bundle;
import android.view.CollapsibleActionView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.oldwounds.R;
import com.example.oldwounds.utils.TimeCount;

/**
 * 忘记密码就直接去数据库里修改一个密码即可
 */
public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_phone;
    EditText et_authCode;
    Button btn_getAuthCode;

    //计时器
    TimeCount mTimeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initView();
    }

    private void initView() {
        et_phone = findViewById(R.id.et_phone);
        et_authCode = findViewById(R.id.et_authCode);
        btn_getAuthCode = findViewById(R.id.btn_getAuthCode);
        btn_getAuthCode.setOnClickListener(this);
        mTimeCount = new TimeCount(60000, 1000,btn_getAuthCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_phone:
                mTimeCount.start();
                break;
            default:
                break;
        }
    }
}
