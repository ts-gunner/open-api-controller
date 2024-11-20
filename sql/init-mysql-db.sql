
-- create database
create database if not exists open_api_db default charset=utf8mb4;

use open_api_db;



-- 接口信息表
create table if not exists open_api_db.`interface_info`
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