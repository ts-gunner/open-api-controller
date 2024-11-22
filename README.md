# Open API Controller

API开放平台后端

# Get Start




# model folder

dto: Data Transfer Object, 可以理解为请求参数对象

VO: Value Object， 可以理解为相应参数对象

# 接口文档

运行项目后，访问`/doc.html`

# 项目选型


| package      | version         | remark                                                      |
| ------------ | --------------- | ----------------------------------------------------------- |
| java SDK     | 17              |                                                             |
| springboot   | 3.2.12-SNAPSHOT | web框架                                                     |
| mybatis-plus | 3.5.9           | 操作数据库                                                  |
| httpclient5  | 5.2.1           | http请求                                                    |
| fastjson2    | 2.0.53          | JSON操作                                                    |
| knife4j      | 4.4.0           | open api文档                                       |
| JJwt         | 0.12.5          | Jwt                                                      |


# 业务特性

1. 全局异常处理器（AOP）
2. 全局请求拦截器（Interceptor）
3. 自定义状态码
4. 封装通用响应类
5. Swagger + knife4j接口文档
6. 多环境配置
7. 用户鉴权
8. 分页排序


# 实现功能

1. 用户创建
2. 角色创建，授权，删除，查询