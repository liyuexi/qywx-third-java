package com.tobdev.qywxthird.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tobdev.qywxthird.config.QywxCacheConfig;
import com.tobdev.qywxthird.model.entity.WechatCorpLogin;
import com.tobdev.qywxthird.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class QywxThirdCacheService {

    @Autowired
    QywxCacheConfig qywxCacheConfig;

    @Autowired
    RedisUtils strRedis;

    public WechatCorpLogin getAuthLoginCache(String companyId) throws IOException {
        WechatCorpLogin login;
        String loginKey = qywxCacheConfig.getQrLogin(companyId);
        if( strRedis.hasKey(loginKey) ){
            ObjectMapper mapper = new ObjectMapper(); //转换器
            login = mapper.convertValue( strRedis.get(loginKey), WechatCorpLogin.class);
        }else{
            login = new WechatCorpLogin();
        }
        return login;
    }

    public WechatCorpLogin setAuthLoginCache(WechatCorpLogin login){
        //需要有效期 过期时间
        strRedis.set(qywxCacheConfig.getQrLogin(login.getCompanyId()),login,6400);
        return login;
    }

}
