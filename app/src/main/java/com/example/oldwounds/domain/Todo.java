package com.example.oldwounds.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.UUID;

/**
 * Create by Politness Chen on 2019/10/24--18:26
 * desc:   todo的bean对象
 */
public class Todo implements Serializable {

    private boolean isCheck;
    private String content;
    private UUID mTodoIdentifier;

    private static final String TODOCONTENT = "todoContent";
    private static final String TODOCHECK = "todoCheck";
    private static final String TODOIDENTIFIER = "todoidentifier";

    public Todo(String content,boolean isCheck) {
        this.content = content;
        this.isCheck = isCheck;
        mTodoIdentifier = UUID.randomUUID();
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TODOCONTENT,content);
        jsonObject.put(TODOCHECK,isCheck);
        jsonObject.put(TODOIDENTIFIER,mTodoIdentifier.toString());
        return jsonObject;
    }

    public Todo(JSONObject jsonObject) throws JSONException {
        content = jsonObject.getString(TODOCONTENT);
        isCheck = jsonObject.getBoolean(TODOCHECK);
        mTodoIdentifier = UUID.fromString(jsonObject.getString(TODOIDENTIFIER));
    }

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

    public UUID getIdentifier() {
        return mTodoIdentifier;
    }
}
