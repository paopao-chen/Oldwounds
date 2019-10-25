package com.example.oldwounds.utils;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

/**
 * Create by Politness Chen on 2019/10/17--10:49
 * desc:  设置一个定时器，用于验证码的倒计时
 */
public class TimeCount extends CountDownTimer {

    Button button;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public TimeCount(long millisInFuture, long countDownInterval,Button button) {
        super(millisInFuture, countDownInterval);
        this.button = button;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        button.setClickable(false);
        button.setText(millisUntilFinished/1000 + "秒后重新获取");
    }

    @Override
    public void onFinish() {
        button.setClickable(true);
        button.setText("获取验证码");
    }
}
