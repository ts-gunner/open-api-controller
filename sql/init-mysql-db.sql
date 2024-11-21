
-- create database
create database if not exists open_api_db default charset=utf8mb4;

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
    `response_header` text not null comment '响应头',
    `status` tinyint default 0 comment '接口状态， 0 - 关闭， 1 - 开启',
    `method` varchar(255) not null comment '请求类型',
    `user_id` bigint not null comment '创建人',
    `is_delete` tinyint default 0 comment '逻辑删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) comment '接口信息表';

-- 用户表
DROP TABLE IF EXISTS `user_info`;
create table if not exists `user_info`
(
    id bigint auto_increment comment 'id' primary key,
    user_account varchar(256) not null comment '账号',
    password varchar(512) not null comment '密码',
    union_id varchar(256) null comment '微信开放平台id',
    mp_open_id varchar(256) null comment '公众号openId',
    user_name varchar(256) null comment '用户昵称',
    user_avatar varchar(1024) null comment '用户头像',
    user_profile varchar(512) null comment '用户简介',
    is_delete tinyint default 0 not null comment '是否删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) default charset=utf8mb4 comment '用户信息表';


