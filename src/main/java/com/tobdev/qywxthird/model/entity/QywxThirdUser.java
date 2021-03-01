package com.tobdev.qywxthird.model.entity;

import java.util.List;

public class QywxThirdUser {


    private Integer id;
    private String corpId;
    private String userId;
    private String name;
    private String avatar;
    private Integer status;
    private QywxThirdCompany company;
    private List<QywxThirdDepartment> departmentList;
    private Integer userType;


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public QywxThirdCompany getCompany() {
        return company;
    }

    public void setCompany(QywxThirdCompany company) {
        this.company = company;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<QywxThirdDepartment> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<QywxThirdDepartment> departmentList) {
        this.departmentList = departmentList;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "QywxThirdUser{" +
                "id=" + id +
                ", corpId='" + corpId + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", status=" + status +
                ", company=" + company +
                ", departmentList=" + departmentList +
                '}';
    }
}
