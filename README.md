# 项目简介
            本项目核心设计目的是提供企业级的单点登录,会话管理,权限认证。
            子应用只需依赖sso-common,简单配置后项目中即可调用sso-server提供的主数据,权限,日志等服务；
            优点：开发迅速、学习简单、轻量级、易扩展。

# 环境要求
    1.jdk1.8 +
    2.redis mysql
    3.maven
    4.开发工具:eclipse/idea
    5.采用技术SpringBoot,Spring-Session,Redis,Mysql,sso-server端前端采用easyui


# Features
    1.提供企业级组织架构(公司/部门/岗位),主数据管理,RBAC权限设计
    2.统一会话管理,统一配置权限/子应用菜单
    3.应用横向拓展,支持nginx负载均衡,支持docker
    4.Redis异步汇集日志(LogUtil)
    5.简单的鉴权操作(AuthUtil,注解,jsp标签)
    6.可以很方便的在此基础上开发更多通用功能

# 预览
<p align="center">
</p>

