package com.lyx.frame.frame.network;

/**
 * ErrMsg
 * <p/>
 * Created by luoyingxing on 2017/5/18.
 */
public class ErrMsg {

    private static final int PARSE_ERROR = 2001;
    private static final String PARSE_ERROR_MSG = "数据类型解析错误";

    public static ApiMsg parseError() {
        return new ApiMsg(PARSE_ERROR, PARSE_ERROR_MSG);
    }
}