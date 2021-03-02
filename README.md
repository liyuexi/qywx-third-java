##友情链接
ToB Dev 李月喜专注B端企业服务开发；  
加开发同行群及咨询联系"li570467731";   
获取更多教程及分享关注公众号“tob dev”;  

企业微信开发指南  
<https://github.com/liyuexi/qywx-guide>  
企业微信自建内部应用开发视频教程  
[自建内部应用开发视频](https://mp.weixin.qq.com/mp/appmsgalbum?__biz=MzA5ODcyODY0Nw==&action=getalbum&album_id=1745513894715916289#wechat_redirect)  
企业微信三方应用开发视频教程  
[第三方应用开发视频](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1503367187451936773&__biz=MzA5ODcyODY0Nw==#wechat_redirect) 

##企业微信第三方应用开发java版demo（本仓库）
###最近更新
2021/03/01；
###demo简介
demo为视频教程[企业微信开发之第三方应用开发篇](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1503367187451936773&__biz=MzA5ODcyODY0Nw==#wechat_redirect) 
产出的企业微信第三方应用java springboot版本源码，教程主要内容如下：
```
课程名称	企业微信开发之第三方应用开发篇
课程简介	从0到1完成企业微信第三方网页应用及第三方小程序应用开发到上线
课程对象	企业微信第三方应用开发者
教学目标	从0到1完成企业微信第三方网页应用及第三方小程序应用开发到上线
章节内容	企业微信第三方应用开发课程概述
	第三方应用与自建内部应用对比，第三方应用的限制，及对H5网页和小程序的支持
	第三方应用概述、开发流程、上线与上架
	第三方市场分析，安装试用第三方网页应用及小程序应用，了解安装及授权安装流程
	应用在哪使用，工作台、微信插件、聊天工具栏概述
	服务商申请注册，服务商助手，加入服务商成长计划及其概述
	第三方应用与标准应用服务商,行业解决方案服务商及第三方仅通讯录应用
	我是选择网页应用还是小程序
	注册创建第三方网页应用及开发配置
	注册创建第三方小程序应用及开发配置
	应用授权安装的几种方式及流程概述【后端】
	回调服务概述,内网穿透配置支持回调路由到本地【后端】
	回调配置概述,回调签名验证及消息解密集成 【后端】
	调用集成类完成Http Get请求验证URL有效性【后端】
	回调支持Http Post请求接收业务数据，完成测试安装【后端】
	回调接口接收suite_ticket获取suite_access_token生成预授权码【后端】
	服务商网站通过授权链接安装应用,获取临时授权码及永久授权码【前端+后端】
	授权链接安装及测试二维码安装demo，程序永久授权入库【前端+后端】
	通讯录接口，获取公司部门及人员信息【后端】
	应用本地调式及真机调试，指定host及代理相关
	网页应用登录，Oauth授权登录【H5+后端】
	网页应用jssdk调用 【H5+后端】
	网页应用jssdk调用agentConfig相关 【H5+后端】
	企业微信小程序应用安装及如何本地调式【小程序】
	小程序企业微信环境兼容【小程序】
	小程序企业微信登录【小程序+后端】
	最小应用提审及上线
	推广二维码，推广包id生成安装链接【后端】
	扫码SSO登录及业务设置url登录【后端】
	解决方案录入，应用搜索可见，应用上架概述
	通讯录展示组件[H5+后端]
	通讯录id转译[后端]
	客户联系客户及客户群列表详情[后端]
	发送应用消息，数据回调接收消息及事件，回复消息[后端]
	素材管理，上传下载素材，jssdk上传及下载图片案例[H5+后端]
	OA审批应用相关接口[H5+后端]
	OA审批流程引擎相关接口[H5+后端]
	家校沟通概述及网页授权登录获取用户信息[后端]
	家校沟通部门、学生与家长获取及发送学校通知[后端]
	java+前端一
	java+前端二
	php+前端一
	php+前端二
	python+前端一
	python+前端一
	补遗及答疑一
	补遗及答疑二
```
###源码相关
基于java springboot开发；  
需要启用redis用于缓存suiteticket等信息；   
需要启用msyql建立数据库及对应的表，用于存储企业,部门,人员等信息；  
###配置
 ####环境配置
- 先配置好环境，初始化数据库，再配置application.yml文件corpid，应用配置等...  
- 配置好域名，如果是本地调试，配置好内网穿透
- 回调配置设置回调链接，看加调get验证是否正常，如果正常点刷新ticket  
- 注意：如是本地调试，ip经常变动，服务商信息里设置好ip白名单，另ticket是十分钟一次如失效或者过期上应用设置里手动刷新即可  
 ####应用配置
- 数据回调URL	http://xx.xx.com:9900/callback/data
- 指令回调URL	http://xx.xx.com:9900/callback/instruct
- 服务商应用主页（应用安装入口 ）http://域名:端口号/ser/index
- H5应用（应用安装后）主页 http://域名:端口/front/index
 
#####库表sql如下
```sql
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
```
