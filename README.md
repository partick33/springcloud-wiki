# SpringCloud-wiki工程
## 本项目是基于我之前的SpringBoot-wiki的旧项目进行服务拆分，引入了es进行快照数据的统计，重新构建
## 技术栈：SpringCloudAlibaba Nacos + openFeign + gateway + redis + rabbitmq + elasticsearch + mysql + mybatis
# 主要模块：
## 1.用户模块 主要是用户授权校验（没有使用shiro和Seurity等安全框架）
## 2.电子书模块 主要是电子书的内容的管理
## 3.分类模块 主要是分类的管理
## 4.文档模块 主要是文档的内容、文档点赞等
## 5.电子书快照模块 主要是电子书相关信息的快照数据统计
## 6.定时任务模块 主要是一些定时任务的管理
## 7.网关模块 主要是管理地址路由跳转
## 8.通用模块 主要是一些通用的配置
## 9.数据模块 主要是与持久层数据库、es数据库等交互相关
