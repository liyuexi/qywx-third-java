package com.tobdev.qywxthird.model.excel;


import com.alibaba.excel.annotation.ExcelProperty;

public class QywxContact {

    @ExcelProperty("企业id")
    private String corpId;
    @ExcelProperty("id类型 1部门 2人员")
    private Integer idType;
    @ExcelProperty("id")
    private String idKey;
    @ExcelProperty("id对应名称")
    private String idValue;

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Integer getIdType() {
        return idType;
    }

    public void setIdType(Integer idType) {
        this.idType = idType;
    }

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    public String getIdValue() {
        return idValue;
    }

    public void setIdValue(String idValue) {
        this.idValue = idValue;
    }

}
