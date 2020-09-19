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
    private String servieUrl = "https://qyapi.weixin.qq.com/cgi-bin/service/";
    private String suiteTokenUrl = servieUrl+"get_suite_token";
    private String preAuthCodeUrl = servieUrl+"get_pre_auth_code?suite_access_token={suite_access_token}";
    private String permanentCodeUrl = servieUrl+"get_permanent_code?suite_access_token=%s";
    private String sessionInfoUrl = servieUrl+"set_session_info?suite_access_token=%s";
    private String installUrl = "https://open.work.weixin.qq.com/3rdapp/install?suite_id=%s&pre_auth_code=%s&redirect_uri=%s&state=STATE";

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
