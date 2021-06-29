package com.tobdev.qywxthird.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "qywx-cache")
public class QywxCacheConfig {

    private String keyPrefix;
    private String suitTicket;
    private String qrLogin;
    private String qrLoginDuration;

    private String getKeyPrefix() {
        return keyPrefix;
    }

    private void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getSuitTicket() {

        return keyPrefix+suitTicket;
    }

    public void setSuitTicket(String suitTicket) {
        this.suitTicket = suitTicket;
    }

    public String getQrLogin(String companyId) {
        return keyPrefix+companyId;
    }

    public void setQrLogin(String qrLogin) {
        this.qrLogin = qrLogin;
    }

    public String getQrLoginDuration() {
        return qrLoginDuration;
    }

    public void setQrLoginDuration(String qrLoginDuration) {
        this.qrLoginDuration = qrLoginDuration;
    }

}
