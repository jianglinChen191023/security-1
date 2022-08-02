package com.spring.security1;

import com.spring.security1.config.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    /**
     * 建立BCryptPasswordEncoder的bean
     * <p>
     * 获取方法:
     *
     * @return
     * @Autowired private PasswordEncoder passwordEncoder;
     * </p>
     */
    @Bean
    public PasswordEncoder create() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);

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
                .logout()
                .and()
                /* 403 页面 */
//                .exceptionHandling(handling -> handling
//                                .accessDeniedPage("/403"));
                .exceptionHandling()
                .accessDeniedHandler((httpServletRequest, httpServletResponse, e) -> {
//                            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                            httpServletRequest.setAttribute(WebAttributes.ACCESS_DENIED_403, e);
                    httpServletRequest.setAttribute("message", "抱歉! 您无法访问这个资源!");
                    httpServletRequest.getRequestDispatcher("/403").forward(httpServletRequest, httpServletResponse);
                })
                .and()
                /* 记住我 */
                .rememberMe(rememberMeConfigurer -> rememberMeConfigurer
                        .tokenRepository(jdbcTokenRepository));
    }

//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        User.UserBuilder users = User.withDefaultPasswordEncoder();
//        UserDetails user = users
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//        UserDetails admin = users
//                .username("admin")
//                .password("password")
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }

}
