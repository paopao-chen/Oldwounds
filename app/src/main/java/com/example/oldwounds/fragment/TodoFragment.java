package com.example.oldwounds.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.oldwounds.R;
import com.example.oldwounds.domain.Todo;
import com.example.oldwounds.activity.AddTodoActivity;
import com.example.oldwounds.utils.DateTransUtils;
import com.example.oldwounds.utils.LogUtil;
import com.example.oldwounds.utils.StaticData;
import com.example.oldwounds.utils.TodoDataUtil;
import com.example.oldwounds.view.EmptyRecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Create by Politness Chen on 2019/10/16--14:04
 * desc:   目标Fragment
 */
public class TodoFragment extends Fragment implements Toolbar.OnMenuItemClickListener,
        DatePickerDialog.OnDateSetListener, View.OnClickListener {
    private static TodoFragment todoFragment;

    public TodoFragment() {
    }

    public static Fragment getInstance() {
        if (todoFragment == null)
            todoFragment = new TodoFragment();
        return todoFragment;
    }

    private Toolbar todo_toolbar;
    private EmptyRecyclerView rv_todo;
    private FloatingActionButton fab_add_task;
    private ArrayList<Todo> todoList;
    private TodoAdapter adapter;
    private TodoDataUtil todoDataUtil;
    private LinearLayout ll_empty;

    private static final String FILENAME = "todo.json";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

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

        //添加目标的悬浮按钮
        fab_add_task = view.findViewById(R.id.fab_add_task);
        fab_add_task.setOnClickListener(this);

        ll_empty = view.findViewById(R.id.ll_empty);

        //filename前面加上日期   2019-10-28
        todoDataUtil = new TodoDataUtil(FILENAME, getContext());
        todoList = getLocallyStoredData(todoDataUtil);
        adapter = new TodoAdapter(todoList);
        //RecyclerView的初始化
        rv_todo = view.findViewById(R.id.rv_todo);
        rv_todo.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_todo.setAdapter(adapter);
        rv_todo.setEmptyView(ll_empty);
    }

    private ArrayList<Todo> getLocallyStoredData(TodoDataUtil todoDataUtil) {
        ArrayList<Todo> items = null;

        try {
            items = todoDataUtil.loadFromFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (items == null) {
            items = new ArrayList<>();
        }
        return items;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_task:
                Intent intent = new Intent(getActivity(), AddTodoActivity.class);
                Todo todo = new Todo("", false);
                intent.putExtra(StaticData.TODOITEM, todo);
                startActivityForResult(intent, StaticData.REQUEST_ID_TODO_ITEM);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_CANCELED && requestCode == StaticData.REQUEST_ID_TODO_ITEM) {
            Todo todo = (Todo) data.getSerializableExtra(StaticData.TODOITEM);
            if (todo.getContent().length() <= 0)
                return;
            boolean existed = false;

            for (int i = 0; i < todoList.size(); i++) {
                if (todo.getIdentifier().equals(todoList.get(i).getIdentifier())) {
                    todoList.set(i, todo);
                    existed = true;
                    adapter.notifyDataSetChanged();  //刷新
                    break;
                }
            }

            if (!existed) {
                todoList.add(todo);
                //局部刷新
                adapter.notifyItemInserted(todoList.size() - 1);
//                addToDataStore(item);
            }
        }
    }

    class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

        private List<Todo> mList;

        public TodoAdapter(List<Todo> mList) {
            this.mList = mList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_layout, parent, false);
            final ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.cb_todo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Todo todo = mList.get(viewHolder.getAdapterPosition());
                    boolean isCheck = todo.isCheck();
                    LogUtil.e("TodoFragment", String.valueOf(isCheck));
                    if (isCheck) {
                        viewHolder.cb_todo.setChecked(false);
                        viewHolder.tv_content.setTextColor(Color.GRAY);
                    } else {
                        viewHolder.cb_todo.setChecked(true);
                        viewHolder.tv_content.setTextColor(Color.GREEN);
                    }
                    todo.setCheck(!isCheck);
                    LogUtil.e("TodoFragment", String.valueOf(todo.isCheck()));
                }
            });
            //对单项设置点击事件
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Todo todo = mList.get(viewHolder.getAdapterPosition());
                    Intent intent = new Intent(getContext(), AddTodoActivity.class);
                    intent.putExtra(StaticData.TODOITEM, todo);
                    startActivityForResult(intent, StaticData.REQUEST_ID_TODO_ITEM);
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    todoList.remove(viewHolder.getAdapterPosition());
                    adapter.notifyDataSetChanged();
                    return true;   //true的话就不会向下传递事件给onClick
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Todo todo = mList.get(position);
            holder.cb_todo.setChecked(todo.isCheck());
            holder.tv_content.setText(todo.getContent());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private CheckBox cb_todo;
            private TextView tv_content;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                cb_todo = itemView.findViewById(R.id.cb_todo);
                tv_content = itemView.findViewById(R.id.tv_content);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_todo, menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        todo_toolbar.setTitle(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
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
    public void onPause() {
        super.onPause();
        todoDataUtil.saveToFile(todoList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        todoDataUtil.saveToFile(todoList);
    }
}
