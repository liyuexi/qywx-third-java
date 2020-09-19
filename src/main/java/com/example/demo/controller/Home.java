package com.example.demo.controller;

import com.example.demo.service.QywxThirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Home {

    @Autowired
    private QywxThirdService qywxThirdService;

    @RequestMapping("/app/index")
    String index(ModelMap model){
        String redirectUrl = "http://tobdev.ant-xy.com:9900/app/install";
        String installUrl = qywxThirdService.getInstallUrl(redirectUrl);
        model.put("installUrl",installUrl);
        return  "home/index";
    }

    @GetMapping("/app/install")
    String install(@RequestParam(value = "auth_code") String authCode){
        //通过临时授权码获取永久授权码及授权公司信息
        boolean result = qywxThirdService.getPermentCode(authCode);
        //成功再跳转应用后台
        if(result){
            return "redirect:/admin/index";
        }
        return  "home/error";
    }

    @RequestMapping("/admin/index")
    String admin(){
        return  "admin/index";
    }

    @RequestMapping("home/error")
    String error(){
        return  "error";
    }

    @RequestMapping("/test")
    String test(){
        return "redirect:/app/index";
    }

}