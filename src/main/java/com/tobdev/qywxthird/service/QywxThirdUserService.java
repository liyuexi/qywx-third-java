package com.tobdev.qywxthird.service;

import com.tobdev.qywxthird.model.entity.QywxThirdUser;
import org.springframework.stereotype.Service;


@Service
public interface QywxThirdUserService {

    public QywxThirdUser getUserByCorpIdAndUserId(String corpId,String userId);

    public Integer saveUser(QywxThirdUser company);

    public Boolean deleteUserByCorpId(String corpId);

}
