package net.engining.sacl.zuul.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author luxue
 *
 */
@Profile("secure")
@Configuration
//标识该服务使用远端认证授权服务，替换应用自身的用户登录鉴权security逻辑，实现单点登录功能。 
//简要步骤：访问应用系统资源-> 
//				应用系统发现未登录-> 
//					302 跳转到登录页面（登录页面地址已经与获取token逻辑自动关联）-> 
//						应用系统发现符合获取token条件，根据授权类型拼装url->
//							302 跳转到认证授权地址（认证授权服务提供）进行认证、授权。
@EnableOAuth2Sso
public class OAuthSecurityExtContextConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
    	//声明网关层需要授权的url信息，这里标明"/login"不需要授权；
		// @formatter:off
		http
			.antMatcher("/admin/**")
			.authorizeRequests()
			.anyRequest().hasRole("ACTUATOR_MONITOR")
			.and().httpBasic()
			.and()
//			.authorizeRequests()
//			.antMatchers("/login")
//			.permitAll()
//			.anyRequest()
//			.authenticated()
//			.and()
			.csrf().disable()
			;
		// @formatter:on
    	
    }

//	@Override
//    public void configure(HttpSecurity http) throws Exception {
//		
//	    //针对所有EndpointRequest采用http basic方式进行安全控制，通常是ACTUATOR的管理监控类API; 
//		//指定ACTUATOR_MONITOR作为访问权限，注意注意spring security进行权限比较会默认加上前缀ROLE;
//	    http
//	    	.antMatcher("/**")
//	    	.authorizeRequests()
//	        .anyRequest().hasRole("ACTUATOR_MONITOR")
//	        .and()
//	        .httpBasic()
//	        .and()
//	        .csrf().disable()//针对shutdwon需要关闭跨站请求伪造保护功能，原因不明
//	        ;
//	    
//    }

}
