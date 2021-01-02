package com.tobdev.qywxthird.controller;

import com.tobdev.qywxthird.service.QywxThirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * 小程序应用专用
 */
@Controller
public class XcxController {

    @Autowired
    private QywxThirdService qywxThirdService;


//    @RequestMapping("/xcx/index")
//    @ResponseBody()
//    public Map index(){
//
//        return  0;
//    }


    @RequestMapping("/xcx/login")
    @ResponseBody()
    public Map oauthCallback(@RequestParam("code") String code){
        //通过code获取信息
        Map result = qywxThirdService.getCode2sessionUser(code);
        return  result;

    }

}
