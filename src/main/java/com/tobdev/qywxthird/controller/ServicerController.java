package com.tobdev.qywxthird.controller;

import com.tobdev.qywxthird.model.entity.QywxThirdUser;
import com.tobdev.qywxthird.service.QywxThirdService;
import com.tobdev.qywxthird.service.impl.QywxThirdCompanyServiceImpl;
import com.tobdev.qywxthird.utils.CommonUtils;
import com.tobdev.qywxthird.utils.CookieUtils;
import com.tobdev.qywxthird.utils.JWTUtils;
import com.tobdev.qywxthird.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


/**
 * 服务商应用页，H5及小程序应用公用
 */
@Controller
public class ServicerController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private QywxThirdService qywxThirdService;

    @Autowired
    private QywxThirdCompanyServiceImpl qywxThirdCompanyService;

    @RequestMapping({"/ser/index","/"})
    String index(HttpServletRequest request, ModelMap model){

        model.put("suite_ticket",qywxThirdService.getSuitTicket());
        model.put("suite_access_token",qywxThirdService.getSuiteToken());
        model.put("pre_auth_code",qywxThirdService.getPreAuthCode());

        String redirectUrl = CommonUtils.RouteToUrl(request,"/ser/install");
        String installUrl = qywxThirdService.getInstallUrl(redirectUrl);
        model.put("install_url",installUrl);


        //推广安装
        model.put("register_url",qywxThirdService.getRegisterUrl());

        String oauthRedirectUrl = CommonUtils.RouteToUrl(request,"/ser/oauth_callback");
        String oauthUrl = qywxThirdService.getSsoUrl(oauthRedirectUrl,"admin");
        model.put("oauth_url",oauthUrl);

        return  "servicer/index";

    }

    @GetMapping("/ser/install")
    String install(@RequestParam(value = "auth_code") String authCode){
        //通过临时授权码获取永久授权码及授权公司信息
        boolean result = qywxThirdService.getPermentCode(authCode);
        //成功再跳转应用后台
        if(result){
            return "redirect:/admin/index";
        }
        return  "servicer/error";
    }


    @GetMapping("/ser/oauth_callback")
    @ResponseBody()
    void  callback(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "auth_code") String authCode) throws IOException {
        //通过临时授权码获取永久授权码及授权公司信息

        Map result = qywxThirdService.getLoginInfo(authCode);
//        // 成功再跳转应用后台
////        if(result){
////            return "redirect:/admin/index";
////        }
        //查数据库获取人员
        System.out.println(result.toString());
        //人员已侦破产生token登录  //本案例仅从企业微信接口获取未从数据表中获取
        QywxThirdUser user = new QywxThirdUser();
        Map userInfo = (Map)result.get("user_info");
        Map corpInfo= (Map)result.get("corp_info");
        user.setCorpId((String) corpInfo.get("corpid"));
        user.setUserId((String) userInfo.get("userid"));
        user.setName((String) userInfo.get("name"));
        user.setAvatar((String) userInfo.get("avatar"));
        String token=  JWTUtils.geneJsonWebToken(user);

//        result.put("token",token);

        //本案例写入cookie并跳转
        CookieUtils.setCookie(response,"token",token,24*60*60);

        String priIndexUrl = CommonUtils.RouteToUrl(request,"/admin/pri/index");
        response.sendRedirect(priIndexUrl);

    }


//    @RequestMapping("servicer/error")
//    String error(){
//        return  "error";
//    }
//
    @RequestMapping("/test")
    @ResponseBody
    String test(){

        redisUtils.set("xx","dsfdf");
        return redisUtils.get("xx");
//        return "redirect:/ser/index";
    }

}
