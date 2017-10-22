package com.lyx.sample.entity;

import java.io.Serializable;

/**
 * UserInfo
 * <p>
 * Created by luoyingxing on 2017/5/22.
 */

public class UserInfo implements Serializable {
    private int id;    //	用户ID
    private String nickname;    //	昵称
    private String phone;    //	手机号码
    private String email;    //	邮箱
    private String avatar;    //	头像

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
