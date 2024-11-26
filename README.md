# Open API Controller

API开放平台后端

# Get Start




# model folder

dto: Data Transfer Object, 可以理解为请求参数对象

VO: Value Object， 可以理解为相应参数对象

# 接口文档

运行项目后，访问`/doc.html`

# 项目选型


| project           | package      | version | remark                                              |
| ----------------- | ------------ | ------- | --------------------------------------------------- |
| backend,interface | java SDK     | 17      |                                                     |
| backend,interface | springboot   | 3.3.5   | web框架                                             |
| backend           | mybatis-plus | 3.5.9   | 操作数据库                                          |
| backend           | fastjson2    | 2.0.53  | JSON操作                                            |
| backend           | knife4j      | 4.4.0   | open api文档                                        |
| backend           | JJwt         | 0.12.5  | Jwt                                                 |
| interface         | Hutool       | 5.8.16  | 工具包（封装http请求）https://www.hutool.cn/docs/#/ |

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
3. API签名认证



## API签名认证

本质：

1.   签发方签名
2.   使用方签名（校验签名）



作用：

1.   保证安全性，不能随便一个人都能使用



### 如何实现？

通过http request Header传递

-   参数 1： accessKey: 调用的标识，（复杂、无序、无规律）

-   参数 2： secretKey: 密钥（复杂、无序、无规律）

（类似用户名和密码， 区别： ak、sk是无状态的）



注意点：

1.   密钥不能直接在服务器之间传递，有可能会被拦截



参数 3：sign签名

加密方式： 对称加密，非对称加密，md5加密（不可解密）



用户参数 + 密钥 -> 签名生成算法 -> 不可解密的值

abc + abcdefgh -> xcasdsadfh1i2301hsiaodnsa-



服务端如何校验？

服务端用一样的参数和 签名算法去生成签名，校验签名是否一致。



**如何防重放？**

重放就是同一个签名调用多次

措施：

1.   加随机数， 只能用一次

     服务端要保存用过的随机数

2.   加timestamp时间戳

3.   方法1和方法2搭配使用，在时间戳的许可时间内，记录使用过的随机数。当超过许可时间，清除记录过的随机数。