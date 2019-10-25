package com.example.oldwounds.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.oldwounds.R;
import com.example.oldwounds.adapter.UseTimeAdapter;
import com.example.oldwounds.ui.UseTimeDetailActivity;
import com.example.oldwounds.utils.DateTransUtils;
import com.example.oldwounds.utils.LogUtil;
import com.example.oldwounds.utils.UseTimeDataManager;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by Politness Chen on 2019/10/16--14:04
 * desc:  主页的fragment
 */
public class HomeFragment extends Fragment implements DatePickerDialog.OnDateSetListener,Toolbar.OnMenuItemClickListener {

    private static HomeFragment homeFragment;
    private HomeFragment(){}

    public static Fragment getInstance(){
        if (homeFragment == null)
            homeFragment = new HomeFragment();
        return homeFragment;
    }


    private Toolbar home_toolbar;
    //Recycle相关
    private RecyclerView mRecyclerView;
    private UseTimeAdapter mUseTimeAdapter;

    private ArrayList<String> mDateList;
    private UseTimeDataManager mUseTimeDataManager;
    private int dayNum = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //在fragment中必须要这样
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        home_toolbar = view.findViewById(R.id.home_toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(home_toolbar);  会导致Fragment的ToolBar的菜单错乱
        home_toolbar.inflateMenu(R.menu.toolbar_home);
        home_toolbar.setOnMenuItemClickListener(this);
        home_toolbar.setTitle("应用使用时长");
        home_toolbar.setTitleMarginStart(20);
        initData(dayNum);
        initView(view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initData(int dayNum) {
        mDateList = DateTransUtils.getSearchDays();
        mUseTimeDataManager = UseTimeDataManager.getInstance(getContext());
        mUseTimeDataManager.refreshData(dayNum);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.show_statistics);
        showView(dayNum);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showView(int dayNumber) {
        mUseTimeAdapter = new UseTimeAdapter(getActivity(),mUseTimeDataManager.getmPackageInfoListOrderByTime());
        mUseTimeAdapter.setOnItemClickListener(new UseTimeAdapter.OnRecyclerViewIetmClick() {
            @Override
            public void onItemClick(View view, String pkg) {
                Intent intent = new Intent(getActivity(), UseTimeDetailActivity.class);
                intent.putExtra("type","times");
                intent.putExtra("pkg",pkg);
                LogUtil.e("HomeFragment","  showView : " + pkg);

                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mUseTimeAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_home,menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        /*Toast.makeText(getActivity(),"您选择的日期是："+year+"年"+(month+1)+"月"+dayOfMonth+"日!",Toast
                .LENGTH_SHORT).show();*/
        Calendar choseCalendar = Calendar.getInstance();
        choseCalendar.set(year,month,dayOfMonth);
        long millis = System.currentTimeMillis();
        Calendar currentCalendar = DateTransUtils.stampToCalendar(millis);
        //dayNum
        long diffDays = (currentCalendar.getTimeInMillis() - choseCalendar.getTimeInMillis()) / DateTransUtils.DAY_IN_MILLIS;
        if (diffDays >= 0 && diffDays <= 7) {
            mUseTimeDataManager.refreshData((int) diffDays);
            showView((int)diffDays);
        } else if (diffDays < 0){
            Toast.makeText(getActivity(),"我们无法穿越哦~",Toast.LENGTH_SHORT).show();
        } else {
            //超过7天系统就不保存，直接调用数据库中的数据
            Toast.makeText(getActivity(),"我们正在请求数据，，",Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_content:

                LogUtil.e("HomeFragment","content");
                break;
            case R.id.item_setting:
                //去调用系统的设置
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
                LogUtil.e("HomeFragment","setting");
                break;
            case R.id.item_calendar:
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(getContext(),this,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                LogUtil.e("HomeFragment","calendar");
                break;
            default:
                break;
        }
        return true;
    }
}
