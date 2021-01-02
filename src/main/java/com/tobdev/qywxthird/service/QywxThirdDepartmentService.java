package com.tobdev.qywxthird.service;



import com.tobdev.qywxthird.model.entity.QywxThirdDepartment;
import org.springframework.stereotype.Service;


@Service
public interface QywxThirdDepartmentService {

    public QywxThirdDepartment getDepartmentByCorpId(String corpId);

    public Integer saveDepartment(QywxThirdDepartment Department);

    public Boolean deleteDepartmentByCorpId(String corpId);

}
