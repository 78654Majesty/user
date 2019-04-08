package com.security.demo.util;

/**
 * @author fanglingxiao
 * @createTime 2019/3/26
 */
public class ResultApi<T> {

    private int resCode;
    private String resMsg;
    private T date;

    public ResultApi(Builder<T> builder) {
        this.resCode=builder.resCode;
        this.resMsg=builder.resMsg;
        this.date=builder.date;
    }

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



    public static class Builder<T>{
        private int resCode;
        private String resMsg;
        private T date;

        public Builder setResCode(int resCode) {
            this.resCode = resCode;
            return this;
        }

        public Builder setResMsg(String resMsg) {
            this.resMsg = resMsg;
            return this;
        }

        public Builder setDate(T date) {
            this.date = date;
            return this;
        }

        public ResultApi<T> build(){
            return new ResultApi<>(this);
        }
    }
}
