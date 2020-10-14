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
import java.io.IOException;
import java.util.HashMap;
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
    public String oauth(HttpServletResponse response) throws IOException {

        return "redirect:/front/test";

//        String oauthRedirectUrl = "http://tobdev.ant-xy.com:9900/front/oauth_callback";
//        String oauthUrl = qywxThirdService.getOauthUrl(oauthRedirectUrl);
//        //return  new ModelAndView(new RedirectView(oauthUrl));
//        response.sendRedirect(oauthUrl);

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


    @RequestMapping("/front/test")
    String test(ModelMap  model) throws Exception {
        //先判断是否登录
        String  timestamp=""+System.currentTimeMillis();
        //随机字符串
        String nonce = "sdfsdf";
        String url = "http://tobdev.ant-xy.com:9900/front/test";
        Map result = qywxThirdService.getJsSign(nonce,timestamp,url);
        model.addAttribute("sign",result);
//        model.addAttribute("xx","sdffdf");
//        model.addObject("sign",result);
//        model.setViewName("front/test");
         return "front/test";

    }


    @RequestMapping("/front/jsSign")
    @ResponseBody()
    public Map getJsSign(@RequestParam("url") String url) throws Exception{
        //获取当前时间戳
        String  timestamp=""+System.currentTimeMillis();
        //随机字符串
        String nonce = "sdfsdf";
        Map result = qywxThirdService.getJsSign(nonce,timestamp,url);
        return  result;
    }


}
