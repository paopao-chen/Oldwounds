package com.example.oldwounds.domain;

/**
 * Create by Politness Chen on 2019/10/24--18:26
 * desc:   目标的bean对象
 */
public class Todo {

    private boolean isCheck;
    private String content;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
