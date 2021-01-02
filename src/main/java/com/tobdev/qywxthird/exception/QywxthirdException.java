package com.tobdev.qywxthird.exception;

public class QywxthirdException extends RuntimeException{
    private Integer code;
    private String msg;

    public QywxthirdException(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
