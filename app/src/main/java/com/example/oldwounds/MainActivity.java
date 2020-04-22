package com.example.oldwounds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.oldwounds.adapter.MyFragmentPagerAdapter;
import com.example.oldwounds.fragment.OldFragment;
import com.example.oldwounds.fragment.MineFragment;
import com.example.oldwounds.fragment.HomeFragment;
import com.example.oldwounds.fragment.TodoFragment;
import com.example.oldwounds.utils.MyViewPager;
import com.example.oldwounds.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyViewPager mViewPager;
    private RadioGroup mRadioGroup;

    private List<Fragment> fragments;
    private FragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //声明权限
        checkPermissions();
        initView();
        initData();
    }

    private void initData() {
        //初始化fragment
        fragments = new ArrayList<>(4);
        fragments.add(HomeFragment.getInstance());
        fragments.add(OldFragment.getInstance());
        fragments.add(TodoFragment.getInstance());
        fragments.add(MineFragment.getInstance());
        //设置适配器
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(mAdapter);
        //缓存4个,避免fragment的重复创建
        mViewPager.setOffscreenPageLimit(4);
        //对viewPager的事件进行监听
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        mRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    private void initView() {
        mViewPager = findViewById(R.id.mViewPager);
        mRadioGroup = findViewById(R.id.mRadioGroup);
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) mRadioGroup.getChildAt(position);
            radioButton.setChecked(true);
//            radioButton.setTextColor(getResources().getColor(R.color.blue));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for (int i = 0; i < mRadioGroup.getChildCount(); i++) {
                if (mRadioGroup.getChildAt(i).getId() == checkedId) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeAllViews();
    }

    private static final int REQUEST_PERMISSION = 100;
    private boolean hasPermission = false;
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查是否有存储和拍照权限
            if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                hasPermission = true;
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hasPermission = true;
            } else {
                Toast.makeText(this, "必须同意所有权限才能使用本程序！", Toast.LENGTH_SHORT).show();
                hasPermission = false;
                finish();
            }
        }
    }

    private static final int TIME_EXIT = 2000;
    private long mExitTime;

    @Override
    public void onBackPressed() {
        if (mExitTime + TIME_EXIT > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            ToastUtil.showShort(getResources().getString(R.string.EXIT_TIME));
            mExitTime = System.currentTimeMillis();
        }

    }
}
