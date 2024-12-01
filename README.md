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
3. API签名认证(亮点)



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

参数 3： 用户参数

参数 4：sign签名

加密方式： 对称加密，非对称加密，md5加密（不可解密）



用户参数 + 密钥 -> 签名生成算法 -> 不可解密的值

abc + abcdefgh -> xcasdsadfh1i2301hsiaodnsa-



服务端如何校验？

服务端用一样的参数和 签名算法去生成签名，校验签名是否一致。



**如何防重放？**

重放就是同一个签名调用多次

措施：

1.   参数5： 加随机数， 只能用一次

     服务端要保存用过的随机数

2.   参数6： 加timestamp时间戳

3.   方法1和方法2搭配使用，在时间戳的许可时间内，记录使用过的随机数。当超过许可时间，清除记录过的随机数。





完整思路：



Controller方：

1.   用户创建时，会自动生成一个secret ID(√)

2.   用户点击创建secret Key的时候，会创建一个secret Key， 但创建后需要用户自己保存，之后用户无法查看创建过的secret key，只能重新创建一个(√)

     

Client方：

1.   将accessKey，用户参数，随机数，时间戳添加到Header中(√)
2.   使用 用户参数和secret key生成一个sign，并添加到Header中(√)
3.   将header添加到http请求中调用API(√)



API Service方：

验证签名是否合法:

1.   查询数据库中是否有accessKey且可用状态，则pass(√)
2.   查询数据库，用accessKey查询secretKey, 用secretKey和用户参数生成一个sign， 比对这个sign和client传来的sign是否一致， 一致则pass(√)
3.   查看nonce是否存在于redis中，如果存在，则是接口重放，不存在则pass(√)
4.   查看时间戳是否在允许范围内（假设5分钟内），如果时间戳超出范围，则服务器拒绝该请求。
5.   服务器对IP的请求频率进行监控，请求频率过高的，直接禁止。


## OpenAPI SDK

编写一个springboot starter SDK，开发者引入后可以直接在application.yaml中写配置，自动创建客户端

如何构建一个starter
1. 引入lombok和processor(自动生成application引入提示)

    ```xml
     <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    ```

    

2. 去除maven中的build标签, starter项目不需要构建

    ```xml
     <build>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                    <configuration>
                        <excludes>
                            <exclude>
                                <groupId>org.projectlombok</groupId>
                                <artifactId>lombok</artifactId>
                            </exclude>
                        </excludes>
                    </configuration>
                </plugin>
            </plugins>
        </build>
    ```

    
