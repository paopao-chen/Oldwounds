package com.example.oldwounds.ui;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oldwounds.R;
import com.example.oldwounds.utils.LogUtil;
import com.example.oldwounds.utils.StaticData;
import com.example.oldwounds.utils.TimeCount;
import com.example.oldwounds.utils.ToastUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Bmob.initialize(this,"e235bbdce11e1bb33e75a32711c3d0fe");
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
        String phone = et_phone.getText().toString();
        switch (v.getId()) {
            case R.id.btn_getAuthCode:
                /**
                 * 检验手机号是否正确（正则表达式）
                 * 调用定时器定时改变
                 */
                /*Pattern pattern = Pattern.compile(StaticData.REGEX);
                Matcher matcher = pattern.matcher(et_phone.getText());
                if (!matcher.matches()) {
                    Toast.makeText(this,"输入的电话号码错误,请重新输入",Toast.LENGTH_SHORT).show();
                } else {
                    //通过okhttp发出网络请求

                    Request request = new Request.Builder()
                            .url(StaticData.NOTE_URL)
                            .post()
                            .build();

                    OkHttpClient client = new OkHttpClient();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                        }
                    });
                    //开启计时器
                    mTimeCount.start();
                }*/
                Pattern pattern = Pattern.compile(StaticData.REGEX);
                Matcher matcher = pattern.matcher(phone);
                if (!matcher.matches()) {
//                    Toast.makeText(this,"输入的电话号码错误,请重新输入",Toast.LENGTH_SHORT).show();
                    ToastUtil.showShort(this,"输入的电话号码错误,请重新输入");
                } else {
                    BmobSMS.requestSMSCode(phone, "demo", new QueryListener<Integer>() {
                        @Override
                        public void done(Integer smsId, BmobException e) {
                            if (e == null) {
                                LogUtil.e("RegisterActivity","发送验证码成功，短信ID：" + smsId);
                            } else {
                                LogUtil.e("RegisterActivity","发送验证码失败：" + e.getErrorCode() + "-" + e.getMessage());
                            }
                        }
                    });
                    //开启计时器
                    mTimeCount.start();
                }
                break;
            case R.id.btn_register:
                /**
                 * 检验输入的是否正确（验证码，密码）
                 * 1、检验用户名是否已经存在
                 * 2、检验两次输入的密码是否一致
                 * 3、检验验证码是否正确
                 * 跳转到登录界面
                 */
                if (TextUtils.isEmpty(et_phone.getText())) {
                    ToastUtil.showShort(this,"请输入手机号码");
                } else if (TextUtils.isEmpty(et_psw.getText())) {
                    ToastUtil.showShort(this,"请输入密码");
                } else if (TextUtils.isEmpty(et_ackPsw.getText()) || ! et_psw.getText().toString().equals(et_ackPsw.getText().toString())) {
                    ToastUtil.showShort(this,"两次输入的密码不一样");
                }/* else if("用户名已存在") {


                }*/
                else {
                    //检验验证码
                    BmobSMS.verifySmsCode(phone, et_authCode.getText().toString(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                /*mTvInfo.append("验证码验证成功，您可以在此时进行绑定操作！\n");
                                //验证码验证成功，可以在此时进行绑定操作
                                User user = BmobUser.getCurrentUser(User.class);
                                user.setMobilePhoneNumber(phone);
                                user.setMobilePhoneNumberVerified(true);
                                user.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            mTvInfo.append("绑定手机号码成功");
                                        } else {
                                            mTvInfo.append("绑定手机号码失败：" + e.getErrorCode() + "-" + e.getMessage());
                                        }
                                    }
                                });*/
                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                ToastUtil.showShort(getApplicationContext(),"注册成功");
                            } else {
                                ToastUtil.showShort(RegisterActivity.this,"验证码验证失败");
                            }
                        }
                    });
                }
                break;
            default:
                break;
        }
    }
}
