package com.tobdev.qywxthird.controller;

import com.tobdev.qywxthird.service.QywxThirdService;
import com.tobdev.qywxthird.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class MessageController {

    @Autowired
    private QywxThirdService qywxThirdService;

    @RequestMapping("/message/index")
    public String index(HttpServletRequest request, ModelMap model) throws Exception{

        String sendTextUrl = CommonUtils.RouteToUrl(request,"/message/sendtext");
        model.put("sendtext_url",sendTextUrl);

        return  "message/index";

    }


    @RequestMapping("/message/sendtext")
    @ResponseBody()
    public Map sendtext(HttpServletRequest request,@RequestParam(name = "text",required = false,defaultValue = "测试") String text) throws Exception{

        String corpId = (String) request.getAttribute("corp_id");
        String userId = (String) request.getAttribute("user_id");
        return  qywxThirdService.sendMessageText(corpId,userId,text);

    }






}
