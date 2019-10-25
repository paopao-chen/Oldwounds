package com.example.oldwounds.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.oldwounds.R;
import com.example.oldwounds.domain.Todo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by Politness Chen on 2019/10/24--18:22
 * desc:   todo的Adapter
 */
public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private List<Todo> mList;

    public TodoAdapter(List<Todo> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo todo = mList.get(position);
        holder.cb_todo.setEnabled(todo.isCheck());
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
