# qywx-third-java

## 企业微信开发指南
https://github.com/liyuexi/qywx-guide

### 企业微信开发第三方应用开发

视频：
https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1503367187451936773&__biz=MzA5ODcyODY0Nw==#wechat_redirect


### 企业微信开发第三方应用开发java版
最新更新时间：2020/09/20；

demo基于springboot；
需要启用redis用于缓存suiteticket；
需要在mysql建立名为qywx_third_demo的数据库，用于存储安装完成获取的永久授权码部分数据；
application.yml中的jpa:hibernate:ddl-auto: 先设置为create,这样jpa会自动创建qywx_third_company和hibernate_sequence两张表，创建完了后设置为update就只更新了。


1，回调url
数据回调URL	http://xx.xx.com:9900/callback/data
指令回调URL	http://xx.xx.com:9900/callback/instruct

2，服务商应用安装入口
http://xx.xx.:9900/app/index

3,应用主页
http://xx.xx.:9900/front/oauth

其它问题的可以看视频教程