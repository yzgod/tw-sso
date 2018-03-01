/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : tw-sso

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2018-03-01 10:53:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tm_group_role`
-- ----------------------------
DROP TABLE IF EXISTS `tm_group_role`;
CREATE TABLE `tm_group_role` (
  `group_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公司id',
  `role_id` int(11) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`group_id`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tm_group_role
-- ----------------------------

-- ----------------------------
-- Table structure for `tm_group_user`
-- ----------------------------
DROP TABLE IF EXISTS `tm_group_user`;
CREATE TABLE `tm_group_user` (
  `group_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公司id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`group_id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tm_group_user
-- ----------------------------

-- ----------------------------
-- Table structure for `tm_org_role`
-- ----------------------------
DROP TABLE IF EXISTS `tm_org_role`;
CREATE TABLE `tm_org_role` (
  `org_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公司id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`org_id`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tm_org_role
-- ----------------------------

-- ----------------------------
-- Table structure for `tm_org_user`
-- ----------------------------
DROP TABLE IF EXISTS `tm_org_user`;
CREATE TABLE `tm_org_user` (
  `org_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公司id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `is_default` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`org_id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tm_org_user
-- ----------------------------

-- ----------------------------
-- Table structure for `tm_position_role`
-- ----------------------------
DROP TABLE IF EXISTS `tm_position_role`;
CREATE TABLE `tm_position_role` (
  `position_id` int(11) NOT NULL COMMENT '岗位id',
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  PRIMARY KEY (`role_id`,`position_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tm_position_role
-- ----------------------------

-- ----------------------------
-- Table structure for `tm_position_user`
-- ----------------------------
DROP TABLE IF EXISTS `tm_position_user`;
CREATE TABLE `tm_position_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `position_id` int(11) NOT NULL COMMENT '岗位id',
  `is_default` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`user_id`,`position_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tm_position_user
-- ----------------------------

-- ----------------------------
-- Table structure for `tm_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `tm_role_menu`;
CREATE TABLE `tm_role_menu` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `menu_id` int(11) NOT NULL COMMENT '权限id',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tm_role_menu
-- ----------------------------
INSERT INTO `tm_role_menu` VALUES ('1', '1');
INSERT INTO `tm_role_menu` VALUES ('1', '2');
INSERT INTO `tm_role_menu` VALUES ('1', '5');
INSERT INTO `tm_role_menu` VALUES ('1', '6');
INSERT INTO `tm_role_menu` VALUES ('1', '7');
INSERT INTO `tm_role_menu` VALUES ('1', '8');
INSERT INTO `tm_role_menu` VALUES ('1', '9');
INSERT INTO `tm_role_menu` VALUES ('1', '10');
INSERT INTO `tm_role_menu` VALUES ('1', '11');
INSERT INTO `tm_role_menu` VALUES ('1', '12');
INSERT INTO `tm_role_menu` VALUES ('1', '13');
INSERT INTO `tm_role_menu` VALUES ('1', '14');
INSERT INTO `tm_role_menu` VALUES ('1', '15');
INSERT INTO `tm_role_menu` VALUES ('1', '23');
INSERT INTO `tm_role_menu` VALUES ('1', '24');
INSERT INTO `tm_role_menu` VALUES ('1', '25');
INSERT INTO `tm_role_menu` VALUES ('1', '26');
INSERT INTO `tm_role_menu` VALUES ('1', '27');
INSERT INTO `tm_role_menu` VALUES ('1', '29');
INSERT INTO `tm_role_menu` VALUES ('1', '32');
INSERT INTO `tm_role_menu` VALUES ('1', '33');
INSERT INTO `tm_role_menu` VALUES ('1', '34');
INSERT INTO `tm_role_menu` VALUES ('12', '1');
INSERT INTO `tm_role_menu` VALUES ('12', '2');
INSERT INTO `tm_role_menu` VALUES ('12', '9');
INSERT INTO `tm_role_menu` VALUES ('12', '10');
INSERT INTO `tm_role_menu` VALUES ('12', '11');
INSERT INTO `tm_role_menu` VALUES ('12', '12');
INSERT INTO `tm_role_menu` VALUES ('12', '13');
INSERT INTO `tm_role_menu` VALUES ('12', '14');
INSERT INTO `tm_role_menu` VALUES ('12', '15');
INSERT INTO `tm_role_menu` VALUES ('12', '23');
INSERT INTO `tm_role_menu` VALUES ('12', '24');
INSERT INTO `tm_role_menu` VALUES ('12', '25');
INSERT INTO `tm_role_menu` VALUES ('12', '26');
INSERT INTO `tm_role_menu` VALUES ('12', '27');
INSERT INTO `tm_role_menu` VALUES ('12', '29');
INSERT INTO `tm_role_menu` VALUES ('12', '32');
INSERT INTO `tm_role_menu` VALUES ('12', '33');
INSERT INTO `tm_role_menu` VALUES ('12', '34');

-- ----------------------------
-- Table structure for `tm_role_perm`
-- ----------------------------
DROP TABLE IF EXISTS `tm_role_perm`;
CREATE TABLE `tm_role_perm` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `perm_id` int(11) NOT NULL COMMENT '权限id',
  PRIMARY KEY (`role_id`,`perm_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tm_role_perm
-- ----------------------------

-- ----------------------------
-- Table structure for `tm_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `tm_user_role`;
CREATE TABLE `tm_user_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`role_id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tm_user_role
-- ----------------------------
INSERT INTO `tm_user_role` VALUES ('1', '1');
INSERT INTO `tm_user_role` VALUES ('12', '2');

-- ----------------------------
-- Table structure for `tp_aliyun_sms`
-- ----------------------------
DROP TABLE IF EXISTS `tp_aliyun_sms`;
CREATE TABLE `tp_aliyun_sms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phones` varchar(1000) DEFAULT NULL COMMENT '电话号码',
  `sign_name` varchar(255) DEFAULT NULL COMMENT '签名',
  `code` varchar(255) DEFAULT NULL COMMENT '模版',
  `params` varchar(1000) DEFAULT NULL COMMENT '参数',
  `type` varchar(255) DEFAULT NULL COMMENT '短信类型描述',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='阿里云短信发送记录';

-- ----------------------------
-- Records of tp_aliyun_sms
-- ----------------------------

-- ----------------------------
-- Table structure for `tp_log_access`
-- ----------------------------
DROP TABLE IF EXISTS `tp_log_access`;
CREATE TABLE `tp_log_access` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_code` varchar(255) NOT NULL COMMENT '应用编码',
  `login_name` varchar(255) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL COMMENT '请求方法get post',
  `url` varchar(255) DEFAULT NULL COMMENT '地址',
  `ip` varchar(255) DEFAULT NULL COMMENT '远程ip',
  `parameter` text COMMENT '参数',
  `time_used` int(11) DEFAULT NULL COMMENT '请求耗时,毫秒',
  PRIMARY KEY (`id`),
  KEY `app_code` (`app_code`),
  KEY `create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tp_log_access
-- ----------------------------

-- ----------------------------
-- Table structure for `tp_log_login`
-- ----------------------------
DROP TABLE IF EXISTS `tp_log_login`;
CREATE TABLE `tp_log_login` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(255) NOT NULL,
  `real_name` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL COMMENT '远程ip',
  `create_date` datetime DEFAULT NULL,
  `type` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `create_date` (`create_date`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tp_log_login
-- ----------------------------
INSERT INTO `tp_log_login` VALUES ('1', 'admin', '管理员', '0:0:0:0:0:0:0:1', '2018-03-01 10:51:42', '0');
INSERT INTO `tp_log_login` VALUES ('2', 'appadmin', '子应用管理员', '0:0:0:0:0:0:0:1', '2018-03-01 10:51:53', '1');
INSERT INTO `tp_log_login` VALUES ('3', 'appadmin', '子应用管理员', '0:0:0:0:0:0:0:1', '2018-03-01 10:52:01', '0');
INSERT INTO `tp_log_login` VALUES ('4', 'admin', '管理员', '0:0:0:0:0:0:0:1', '2018-03-01 10:52:07', '1');
INSERT INTO `tp_log_login` VALUES ('5', 'admin', '管理员', '0:0:0:0:0:0:0:1', '2018-03-01 10:52:39', '0');
INSERT INTO `tp_log_login` VALUES ('6', 'appadmin', '子应用管理员', '0:0:0:0:0:0:0:1', '2018-03-01 10:52:51', '1');
INSERT INTO `tp_log_login` VALUES ('7', 'appadmin', '子应用管理员', '0:0:0:0:0:0:0:1', '2018-03-01 10:53:25', '0');

-- ----------------------------
-- Table structure for `tp_log_operate`
-- ----------------------------
DROP TABLE IF EXISTS `tp_log_operate`;
CREATE TABLE `tp_log_operate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_code` varchar(255) NOT NULL COMMENT '应用编码',
  `login_name` varchar(255) NOT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `app_code` (`app_code`),
  KEY `create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tp_log_operate
-- ----------------------------

-- ----------------------------
-- Table structure for `tp_register_app`
-- ----------------------------
DROP TABLE IF EXISTS `tp_register_app`;
CREATE TABLE `tp_register_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_code` varchar(255) NOT NULL COMMENT '注册的appcode 唯一',
  `name` varchar(255) NOT NULL COMMENT '应用名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `location` varchar(1000) DEFAULT NULL COMMENT '部署地址',
  `author` varchar(255) DEFAULT NULL COMMENT '负责人',
  `is_alert` bit(1) DEFAULT NULL COMMENT '是否开启异常报警',
  `alert_email` varchar(255) DEFAULT NULL COMMENT '报警邮箱地址',
  PRIMARY KEY (`id`),
  UNIQUE KEY `app_code` (`app_code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='应用注册表';

-- ----------------------------
-- Records of tp_register_app
-- ----------------------------
INSERT INTO `tp_register_app` VALUES ('1', 'sso', '认证中心', '权限认证中心', null, 'yangz', '', '');
INSERT INTO `tp_register_app` VALUES ('5', 'cg', '采购系统', '', null, 'yangz', null, '');

-- ----------------------------
-- Table structure for `ts_base_dept`
-- ----------------------------
DROP TABLE IF EXISTS `ts_base_dept`;
CREATE TABLE `ts_base_dept` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL COMMENT '部门名称',
  `code` varchar(255) NOT NULL COMMENT '部门编码',
  `remark` varchar(255) DEFAULT NULL COMMENT '介绍',
  PRIMARY KEY (`id`),
  KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='基础部门表,公司建立部门时,需选择基础表中的部门,规范code';

-- ----------------------------
-- Records of ts_base_dept
-- ----------------------------
INSERT INTO `ts_base_dept` VALUES ('1', '财务部', 'cwb', null);

-- ----------------------------
-- Table structure for `ts_base_position`
-- ----------------------------
DROP TABLE IF EXISTS `ts_base_position`;
CREATE TABLE `ts_base_position` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL COMMENT '岗位名称',
  `code` varchar(255) NOT NULL COMMENT '岗位编码',
  `remark` varchar(255) DEFAULT NULL COMMENT '介绍',
  PRIMARY KEY (`id`),
  KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='基础岗位表,组织建立岗位时,需选择基础表中的岗位,规范code';

-- ----------------------------
-- Records of ts_base_position
-- ----------------------------

-- ----------------------------
-- Table structure for `ts_menu`
-- ----------------------------
DROP TABLE IF EXISTS `ts_menu`;
CREATE TABLE `ts_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_code` varchar(255) NOT NULL COMMENT '菜单所属的app',
  `name` varchar(255) NOT NULL COMMENT '菜单名称',
  `parent_id` int(11) NOT NULL COMMENT '父菜单id,一般父菜单不存在url和pattern',
  `pattern` varchar(255) DEFAULT NULL COMMENT '菜单权限控制antPathcher,用于权限控制,允许为空,填写按照antpacher规则',
  `url` varchar(255) DEFAULT NULL COMMENT '点击菜单触发的地址',
  `icon` varchar(255) DEFAULT NULL COMMENT '菜单样式',
  `ord` int(11) DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `f1` varchar(255) DEFAULT NULL COMMENT '预留菜单扩展字段1,比如点击菜单触发的脚本',
  `f2` varchar(255) DEFAULT NULL COMMENT '预留菜单扩展字段2',
  `f3` varchar(255) DEFAULT NULL COMMENT '预留菜单扩展字段3',
  `f4` varchar(255) DEFAULT NULL COMMENT '预留菜单扩展字段4',
  PRIMARY KEY (`id`),
  KEY `app_code` (`app_code`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ts_menu
-- ----------------------------
INSERT INTO `ts_menu` VALUES ('1', 'sso', '系统管理', '0', '/**', '', 'iconfont icon-xitongguanli', '1', '系统', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('2', 'sso', '权限管理', '1', '', '/perm', 'iconfont icon-quanxianguanli', '7', '', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('5', 'sso', '用户管理', '1', '', '/user', 'iconfont icon-icon01', '4', '', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('6', 'sso', '组织机构', '1', '', '/org', 'iconfont icon-553zuzhijiagou', '1', '', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('7', 'sso', '用户组', '1', '', '/ug', 'iconfont icon-zugroup', '3', '', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('8', 'sso', '岗位管理', '1', '', '/position', 'iconfont icon-hezuohuobanguanli', '2', '', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('9', 'sso', '菜单管理', '1', '', '/menu', 'iconfont icon-titlebarcaidan', '6', '', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('10', 'sso', '应用管理', '0', '', '', 'iconfont icon-yunyingyong', '3', '', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('11', 'sso', '日志管理', '32', '', '', 'iconfont icon-rizhi', '10', '', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('12', 'sso', '操作日志', '11', '', '/log/op', 'iconfont icon-renwu', '4', '', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('13', 'sso', '登录日志', '11', '', '/log/lg', 'iconfont icon-fenlei', '1', '', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('14', 'sso', '角色管理', '1', '', '/role', 'iconfont icon-jingxiaoshangfuwu', '8', '', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('15', 'sso', '应用注册', '10', '', '/app/register', 'iconfont icon-45354354', '1', '11', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('23', 'sso', '授权管理', '1', '', '', 'iconfont icon-quanxianguanli1', '9', '', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('24', 'sso', '人员授权', '23', '', '/role/user', 'iconfont icon-renyuanguanli2', '1', '', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('25', 'sso', '组织授权', '23', '', '/role/org', 'iconfont icon-renyuanguanli2', '2', '', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('26', 'sso', '岗位授权', '23', '', '/role/position', 'iconfont icon-renyuanguanli2', '3', '', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('27', 'sso', '用户组授权', '23', '', '/role/ug', 'iconfont icon-renyuanguanli2', '4', '', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('29', 'sso', '访问日志', '11', '', '/log/access', 'iconfont icon-richangyewu', '3', '', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('32', 'sso', '基础服务', '0', '', '', 'fuwuzhongxingongchangrenzheng', '2', '', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('33', 'sso', '短信管理', '32', '', '', 'jinlingyingcaiwangtubiao98', '20', '', null, null, null, null);
INSERT INTO `ts_menu` VALUES ('34', 'sso', '发送记录', '33', '', '/sms', '', '10', '', null, null, null, null);

-- ----------------------------
-- Table structure for `ts_org`
-- ----------------------------
DROP TABLE IF EXISTS `ts_org`;
CREATE TABLE `ts_org` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '组织名称',
  `code` varchar(255) NOT NULL COMMENT '组织编码',
  `type_id` int(11) NOT NULL,
  `parent_id` int(11) NOT NULL COMMENT '上级公司',
  `ord` int(11) DEFAULT NULL COMMENT '排序',
  `f1` varchar(255) DEFAULT NULL COMMENT '拓展字段1',
  `f2` varchar(255) DEFAULT NULL COMMENT '拓展字段2',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='组织表';

-- ----------------------------
-- Records of ts_org
-- ----------------------------

-- ----------------------------
-- Table structure for `ts_org_type`
-- ----------------------------
DROP TABLE IF EXISTS `ts_org_type`;
CREATE TABLE `ts_org_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '组织类型名称',
  `code` varchar(255) NOT NULL COMMENT '组织类型编码',
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='组织类型';

-- ----------------------------
-- Records of ts_org_type
-- ----------------------------
INSERT INTO `ts_org_type` VALUES ('1', '集团总公司', 'AC', null);
INSERT INTO `ts_org_type` VALUES ('2', '股份总公司', 'BC', null);
INSERT INTO `ts_org_type` VALUES ('3', '分子公司', 'CC', null);
INSERT INTO `ts_org_type` VALUES ('4', '合资公司', 'DC', null);
INSERT INTO `ts_org_type` VALUES ('5', '区域', 'AR', null);
INSERT INTO `ts_org_type` VALUES ('6', '事业部', 'SD', null);
INSERT INTO `ts_org_type` VALUES ('7', '部门', 'D', '');

-- ----------------------------
-- Table structure for `ts_perm`
-- ----------------------------
DROP TABLE IF EXISTS `ts_perm`;
CREATE TABLE `ts_perm` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_code` varchar(255) NOT NULL COMMENT '权限所属的应用编码',
  `name` varchar(255) NOT NULL COMMENT '权限名称',
  `code` varchar(255) NOT NULL COMMENT '权限编码',
  `group_code` varchar(255) NOT NULL COMMENT '所属组编码',
  `group_id` int(11) NOT NULL COMMENT '权限组id',
  `ord` int(11) DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `app_code_2` (`app_code`,`code`,`group_code`),
  KEY `code` (`code`) USING BTREE,
  KEY `app_code` (`app_code`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ts_perm
-- ----------------------------

-- ----------------------------
-- Table structure for `ts_perm_group`
-- ----------------------------
DROP TABLE IF EXISTS `ts_perm_group`;
CREATE TABLE `ts_perm_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '权限组名称',
  `app_code` varchar(255) NOT NULL,
  `code` varchar(255) NOT NULL COMMENT '权限组code',
  `parent_id` int(11) NOT NULL COMMENT '上级权限组id',
  `ord` int(11) DEFAULT NULL COMMENT '权限组排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '权限组备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `app_code` (`app_code`,`code`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='权限组';

-- ----------------------------
-- Records of ts_perm_group
-- ----------------------------

-- ----------------------------
-- Table structure for `ts_position`
-- ----------------------------
DROP TABLE IF EXISTS `ts_position`;
CREATE TABLE `ts_position` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '岗位名称',
  `code` varchar(255) NOT NULL COMMENT '岗位编码',
  `parent_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL COMMENT '组织id',
  `ord` int(11) DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ts_position
-- ----------------------------

-- ----------------------------
-- Table structure for `ts_role`
-- ----------------------------
DROP TABLE IF EXISTS `ts_role`;
CREATE TABLE `ts_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '角色名称',
  `code` varchar(255) NOT NULL COMMENT '角色编码',
  `app_code` varchar(255) NOT NULL COMMENT '角色对应的appcode',
  `parent_id` int(255) NOT NULL COMMENT '父角色id,没有父角色为0',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `f1` varchar(255) DEFAULT NULL,
  `f2` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_2` (`code`,`app_code`),
  KEY `app_code` (`app_code`),
  KEY `code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ts_role
-- ----------------------------
INSERT INTO `ts_role` VALUES ('1', '超级管理员角色', 'system_sso', 'sso', '0', '超级管理员角色-内置角色', null, null);
INSERT INTO `ts_role` VALUES ('12', '应用管理员角色-采购系统', 'system_cg', 'sso', '0', '应用管理员-内置角色', null, null);

-- ----------------------------
-- Table structure for `ts_user`
-- ----------------------------
DROP TABLE IF EXISTS `ts_user`;
CREATE TABLE `ts_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(255) NOT NULL COMMENT '登录名',
  `real_name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `cell_phone` varchar(255) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱号码',
  `password` varchar(255) DEFAULT NULL COMMENT '密码加密',
  `forbidden` bit(1) NOT NULL DEFAULT b'0' COMMENT '禁用与否',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_name` (`login_name`),
  UNIQUE KEY `cell_phone` (`cell_phone`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ts_user
-- ----------------------------
INSERT INTO `ts_user` VALUES ('1', 'admin', '管理员', '12345678', null, '301DE9290755171627162B065DFE425F', '', '2018-02-01 16:48:25', null);
INSERT INTO `ts_user` VALUES ('2', 'appadmin', '子应用管理员', '18109047620', '', '301DE9290755171627162B065DFE425F', '', '2018-03-01 10:51:05', '');

-- ----------------------------
-- Table structure for `ts_user_group`
-- ----------------------------
DROP TABLE IF EXISTS `ts_user_group`;
CREATE TABLE `ts_user_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '用户组名称',
  `code` varchar(255) NOT NULL COMMENT '用户组编码',
  `ord` int(11) DEFAULT NULL COMMENT '用户组排序',
  `parent_id` int(11) NOT NULL COMMENT '用户组父id',
  `f1` varchar(255) DEFAULT NULL,
  `f2` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL COMMENT '用户组备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户组';

-- ----------------------------
-- Records of ts_user_group
-- ----------------------------
