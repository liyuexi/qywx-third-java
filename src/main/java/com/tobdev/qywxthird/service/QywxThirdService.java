package com.tobdev.qywxthird.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tobdev.qywxthird.com.qq.weixin.mp.aes.AesException;
import com.tobdev.qywxthird.com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.tobdev.qywxthird.com.qq.weixin.mp.aes.XMLParse;
import com.tobdev.qywxthird.config.QywxCacheConfig;
import com.tobdev.qywxthird.config.QywxThirdConfig;

import com.tobdev.qywxthird.model.entity.QywxThirdCompany;
import com.tobdev.qywxthird.model.entity.QywxThirdUser;
import com.tobdev.qywxthird.model.entity.WechatCorpLogin;
import com.tobdev.qywxthird.model.xml.MessageReply;
import com.tobdev.qywxthird.model.xml.MessageText;
import com.tobdev.qywxthird.service.impl.QywxThirdCompanyServiceImpl;

import com.tobdev.qywxthird.service.impl.QywxThirdUserServiceImpl;
import com.tobdev.qywxthird.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QywxThirdService {

    private final static Logger logger = LoggerFactory.getLogger("test");



    @Autowired
    private QywxThirdConfig qywxThirdConfig;
    @Autowired
    private QywxCacheConfig qywxCacheConfig;

    @Autowired
    private RedisUtils strRedis;
    @Autowired
    private QywxThirdCompanyServiceImpl qywxThirdCompanyService;
    @Autowired
    private QywxThirdUserServiceImpl qywxThirdUserService;



    //********************************** 回调处理   *************************//
    /**
     * 指令回调url验证 get请求
     * @param sVerifyMsgSig
     * @param sVerifyTimeStamp
     * @param sVerifyNonce
     * @param sVerifyEchoStr
     * @return
     */
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

    /**
     * 指令回调接收 post请求处理
     * @param sVerifyMsgSig
     * @param sVerifyTimeStamp
     * @param sVerifyNonce
     * @param sData
     * @return
     */
    public String instructCallback(String sVerifyMsgSig,String sVerifyTimeStamp,String sVerifyNonce,String sData){

        String sToken = qywxThirdConfig.getToken();
        String sSuiteid =qywxThirdConfig.getSuiteId();
        String sEncodingAESKey = qywxThirdConfig.getEncodingAESKey();
        String result = "error";
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
                    //获取corp_id
                    NodeList authCorpNode = root.getElementsByTagName("AuthCorpId");
                    String corpId = authCorpNode.item(0).getTextContent();
                    deleteCompany(corpId);
                    ;
                    break;
                case "register_corp":
                    NodeList stateNode = root.getElementsByTagName("state");
                    String state = stateNode.item(0).getTextContent();
                    logger.info("state :"+state);
                    break;
                case "batch_job_result":
                    //通讯录id转译异步任务回调  https://open.work.weixin.qq.com/api/doc/90001/90143/91875

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
        result = "success";
        return  result;
    }


    public String dataCallback(String sVerifyMsgSig,String sVerifyTimeStamp,String sVerifyNonce,String sData){

        String sToken = qywxThirdConfig.getToken();
        String sEncodingAESKey = qywxThirdConfig.getEncodingAESKey();

        String result = "error";
        WXBizMsgCrypt wxcpt = null;
        try {
            /**
             * 收到来自企业微信的回调为：
             *
             * POST /cgi-bin/wxpush?msg_signature=477715d11cdb4164915debcba66cb864d751f3e6&timestamp=1409659813&nonce=1372623149 HTTP/1.1
             * Host: qy.weixin.qq.com
             * Content-Length: 613
             * <xml>
             * <ToUserName><![CDATA[wx5823bf96d3bd56c7]]></ToUserName><Encrypt><![CDATA[RypEvHKD8QQKFhvQ6QleEB4J58tiPdvo+rtK1I9qca6aM/wvqnLSV5zEPeusUiX5L5X/0lWfrf0QADHHhGd3QczcdCUpj911L3vg3W/sYYvuJTs3TUUkSUXxaccAS0qhxchrRYt66wiSpGLYL42aM6A8dTT+6k4aSknmPj48kzJs8qLjvd4Xgpue06DOdnLxAUHzM6+kDZ+HMZfJYuR+LtwGc2hgf5gsijff0ekUNXZiqATP7PF5mZxZ3Izoun1s4zG4LUMnvw2r+KqCKIw+3IQH03v+BCA9nMELNqbSf6tiWSrXJB3LAVGUcallcrw8V2t9EL4EhzJWrQUax5wLVMNS0+rUPA3k22Ncx4XXZS9o0MBH27Bo6BpNelZpS+/uh9KsNlY6bHCmJU9p8g7m3fVKn28H3KDYA5Pl/T8Z1ptDAVe0lXdQ2YoyyH2uyPIGHBZZIs2pDBS8R07+qN+E7Q==]]></Encrypt>
             * <AgentID><![CDATA[218]]></AgentID>
             * </xml>
             */
            //https://work.weixin.qq.com/api/doc/90001/90148/91144#%E9%99%84%E6%B3%A8
            // ReceiveId 在各个场景的含义不同：
            //企业应用的回调，表示corpid
            //第三方事件的回调，表示suiteid
            //但一般为推送过来的ToUserName

            //https://open.work.weixin.qq.com/api/doc/90001/90143/90376
//        String sSuiteid =qywxThirdConfig.getSuiteId();
            Object[] encrypt = XMLParse.extract(sData);
            String sSuiteid = (String) encrypt[2];

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

            //获取消息类型
            // 如果是事件  <MsgType><![CDATA[event]]></MsgType>
            //https://open.work.weixin.qq.com/api/doc/90001/90143/90376
            //如果是成员给应用发送消息  <MsgType><![CDATA[text]]></MsgType> 文本 图片 语音 视频  位置 链接等
            //https://open.work.weixin.qq.com/api/doc/90001/90143/90375
            NodeList msgTypeNode = root.getElementsByTagName("MsgType");
            String msgType = msgTypeNode.item(0).getTextContent();;
            System.out.println("msgType: " + msgType);
            if(msgType.equals("event")){
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
                    //第三方应用审批状态变化通知回调 https://work.weixin.qq.com/api/doc/90001/90143/93798
                    case "open_approval_change":

                        break;
                    case "click":
                        break;
                    case "view":

                        String createTime = String.valueOf(System.currentTimeMillis());
                        String nonce = "sdfsdfsd";

                        //文本消息
                        MessageText msgText = new MessageText();
                        msgText.setToUserName("");
                        msgText.setFromUserName("");
                        msgText.setCreateTime(createTime);
                        msgText.setMsgType("text");
                        msgText.setContent("test");
                        msgText.setMsgId("111111111111111111");
                        msgText.setAgentID("");
                        String msgTextXmlStr = XmlConvertUtils.convertToXml(msgText,"utf-8");
                        //加密消息xml
                        result  = wxcpt.EncryptMsg(msgTextXmlStr,createTime,nonce);
                        break;
                    default:
                        logger.info(event);
                }
            }else{
               switch (msgType){
                   case "text" :
                       break;
                   case "image" :
                       break;
                   case "voice" :
                       break;
                   case "location" :
                       break;
                   case "video" :
                       break;
                   case "link" :
                       break;
               }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            // 加密失败
            System.out.println(result);
            return result;
        }
        result = "success";
        return  result;

    }


    public  String registerCallback(String sVerifyMsgSig,String sVerifyTimeStamp,String sVerifyNonce,String sData){
        String sToken = qywxThirdConfig.getToken();
        String sSuiteid =qywxThirdConfig.getSuiteId();
        String sEncodingAESKey = qywxThirdConfig.getEncodingAESKey();
        String result = "error";
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

                case "create_auth":
                    //获取auth_code
                    NodeList authcodeNode = root.getElementsByTagName("AuthCode");
                    String authcode = authcodeNode.item(0).getTextContent();
                    logger.info("auth code:"+authcode);
                    getPermentCode(authcode);
                    ;
                    break;

                case "register_corp":
                    NodeList stateNode = root.getElementsByTagName("state");
                    String state = stateNode.item(0).getTextContent();
                    logger.info("state :"+state);
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
        result = "success";
        return  result;

    }
//    private String getSign(String timeStamp,String nonce,String data){
//        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sSuiteid);
//
//    }

    //suite_ticket缓存
    private String setSuitTicket(Element root){
        NodeList nodelist = root.getElementsByTagName("SuiteTicket");
        String result = nodelist.item(0).getTextContent();
        logger.info("set"+qywxCacheConfig.getSuitTicket());
        strRedis.set(qywxCacheConfig.getSuitTicket(),result,600);
        return result;
    }

    //suite_ticket获取
    public String getSuitTicket(){
        String result;
        result = (String) strRedis.get(qywxCacheConfig.getSuitTicket());
        logger.info("get:"+qywxCacheConfig.getSuitTicket()+":"+result);
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
        //方式二 对象转字符串j
        String  suiteTicket = getSuitTicket();
        JSONObject postJson = new JSONObject();
        postJson.put("suite_id",qywxThirdConfig.getSuiteId());
        postJson.put("suite_secret",qywxThirdConfig.getSuiteSecret());
        postJson.put("suite_ticket",suiteTicket);
        Map response = RestUtils.post(qywxThirdConfig.getSuiteTokenUrl(),postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        String result = (String) response.get("suite_access_token");
        return result;
    }


    //********************************** 应用安装   *************************//
    public String getPreAuthCode(){

        String result = "";
        String token = getSuiteToken();
        //获取预授权码
//        Map paramsMap = new HashMap();
//        paramsMap.put("suite_access_token",token);
        String url = String.format(qywxThirdConfig.getPreAuthCodeUrl(),token);
        Map response = RestUtils.get(url);
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
        //if(qywxThirdConfig.getAuthType() == 1){

            JSONObject sessionInfo = new JSONObject();
            sessionInfo.put("appid", new int[0]);
            sessionInfo.put("auth_type",qywxThirdConfig.getAuthType());
            JSONObject postJson = new JSONObject();
            postJson.put("pre_auth_code",preAuthCode);
            postJson.put("session_info",sessionInfo);
            logger.error(postJson.toString());
            String sessionInfoUrl = String.format(qywxThirdConfig.getSessionInfoUrl(),token);
            Map sessionResponse = RestUtils.post(sessionInfoUrl,postJson);
            //获取错误日志
            if(sessionResponse.containsKey("errcode") && (Integer) sessionResponse.get("errcode") != 0){
                logger.error(sessionResponse.toString());
                return false;
            }
        //}
        return  true;
    }

    public String getInstallUrl(String url){
        String preAuthCode= getPreAuthCode();
        String result = String.format(qywxThirdConfig.getInstallUrl(),qywxThirdConfig.getSuiteId(),preAuthCode,url);
        return  result;
    }

    public Boolean getPermentCode(String authCode){
        //通过auth code获取公司信息及永久授权码

        JSONObject postJson = new JSONObject();
        postJson.put("auth_code",authCode);
        logger.error(postJson.toString());
        String url = String.format(qywxThirdConfig.getPermanentCodeUrl(),getSuiteToken());
        Map response = RestUtils.post(url,postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
            return  false;
        }
        logger.error(response.toString());

        //保存授权公司信息
        //https://open.work.weixin.qq.com/api/doc/90001/90143/90604
        /**
         "auth_corp_info":
         {
         "corpid": "xxxx",
         "corp_name": "name",
         "corp_type": "verified",
         "corp_square_logo_url": "yyyyy",
         "corp_user_max": 50,
         "corp_agent_max": 30,
         "corp_full_name":"full_name",
         "verified_end_time":1431775834,
         "subject_type": 1,
         "corp_wxqrcode": "zzzzz",
         "corp_scale": "1-50人",
         "corp_industry": "IT服务",
         "corp_sub_industry": "计算机软件/硬件/信息服务",
         "location":"广东省广州市"
         },
         "auth_info":
         {
         "agent" :
         [
         {
         "agentid":1,
         "name":"NAME",
         "round_logo_url":"xxxxxx",
         "square_logo_url":"yyyyyy",
         "appid":1,
         "privilege":
         {
         "level":1,
         "allow_party":[1,2,3],
         "allow_user":["zhansan","lisi"],
         "allow_tag":[1,2,3],
         "extra_party":[4,5,6],
         "extra_user":["wangwu"],
         "extra_tag":[4,5,6]
         }
         },
         {
         "agentid":2,
         "name":"NAME2",
         "round_logo_url":"xxxxxx",
         "square_logo_url":"yyyyyy",
         "appid":5
         }
         ]
         }
         * **/
        //获取永久授权码
        String permanenCode= (String) response.get("permanent_code");
        //获取corpId
        Map authCorpInfo =(Map) response.get("auth_corp_info");
        String corpId = (String) authCorpInfo.get("corpid");
        //获取agent
        Map authInfo = (Map) response.get("auth_info");
        List agentList = (List) authInfo.get("agent");
        Map agent = (Map) agentList.get(0);
        Integer agentId = (Integer) agent.get("agentid");

        QywxThirdCompany company = new QywxThirdCompany();
        company.setPermanentCode(permanenCode);
        company.setCorpId(corpId) ;
        company.setCorpName((String) authCorpInfo.get("corp_name"));
        String fullName = authCorpInfo.get("corp_full_name") ==  null  ? "" :  (String)authCorpInfo.get("corp_full_name");
        company.setCorpFullName(fullName);
        company.setSubjectType((Integer) authCorpInfo.get("subject_type"));
        //设置授权应用id  用于Jssdk agentconfig等使用
        company.setAgentId(agentId);
        company.setStatus(1);
        logger.info(company.toString());
        qywxThirdCompanyService.saveCompany(company);

        //保存授权用户信息
        /**
        "auth_user_info":
        {
            "userid":"aa",
                "name":"xxx",
                "avatar":"http://xxx"
        },
         **/

        QywxThirdUser  user = new QywxThirdUser();
        Map authUserInfo = (Map) response.get("auth_user_info");
        user.setUserId((String)authUserInfo.get("userid"));
        user.setName((String)authUserInfo.get("name"));
        user.setAvatar((String)authUserInfo.get("avatar"));
        user.setCorpId(corpId);
        user.setStatus(1);
        logger.info(user.toString());
        qywxThirdUserService.saveUser(user);

        //异步同步部门，人员 待处理

        return true;

    }

    private boolean deleteCompany(String corpId){
        return  qywxThirdCompanyService.deleteCompanyByCorpId(corpId) ;
    }



    //**********************************  服务商相关   *************************//
    public String getProviderToken(){

        JSONObject postJson = new JSONObject();
        postJson.put("corpid",qywxThirdConfig.getCorpId());
        postJson.put("provider_secret",qywxThirdConfig.getProviderSecret());

        String url = qywxThirdConfig.getProviderTokenUlr();
        logger.info(url);
        logger.info(postJson.toJSONString());

        JSONObject response = RestUtils.post(url,postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        String token =  (String) response.get("provider_access_token");
        return  token;

    }

    public String getRegisterCode(){

        String url = String.format(qywxThirdConfig.getRegisterCodeUrl(),getProviderToken()) ;

        JSONObject postJson = new JSONObject();
        postJson.put("template_id",qywxThirdConfig.getTemplateId());
        postJson.put("state","lyx123456");

        JSONObject response = RestUtils.post(url,postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        String token =  (String) response.get("register_code");
        return  token;

    }

    public String getRegisterUrl(){
        String registerUrl = String.format(qywxThirdConfig.getRegisterUrl(),getRegisterCode());
        return  registerUrl;
    }



    //**********************************  通讯录相关   *************************//
    /**
     *
     * @param filePath
     * @return
     * {
     *    "errcode": 0,
     *    "errmsg": ""，
     *    "type": "image",
     *    "media_id": "1G6nrLmr5EC3MMb_-zK1dDdzmd0p7cNliYu9V5w7o8K0",
     *    "created_at": "1380000000"
     * }
     *
     */
    public Map uploadContact(String filePath){

        //https://open.work.weixin.qq.com/api/doc/90001/90143/91883
        String providerToken = getProviderToken();
        String url = String.format(qywxThirdConfig.getContactUploadUrl(),providerToken,"file");

        MultiValueMap<String, Object> params= new LinkedMultiValueMap<>();
        FileSystemResource resource = new FileSystemResource(new File(filePath));
        params.add("media",resource);
        return  RestUtils.upload(url,params);

    }

    /**
     *
     * @return
     * {
     *     "errcode": 0,
     *     "errmsg": "ok",
     *     "jobid": "xxxxx"
     * }
     */
    public Map transContact(String corpId,String mediaId){
        //https://open.work.weixin.qq.com/api/doc/90001/90143/91846
        /**
         * 参数
         * {
         *     "auth_corpid": "wwxxxx",
         *     "media_id_list": ["1G6nrLmr5EC3MMb_-zK1dDdzmd0p7cNliYu9V5w7o8K0"],
         *     "output_file_name": "学习手册",
         *     "output_file_format": "pdf"
         * }
         */
        String providerToken = getProviderToken();
        String url = String.format(qywxThirdConfig.getContactTransUrl(),providerToken);

        JSONObject postJson = new JSONObject();
        postJson.put("auth_corpid",corpId);

        JSONArray mediaList = new JSONArray();
        mediaList.add(mediaId);
        postJson.put("media_id_list",mediaList);

        return  RestUtils.post(url,postJson);

    }

    /**
     *
     * @param jobId
     * @return
     * {
     *     "errcode": 0,
     *     "errmsg": "ok",
     *     "status": 1,
     *     "type": "contact_id_translate",
     *     "result": {
     *         "contact_id_translate":{
     *             "url":"xxxx"
     *         }
     *     }
     * }
     */
    public Map getTransResult(String jobId){
        //https://open.work.weixin.qq.com/api/doc/90001/90143/91882
        /**
         * id
         */
        String corpToken = getProviderToken();
        String url = String.format(qywxThirdConfig.getTransResultUrl(),corpToken,jobId);
        Map rs = RestUtils.get(url);
        rs.put("rs_url",url);
        return rs;

    }

    public String downloadTrans(String rerefeUrl,String downloadUrl,String targetPath,WechatCorpLogin login) throws IOException {

        //通过cookie及refer模拟下载转译结果
        //设置请求头cookie及refer
        //指定header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Referer", rerefeUrl);
        headers.set("Cookie", login.getCookieSid());
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>( headers);
        String filePath = RestUtils.download(downloadUrl,targetPath,httpEntity);
        return filePath;
    }


    //**********************************  客户联系相关   *************************//
    //获取配置了客户联系功能的成员列表
    public Map getExtContactFollowUserList(String corpId){
        String corpToken = qywxThirdCompanyService.getCorpAccessToken(corpId);
        String url = String.format(qywxThirdConfig.getExtContactFollowUserListUrl(),corpToken) ;
        JSONObject response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }

    //获取指定成员添加的客户列表
    public Map getExtContactList(String corpId,String userId){

        String corpToken = qywxThirdCompanyService.getCorpAccessToken(corpId);
        String url = String.format(qywxThirdConfig.getExtContactListUrl(),corpToken,userId) ;

        JSONObject response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }


    //获取配置过客户群管理的客户群列表。
    public Map getExtContactGroupchatList(String corpId,String userId){

        //https://open.work.weixin.qq.com/api/doc/90001/90143/93414
        String corpToken = qywxThirdCompanyService.getCorpAccessToken(corpId);
        String url = String.format(qywxThirdConfig.getExtContactGroupchatUrl(),corpToken) ;

        //分页，预期请求的数据量，取值范围 1 ~ 1000  测试写死1000
        JSONObject postJson = new JSONObject();
        //owner_filter  userid_list	否	用户ID列表。最多100个
        postJson.put("limit",1000);
        JSONObject response = RestUtils.post(url,postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }


    //**********************************  消息相关   *************************//
    //消息推送
    //发送消息
    public Map sendMessageText(String corpId,String userId,String text){

        //https://open.work.weixin.qq.com/api/doc/90001/90143/93414
        String corpToken = qywxThirdCompanyService.getCorpAccessToken(corpId);

        String url = String.format(qywxThirdConfig.getMessageSendUrl(),corpToken) ;
        //获取企业的agentid
        QywxThirdCompany company =  qywxThirdCompanyService.getCompanyByCorpId(corpId);
        Integer agentId = company.getAgentId();

        JSONObject postJson = new JSONObject();
        postJson.put("msgtype","text");
        postJson.put("agentid",agentId);

        JSONObject testJson =  new JSONObject();
        testJson.put("content",text);
        postJson.put("text",testJson);

        postJson.put("touser",userId);
        JSONObject response = RestUtils.post(url,postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }

    public  String replyMessage(){

//        XStream xstream = new XStream();
//              '<xml>
//               <Encrypt><![CDATA[msg_encrypt]]></Encrypt>
//               <MsgSignature><![CDATA[msg_signature]]></MsgSignature>
//               <TimeStamp>timestamp</TimeStamp>
//               <Nonce><![CDATA[nonce]]></Nonce>
//               </xml>'
        return  "";
    }


    //**********************************  素材管理相关   *************************//
    public  byte[] downloadMedia(String corpId,String mediaId){
        String corpToken = qywxThirdCompanyService.getCorpAccessToken(corpId);
        String mediaDownloadUrl = String.format(qywxThirdConfig.getMediaGetUrl(),corpToken,mediaId);
        return RestUtils.dowload(mediaDownloadUrl);

    }

    //**********************************  oa审批相关   *************************//

    //审批流程引擎
    public Map getApprovalFlow(){
        Map approval = new HashMap();
        approval.put("templateId",qywxThirdConfig.getApprovalFlowId());
        approval.put("thirdNo",new SnowFlakeUtils(0, 0).createOrderNo());
        return approval;
    }

    public Map getApprovalFlowStatus(String corpId,String thirdNo){
        String corpToken = qywxThirdCompanyService.getCorpAccessToken(corpId);
        String approvalUrl = String.format(qywxThirdConfig.getOpenApprovalDataUrl(),corpToken);
        JSONObject postJson = new JSONObject();
        postJson.put("thirdNo",thirdNo);
        return RestUtils.post(approvalUrl,postJson);
    }

    //**********************************  PC相关   *************************//
    //PC网页 sso  用于非企业微信环境下扫码登录，如运行在浏览的器应用后台或者脱离企业微信环境下H5应用
    //https://open.work.weixin.qq.com/wwopen/sso/3rd_qrConnect?appid=ww100000a5f2191&redirect_uri=http%3A%2F%2Fwww.oa.com&state=web_login@gyoss9&usertype=admin
    public  String getSsoUrl(String redirectUrl,String userType){
        String state = "test";
        String ssoUrl = String.format(qywxThirdConfig.getSsoAuthUrl(),qywxThirdConfig.getCorpId(),redirectUrl,state,userType);
        return ssoUrl;
    }

    public Map getLoginInfo(String authCode){

        String url = String.format(qywxThirdConfig.getLoginInfoUrl(),getProviderToken()) ;

        JSONObject postJson = new JSONObject();
        postJson.put("auth_code",authCode);
        JSONObject response = RestUtils.post(url,postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }


    //********************************** H5应用 Oauth   *************************//
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

    public Map getOauthUser(String code) {

        String suiteToken = getSuiteToken();

        //获取访问用户身份

//        方法一
//        使用此url的suiteToken带了-_请求通过 java.lang.IllegalArgumentException: Not enough variable values available to expand 'suite_access_token'
//        https://blog.csdn.net/xs_challenge/article/details/109451263

//        Map<String,String> paramsMap = new HashMap();
//        String suiteToken = "9EEq7bDkw3zQlHU6CDfBqbjFCYHYt1O6OmTfKWOEfOvDGLSq-stlraI6rkeGgTFiMhQ8wu24ivyGdTvuuOR1hETOXgSXUoNINqALHF1I4Z3BxfAuagNh_XTGFcRpXnAq";
//        paramsMap.put("suite_access_token",suiteToken);
//        paramsMap.put("code",code);
//        Map response = qywxThirdHttpClient.getForObject(qywxThirdConfig.getOauthUserUrl(),Map.class,paramsMap);

//        方法二
//        String getOauthUrl = String.format(qywxThirdConfig.getOauthUserUrl(),suiteToken,code);
//        HttpHeaders httpHeaders = new HttpHeaders();
//        //httpHeaders.set("Cookie", cookies);
//        httpHeaders.set("Content-Type", "application/json; charset=utf-8");
//        URI uri = null;
//        try {
//            uri = new URI(getOauthUrl);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            return null;
//        }
//        logger.info(getOauthUrl);
//        Map  response = qywxThirdHttpClient.exchange(URI.create(getOauthUrl), HttpMethod.GET,new HttpEntity<>(httpHeaders),Map.class).getBody();
//

        //方法三
        String getOauthUrl = String.format(qywxThirdConfig.getOauthUserUrl(),suiteToken,code);
        Map  response = RestUtils.get(getOauthUrl);
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
            return  response;
        }

        String userTicket = (String) response.get("user_ticket");
        //获取访问用户敏感信息
        JSONObject postJson = new JSONObject();
        postJson.put("user_ticket",userTicket);
        String url = String.format(qywxThirdConfig.getOauthUserDetailUrl(),suiteToken);
        Map detaiResponse = RestUtils.post(url,postJson);
        //获取错误日志
        if(detaiResponse.containsKey("errcode") && (Integer) detaiResponse.get("errcode") != 0){
            logger.error(detaiResponse.toString());
        }
        /**
         * {
         * 	"errcode": 0,
         * 	"errmsg": "ok",
         * 	"corpid": "wwcc3b4b831051d56e",
         * 	"userid": "LiYueXi",
         * 	"name": "LiYueXi",
         * 	"department": [1],
         * 	"gender": "1",
         * 	"avatar": "https://rescdn.qqmail.com/node/wwmng/wwmng/style/images/independent/DefaultAvatar$73ba92b5.png",
         * 	"open_userid": "woMAh2BwAApVMP0ZDYUk42tUw3CIeHFA"
         * }
         */

        return detaiResponse;

    }



    public Map  getJsSignAgent(String corpId,String nonce, String timestamp,  String signUrl) throws  Exception{
        //https://work.weixin.qq.com/api/doc/90001/90144/90548
        //https://work.weixin.qq.com/api/doc/90001/90144/90539#%E8%8E%B7%E5%8F%96%E5%BA%94%E7%94%A8%E7%9A%84jsapi_ticket

        //获取jsticket
        String corpToken = qywxThirdCompanyService.getCorpAccessToken(corpId);
        String url = String.format(qywxThirdConfig.getJsapiTicketAgentUrl(),corpToken);
        Map response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        String jsapiTicket = (String) response.get("ticket");
        //通过 获取jsticket  获取签名
        String sign = QywxSHA.getSHA1(jsapiTicket,nonce,timestamp,signUrl);

        //获取appid
        QywxThirdCompany company =  qywxThirdCompanyService.getCompanyByCorpId(corpId);
        Integer agentId = company.getAgentId();

        HashMap result = new HashMap();
        result.put("corpId", corpId);
        result.put("agentId", agentId);
        result.put("timestamp", timestamp);
        result.put("nonceStr", nonce);
        result.put("signature", sign);
        return  result;

    }

    //******************************  家校沟通   *********************//
    public  String getSchoolOauthUrl(String url){

//        应用授权作用域。
//        snsapi_base：静默授权，可获取成员的基础信息（UserId与DeviceId）；
//        snsapi_userinfo：静默授权，可获取成员的详细信息，但不包含手机、邮箱等敏感信息；
//        snsapi_privateinfo：手动授权，可获取成员的详细信息，包含手机、邮箱等敏感信息（已不再支持获取手机号/邮箱）。
        String scope = "snsapi_userinfo";
        String state = "sdfds343";
        String result = String.format(qywxThirdConfig.getSchoolOauthUrl(),qywxThirdConfig.getSuiteId(),url,scope,state);
        return  result;
    }

    public Map getSchoolOauthUser(String code) {

        String suiteToken = getSuiteToken();
        //获取访问用户身份
        String getOauthUrl = String.format(qywxThirdConfig.getSchoolOauthUserUrl(),suiteToken,code);
        Map  response = RestUtils.get(getOauthUrl);
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
            return  response;
        }

        Map detaiResponse = new HashMap();
        //家长
        if(response.containsKey("external_userid")){


            //https://work.weixin.qq.com/api/doc/90001/90143/91711

            String coprId = (String) response.get("CorpId");
            String corpToken = qywxThirdCompanyService.getCorpAccessToken(coprId);

            ArrayList parentsArray = (ArrayList) response.get("parents");
            if(parentsArray.size()<0){
                JsonData.buildError("无家长信息");
            }
            HashMap parentsMap = (HashMap) parentsArray.get(0);
            String userId = (String) parentsMap.get("parent_userid");
            //获取访问用户敏感信息
            //https://work.weixin.qq.com/api/doc/90001/90143/92038
            String url = String.format(qywxThirdConfig.getSchoolUserGetUrl(),corpToken,userId);
             detaiResponse = RestUtils.get(url);
            //获取错误日志
            if(detaiResponse.containsKey("errcode") && (Integer) detaiResponse.get("errcode") != 0){
                logger.error(detaiResponse.toString());
            }

        }else{
          //公司成员
            String userTicket = (String) response.get("user_ticket");
            //获取访问用户敏感信息
            JSONObject postJson = new JSONObject();
            postJson.put("user_ticket",userTicket);
            String url = String.format(qywxThirdConfig.getOauthUserDetailUrl(),suiteToken);
             detaiResponse = RestUtils.post(url,postJson);
            //获取错误日志
            if(detaiResponse.containsKey("errcode") && (Integer) detaiResponse.get("errcode") != 0){
                logger.error(detaiResponse.toString());
            }
            detaiResponse.put("user_type",0);
        }



        /**
         * 家长
         {
         "errcode": 0,
         "errmsg": "ok",
         "user_type": 1,
         "student":{
         "student_userid": "zhangsan",
         "name": "张三",
         "department": [1, 2],
         "parents":[
         {
         "parent_userid": "zhangsan_parent1",
         "relation": "爸爸",
         "mobile":"18000000000",
         "is_subscribe": 1,
         "external_userid":"xxxxx"
         },
         {
         "parent_userid": "zhangsan_parent2",
         "relation": "妈妈",
         "mobile": "18000000001",
         "is_subscribe": 0
         }
         ]
         },
         "parent":{
         "parent_userid": "zhangsan_parent2",
         "mobile": "18000000003",
         "is_subscribe": 1,
         "external_userid":"xxxxx",
         "children":[
         {
         "student_userid": "zhangsan",
         "relation": "妈妈"
         },
         {
         "student_userid": "lisi",
         "relation": "伯母"
         }
         ]
         }
         }
         */

        return detaiResponse;

    }

    public Map getSchoolDepartmentList(String corpId,String deptId){
        String corpToken = qywxThirdCompanyService.getCorpAccessToken(corpId);
        String url = String.format(qywxThirdConfig.getSchoolDepartmentListUrl(),corpToken,deptId) ;

        JSONObject response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;
    }

    public Map getSchoolUserList(String corpId,String deptId,String fetchChild){
        String corpToken = qywxThirdCompanyService.getCorpAccessToken(corpId);
        String url = String.format(qywxThirdConfig.getSchoolUserListUrl(),corpToken,deptId,fetchChild) ;

        JSONObject response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }

    public Map extContactMessageSend(String corpId,String studentUserid,String text){

        String corpToken = qywxThirdCompanyService.getCorpAccessToken(corpId);
        String url = String.format(qywxThirdConfig.getExtContactMessageSendUrl(),corpToken) ;

        //获取企业的agentid
        QywxThirdCompany company =  qywxThirdCompanyService.getCompanyByCorpId(corpId);
        Integer agentId = company.getAgentId();

        JSONObject postJson = new JSONObject();
        postJson.put("msgtype","text");
        postJson.put("agentid",agentId);
        postJson.put("to_student_userid",studentUserid);

        JSONObject testJson =  new JSONObject();
        testJson.put("content",text);
        postJson.put("text",testJson);

        JSONObject response = RestUtils.post(url,postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }

    //******************************  小程序应用   *********************//
    public Map getCode2sessionUser(String code){
        String suiteToken = getSuiteToken();
        //获取访问用户身份
//        Map paramsMap = new HashMap();
//        paramsMap.put("suite_access_token",suiteToken);
//        paramsMap.put("code",code);
        String url = String.format(qywxThirdConfig.getCode2sessionUrl(),suiteToken,code);
        Map response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;
    }


}
