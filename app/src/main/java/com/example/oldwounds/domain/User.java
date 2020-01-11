package com.example.oldwounds.domain;

import cn.bmob.v3.BmobUser;

/**
 * Create by Politness Chen on 2019/12/7--15:32
 * desc:   用户的bean对象
 */
public class User extends BmobUser {
    private String image;
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
