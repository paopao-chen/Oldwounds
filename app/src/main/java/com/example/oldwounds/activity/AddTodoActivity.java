package com.example.oldwounds.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.oldwounds.R;
import com.example.oldwounds.domain.Todo;
import com.example.oldwounds.utils.StaticData;

public class AddTodoActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_content;
    private Button btn_add;
    private Todo todo;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        initView();
    }

    private void initView() {
        et_content = findViewById(R.id.et_content);
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        todo = (Todo) getIntent().getSerializableExtra(StaticData.TODOITEM);
        content = todo.getContent();
        et_content.setText(content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                if (et_content.toString().trim().equals("")){
                    et_content.setError("您的输入为空");
                } else {
                    content = et_content.getText().toString();
                    todo.setContent(content);
                    todo.setCheck(false);
                    Intent intent = new Intent();
                    intent.putExtra(StaticData.TODOITEM, todo);
                    setResult(RESULT_OK,intent);
                    //(这里出过bug)fragment的继承类不要去实现带参的构造函数，因为这些带参构造函数在fragment被再次实例化的时候将不会被调用
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
