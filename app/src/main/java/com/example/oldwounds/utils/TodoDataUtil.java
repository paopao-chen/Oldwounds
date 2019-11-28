package com.example.oldwounds.utils;

import android.content.Context;

import com.example.oldwounds.domain.Todo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.Buffer;
import java.util.ArrayList;

/**
 * Create by Politness Chen on 2019/10/28--16:58
 * desc:  todo的数据操作类
 */
public class TodoDataUtil {

    private String fileName;
    private Context mContext;

    public TodoDataUtil(String fileName, Context mContext) {
        this.fileName = fileName;
        this.mContext = mContext;
    }

    public JSONArray toJSONArray(ArrayList<Todo> todos) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Todo todo : todos) {
            JSONObject jsonObject = todo.toJson();
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public void saveToFile(ArrayList<Todo> todos) {
        FileOutputStream fileOutputStream = null;
        BufferedWriter writer = null;
        try {
            fileOutputStream = mContext.openFileOutput(fileName,Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            writer.write(toJSONArray(todos).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Todo> loadFromFile() {
        ArrayList<Todo> todos = new ArrayList<>();
        BufferedReader reader = null;
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = mContext.openFileInput(fileName);
            StringBuilder builder = new StringBuilder();
            String line;
            reader = new BufferedReader(new InputStreamReader(fileInputStream));
            while (((line = reader.readLine()) != null)) {
                builder.append(line);
            }
            //JSONTokener.nextValue() 会给出一个对象，然后可以动态的转换为适当的类型
            JSONArray jsonArray = (JSONArray) new JSONTokener(builder.toString()).nextValue();
            for (int i = 0; i < jsonArray.length(); i++) {
                Todo todo = new Todo(jsonArray.getJSONObject(i));
                todos.add(todo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return todos;
    }
}
