package net.engining.sacl.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 * @author luxue
 *
 */
@Configuration
@EnableWebSecurity
public class Only4ActuatorWebSecurityExtContextConfig extends WebSecurityConfigurerAdapter{
	
	@Bean
    public static NoOpPasswordEncoder passwordEncoder() {
      return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.requestMatcher(EndpointRequest.toAnyEndpoint())
        	.authorizeRequests()
            .anyRequest().hasRole("ACTUATOR_MONITOR")
            .and()
            .httpBasic()
            .and()
            .csrf().disable()//针对shutdwon需要关闭跨站请求伪造保护功能，原因不明
            ;
    }
}
