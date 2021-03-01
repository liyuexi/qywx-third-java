# qywx-third-java
关注公众号“tob dev”获取更多信息;
加企业微信开发同行微信群及咨询等联系"li570467731";

## 企业微信开发指南
https://github.com/liyuexi/qywx-guide

### 企业微信开发第三方应用开发

视频：
https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1503367187451936773&__biz=MzA5ODcyODY0Nw==#wechat_redirect


### 企业微信开发第三方应用开发java版
最新更新时间：2021/03/01；

demo基于springboot；
需要启用redis用于缓存suiteticket等信息；
需要启用msyql建立数据库及对应的表，用于存储企业,部门,人员等信息；
库表sql如下：
CREATE TABLE `qywx_third_company` (
 `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
 `corp_id` varchar(45) NOT NULL DEFAULT '' COMMENT '企业id',
 `permanent_code` varchar(512) NOT NULL DEFAULT '' COMMENT '企业永久授权码',
 `corp_name` varchar(50) NOT NULL DEFAULT '' COMMENT '企业名称',
 `corp_full_name` varchar(100) NOT NULL DEFAULT '' COMMENT '企业全称',
 `subject_type` varchar(512) NOT NULL DEFAULT '' COMMENT '企业类型',
 `verified_end_time` varchar(512) NOT NULL DEFAULT '' COMMENT '企业认证到期时间',
 `agent_id` int(10) DEFAULT '0' COMMENT '授权应用id',
 `status` tinyint(3) DEFAULT '0' COMMENT '账户状态，-1为删除，禁用为0 启用为1',
 `addtime` int(10) unsigned DEFAULT '0' COMMENT '创建时间',
 `modtime` int(10) unsigned DEFAULT '0' COMMENT '修改时间',
 `rectime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '变动时间',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='企业微信三方应用授权公司';

CREATE TABLE `qywx_third_department` (
 `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
 `corp_id` varchar(45) NOT NULL DEFAULT '' COMMENT '企业id',
 `dept_id` varchar(100) NOT NULL DEFAULT '' COMMENT '部门',
 `name` varchar(50) NOT NULL DEFAULT '' COMMENT '部门名称',
 `name_en` varchar(100) NOT NULL DEFAULT '' COMMENT '部门英文问',
 `parentid` varchar(512) NOT NULL DEFAULT '' COMMENT '父部门id',
 `order` int(10) NOT NULL DEFAULT '0' COMMENT '排序',
 `status` tinyint(3) DEFAULT '0' COMMENT '状态，-1为删除，禁用为0 启用为1',
 `addtime` int(10) unsigned DEFAULT '0' COMMENT '创建时间',
 `modtime` int(10) unsigned DEFAULT '0' COMMENT '修改时间',
 `rectime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '变动时间',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='企业微信三方应用授权部门';

CREATE TABLE `qywx_third_user` (
 `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
 `corp_id` varchar(45) NOT NULL DEFAULT '' COMMENT '企业id',
 `user_id` varchar(100) NOT NULL DEFAULT '' COMMENT '用户id',
 `name` varchar(50) NOT NULL DEFAULT '' COMMENT '部门名称',
 `parentid` int(10) NOT NULL DEFAULT '0' COMMENT '父部门id',
 `position` varchar(100) NOT NULL DEFAULT '0' COMMENT '职位',
 `gender` char(10) NOT NULL DEFAULT '' COMMENT '性别',
 `email` char(100) NOT NULL DEFAULT '' COMMENT '邮箱',
 `is_leader_in_dept` char(10) NOT NULL DEFAULT '' COMMENT '是否是部门负责人',
 `avatar` varchar(512) NOT NULL DEFAULT '' COMMENT '头像',
 `thumb_avatar` varchar(512) NOT NULL DEFAULT '' COMMENT '头像缩略图',
 `telephone` char(50) NOT NULL DEFAULT '' COMMENT '电话',
 `alias` char(50) NOT NULL DEFAULT '' COMMENT '别吃饭去',
 `address` varchar(100) NOT NULL DEFAULT '' COMMENT '地址',
 `open_userid` varchar(100) NOT NULL DEFAULT '' COMMENT 'open_userid',
 `main_department` int(10) NOT NULL DEFAULT '0' COMMENT '主部门id',
 `qr_code` varchar(512) NOT NULL DEFAULT '' COMMENT '二维码',
 `status` tinyint(3) DEFAULT '0' COMMENT '状态，-1为删除，禁用为0 启用为1',
 `addtime` int(10) unsigned DEFAULT '0' COMMENT '创建时间',
 `modtime` int(10) unsigned DEFAULT '0' COMMENT '修改时间',
 `rectime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '变动时间',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='企业微信三方应用授权人员';

CREATE TABLE `qywx_third_user_department` (
 `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
 `corp_id` varchar(45) NOT NULL DEFAULT '' COMMENT '企业id',
 `dept_id` varchar(100) NOT NULL DEFAULT '' COMMENT '部门',
 `user_id` varchar(100) NOT NULL DEFAULT '' COMMENT '用户id',
 `order` int(10) NOT NULL DEFAULT '0' COMMENT '排序',
 `status` tinyint(3) DEFAULT '0' COMMENT '状态，-1为删除，禁用为0 启用为1',
 `addtime` int(10) unsigned DEFAULT '0' COMMENT '创建时间',
 `modtime` int(10) unsigned DEFAULT '0' COMMENT '修改时间',
 `rectime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '变动时间',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='企业微信三方应用授权部门'


application.yml配置如下：

；

0，环境配置
（1）先配置好环境，初始化数据库，再配置好配置文件corpid，应用配置等...
 (2) 配置好域名，如果是本地调试，配置好内网穿透
（3）回调配置设置回调链接，看加调get验证是否正常，如果正常点刷新ticket
 注意：如是本地调试，ip经常变动，服务商信息里设置好ip白名单，另ticket是十分钟一次如失效或者过期上应用设置里手动刷新即可
 
1，回调url
数据回调URL	http://xx.xx.com:9900/callback/data
指令回调URL	http://xx.xx.com:9900/callback/instruct

2，服务商应用主页（应用安装入口 ）
http://域名:端口号/ser/index

3，H5应用（应用安装后）
H5应用测试主页 http://域名:端口/front/index
H5应用测试主页 http://域名:端口/front/oauth


其它问题的可以看视频教程