# arch-design.md

该文档用来详细记录SpringBoot项目的架构设计方法

在本次项目开发中，前端页面作为静态资源已经给出，存放在[src/main/resources/static](../store/src/main/resources/static)文件夹下。数据库表的设计也已经完成，所以下面讨论的主要是后端设计思路

## 数据库连接配置

在新创建一个SpringBoot项目后，为保证数据库连接正常，需要修改[src/main/resources/application.properties](../store/src/main/resources/application.properties)文件，具体格式如下

```properties
# spring.application.name=store
spring.datasource.url=jdbc:mysql://localhost:3306/store?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=123456

mybatis.mapper-locations=classpath:mapper/*.xml
# user.address.max-count=20

# server.servlet.context-path=/store
# spring.servlet.multipart.maxFileSize=10MB
# spring.servlet.multipart.maxRequestSize=10MB
```

## 后端连接数据库

以用户注册功能为例

### 实体类设计

所谓**实体类**，就是用来对应数据库表的Java类

#### 实体类属性设计

1. 首先分析数据库表共有的字段，这样可以避免在不同表对应的实体类中反复设计这些重复字段  
可以利用Java中的继承特性来将共有字段设计成一个基类，如[BaseEntity.java](../store/src/main/java/com/cy/store/entity/BaseEntity.java)  
因为所有数据库表都有这样四个相同的字段

    ```sql
    created_user VARCHAR(20) COMMENT '日志-创建人',
    created_time DATETIME COMMENT '日志-创建时间',
    modified_user VARCHAR(20) COMMENT '日志-最后修改执行人',
    modified_time DATETIME COMMENT '日志-最后修改时间',
    ```

    所以BaseEntity.java也就只有这四个属性

    ```java
    private String createdUser;
    private Date createdTime;
    private String modifiedUser;
    private Date modifiedTime;
    ```

2. 每张表对应的实体类先继承该基类  

   ```java
   public class User extends BaseEntity {}
   ```

   然后按照每张表的字段分别进行设计

#### 实体类方法设计

实体类的方法并不需要去具体设计，只需要利用IDEA自带的Generate功能进行补全  
需要补全的方法有

- `get()`和`set()`方法  
  我们需要对实体类进行封装设计，属性均声明为`private`，所以需要设计对应的方法

- `equals()`和`hashCode()`方法  
  这两个方法通常成对出现，方便HashSet和HashMap的使用  
  在Java中，如果两个对象的`equals()`相等，则它们的`hashCode()`必须相同；如果`equals()`不相等，则`hashCode()`可以不同也可以相同

- `toString()`方法  
  方便观察对象的值

### 数据库映射

在本项目中，使用MyBatis来连接后端与数据库

#### 持久层接口设计

对于一个实体类，需要设计相应的接口，接口中声明的方法即希望执行的SQL语句  
比如说，[UserMapper.java](../store/src/main/java/com/cy/store/mapper/UserMapper.java)中声明了查询与插入的SQL语句

```java
/**
 * 插入用户的数据
 * @param user 用户的数据
 * @return 受影响的行数(增、删、改，都会有受影响的行数作为返回值，可以根据返回值来判断是否执行成功)
 */
Integer insert(User user);

/**
 * 根据用户名来查询用户的数据
 * @param username 用户名
 * @return 如果找到对应的用户则返回这个用户的数据，如果没有找到则返回null值
 */
User findByUsername(String username);
```

#### XML文件设计

因为使用了MyBatis，可以很好地将Java代码与SQL代码区分开来，我们在[UserMapper.xml](../store/src/main/resources/mapper/UserMapper.xml)中编写相应的SQL语句

---
到此为止，持久层的功能已经编写完毕，可以针对该功能进行一次单元测试，如[UserMapperTests.java](../store/src/test/java/com/cy/store/mapper/UserMapperTests.java)

## 业务层功能编写

所谓业务层，是处理该系统中业务逻辑的地方。通过调用持久层，根据需要处理的业务在数据库中进行增删改查的操作

### 业务层总体设计逻辑

#### 异常类设计

同实体类的基类类似，我们先定义了一个业务层的基类[ServiceException.java](../store/src/main/java/com/cy/store/service/ex/ServiceException.java)，该类继承自`RuntimeException`，仅包含不同的构造方法  
针对当前处理的业务，需要先分析可能抛出的异常，以用户注册功能为例，在注册阶段可能出现的异常有

- 用户名被占用
- 插入数据库时因服务器宕机引发的失败

针对这两个异常，分别编写[UsernameDuplicatedException.java](../store/src/main/java/com/cy/store/service/ex/UsernameDuplicatedException.java)和[InsertException.java](../store/src/main/java/com/cy/store/service/ex/InsertException.java)，它们所做的事只有继承异常类基类，用于区分异常的类型

#### 业务层接口设计

在业务层，根据业务的需求来设计接口，用户的注册业务在[IUserService.java](../store/src/main/java/com/cy/store/service/IUserService.java)中被声明

#### 业务层功能实现

根据业务层的接口设计，实现具体的功能模块，体现为实现接口中声明的各种方法，如[UserServiceImpl.java](../store/src/main/java/com/cy/store/service/impl/UserServiceImpl.java)

---
至此，业务层的逻辑也已介绍完毕，在进行单元测试的过程中，由于异常类设计被放在了控制层中，为方便定位问题，需要在业务层测试类中手动捕获异常信息，如[UserServiceTests.java](../store/src/test/java/com/cy/store/service/UserServiceTests.java)

## 控制层

控制层负责与前端接收到的数据进行交互，并将后端的响应结果送回给前端

### 数据交互格式

一般而言，数据交互格式定义为JSON格式，根据需要增添不同的属性字段，如[JsonResult.java](../store/src/main/java/com/cy/store/util/JsonResult.java)

### 控制层设计

#### 控制层基类

为避免重复处理相同的异常，在SpringBoot中，可以通过`@ExceptionHandler`注解将该项目中产生的所有异常交给被该注解修饰的方法进行处理，如[BaseController.java](../store/src/main/java/com/cy/store/controller/BaseController.java)

#### 数据交互

指定好前端的URL路径，利用`@RequestMapping`注解将其一一对应  
因为产生的异常将被自动交由异常处理方法处理，在编写控制层逻辑时异常处理并不需要我们去操心  
详情请看[UserController.java](../store/src/main/java/com/cy/store/controller/UserController.java)

---
在进行控制层单元测试时，我们已经建立了前端与后端的通信，在运行服务后，可以通过前端URL路径进行测试，例如，针对用户注册功能，就可以通过输入下面的URL测试，检查数据库中是否有相应的修改

```txt
http://localhost:8080/users/reg?username=Takamatsu Tomori&password=1122
```
