package com.tobdev.qywxthird.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "qywx-cache")
public class QywxCacheConfig {

    private String keyPrefix;
    private String suitTicket;

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getSuitTicket() {

        return keyPrefix+suitTicket;
    }

    public void setSuitTicket(String suitTicket) {
        this.suitTicket = suitTicket;
    }

}
