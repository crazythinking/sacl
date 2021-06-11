package net.engining.sacl.online2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 在 OAuth2 的概念里，所有的接口都被称为资源，接口的权限也就是资源的权限，
 * 所以 Spring Security OAuth2 中提供了关于资源的注解 @EnableResourceServer，和 @EnableWebSecurity的作用类似;
 *
 * @author Eric Lu
 * @create 2019-11-20 17:21
 **/
//@Configuration
//@EnableResourceServer
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OauthResourceServerExtContextConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    ResourceServerProperties resourceServerProperties;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //在security.oauth2.resource.id设置并不能直接影响这里，需要手动设置
        resources.resourceId(resourceServerProperties.getResourceId());

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                    .antMatchers("/learnNacos/**")
                        .authenticated()
        ;
    }
}
