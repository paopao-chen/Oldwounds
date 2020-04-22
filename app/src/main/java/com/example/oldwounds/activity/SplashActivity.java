package com.example.oldwounds.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.oldwounds.MainActivity;
import com.example.oldwounds.R;

/**
 * 刚开始的闪屏页，设置一个延迟事件就好
 */

public class SplashActivity extends AppCompatActivity {

    private static final int TOMAIN = 1;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case TOMAIN:
                    SharedPreferences preferences = getSharedPreferences("login",MODE_PRIVATE);
                    String userName = preferences.getString("userName","");
                    boolean isLogin = preferences.getBoolean("isLogin",false);
                    if (isLogin) {
                        startActivity(new Intent(SplashActivity.this,MainActivity.class));
                        /**
                         * 发送一个EventBus
                         */
//                        Log.e("Splash", userName );
                    } else {
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    }
//                    Toast.makeText(SplashActivity.this,userName,Toast.LENGTH_LONG);
                    finish();
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);   //照片要一个大的，或者自己写一个view
        hideBootomUIMeun();
        handler.sendEmptyMessageDelayed(TOMAIN,1000L);
    }

    /**
     * 隐藏底部的虚拟键盘
     */
    private void hideBootomUIMeun(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    //禁止返回
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
