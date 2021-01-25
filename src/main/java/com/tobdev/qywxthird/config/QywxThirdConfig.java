package com.tobdev.qywxthird.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "qywx-third")
public class QywxThirdConfig {

    private String corpId;
    private String providerSecret;

    private String suiteId;
    private String suiteSecret;
    private String token;
    private String encodingAESKey;
    private Integer authType;
    private String templateId;

    private String baseUrl = "https://qyapi.weixin.qq.com/cgi-bin/";

    //服务商相关
    private String serviceUrl = baseUrl+"service/";
    private String suiteTokenUrl = serviceUrl+"get_suite_token";
    private String preAuthCodeUrl = serviceUrl+"get_pre_auth_code?suite_access_token=%s";
    private String permanentCodeUrl = serviceUrl+"get_permanent_code?suite_access_token=%s";
    private String sessionInfoUrl = serviceUrl+"set_session_info?suite_access_token=%s";
    private String installUrl = "https://open.work.weixin.qq.com/3rdapp/install?suite_id=%s&pre_auth_code=%s&redirect_uri=%s&state=STATE";

    private String providerTokenUlr = serviceUrl+"get_provider_token";
    private String registerCodeUrl = serviceUrl+ "get_register_code?provider_access_token=%s";
    private String registerUrl =  "https://open.work.weixin.qq.com/3rdservice/wework/register?register_code=%s";


    private String ssoAuthUrl = "https://open.work.weixin.qq.com/wwopen/sso/3rd_qrConnect?appid=%s&redirect_uri=%s&state=%s&usertype=%s";
    private String loginInfoUrl = serviceUrl+"get_login_info?access_token=%s";

    //通讯录转译
    private String contactUploadUrl = serviceUrl+"media/upload?provider_access_token=%s&type=%s";
    private String contactTransUrl = serviceUrl + "contact/id_translate?provider_access_token=%s";
    private String transResultUrl = serviceUrl + "batch/getresult?provider_access_token=%s&jobid=%s";

    //公司相关
    private String corpTokenUrl = serviceUrl+"get_corp_token?suite_access_token=%s";
    private String departmentUrl = baseUrl+"department/list?access_token=%s";
    private String userSimplelistUrl = baseUrl+"user/simplelist?access_token=%s&department_id=%s&fetch_child=%s";
    private String userDetailUrl = baseUrl+"user/get?access_token={access_token}&userid={user_id}";

    //外部联系人

    //获取配置了客户联系功能的成员列表 https://work.weixin.qq.com/api/doc/90001/90143/92576
    private String extContactFollowUserListUrl = baseUrl+"externalcontact/get_follow_user_list?access_token=%s";
    //获取客户列表  https://work.weixin.qq.com/api/doc/90001/90143/92264
    private  String extContactListUrl = baseUrl+"externalcontact/list?access_token=%s&userid=%s";
    //获取客户群列表 https://work.weixin.qq.com/api/doc/90001/90143/93414
    private  String  extContactGroupchatUrl = baseUrl+"externalcontact/groupchat/list?access_token=%s";

    //消息推送
    private String messageSendUrl= baseUrl+"message/send?access_token=%s";


    // H5应用
    //scope应用授权作用域。
    //snsapi_base：静默授权，可获取成员的基础信息（UserId与DeviceId）；
    //snsapi_userinfo：静默授权，可获取成员的详细信息，但不包含手机、邮箱等敏感信息；
    //snsapi_privateinfo：手动授权，可获取成员的详细信息，包含手机、邮箱等敏感信息（已不再支持获取手机号/邮箱）。
    //https://work.weixin.qq.com/api/doc/90001/90143/91120
    private String oauthUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";
    //https://work.weixin.qq.com/api/doc/90001/90143/91121
    private String oauthUserUrl = serviceUrl+"getuserinfo3rd?suite_access_token=%s&code=%s";
    //https://work.weixin.qq.com/api/doc/90001/90143/91122
    private String oauthUserDetailUrl = serviceUrl+"getuserdetail3rd?suite_access_token=%s";
    //https://work.weixin.qq.com/api/doc/90001/90144/90539
    private String jsapiTicketUrl = baseUrl+"get_jsapi_ticket?access_token=%s";
    //https://work.weixin.qq.com/api/doc/90001/90144/90539#%E8%8E%B7%E5%8F%96%E5%BA%94%E7%94%A8%E7%9A%84jsapi_ticket
    private String jsapiTicketAgentUrl = baseUrl+"ticket/get?access_token=%s&type=agent_config";

    //小程序应用
    //小程序登录流程 https://work.weixin.qq.com/api/doc/90001/90144/92427
    //code2Session https://work.weixin.qq.com/api/doc/90001/90144/92423
    private String code2sessionUrl = serviceUrl+"miniprogram/jscode2session?suite_access_token=%s&js_code=%s&grant_type=authorization_code";


    public String getProviderSecret() {
        return providerSecret;
    }

    public void setProviderSecret(String providerSecret) {
        this.providerSecret = providerSecret;
    }
    public String getJsapiTicketUrl() {
        return jsapiTicketUrl;
    }

    public String getOauthUrl() {
        return oauthUrl;
    }

    public String getUserSimplelistUrl() {
        return userSimplelistUrl;
    }

    public String getCorpTokenUrl() {
        return corpTokenUrl;
    }

    public String getDepartmentUrl() {
        return departmentUrl;
    }

    public String getSuiteTokenUrl() {
        return suiteTokenUrl;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    public String getPreAuthCodeUrl() {
        return preAuthCodeUrl;
    }

    public String getPermanentCodeUrl() {
        return permanentCodeUrl;
    }

    public String getSessionInfoUrl() {
        return sessionInfoUrl;
    }

    public String getCorpId() {
        return corpId;
    }

    public String getSuiteId() {
        return suiteId;
    }

    public String getSuiteSecret() {
        return suiteSecret;
    }

    public String getToken() {
        return token;
    }

    public String getEncodingAESKey() {
        return encodingAESKey;
    }

    public Integer getAuthType() {
        return authType;
    }

    public String getUserDetailUrl() {
        return userDetailUrl;
    }

    public String getOauthUserUrl() {
        return oauthUserUrl;
    }

    public String getOauthUserDetailUrl() {
        return oauthUserDetailUrl;
    }

    public String getCode2sessionUrl() {
        return code2sessionUrl;
    }

    public String getProviderTokenUlr() {
        return providerTokenUlr;
    }

    public String getRegisterCodeUrl() {
        return registerCodeUrl;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public void setSuiteId(String suiteId) {
        this.suiteId = suiteId;
    }

    public void setSuiteSecret(String suiteSecret) {
        this.suiteSecret = suiteSecret;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setEncodingAESKey(String encodingAESKey) {
        this.encodingAESKey = encodingAESKey;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getRegisterUrl() {
        return registerUrl;
    }

    public String getSsoAuthUrl() {
        return ssoAuthUrl;
    }

    public String getLoginInfoUrl() {
        return loginInfoUrl;
    }

    public String getJsapiTicketAgentUrl() {
        return jsapiTicketAgentUrl;
    }

    public String getContactUploadUrl() {
        return contactUploadUrl;
    }

    public String getContactTransUrl() {
        return contactTransUrl;
    }

    public String getTransResultUrl() {
        return transResultUrl;
    }

    public String getExtContactFollowUserListUrl() {
        return extContactFollowUserListUrl;
    }

    public String getExtContactListUrl() {
        return extContactListUrl;
    }

    public String getExtContactGroupchatUrl() {
        return extContactGroupchatUrl;
    }

    public String getMessageSendUrl() {
        return messageSendUrl;
    }
}
