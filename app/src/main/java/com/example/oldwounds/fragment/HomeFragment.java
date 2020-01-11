package com.example.oldwounds.fragment;

import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oldwounds.R;
import com.example.oldwounds.utils.StaticData;
import com.example.oldwounds.utils.ToastUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Create by Politness Chen on 2019/10/16--14:04
 * desc:   主页Fragment
 */
public class HomeFragment extends Fragment {
    private static HomeFragment homeFragment;
    public HomeFragment(){}

    public static Fragment getInstance(){
        if (homeFragment == null)
            homeFragment = new HomeFragment();
        return homeFragment;
    }

    private static final String TAG = HomeFragment.class.getSimpleName();
    private ImageView todayImage;
    private TextView todayWord;
    private TextView imageAuthor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initView(view);
        Log.e(TAG, "onCreateView: 11111");
        loadContent();
        return view;
    }

    private void initView(View view) {
        todayImage = (ImageView) view.findViewById(R.id.todayImage);
        todayWord = (TextView) view.findViewById(R.id.todayWord);
        imageAuthor = (TextView) view.findViewById(R.id.imageAuthor);
    }

    private void loadContent() {
        RequestBody requestBody = new FormBody.Builder()
                .add("key", StaticData.TIANXING_APIKEY)
                .build();
        final Request request = new Request.Builder()
                .url(StaticData.TIANXING_URL)
                .post(requestBody)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        mHandler.obtainMessage(1, response.body().string()).sendToTarget();
                    }
                });
            }
        }).start();
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                try {
                    JSONObject jsonObject = new JSONObject((String) msg.obj);
                    JSONArray jsonArray = new JSONArray(jsonObject.get("newslist").toString());
                    JSONObject result = new JSONObject(jsonArray.get(0).toString());
                    String word = result.getString("word");
                    String imgUrl = result.getString("imgurl");
                    String imgAuthor = result.getString("imgauthor");

                    if (!TextUtils.isEmpty(imgUrl)) {
                        Glide.with(getContext()).load(imgUrl).into(todayImage);
                    }
                    if (!TextUtils.isEmpty(word)) {
                        todayWord.setText(word);
                    }
                    if (!TextUtils.isEmpty(imgAuthor)) {
                        imageAuthor.setText(imgAuthor);
                    }
                    Log.e(TAG, "jsonArray: " + jsonArray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    });
}
