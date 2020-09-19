# qywx-third-java

## 企业微信开发指南
https://github.com/liyuexi/qywx-guide

### 企业微信开发第三方应用开发

视频：
https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1503367187451936773&__biz=MzA5ODcyODY0Nw==#wechat_redirect


### 企业微信开发第三方应用开发java版

企业微信第三方应用授权公司表

```CREATE TABLE `qywx_third_company` (
 `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
 `corp_id` varchar(45) NOT NULL DEFAULT '' COMMENT '企业id',
 `permanent_code` varchar(512) NOT NULL DEFAULT '' COMMENT '企业永久授权码',
 `corp_name` varchar(50) NOT NULL DEFAULT '' COMMENT '企业名称',
 `corp_full_name` varchar(100) NOT NULL DEFAULT '' COMMENT '企业全称',
 `subject_type` varchar(512) NOT NULL DEFAULT '' COMMENT '企业类型',
 `verified_end_time` varchar(512) NOT NULL DEFAULT '' COMMENT '企业认证到期时间',
 `status` tinyint(3) DEFAULT '0' COMMENT '账户状态，-1为删除，禁用为0 启用为1',
 `addtime` int(10) unsigned DEFAULT '0' COMMENT '创建时间',
 `modtime` int(10) unsigned DEFAULT '0' COMMENT '修改时间',
 `rectime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '变动时间',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='企业微信三方应用授权公司'```
