package com.example.oldwounds.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.oldwounds.R;
import com.example.oldwounds.adapter.UseTimeDetailAdapter;
import com.example.oldwounds.utils.UseTimeDataManager;


/**
 * 应用使用次数详情统计(每次打开一个应用的使用信息)
 */
public class UseTimeDetailActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private UseTimeDetailAdapter mAdapter;
    private UseTimeDataManager mUseTimeDataManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_time_detail);

        mUseTimeDataManager = UseTimeDataManager.getInstance(this);
        mRecyclerView = findViewById(R.id.rv_detail);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String pkg = intent.getStringExtra("pkg");
        initView(type,pkg);
    }

    private void initView(String type, String pkg) {
        if ("times".equals(type)) {
            try {
                PackageManager packageManager = getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(pkg, PackageManager.GET_ACTIVITIES);
                //app的名字
                String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                setTitle(appName + "的使用详情");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            mAdapter = new UseTimeDetailAdapter(mUseTimeDataManager.getPkgOneTimeDetailList(pkg));
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}
