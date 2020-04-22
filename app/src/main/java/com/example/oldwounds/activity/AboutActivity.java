package com.example.oldwounds.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.oldwounds.R;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends BaseActivity {

    private ListView mListView;
    private List<String> mList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView() {
        mListView = findViewById(R.id.mListView);

        mList.add("应用名：" + getString(R.string.app_name));
        mList.add("个人博客：https://blog.csdn.net/weixin_42863849");
        mList.add("github：https://github.com/553474628");

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList);
        mListView.setAdapter(mAdapter);
    }
}
