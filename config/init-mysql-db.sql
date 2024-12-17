
-- create database
-- create database if not exists open_api_db default charset=utf8mb4;
--
use open_api_db;



-- 接口信息表
DROP TABLE IF EXISTS `interface_info`;
create table if not exists `interface_info`
(
    `id` int not null auto_increment comment 'id' primary key,
    `name` varchar(255) null comment '接口名称',
    `description` varchar(255) null comment '接口描述',
    `url` varchar(512) not null comment '接口地址',
    `request_header` text null comment '请求头',
    `response_header` text null comment '响应头',
    `request_body` text null comment '请求体',
    `response_body` text null comment '响应体',
    `status` tinyint default 0 comment '接口状态， 0 - 关闭， 1 - 开启',
    `method` varchar(255) not null comment '请求类型',
    `total_calls` bigint default 0 not null comment '接口总调用次数（所有人）',
    `user_account` varchar(255) not null comment '创建人',
    `is_delete` tinyint default 0 comment '逻辑删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) comment '接口信息表';

-- 用户表
DROP TABLE IF EXISTS `user_info`;
create table if not exists `user_info`
(
    id bigint auto_increment primary key comment 'id',
    user_account varchar(256) not null comment '账号',
    `password` varchar(512) not null comment '密码',
    union_id varchar(256) null comment '微信开放平台id',
    mp_open_id varchar(256) null comment '公众号openId',
    secret_id varchar(255) not null comment '用于open api的关键参数之一',
    user_name varchar(256) null comment '用户昵称',
    user_avatar varchar(1024) null comment '用户头像',
    user_profile varchar(512) null comment '用户简介',
    is_delete tinyint default 0 not null comment '是否删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    ) default charset=utf8mb4 comment '用户信息表';
INSERT INTO `user_info` (`id`,`user_account`, `password`, `secret_id`) VALUES
(1, "admin", "c9e71845c91e52eb5823d14c66b16b34", "e8de092f5f9e91f6d451870cfcf6133b3a16ec4c44751e432efd5463e380b946");

-- 用户角色表
DROP TABLE IF EXISTS `user_role`;
create table if not exists `user_role`
(
    role_id int auto_increment not null comment '角色id' primary key,
    role_name varchar(256) not null comment '账号',
    is_delete tinyint default 0 not null comment '是否删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) default charset=utf8mb4 comment '用户角色表';

INSERT INTO `user_role` (`role_id`, `role_name`) VALUES (1, 'superadmin');
INSERT INTO `user_role` (`role_id`, `role_name`) VALUES (2, 'admin');


-- 用户 - 角色 一对多
DROP TABLE IF EXISTS `role_assignment`;
create table if not exists `role_assignment`
(
    id bigint auto_increment comment 'id' primary key,
    role_id int not null comment '角色id',
    user_id varchar(256) not null comment '用户id',
    is_delete tinyint default 0 not null comment '是否删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) default charset=utf8mb4 comment '用户-角色映射表';
INSERT INTO `role_assignment`(`role_id`, `user_id`) VALUES (1, 1);
INSERT INTO `role_assignment`(`role_id`, `user_id`) VALUES (2, 1);

-- 密钥管理 一个角色有多个密钥
DROP TABLE IF EXISTS `secret_info`;
create table if not exists `secret_info`
(
    id int auto_increment comment 'id' primary key,
    user_id varchar(256) not null comment '用户id',
    secret_id varchar(255) not null comment '密钥id',
    secret_key varchar(255) not null comment '密钥key',
    available tinyint default 1 not null comment '是否可用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) default charset=utf8mb4 comment '密钥管理表';


-- 用户调用接口关系表
DROP TABLE IF EXISTS `user_interface_info`;
create table if not exists `user_interface_info`
(
    `id` int not null auto_increment comment 'id' primary key,
    `user_id` bigint null comment '调用者Id',
    `interface_id` int not null comment '接口id',
    `total_count` int default 0 null comment '调用总次数',
    `remain_count` int default 0 null comment '调用剩余次数',
    `status` int default 1 comment '接口状态， 0 - 禁用， 1 - 正常',
    `is_delete` tinyint default 0 comment '逻辑删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) comment '用户调用接口关系表';