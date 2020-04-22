package com.example.oldwounds.domain;

/**
 * Create by Politness Chen on 2020/4/21--16:01
 * desc:
 */
public class MessageEvent {
    private String message;
    public  MessageEvent(String message){
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
