# Shiro学习

### 一、搭建项目

### 二、添加POM依赖

```php+HTML
<dependencies>
        <!--引入 web支持 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--引入shiro-->
        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-core</artifactId>
            <version>1.3.2</version>
        </dependency>
        <!-- 引入Mysql数据库-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.25</version>
        </dependency>
 		<!--导入thymeleaf页面模板-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.3</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>
```

### 三、测试

##### ~编写测试controller

```Java
@Controller
public class TestController {

    @GetMapping("/hello")
    @ResponseBody
    public String _Hello(){
        return "hello word";
    }
}
```

##### 编写配置文件 application.yml

```properties
#tomcat端口号
server:
  port: 80
#mysql
spring:
  datasource:
    #mysql配置
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/sportplay?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC
    username: root
    password: 123456
```

##### 编写完成之后：

**测试**：

​		浏览器输入localhost:80/hello 返回hello word 则测试成功

![image-20200703175854072](C:\Users\34675\AppData\Roaming\Typora\typora-user-images\image-20200703175854072.png)

### 四、引入thymeleaf

```php+HTML
<!--导入thymeleaf页面模板-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
```

##### 测试

1、**编写控制层测试类**

```java
    @GetMapping("/")
    public String testThymeleaf(Model model){
        model.addAttribute("username","Herther");
        return "index";
    }
```

2、**编写html代码**

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1 th:text="${username}">
</h1>
</body>
</html>
```

3、**测试结果**

![image-20200705115652302](C:\Users\34675\AppData\Roaming\Typora\typora-user-images\image-20200705115652302.png)

### 五、Spring Boot与shiro的整合实现用户认证

##### 一、Shiro的核心Api

**Subject：用户主体（把操作交给SecurityManager）**

**SecurityManager：安全管理器（关联Realm）**

**Realm: Shiro链接数据的桥梁**

##### 二、Spring Boot整合shiro依赖

```php+HTML
   <!--引入shiro-->
        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-core</artifactId>
            <version>1.3.2</version>
        </dependency>
```

##### 三、编写Shiro配置类

**创建一个Shiro的包**

**创建一个ShiroConfig的类**

```java
package xyz.herther.Shiro;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * shiro配置类
 * Herther
 * 2020/7/5
 */
@Configuration
public class ShiroConfig {
    /**
     *创建 ShiroFilterfactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager")DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean= new ShiroFilterFactoryBean();
        //1、设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        /**
         *Shiro内置过滤器，可以实现权限相关拦截
         * 常用的过滤器：
         * anon：无需认证（登录可以访问）
         * authc：必须认证才可以访问
         * user：如果使用rememberMe的功能才可以直接访问
         * role：该资源必须得到角色的权限才可以访问
         */
        Map<String, String> filterMap=new LinkedHashMap<String,String>();
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        //filterMap.put("/add","authc");
        //filterMap.put("/update", "authc");
        //filterMap.put("/tologin","anon");
        filterMap.put("/","anon");
        //修改登录页面
        filterMap.put("/*","authc");
        shiroFilterFactoryBean.setLoginUrl("/tologin");
        return shiroFilterFactoryBean;
    }

    /**
     * 安全管理器
     * 创建 DeafultWebSecurityManager
     */
    @Bean(name = "defaultWebSecurityManager")
    public DefaultWebSecurityManager getDeafaultWebSecurityManager(@Qualifier("userReaml")UserReaml userReaml){
        DefaultWebSecurityManager WebSecurityManager = new DefaultWebSecurityManager();
        //关联Reaml
        WebSecurityManager.setRealm(userReaml);
        return WebSecurityManager;
    }
    /**
     * 创建Reaml
     */
    @Bean(name = "userReaml")
    public UserReaml getRealm(){
        return new UserReaml();
    }

}


```

**创建一个UserReaml的类**

```java
package xyz.herther.Shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class UserReaml extends AuthorizingRealm {
    //获取授权信息
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("获取授权信息");
        return null;
    }
    //获取身份验证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("获取身份验证");
        return null;
    }
}

```

**编写跳转页面**

**addUser**

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>用户添加页面</title>
</head>
<body>
    <h1>用户添加页面</h1>
</body>
</html>
```

**UpdateUser**

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>用户更新页面</title>
</head>
<body>
    <h1>用户更新页面</h1>
</body>
</html>
```

**Index**

```Html
<!DOCTYPE html>

<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1 th:text="${username}">
</h1>
<hr>
<h2>
    这是用户添加页面:
    <a href="add">添加</a>
</h2>

<h2>
    这是用户更新页面:
    <a href="update">更新</a>
</h2>

</body>
</html>
```

**login**

```Html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>登录</title>
</head>
<body>
<h1>登录页面</h1>
</body>
</html>
```

**TestController.class**

```java
package xyz.herther.controller;

import com.sun.net.httpserver.HttpsServer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 测试页面
 * @Herther
 * 时间2020/7/5
 */
@Controller
public class TestController {

    @GetMapping("/hello")
    @ResponseBody
    public String _Hello(){
        return "hello word";
    }

//    测试thymeleaf页面
    @GetMapping("/")
    public String testThymeleaf(Model model){
        model.addAttribute("username","Herther");
        return "index";
    }
    @GetMapping("/add")
    public String addUser( ){
        return "user/AddUser";
    }
    @GetMapping("/update")
    public String updateUser( ){
        return "user/UpdateUser";
    }
    @GetMapping("/tologin")
    public String login(){
        return "/login";
    }
}

```

##### 四、实现用户认证登录操作

**编写控制层代码**

```JAVA
@PostMapping("/login")
    public String login(String username,String passowrd,Model model) {
        System.out.println("username：" + username + "password：" + passowrd);
        /**
         * shiro编写认证操作
         */
        //1、获取subject
        Subject subject = SecurityUtils.getSubject();
        //2、封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, passowrd);
        //3、执行登录方法
        try {
            subject.login(token);
            return "redirect:/index";
        } catch (UnknownAccountException e) {
            model.addAttribute("msg", "用户名不存在");
            return "login";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("msg","用户名密码错误");
            return "login";
        }

    }
```

**编写html页面**

```HTML
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>登录</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container">
    <form action="login" method="post" class="login-form">
        <h2>登录</h2>
        <input type="text" name="username" placeholder="用户名"/>
        <input type="password" name="password" placeholder="密码">
        <button type="submit">登录</button>
    </form>
    <h5 th:text="${msg}" style="color: #4186D3"></h5>
</div>


</body>
</html>
<script>

</script>

```

**编写Reaml的认证逻辑**

```java
package xyz.herther.Shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class UserReaml extends AuthorizingRealm {
    //获取授权信息
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("获取授权信息");
        return null;
    }
    //获取身份验证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行认证操作");
        //假设数据库用户名密码
        String username= "admin";
        String password ="123";
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
//        1、判断用户名
        if (!token.getUsername().equals(username)){
            //用户名不存在
            return null;    //shiro的底层会抛出UnknowAcountException
        }
        //2、判断密密码
        //返回new一个子类 第一参数是返回subject的一些消息可以为空，第二个参数是数据的用户的密码， 第三个是shiro的名字
        return new SimpleAuthenticationInfo("",password,"");
    }
}

```

