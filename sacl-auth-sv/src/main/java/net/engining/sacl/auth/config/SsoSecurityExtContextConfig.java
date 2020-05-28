package net.engining.sacl.auth.config;

import net.engining.gm.config.props.GmCommonProperties;
import net.engining.profile.security.listener.ClearPasswordTriesListener;
import net.engining.profile.security.listener.PasswordTriesListener;
import net.engining.profile.security.service.ProfileUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 这个类的重点就是声明 PasswordEncoder 和 AuthenticationManager两个 Bean.
 * 其中 BCryptPasswordEncoder是一个密码加密工具类，它可以实现不可逆的加密;
 * AuthenticationManager是为了实现 OAuth2 的 password 模式必须要指定的授权管理 Bean.
 *
 * @author Eric Lu
 * @create 2019-11-19 16:00
 **/
@Configuration
@EnableWebSecurity(debug = true)
public class SsoSecurityExtContextConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    GmCommonProperties commonProperties;

    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ProfileUserDetailsServiceImpl profileUserDetailsService() {
        return new ProfileUserDetailsServiceImpl();
    }

    @Bean
    public PasswordTriesListener passwordTriesListener() {
        return new PasswordTriesListener();
    }

    @Bean
    public ClearPasswordTriesListener clearPasswordTriesListener() {
        return new ClearPasswordTriesListener();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 允许匿名访问所有接口 主要是 oauth 接口
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/oauth/**",
                        "/token/**"
                )
                .permitAll()
                .and()
                //关闭防跨域
                .csrf().disable()
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置不拦截规则
        web.ignoring().antMatchers(
                "/webjars/springfox-swagger-ui/**",
                // swagger api json
                "/v2/api-docs",
                // 用来获取api-docs的URI
                "/swagger-resources",
                // 用来获取支持的动作
                "/swagger-resources/configuration/ui/**",
                // 安全选项
                "/swagger-resources/configuration/security/**",
                "/swagger-ui.html",
                "/error/**",
                "/**/favicon.ico",
                //druid监控
                "/druid/**"
        );
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用自定义身份验证组件
        auth.userDetailsService(profileUserDetailsService()).passwordEncoder(passwordEncoder());
        auth.eraseCredentials(true);
    }

}
