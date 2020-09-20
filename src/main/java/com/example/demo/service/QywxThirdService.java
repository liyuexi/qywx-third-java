package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.com.qq.weixin.mp.aes.AesException;
import com.example.demo.com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.example.demo.config.QywxThirdConfig;
import com.example.demo.dao.QywxThirdCompany;
import com.example.demo.dao.QywxThirdCompanyRepository;
import com.example.demo.util.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class QywxThirdService {

    private final static Logger logger = LoggerFactory.getLogger("test");

    @Autowired
    private RestTemplate qywxThirdHttpClient;
    @Autowired
    private QywxThirdConfig qywxThirdConfig;
    @Autowired
    private RedisOperator strRedis;
    @Autowired
    private QywxThirdCompanyRepository qywxThirdCompanyRep;

    public String getInstallUrl(String url){
        String preAuthCode= getPreAuthCode();
        String result = String.format(qywxThirdConfig.getInstallUrl(),qywxThirdConfig.getSuiteId(),preAuthCode,url);
        return  result;
    }

    public Boolean getPermentCode(String authCode){
        //通过auth code获取公司信息及永久授权码
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject postJson = new JSONObject();
        postJson.put("auth_code",authCode);
        logger.error(postJson.toString());
        HttpEntity request = new HttpEntity(postJson.toString(),headers);
        String url = String.format(qywxThirdConfig.getPermanentCodeUrl(),getSuiteToken());
        Map response = qywxThirdHttpClient.postForObject(url,request ,Map.class);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
            return  false;
        }
        logger.error(response.toString());
        //保存公司信息
        createCompany(response);
        return true;
    }

    private Boolean createCompany(Map data){
        QywxThirdCompany company = new QywxThirdCompany();
        company.setPermanentCode((String) data.get("permanent_code"));
        Map authCorpInfo =(Map) data.get("auth_corp_info");
        company.setCorpId((String) authCorpInfo.get("corpid")) ;
        qywxThirdCompanyRep.save(company);
        return true;
    }

    public Map getDepartmentList(String id){

        String corpToken = getCorpAccessToken();
        Map paramsMap = new HashMap();
        paramsMap.put("access_token",corpToken);
        paramsMap.put("id",id);
        logger.error(paramsMap.toString());
        logger.error(qywxThirdConfig.getDepartmentUrl());
        Map response = qywxThirdHttpClient.getForObject(qywxThirdConfig.getDepartmentUrl(),Map.class,paramsMap);
        //获取错误日志
        logger.error(response.toString());
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return response;
    }

    public Map getUserSimplelist(String id,String fetch_child){

        String corpToken = getCorpAccessToken();
        Map paramsMap = new HashMap();
        paramsMap.put("access_token",corpToken);
        paramsMap.put("department_id",id);
        paramsMap.put("fetch_child",fetch_child);
        Map response = qywxThirdHttpClient.getForObject(qywxThirdConfig.getUserSimplelist(),Map.class,paramsMap);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return response;
    }

    public String getOauthUrl(String url){
//        应用授权作用域。
//        snsapi_base：静默授权，可获取成员的基础信息（UserId与DeviceId）；
//        snsapi_userinfo：静默授权，可获取成员的详细信息，但不包含手机、邮箱等敏感信息；
//        snsapi_privateinfo：手动授权，可获取成员的详细信息，包含手机、邮箱等敏感信息（已不再支持获取手机号/邮箱）。
        String scope = "snsapi_userinfo";
        String state = "sdfds343";
        String result = String.format(qywxThirdConfig.getOauthUrl(),qywxThirdConfig.getSuiteId(),url,scope,state);
        return  result;
    }

    public Map getOauthUser(String code){

        String suiteToken = getSuiteToken();
        //获取访问用户身份
        Map paramsMap = new HashMap();
        paramsMap.put("suite_access_token",suiteToken);
        paramsMap.put("code",code);
        Map response = qywxThirdHttpClient.getForObject(qywxThirdConfig.getOauthUser(),Map.class,paramsMap);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
            String userTicket = (String) response.get("user_ticket");
            //获取访问用户敏感信息
            //通过auth code获取公司信息及永久授权码
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject postJson = new JSONObject();
            postJson.put("user_ticket",userTicket);
            HttpEntity request = new HttpEntity(postJson.toString(),headers);
            String url = String.format(qywxThirdConfig.getOauthUserDetail(),suiteToken);
            Map detaiResponse = qywxThirdHttpClient.postForObject(url,request ,Map.class);
            //获取错误日志
            if(detaiResponse.containsKey("errcode") && (Integer) detaiResponse.get("errcode") != 0){
                logger.error(detaiResponse.toString());
            }

            return detaiResponse;
        }{
            return response;
        }

    }


    public String getVerify(String sVerifyMsgSig,String sVerifyTimeStamp,
                         String sVerifyNonce,String sVerifyEchoStr){

        String sToken = qywxThirdConfig.getToken();
        String sCorpID =  qywxThirdConfig.getCorpId();
        String sEncodingAESKey = qywxThirdConfig.getEncodingAESKey();

        WXBizMsgCrypt wxcpt = null;
        try {
            wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
        }catch (AesException E){
            return "error";
        }
		/*
		------------使用示例一：验证回调URL---------------
		*企业开启回调模式时，企业微信会向验证url发送一个get请求
		假设点击验证时，企业收到类似请求：
		* GET /cgi-bin/wxpush?msg_signature=5c45ff5e21c57e6ad56bac8758b79b1d9ac89fd3&timestamp=1409659589&nonce=263014780&echostr=P9nAzCzyDtyTWESHep1vC5X9xho%2FqYX3Zpb4yKa9SKld1DsH3Iyt3tP3zNdtp%2B4RPcs8TgAE7OaBO%2BFZXvnaqQ%3D%3D
		* HTTP/1.1 Host: qy.weixin.qq.com

		接收到该请求时，企业应		1.解析出Get请求的参数，包括消息体签名(msg_signature)，时间戳(timestamp)，随机数字串(nonce)以及企业微信推送过来的随机加密字符串(echostr),
		这一步注意作URL解码。
		2.验证消息体签名的正确性
		3. 解密出echostr原文，将原文当作Get请求的response，返回给企业微信
		第2，3步可以用企业微信提供的库函数VerifyURL来实现。

		*/
        // 解析出url上的参数值如下：
        //String sVerifyMsgSig = "5c45ff5e21c57e6ad56bac8758b79b1d9ac89fd3";
        //String sVerifyTimeStamp = "1409659589";
        //String sVerifyNonce = "263014780";
        //String sVerifyEchoStr = "P9nAzCzyDtyTWESHep1vC5X9xho/qYX3Zpb4yKa9SKld1DsH3Iyt3tP3zNdtp+4RPcs8TgAE7OaBO+FZXvnaqQ==";

        // String sVerifyMsgSig = HttpUtils.ParseUrl("msg_signature");
        //String sVerifyTimeStamp = "timestamp";
        //String sVerifyNonce = HttpUtils.ParseUrl("nonce");
        //String sVerifyEchoStr = HttpUtils.ParseUrl("echostr");

        String sEchoStr; //需要返回的明文
        try {
            sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp,
                    sVerifyNonce, sVerifyEchoStr);
            //System.out.println("verifyurl echostr: " + sEchoStr);
            // 验证URL成功，将sEchoStr返回
            // HttpUtils.SetResponse(sEchoStr);
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            e.printStackTrace();
            return "error";
        }
        return  sEchoStr;
    }

    public Map instructCallback(String sVerifyMsgSig,String sVerifyTimeStamp,String sVerifyNonce,String sData){

        String sToken = qywxThirdConfig.getToken();
        String sSuiteid =qywxThirdConfig.getSuiteId();
        String sEncodingAESKey = qywxThirdConfig.getEncodingAESKey();
        Map result = null;
        WXBizMsgCrypt wxcpt = null;
        try {
            wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sSuiteid);
        }catch (AesException E){
            return result;
        }
        try{
            String sMsg = wxcpt.DecryptMsg(sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, sData);
            System.out.println("after encrypt sEncrytMsg: " + sMsg);
            // 加密成功
            // TODO: 解析出明文xml标签的内容进行处理
            // For example:
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(sMsg);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            NodeList infoTypeNode = root.getElementsByTagName("InfoType");
            String infoType = infoTypeNode.item(0).getTextContent();
            logger.info(infoType);
            switch (infoType){
                case "suite_ticket" :
                    setSuitTicket(root);
                    break;
                case "create_auth":
                    //获取auth_code
                    NodeList authcodeNode = root.getElementsByTagName("AuthCode");
                    String authcode = authcodeNode.item(0).getTextContent();
                    logger.info("auth code:"+authcode);
                    getPermentCode(authcode);
                    ;
                    break;
                case "change_auth":
                    ;
                    break;
                case "cancel_auth":
                    ;
                    break;
                default:
                    logger.info(infoType);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            // 加密失败
            return result;
        }
        return  result;
    }


    public Map dataCallback(String sVerifyMsgSig,String sVerifyTimeStamp,String sVerifyNonce,String sData){

        String sToken = qywxThirdConfig.getToken();
        String sCorpId =qywxThirdConfig.getCorpId();
        logger.info(sCorpId);
        String sEncodingAESKey = qywxThirdConfig.getEncodingAESKey();
        Map result = null;
        WXBizMsgCrypt wxcpt = null;
        try {
            wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpId);
        }catch (AesException E){
            return result;
        }
        try{
            String sMsg = wxcpt.DecryptMsg(sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, sData);
            System.out.println("after encrypt sEncrytMsg: " + sMsg);
            // 加密成功
            // TODO: 解析出明文xml标签的内容进行处理
            // For example:
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(sMsg);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            NodeList eventNode = root.getElementsByTagName("Event");
            String event = eventNode.item(0).getTextContent();
            logger.info(event);
            switch (event){
                case "subscribe" :
                    System.out.println("event: " + event);
                    break;
                case "unsubscribe":
                    System.out.println("event: " + event);
                    break;
                default:
                    logger.info(event);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            // 加密失败
            return result;
        }
        return  result;
    }

    //suite_ticket缓存
    private String setSuitTicket(Element root){
        NodeList nodelist = root.getElementsByTagName("SuiteTicket");
        String result = nodelist.item(0).getTextContent();
        strRedis.set("suite_ticket",result,600);
        return result;
    }

    //suite_ticket获取
    public String getSuitTicket(){
        String result = strRedis.get("suite_ticket");
        if(result==""){
            logger.error("suit_ticket为空");
        }
        return result;
    }

    public String  getSuiteToken(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //方式一直接json字符串
//        String postStr = "{\n" +
//                "    \"suite_id\":\"xxxx\" ,\n" +
//                "    \"suite_secret\": \"xxxx\", \n" +
//                "    \"suite_ticket\": \"xxxx\" \n" +
//                "}";
//        HttpEntity request = new HttpEntity(postStr,headers);
        //方式二 json对象转字符串
        String  suiteTicket = getSuitTicket();
        JSONObject postJson = new JSONObject();
        postJson.put("suite_id",qywxThirdConfig.getSuiteId());
        postJson.put("suite_secret",qywxThirdConfig.getSuiteSecret());
        postJson.put("suite_ticket",suiteTicket);
        HttpEntity request = new HttpEntity(postJson.toString(),headers);
        Map response = qywxThirdHttpClient.postForObject(qywxThirdConfig.getSuiteTokenUrl(),request ,Map.class);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        String result = (String) response.get("suite_access_token");
        return result;
    }

    public String getPreAuthCode(){
        String result = "";
        String token = getSuiteToken();
        //获取预授权码
        Map paramsMap = new HashMap();
        paramsMap.put("suite_access_token",token);
        Map response = qywxThirdHttpClient.getForObject(qywxThirdConfig.getPreAuthCodeUrl(),Map.class,paramsMap);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }else{
            result = (String) response.get("pre_auth_code");
            //设置授权楝，对某次预授权码pre_auth_code进行授权类型设置
            setSessionInfo(result);
        }
        return result;
    }

    private Boolean setSessionInfo(String preAuthCode){
        String token = getSuiteToken();
        //如是测试授权，设置授权配置
        if(qywxThirdConfig.getAuthType() == 1){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject sessionInfo = new JSONObject();
            sessionInfo.put("appid", new int[0]);
            sessionInfo.put("auth_type",qywxThirdConfig.getAuthType());
            JSONObject postJson = new JSONObject();
            postJson.put("pre_auth_code",preAuthCode);
            postJson.put("session_info",sessionInfo);
            logger.error(postJson.toString());
            HttpEntity request = new HttpEntity(postJson.toString(),headers);
            String sessionInfoUrl = String.format(qywxThirdConfig.getSessionInfoUrl(),token);
            Map sessionResponse = qywxThirdHttpClient.postForObject(sessionInfoUrl,request ,Map.class);
            //获取错误日志
            if(sessionResponse.containsKey("errcode") && (Integer) sessionResponse.get("errcode") != 0){
                logger.error(sessionResponse.toString());
                return false;
            }
        }
        return  true;
    }

    public String getCorpAccessToken(){
        String result = "";
        QywxThirdCompany company;
        Optional<QywxThirdCompany> optional= qywxThirdCompanyRep.findById(1);
        if(optional!=null && optional.isPresent()){
            company = optional.get();
        }else{
            logger.info("无授权公司信息");
            return  "";
        }

        logger.info(company.toString());
        String  suiteToken = getSuiteToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject postJson = new JSONObject();
        postJson.put("auth_corpid",company.getCorpId());
        postJson.put("permanent_code",company.getPermanentCode());
        HttpEntity request = new HttpEntity(postJson.toString(),headers);
        String  corpTokenUrl =  String.format(qywxThirdConfig.getCorpTokenUrl(),suiteToken);
        Map response = qywxThirdHttpClient.postForObject(corpTokenUrl,request ,Map.class);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }else{
            result = (String) response.get("access_token");
        }

        return result;

    }



}
