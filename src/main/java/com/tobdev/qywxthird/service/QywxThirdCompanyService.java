package com.tobdev.qywxthird.service;



import com.tobdev.qywxthird.model.entity.QywxThirdCompany;
import org.springframework.stereotype.Service;


@Service
public interface QywxThirdCompanyService {

    public QywxThirdCompany getCompanyByCorpId(String corpId);

    public Integer saveCompany(QywxThirdCompany company);

    public Boolean deleteCompanyByCorpId(String corpId);

}
