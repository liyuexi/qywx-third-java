package com.tobdev.qywxthird.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tobdev.qywxthird.config.QywxThirdConfig;
import com.tobdev.qywxthird.model.entity.QywxThirdCompany;
import com.tobdev.qywxthird.mapper.QywxThirdCompanyMapper;
import com.tobdev.qywxthird.service.QywxThirdCompanyService;
import com.tobdev.qywxthird.service.QywxThirdService;
import com.tobdev.qywxthird.utils.QywxSHA;
import com.tobdev.qywxthird.utils.RedisUtils;
import com.tobdev.qywxthird.utils.RestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;


@Service
public class QywxThirdCompanyServiceImpl implements QywxThirdCompanyService {

    private final static Logger logger = LoggerFactory.getLogger("test");

    @Autowired
    private QywxThirdCompanyMapper qywxThirdCompanyMapper;

    @Autowired
    private QywxThirdDepartmentServiceImpl qywxThirdDepartmentService;
    @Autowired
    private QywxThirdUserServiceImpl qywxThirdUserService;


    @Autowired
    private QywxThirdConfig qywxThirdConfig;
    @Autowired
    private RedisUtils strRedis;
    @Autowired
    private QywxThirdService qywxThirdService;


    @Override
    public QywxThirdCompany getCompanyByCorpId(String corpId) {
        return qywxThirdCompanyMapper.getCompanyByCorpId(corpId);
    }

    @Override
    public Integer saveCompany(QywxThirdCompany company){
        return qywxThirdCompanyMapper.saveCompany(company);
    }




    public Boolean deleteCompanyByCorpId(String corpId){
        //删除公司
        Integer comResult =   qywxThirdCompanyMapper.deleteCompanyByCorpId(corpId);

        //删除部门
        //Boolean deptResult =   qywxThirdDepartmentService.deleteDepartmentByCorpId(corpId);
        //删除人
        Boolean usrResult =   qywxThirdUserService.deleteUserByCorpId(corpId);

        if(comResult>=0){
            return  true;
        }

        return  false;
    }


    public String getCorpAccessToken(String corpId){
        String result = "";
        QywxThirdCompany company;
        QywxThirdCompany getRs = this.getCompanyByCorpId(corpId);
        if(getRs!=null){
            company = getRs;
        }else{
            logger.info("无授权公司信息");
            return  "";
        }

        logger.info(company.toString());
        String  suiteToken = qywxThirdService.getSuiteToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject postJson = new JSONObject();
        postJson.put("auth_corpid",company.getCorpId());
        postJson.put("permanent_code",company.getPermanentCode());

        String  corpTokenUrl =  String.format(qywxThirdConfig.getCorpTokenUrl(),suiteToken);
        Map response = RestUtils.post(corpTokenUrl,postJson );
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }else{
            result = (String) response.get("access_token");
        }

        return result;

    }



    public Map getDepartmentList(String corpId){

        String corpToken = getCorpAccessToken(corpId);
//        if(id != "0"){
//            paramsMap.put("id",id);
//        }
        String url = String.format(qywxThirdConfig.getDepartmentUrl(),corpToken);
        Map response = RestUtils.get(url);
        //获取错误日志
        logger.error(response.toString());
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return response;
    }


    public Map getUserSimplelist(String corpId,String id,String fetch_child){

        String corpToken = getCorpAccessToken(corpId);
//        Map paramsMap = new HashMap();
//        paramsMap.put("access_token",corpToken);
//        paramsMap.put("department_id",id);
//        paramsMap.put("fetch_child",fetch_child);
        String url =String.format(qywxThirdConfig.getUserSimplelistUrl(),corpToken,id,fetch_child);
        Map response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return response;
    }

    public HashMap getJsSign(String corpId,String nonce, String timestamp,  String signUrl) throws  Exception{


        //获取jsticket
        String corpToken = getCorpAccessToken(corpId);

//        Map paramsMap = new HashMap();
//        paramsMap.put("access_token",corpToken);
        String url = String.format(qywxThirdConfig.getJsapiTicketUrl(),corpToken);
        Map response = RestUtils.get(url);
        //获取错误日志
        logger.error(response.toString());
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        String jsapiTicket = (String) response.get("ticket");
        System.out.println(jsapiTicket);
        String sign = QywxSHA.getSHA1(jsapiTicket,nonce,timestamp,signUrl);
        HashMap result = new HashMap();
        result.put("appId", corpId);
        result.put("timestamp", timestamp);
        result.put("nonceStr", nonce);
        result.put("signature", sign);
        return  result;

    }



}
