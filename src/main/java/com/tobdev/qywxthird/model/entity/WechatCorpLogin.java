package com.tobdev.qywxthird.model.entity;


public class WechatCorpLogin {

    //0 开始 1获取到二维码链接等待扫码  7扫码登录成功 9扫码失败
    private Integer state=0;
    private String logoUrl;
    private String cookieSid;
    private String entryUrlSchema;
    private String companyId;

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getCookieSid() {
        return cookieSid;
    }

    public void setCookieSid(String cookieSid) {
        this.cookieSid = cookieSid;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getEntryUrlSchema() {
        return entryUrlSchema;
    }

    public void setEntryUrlSchema(String entryUrlSchema) {
        this.entryUrlSchema = entryUrlSchema;
    }

    @Override
    public String toString() {
        return "WechatCorpLogin{" +
                "state=" + state +
                ", logoUrl='" + logoUrl + '\'' +
                ", cookieSid='" + cookieSid + '\'' +
                '}';
    }
}
