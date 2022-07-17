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
                .authorizeRequests()
                // 无条件访问 "/" "/home"
                .antMatchers("/", "/home").permitAll()
                // 其他请求需要登录以后才可以访问
                .anyRequest().authenticated()
                .and()
                // 以表单的形式登录
                .formLogin()
                // 指定登录页面 - 无条件访问
                // /login           GET     跳转到登录页面
                // /login           POST    提交登录表单
                // /login?error     GET     登录失败
                // /login?logout    GET     退出登录-注销功能
                .loginPage("/login")
                .permitAll()
                .and()
                // /logout          POST    302 重定向到-/login?logout 注销
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
