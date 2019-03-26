package com.security.demo.util;

/**
 * @author fanglingxiao
 * @createTime 2019/3/26
 */
public class ResultApi<T> {

    private int resCode;
    private String resMsg;
    private T date;

    public int getResCode() {
        return resCode;
    }

    public void setResCode(int resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public T getDate() {
        return date;
    }

    public void setDate(T date) {
        this.date = date;
    }
}
