package com.tobdev.qywxthird.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.tobdev.qywxthird.model.entity.QywxThirdUser;
import com.tobdev.qywxthird.model.entity.WechatCorpLogin;
import com.tobdev.qywxthird.model.excel.QywxContact;
import com.tobdev.qywxthird.service.QywxThirdLoginService;
import com.tobdev.qywxthird.service.QywxThirdService;
import com.tobdev.qywxthird.service.impl.QywxThirdCompanyServiceImpl;
import com.tobdev.qywxthird.service.impl.QywxThirdUserServiceImpl;
import com.tobdev.qywxthird.utils.CommonUtils;
import com.tobdev.qywxthird.utils.RestUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

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

    @Autowired
    private  QywxThirdLoginService loginService ;


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

        String transUrl = CommonUtils.RouteToUrl(request,"/contact/trans");
        model.put("trans_url",transUrl);



        String downloadUrl = CommonUtils.RouteToUrl(request,"/contact/downloadTrans");
        model.put("donwload_url",downloadUrl);

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



    @RequestMapping("/contact/trans")
    String contactTrans(HttpServletRequest request,ModelMap  model) throws Exception {
        String corpId = (String) request.getAttribute("corp_id");


        List<QywxContact> list = new ArrayList<QywxContact>();

        //获取人员部门
        Map deptRs  =   qywxThirdCompanyService.getDepartmentList(corpId);
        if((Integer)deptRs.get("errcode") !=0){
            return "error";
        }
        if(!deptRs.containsKey("department")){
            return "error";
        }

        //遍历部门
        List<Map> deptList = (List)deptRs.get("department");
        for (Map dept:deptList){

            //设置部门
            QywxContact deptExcel = new QywxContact();
            String deptId = (Integer) dept.get("id")+"";
            deptExcel.setCorpId(corpId);
            deptExcel.setIdType(1);
            deptExcel.setIdKey(deptId);
            String deptValue = String.format("$departmentName=%s$",deptId);
            deptExcel.setIdValue(deptValue);
            //添加部门
            list.add(deptExcel);

            //遍历部门下人员
            Map userRs =  qywxThirdCompanyService.getUserSimplelist(corpId,deptId+"","0");
            if(!deptRs.containsKey("department")){
                continue;
            }

            //遍历人员
            List<Map> userList = (List)userRs.get("userlist");
            for (Map user:userList){
                //设置人员
                QywxContact userExcel = new QywxContact();
                String userId = (String) user.get("userid");
                userExcel.setCorpId(corpId);
                userExcel.setIdType(2);
                userExcel.setIdKey(userId);
                String userValue = String.format("$userName=%s$",userId);
                userExcel.setIdValue(userValue);
                //添加人员
                list.add(userExcel);
            }

        }

        //生成excel https://www.yuque.com/easyexcel/doc/write
        String rootPath = System.getProperty("user.dir");//参数即可获得项目相对路径。
        String filePath= rootPath + "/qywxcontact" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(filePath, QywxContact.class).sheet("企业微信转译").doWrite(list);

        //上传通讯录excel素材
        Map uploadRs =  qywxThirdService.uploadContact(filePath);
        if((Integer)uploadRs.get("errcode") !=0){
            return "error";
        }

        //进行通讯录转译
        String mediaId = (String) uploadRs.get("media_id");
        Map transRs =  qywxThirdService.transContact(corpId,mediaId);
        if((Integer)transRs.get("errcode") !=0){
            return "error";
        }

        //跳转到取结果
        String jobId = (String) transRs.get("jobid");
        Map resultRs =  qywxThirdService.getTransResult(jobId);
        if((Integer)resultRs.get("errcode") !=0){
            return "error";
        }

        model.put("file_path",filePath);
        model.put("media_id",mediaId);
        model.put("job_id",jobId);
        model.put("job_url",(String)resultRs.get("rs_url"));
        model.put("job_result_url",CommonUtils.RouteToUrl(request,"/contact/transResult?job_id="+jobId));

        model.put("job_result_download_url",CommonUtils.RouteToUrl(request,"/contact/downloadTrans?job_id="+jobId));

        //扫码自动下载
        String autoLoginUrl = CommonUtils.RouteToUrl(request,"/contact/autoLogin?job_id="+jobId);
        model.put("autologin_url",autoLoginUrl);

        return  "contact/trans";

    }


    @RequestMapping("/contact/transResult")
    @ResponseBody
    Map transResult(HttpServletRequest request,@RequestParam("job_id") String jobId ) throws Exception {

        Map resultRs =  qywxThirdService.getTransResult(jobId);
//        if((Integer)resultRs.get("errcode") !=0){
//            return "error";
//        }
        return resultRs;
    }


    @PostMapping("/contact/autoLogin")
    @ResponseBody
    String autoLogin(HttpServletRequest request,@RequestParam("job_id") String jobId) throws Exception {
        String corpId = (String) request.getAttribute("corp_id");


         return JSONObject.toJSONString(loginService.authLogin(corpId));


    }


    @RequestMapping("/contact/autoLogin")
    @ResponseBody
    Map getAutoLogin(HttpServletRequest request ,@RequestParam("job_id") String jobId) throws Exception {
        String corpId = (String) request.getAttribute("corp_id");

        //需要匹配公司id
        Map rs = new HashMap();

        WechatCorpLogin login  = loginService.getAuthLogin(corpId);
        switch (login.getState()){
            case 0:
            {

            }
            break;
            case 1:
            {
                //提示获取二维码链接成功
                rs.put("qr_url",login.getLogoUrl());

            }
            break;
            case 7:{
                //需要匹配公司id 获取下载url
                Map jobUrlRes =  qywxThirdService.getTransResult(jobId);
                if((Integer)jobUrlRes.get("errcode") !=0){

                }
               String tranFileUrl =  (String) ( (Map )((Map)jobUrlRes.get("result")).get("contact_id_translate")).get("url");
                //通过cookie及refer模拟下载转译结果
                String rootPath = System.getProperty("user.dir");//参数即可获得项目相对路径。
                String filePath= rootPath + "/qywxcontact" + System.currentTimeMillis() + "trans.xlsx";
                //
                String refeUl =  CommonUtils.RouteToUrl(request,"");
                String fileSavePath =  qywxThirdService.downloadTrans(refeUl,tranFileUrl,filePath,login);
                rs.put("save_path",fileSavePath);

            }
            case 9:{

            }
            break;

        }

        rs.put("state",login.getState());

        return   rs;
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
