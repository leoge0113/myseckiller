## 说明
工作经验一直局限在java开发服务端框架，没有使用过java技术栈应用相当广的SSM框架。
现带着秒杀系统的需求，不考虑并发量情况下，使用SSM完成功能开发。
期望有时间再优化。

## 开发笔记
### bootstrip
- .container 类

用于固定宽度并支持响应式布局的容器。

### 浏览器清缓存
ctr+shit+r 清空缓存刷新
或者F12， network 那边找disable cache
https://www.cnblogs.com/niflhum/p/7084111.html
### spring bean生命周期
在Spring容器中Bean的作用域分为singleton、prototype。对于这两种Bean的作用域简单谈一下个人的认识，singleton的对象为单例模式，这样的对象在Spring的容器中只维持一个，需要的时候可以来取，也就是说这个对象将自己的控制权交给了Spring容器，那么他什么时候创建(单例的对象在加载配置文件的时候创建)与销毁取决于Spring容器而不是取决于Bean类本身。而prototype类型的对象，并不是完全交给Spring容器进行管理的，创建的时候需要Spring容器进行创建，但是销毁的时候并不取决于Spring容器，取决于客户端，当客户端访问的时候由Spring创建对象，客户端访问完成后，Bean对象处于未引用的状态下，就会被JVM自动回收。
http://blog.csdn.net/wangyang1354/article/details/50836626
https://www.cnblogs.com/redcool/p/6397398.html

### spring事务注解使用
pring支持编程式事务管理和声明式事务管理两种方式。

编程式事务管理使用TransactionTemplate或者直接使用底层的PlatformTransactionManager。对于编程式事务管理，spring推荐使用TransactionTemplate。

声明式事务管理建立在AOP之上的。其本质是对方法前后进行拦截，然后在目标方法开始之前创建或者加入一个事务，在执行完目标方法之后根据执行情况提交或者回滚事务。声明式事务最大的优点就是不需要通过编程的方式管理事务，这样就不需要在业务逻辑代码中掺杂事务管理的代码，只需在配置文件中做相关的事务规则声明(或通过基于@Transactional注解的方式)，便可以将事务规则应用到业务逻辑中。

显然声明式事务管理要优于编程式事务管理，这正是spring倡导的非侵入式的开发方式。声明式事务管理使业务代码不受污染，一个普通的POJO对象，只要加上注解就可以获得完全的事务支持。和编程式事务相比，声明式事务唯一不足地方是，后者的最细粒度只能作用到方法级别，无法做到像编程式事务那样可以作用到代码块级别。但是即便有这样的需求，也存在很多变通的方法，比如，可以将需要进行事务管理的代码块独立为方法等等。

声明式事务管理也有两种常用的方式，一种是基于tx和aop名字空间的xml配置文件，另一种就是基于@Transactional注解。显然基于注解的方式更简单易用，更清爽。

==下文讲解注解@Transactional的使用。==

    · 必须关闭数据库的自动提交模式
    · 连接关闭时的是否自动提交
   
    当一个连接关闭时，如果有未提交的事务应该如何处理？JDBC规范没有提及，C3P0默认的策略是回滚任何未提交的事务。这是一个正确的策略，但JDBC驱动提供商之间对此问题并没有达成一致。
    C3P0的autoCommitOnClose属性默认是false,没有十分必要不要动它。或者可以显式的设置此属性为false，这样会更明确。
   
    . 事务的属性
    隔离级别，超时时长都好理解。这里说明下传播特性。
    事务传播行为

    所谓事务的传播行为是指，如果在开始当前事务之前，一个事务上下文已经存在，此时有若干选项可以指定一个事务性方法的执行行为。在TransactionDefinition定义中包括了如下几个表示传播行为的常量：
    
    TransactionDefinition.PROPAGATION_REQUIRED：如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。这是默认值。
    TransactionDefinition.PROPAGATION_REQUIRES_NEW：创建一个新的事务，如果当前存在事务，则把当前事务挂起。
    TransactionDefinition.PROPAGATION_SUPPORTS：如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。
    TransactionDefinition.PROPAGATION_NOT_SUPPORTED：以非事务方式运行，如果当前存在事务，则把当前事务挂起。
    TransactionDefinition.PROPAGATION_NEVER：以非事务方式运行，如果当前存在事务，则抛出异常。
    TransactionDefinition.PROPAGATION_MANDATORY：如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。
    TransactionDefinition.PROPAGATION_NESTED：如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；如果当前没有事务，则该取值等价于TransactionDefinition.PROPAGATION_REQUIRED。
    
    
### Spring 实现RestAPI 

使用Jackson jar包、@RequestBody、@ResponseBody注解，达到：

1. 请求JSON消息体映射为JAVA对象

2. 返回JAVA对象映射为JSON消息体

    - Jackson jar主要用于将json与对象，map做转换。
    - responsebody
    作用： 
          该注解用于将Controller的方法返回的对象，通过适当的HttpMessageConverter转换为指定格式后，写入到Response对象的body数据区。
    使用时机：
          返回的数据不是html标签的页面，而是其他某种格式的数据时（如json、xml等）使用；
    - requestbody
    作用： 
      i) 该注解用于读取Request请求的body部分数据，使用系统默认配置的HttpMessageConverter进行解析，然后把相应的数据绑定到要返回的对象上；

### mybatis使用，entity名字
mybatis全局配置,应该是可以做驼峰命名转换的。
```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--配置全局属性-->
    <settings>
        <!--使用jdbc的getGeneratekeys获取自增主键值-->
        <setting name="useGeneratedKeys" value="true"/>
        <!--使用列别名替换列名　　默认值为true
        select name as title(实体中的属性名是title) form table;
        开启后mybatis会自动帮我们把表中name的值赋到对应实体的title属性中
        -->
        <setting name="useColumnLabel" value="true"/>

        <!--开启驼峰命名转换Table:create_time到 Entity(createTime)-->
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>

</configuration>
```
### 使用过的页面标签
原生html标签；bootstrap的标签。
<div 
<h2
<span
<button
<input 

### 泛型使用
```
public class SeckillResult<T> {

    //请求是否成功
    private boolean success;
    private T data;
    private String error;

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
```