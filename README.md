# security-1
security 入门 https://spring.io/guides/gs/securing-web/#initial

### 3.1 SrpingSecurity 入门

#### 3.1.1 下载需要的资源文件 `git clone https://github.com/spring-guides/gs-securing-web.git`

- 使用 Idea 克隆

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1658049953186-a08f66ac-8816-4e8f-bee1-13f41ddd976b.png)



#### 3.1.2 初始化一个项目

- Dependencies选择`Spring Web`和`Thymeleaf`

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1658050362305-5f09a126-dae6-4ff5-a3c2-56fc4afa7238.png)

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1658050399118-cc956163-6220-488f-8f58-eeb2cab760a3.png)



#### 3.1.3 视图

以下 Thymeleaf 模板中定义（来自src/main/resources/templates/home.html）：

- home.html

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="https://www.thymeleaf.org" xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
    <head>
        <title>Spring Security Example</title>
    </head>
    <body>
        <h1>Welcome!</h1>
        
        <p>Click <a th:href="@{/hello}">here</a> to see a greeting.</p>
    </body>
</html>
```

- hello.html

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="https://www.thymeleaf.org"
      xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
    <head>
        <title>Hello World!</title>
    </head>
    <body>
        <h1 th:inline="text">Hello [[${#httpServletRequest.remoteUser}]]!</h1>
        <form th:action="@{/logout}" method="post">
            <input type="submit" value="Sign Out"/>
        </form>
    </body>
</html>
```

- login.html

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="https://www.thymeleaf.org"
      xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
    <head>
        <title>Spring Security Example </title>
    </head>
    <body>
        <div th:if="${param.error}">
            Invalid username and password.
        </div>
        <div th:if="${param.logout}">
            You have been logged out.
        </div>
        <form th:action="@{/login}" method="post">
            <div><label> User Name : <input type="text" name="username"/> </label></div>
            <div><label> Password: <input type="password" name="password"/> </label></div>
            <div><input type="submit" value="Sign In"/></div>
        </form>
    </body>
</html>
```



#### 3.1.4 配置 SpringMVC 的类

```java
package com.spring.security1;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/").setViewName("home");
		registry.addViewController("/hello").setViewName("hello");
		registry.addViewController("/login").setViewName("login");
	}

}
```



#### 3.1.5 设置 Spring Security

1. **添加依赖**

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-test</artifactId>
  <scope>test</scope>
</dependency>
```

1. **以下安全配置（来自src/main/java/com/example/securingweb/WebSecurityConfig.java）确保只有经过身份验证的用户才能看到**

```java
package com.spring.security1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/", "/home").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
            .logout()
            .permitAll();
    }
    
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
            User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();
        
        return new InMemoryUserDetailsManager(user);
    }
}
```

1. **该类WebSecurityConfig被注释@EnableWebSecurity为启用 Spring Security 的 Web 安全支持并提供 Spring MVC 集成。它还扩展WebSecurityConfigurerAdapter和覆盖了它的几个方法来设置 Web 安全配置的一些细节。**
2. **该****configure(HttpSecurity)****方法定义了哪些 URL 路径应该被保护，哪些不应该。具体来说，****/****和****/home****路径被配置为不需要任何身份验证。所有其他路径都必须经过身份验证。**
3. **当用户成功登录时，他们将被重定向到先前请求的需要身份验证的页面。有一个自定义****/login****页面（由 指定****loginPage()****），每个人都可以查看。**
4. **该userDetailsService()方法使用单个用户设置内存中的用户存储。该用户的用户名是user，密码是password，角色是USER。**



#### 3.1.6 结果

1. **应用程序启动后，将浏览器指向**[http://localhost:8080](http://localhost:8080/)**. 您应该会看到主页，如下图所示：**

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1658052240955-07e2950c-cda1-452d-bf81-65dd1b292b5b.png)



1. **当您单击该链接时，它会尝试将您带到位于 的问候语页面/hello。但是，由于该页面是安全的并且您还没有登录，它会将您带到登录页面，如下图所示：**

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1658052295987-e1816846-b932-4e26-a362-e0c381669a2b.png)



1. **在登录页面，分别输入用户名和密码字段，以测试用户身份user登录password。提交登录表单后，您将通过身份验证，然后进入欢迎页面，如下图所示：**

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1658052325920-304a8c12-db2c-4025-8638-9aff09635a11.png)



1. **如果您单击**`**注销**`**按钮，您的身份验证将被撤销，您将返回登录页面，并显示一条消息，表明您已注销。**



#### 3.1.6 Push

![img](https://cdn.nlark.com/yuque/0/2022/png/12811585/1658052505446-b7d58192-3fc5-4e8a-acd2-5b6dc2e3a16b.png)
