package com.tobdev.qywxthird.service.impl;

import com.tobdev.qywxthird.mapper.QywxThirdUserMapper;
import com.tobdev.qywxthird.model.entity.QywxThirdUser;
import com.tobdev.qywxthird.service.QywxThirdUserService;
import com.tobdev.qywxthird.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QywxThirdUserServiceImpl implements QywxThirdUserService {

    @Autowired
    private QywxThirdUserMapper qywxThirdUserMapper;

    @Override
    public QywxThirdUser getUserByCorpIdAndUserId(String corpId,String userId) {
        QywxThirdUser user =  qywxThirdUserMapper.getUserByCorpIdAndUserId(corpId, userId);
        if(user == null) return  null;

        return user;
    }

    @Override
    public Integer saveUser(QywxThirdUser user) {
        return  qywxThirdUserMapper.saveUser(user);
    }

    @Override
    public Boolean deleteUserByCorpId(String corpId){
        Integer rs =   qywxThirdUserMapper.deleteUserByCorpId(corpId);
        if(rs>=0){
            return true;
        }
        return  false;
    }

}
