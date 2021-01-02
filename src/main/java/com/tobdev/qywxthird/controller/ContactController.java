package com.tobdev.qywxthird.controller;

import com.tobdev.qywxthird.model.entity.QywxThirdUser;
import com.tobdev.qywxthird.service.QywxThirdService;
import com.tobdev.qywxthird.service.impl.QywxThirdCompanyServiceImpl;
import com.tobdev.qywxthird.service.impl.QywxThirdUserServiceImpl;
import com.tobdev.qywxthird.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 通讯录相关，H5及小程序应用公用
 */
@Controller
public class ContactController {

    @Autowired
    private QywxThirdService qywxThirdService;

    @Autowired
    private QywxThirdCompanyServiceImpl qywxThirdCompanyService;

    @Autowired
    private QywxThirdUserServiceImpl qywxUserService;

    @RequestMapping("/contact/index")
    String index(HttpServletRequest request,ModelMap model){

        String corpId = (String) request.getAttribute("corp_id");
        model.put("corp_access_token",qywxThirdCompanyService.getCorpAccessToken(corpId));

        String jssdkUrl = CommonUtils.RouteToUrl(request,"/contact/department");
        model.put("dept_url",jssdkUrl);

        String userUrl = CommonUtils.RouteToUrl(request,"/contact/user");
        model.put("user_url",userUrl);

        String opendataUrl = CommonUtils.RouteToUrl(request,"/contact/opendata");
        model.put("opendata_url",opendataUrl);


        return  "contact/index";

    }

    @RequestMapping("/contact/department")
    @ResponseBody
    //@RequestParam(value = "id",defaultValue = "0") String id
    Map department(HttpServletRequest request){
        String corpId = (String) request.getAttribute("corp_id");
        return  qywxThirdCompanyService.getDepartmentList(corpId);
    }

    @RequestMapping("/contact/user")
    @ResponseBody
    Map user(HttpServletRequest request,@RequestParam(value = "department_id") String departmentId){
        String corpId = (String) request.getAttribute("corp_id");
        return  qywxThirdCompanyService.getUserSimplelist(corpId,departmentId,"0");
    }


    @RequestMapping("/contact/opendata")
    String opendata(HttpServletRequest request,ModelMap  model) throws Exception {

        String userId = (String) request.getAttribute("user_id");
        model.addAttribute("userId",userId);

        String  timestamp=""+System.currentTimeMillis();
        //随机字符串
        String nonce = "56565";
        String url = CommonUtils.RouteToUrl(request,"/contact/opendata");
        String corpId = (String ) request.getAttribute("corp_id");

        Map signConig = qywxThirdCompanyService.getJsSign(corpId,nonce,timestamp,url);
        model.addAttribute("signConfig",signConig);

        Map signAgentConig = qywxThirdService.getJsSignAgent(corpId,nonce,timestamp,url);
        System.out.println(signAgentConig);
        model.addAttribute("signAgentConfig",signAgentConig);

        System.out.println(model.toString());
        return "contact/opendata";
    }

//    @RequestMapping("/contact/getUser")
//    @ResponseBody()
//    public QywxThirdUser getUser(HttpServletRequest request) throws Exception{
//
//        String corpId = (String) request.getAttribute("corp_id");
//        String userId = (String) request.getAttribute("user_id");
//        return  qywxUserService.getUserByCorpIdAndUserId(corpId,userId);
//
//    }


}
