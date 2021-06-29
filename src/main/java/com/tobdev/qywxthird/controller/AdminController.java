package com.tobdev.qywxthird.controller;

import com.tobdev.qywxthird.service.QywxThirdService;
import com.tobdev.qywxthird.service.impl.QywxThirdCompanyServiceImpl;
import com.tobdev.qywxthird.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AdminController {

    @Autowired
    private QywxThirdCompanyServiceImpl qywxThirdCompanyService;
    @Autowired
    private QywxThirdService qywxThirdService;

    @RequestMapping("/admin/index")
    String admin(HttpServletRequest request, ModelMap model){

        String oauthRedirectUrl = CommonUtils.RouteToUrl(request,"/ser/oauth_callback");
        String oauthUrl = qywxThirdService.getSsoUrl(oauthRedirectUrl,"admin");
        model.put("oauth_url",oauthUrl);

        return  "admin/index";
    }

    @RequestMapping("/admin/pri/index")
    String index(HttpServletRequest request, ModelMap model){
        //获取身份
        //当前登录身份
        String userId = (String) request.getAttribute("user_id");
        String corpId = (String) request.getAttribute("corp_id");

        System.out.println(userId);
        System.out.println(corpId);

        model.put("user_id",userId);
        model.put("corp_id",corpId);

        model.put("provider_access_token",qywxThirdService.getProviderToken());


        model.put("access_token",qywxThirdCompanyService.getCorpAccessToken(corpId));

        String contactUrl = CommonUtils.RouteToUrl(request,"/contact/index");
        model.put("contact_url",contactUrl);

        String extcontactUrl = CommonUtils.RouteToUrl(request,"/extcontact/index");
        model.put("extcontact_url",extcontactUrl);

        String messageUrl = CommonUtils.RouteToUrl(request,"/message/index");
        model.put("message_url",messageUrl);

        String mediaUrl = CommonUtils.RouteToUrl(request,"/media/index");
        model.put("media_url",mediaUrl);

        return  "admin/pri/index";
    }

}
