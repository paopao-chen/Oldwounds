package com.example.oldwounds.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.oldwounds.R;
import com.example.oldwounds.utils.DateTransUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ViewUtils;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by Politness Chen on 2019/10/16--14:04
 * desc:   目标Fragment
 */
public class TodoFragment extends Fragment implements Toolbar.OnMenuItemClickListener, DatePickerDialog.OnDateSetListener, View.OnClickListener {
    private static TodoFragment todoFragment;
    private TodoFragment(){}

    public static Fragment getInstance(){
        if (todoFragment == null)
            todoFragment = new TodoFragment();
        return todoFragment;
    }

    private Toolbar todo_toolbar;
    private RecyclerView rv_todo;
    private FloatingActionButton fab_add_task;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo,container,false);

        initView(view);
        return view;
    }

    @SuppressLint("ResourceAsColor")
    private void initView(View view) {
        //toolbar的初始化
        todo_toolbar = view.findViewById(R.id.todo_toolbar);
        todo_toolbar.inflateMenu(R.menu.toolbar_todo);
        todo_toolbar.setOnMenuItemClickListener(this);
        todo_toolbar.setTitle(DateTransUtils.stampToDay(System.currentTimeMillis()));
        todo_toolbar.setTitleMarginStart(50);
        todo_toolbar.setTitleTextColor(R.color.colorAccent);

        rv_todo = view.findViewById(R.id.rv_todo);

        //添加目标的悬浮按钮
        fab_add_task = view.findViewById(R.id.fab_add_task);
        fab_add_task.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_calendar:
                DatePickerDialog dialog = new DatePickerDialog(getActivity());
                dialog.setOnDateSetListener(this);
                dialog.show();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_todo,menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        todo_toolbar.setTitle(year + "年" + (month+1) + "月" + dayOfMonth + "日");
    }

    @Override
    public void onClick(View v) {

    }
}
