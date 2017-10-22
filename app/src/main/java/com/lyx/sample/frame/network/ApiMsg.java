package com.lyx.sample.frame.network;

/**
 * ApiMsg
 * <p/>
 * Created by luoyingxing on 2017/5/18.
 */
public class ApiMsg {
    public static final int OK = 0;
    public static final int LOGOUT = 40005;
    private int code;
    private String msg;

    public ApiMsg() {
    }

    public ApiMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static boolean isApiMsg(String json) {
        String msgRegExp = "\\{\"code\":-?\\d*,\"msg\":\"[\\s\\S]*\"\\}";
        return json != null && json.matches(msgRegExp);
    }

    @Override
    public String toString() {
        return "ApiMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}