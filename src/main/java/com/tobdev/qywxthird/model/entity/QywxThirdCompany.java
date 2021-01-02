package com.tobdev.qywxthird.model.entity;

public class QywxThirdCompany {


    private Integer id;
    private String corpId;
    private String permanentCode;
    private String corpName;
    private String corpFullName;
    private Integer subjectType;
    private String verifiedEndTime;
    private Integer agentId;
    private Integer status;


    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCorpFullName() {
        return corpFullName;
    }

    public void setCorpFullName(String corpFullName) {
        this.corpFullName = corpFullName;
    }

    public Integer getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(Integer subjectType) {
        this.subjectType = subjectType;
    }

    public String getVerifiedEndTime() {
        return verifiedEndTime;
    }

    public void setVerifiedEndTime(String verifiedEndTime) {
        this.verifiedEndTime = verifiedEndTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getPermanentCode() {
        return permanentCode;
    }

    public void setPermanentCode(String permanentCode) {
        this.permanentCode = permanentCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    @Override
    public String toString() {
        return "QywxThirdCompany{" +
                "id=" + id +
                ", corpId='" + corpId + '\'' +
                ", permanentCode='" + permanentCode + '\'' +
                ", corpName='" + corpName + '\'' +
                ", corpFullName='" + corpFullName + '\'' +
                ", subjectType='" + subjectType + '\'' +
                ", verifiedEndTime='" + verifiedEndTime + '\'' +
                ", status=" + status +
                '}';
    }
}
