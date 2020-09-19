package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "qywx-third")
public class QywxThirdConfig {

    private String corpId;
    private String suiteId;
    private String suiteSecret;
    private String token;
    private String encodingAESKey;
    private Integer authType;
    private String baseUrl = "https://qyapi.weixin.qq.com/cgi-bin/";
    private String servieUrl = baseUrl+"service/";
    private String suiteTokenUrl = servieUrl+"get_suite_token";
    private String preAuthCodeUrl = servieUrl+"get_pre_auth_code?suite_access_token={suite_access_token}";
    private String permanentCodeUrl = servieUrl+"get_permanent_code?suite_access_token=%s";
    private String sessionInfoUrl = servieUrl+"set_session_info?suite_access_token=%s";
    private String installUrl = "https://open.work.weixin.qq.com/3rdapp/install?suite_id=%s&pre_auth_code=%s&redirect_uri=%s&state=STATE";

    private String corpTokenUrl = servieUrl+"get_corp_token?suite_access_token=%s";
    private String departmentUrl = baseUrl+"department/list?access_token={access_token}&id={id}";
    private String userSimplelist = baseUrl+"user/simplelist?access_token={access_token}&department_id={department_id}&fetch_child={fetch_child}}";

    public String getUserSimplelist() {
        return userSimplelist;
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

}
