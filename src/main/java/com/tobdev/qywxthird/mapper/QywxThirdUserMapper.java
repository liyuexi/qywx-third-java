package com.tobdev.qywxthird.mapper;

import com.tobdev.qywxthird.model.entity.QywxThirdUser;
import org.apache.ibatis.annotations.Param;


public interface QywxThirdUserMapper {

    QywxThirdUser getUserByCorpIdAndUserId(@Param("corp_id") String corpId,@Param("user_id") String userId);

    Integer  saveUser(QywxThirdUser user);

    Integer deleteUser(@Param("user_id") String userId);

    Integer deleteUserByCorpId(@Param("corp_id") String corpId);

}
