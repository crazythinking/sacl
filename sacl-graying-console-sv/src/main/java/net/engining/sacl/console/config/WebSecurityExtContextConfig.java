package net.engining.sacl.console.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * @author luxue
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityExtContextConfig extends WebSecurityConfigurerAdapter{
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

//	@Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//            .anyRequest()
//            .permitAll()
//            .and()
//            .cors().disable()
//            .csrf()
//            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//            .ignoringAntMatchers("/**/console", "/**/actuator/**");
//    }
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.requestMatcher(EndpointRequest.toAnyEndpoint())
        	.authorizeRequests()
            .anyRequest().hasRole("ACTUATOR_MONITOR")
            .and()
            .httpBasic()
            .and()
            .cors().disable()
            .csrf().disable()//针对shutdwon需要关闭跨站请求伪造保护功能，原因不明
            ;
    }
	
}
