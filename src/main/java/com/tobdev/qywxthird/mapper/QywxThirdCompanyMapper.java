package com.tobdev.qywxthird.mapper;

import com.tobdev.qywxthird.model.entity.QywxThirdCompany;
import org.apache.ibatis.annotations.Param;


public interface QywxThirdCompanyMapper {

    QywxThirdCompany  getCompanyByCorpId(@Param("corp_id") String corpId);

    Integer  saveCompany(QywxThirdCompany company);

    Integer deleteCompanyByCorpId(@Param("corp_id") String corpId);

}
