package com.tobdev.qywxthird.controller;

import com.tobdev.qywxthird.service.QywxThirdService;
import com.tobdev.qywxthird.service.impl.QywxThirdCompanyServiceImpl;
import com.tobdev.qywxthird.service.impl.QywxThirdUserServiceImpl;
import com.tobdev.qywxthird.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class OaController {

    @Autowired
    private QywxThirdService qywxThirdService;
    @Autowired
    private QywxThirdUserServiceImpl qywxUserService;

    @Autowired
    private QywxThirdCompanyServiceImpl qywxThirdCompanyService;


    @RequestMapping("/oa/index")
    String index(HttpServletRequest request, ModelMap model) throws Exception {


        String userId = (String) request.getAttribute("user_id");
        model.addAttribute("userId",userId);

        String  timestamp=""+System.currentTimeMillis();
        //随机字符串
        String nonce = "56565";
        //客户传参当前url  当前测试写死
        String url = CommonUtils.RouteToUrl(request,"/oa/index");
        String corpId = (String ) request.getAttribute("corp_id");

        Map signConig = qywxThirdCompanyService.getJsSign(corpId,nonce,timestamp,url);
        model.addAttribute("signConfig",signConig);

        Map signAgentConig = qywxThirdService.getJsSignAgent(corpId,nonce,timestamp,url);
        System.out.println(signAgentConig);
        model.addAttribute("signAgentConfig",signAgentConig);

        //审批流程
        Map approval = qywxThirdService.getApprovalFlow();
        System.out.println(approval.toString());
        model.addAttribute("templateId" ,  approval.get("templateId"));
        model.addAttribute("thirdNo"  ,  approval.get("thirdNo"));


        return "oa/index";

    }


    @RequestMapping("/oa/flowStatus")
    @ResponseBody
    public Map getAprrovalFlowStatus(HttpServletRequest request, @RequestParam("third_no") String thirdNo){
        //当前登录身份
        String userId = (String) request.getAttribute("user_id");
        String corpId = (String) request.getAttribute("corp_id");

        return  qywxThirdService.getApprovalFlowStatus(corpId,thirdNo);
    }


}
