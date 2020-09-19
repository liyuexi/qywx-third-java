package com.example.demo.controller;


import com.example.demo.com.qq.weixin.mp.aes.AesException;
import com.example.demo.service.QywxThirdService;
import com.example.demo.util.QywxThirdHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;




@Controller
public class Callback {

    private final static Logger logger = LoggerFactory.getLogger("test");
    @Autowired
    private QywxThirdService qywxThirdService;

    @ResponseBody
    @GetMapping({"/callback/instruct","/callback/data"})
    String instructGet(@RequestParam(value = "msg_signature") String sVerifyMsgSig,
                       @RequestParam(value = "timestamp") String sVerifyTimeStamp,
                       @RequestParam(value = "nonce") String sVerifyNonce,
                       @RequestParam(value = "echostr") String sVerifyEchoStr
    ){

        logger.info("get回调验证开始");
        logger.info(sVerifyMsgSig);
        logger.info(sVerifyTimeStamp);
        logger.info(sVerifyNonce);
        logger.info(sVerifyEchoStr);
        logger.info("get回调验证");

        String result = qywxThirdService.getVerify(sVerifyMsgSig,sVerifyTimeStamp, sVerifyNonce,sVerifyEchoStr);

        return  result;
    }

    @ResponseBody
    @PostMapping("/callback/instruct")
    String instructPost(@RequestParam(value = "msg_signature") String sVerifyMsgSig,
                    @RequestParam(value = "timestamp") String sVerifyTimeStamp,
                    @RequestParam(value = "nonce") String sVerifyNonce,
                    @RequestBody String body
    ){
        logger.info("指令post回调开始");
        logger.info(sVerifyMsgSig);
        logger.info(sVerifyTimeStamp);
        logger.info(sVerifyNonce);
        logger.info(body);
        logger.info("指令post回调");
        //处理回调
        qywxThirdService.instructCallback(sVerifyMsgSig,sVerifyTimeStamp,sVerifyNonce,body);
        return "success";
    }

    @ResponseBody
    @PostMapping("/callback/data")
    String dataPost(@RequestParam(value = "msg_signature") String sVerifyMsgSig,
                    @RequestParam(value = "timestamp") String sVerifyTimeStamp,
                    @RequestParam(value = "nonce") String sVerifyNonce,
                    @RequestBody String body
    ){
        logger.info("数据post回调开始");
        logger.info(sVerifyMsgSig);
        logger.info(sVerifyTimeStamp);
        logger.info(sVerifyNonce);
        logger.info(body);
        logger.info("数据post回调");
        //处理回调
        qywxThirdService.dataCallback(sVerifyMsgSig,sVerifyTimeStamp,sVerifyNonce,body);
        return "success";
    }




}
