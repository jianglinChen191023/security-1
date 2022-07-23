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
                // 对请求进行授权
                .authorizeRequests(authorize -> authorize
                        // “/hello/” URL 将被限制为具有“ROLE_USER”角色的用户。您会注意到，由于我们正在调用该hasRole方法，因此我们不需要指定“ROLE_”前缀。
                        .antMatchers("/hello").hasRole("USER")
                        .antMatchers("/hello_2").hasRole("ADMIN")
                        // 无条件访问 "/" "/home"
                        .antMatchers("/", "/home").permitAll()
                        // 其他请求需要登录以后才可以访问
                        .anyRequest().authenticated()
                )
                // 以表单的形式登录
                .formLogin(form -> form
                        // 指定登录页面 - 无条件访问
                        // /login           GET     跳转到登录页面
                        // /login           POST    提交登录表单
                        // /login?error     GET     登录失败
                        // /login?logout    GET     退出登录-注销功能
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/home")
                )
                // /logout          POST    302 重定向到-/login?logout 注销
                .logout();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        UserDetails user = users
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        UserDetails admin = users
                .username("admin")
                .password("password")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

}
