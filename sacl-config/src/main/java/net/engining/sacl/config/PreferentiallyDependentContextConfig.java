package net.engining.sacl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 需要优先被依赖的Beans
 *
 * @author Eric Lu
 * @date 2019-12-05 11:51
 **/
@Configuration
public class PreferentiallyDependentContextConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
