package com.example.demo.controller;

import com.example.demo.service.QywxThirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class Front {

    @Autowired
    private QywxThirdService qywxThirdService;

    @RequestMapping("/front/index")
    String index(ModelMap model){

        //先判断是否登录

        //没有登录去登录
        String oauthRedirectUrl = "http://tobdev.ant-xy.com:9900/front/oauth_callback";
        String oauthUrl = qywxThirdService.getOauthUrl(oauthRedirectUrl);
        model.put("oauth_url",oauthUrl);

        return  "front/index";

    }
    @RequestMapping("/front/oauth")
     ModelAndView oauth() {

        String oauthRedirectUrl = "http://tobdev.ant-xy.com:9900/front/oauth_callback";
        String oauthUrl = qywxThirdService.getOauthUrl(oauthRedirectUrl);
        return  new ModelAndView(new RedirectView(oauthUrl));

    }

    @RequestMapping("/front/oauth_callback")
    @ResponseBody()
    public Map oauthCallback(@RequestParam("code") String code){
        //通过code获取信息再跳转
        System.out.print(code);
//        return null;
        Map result = qywxThirdService.getOauthUser(code);
        //取得数据后 可以跳转到前端或者
        return  result;
    }

}
