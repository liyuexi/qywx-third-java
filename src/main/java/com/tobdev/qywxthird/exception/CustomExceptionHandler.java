package com.tobdev.qywxthird.exception;


import com.tobdev.qywxthird.utils.JsonData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class CustomExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(value =  Exception.class)
    @ResponseBody
    public JsonData handle(Exception e){
        logger.error("[全局未知错误]{}",e);
        if(e instanceof QywxthirdException){
            QywxthirdException qywxthirdException = (QywxthirdException) e;
            return JsonData.buildError(qywxthirdException.getCode(),qywxthirdException.getMsg());
        }else{
            return JsonData.buildError("全局未知错误");
        }
    }
}
