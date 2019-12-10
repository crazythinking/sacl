package net.engining.sacl.online.config;

import net.engining.smartstar.config.support.JacksonJsonSerializationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 在 OAuth2 的概念里，所有的接口都被称为资源，接口的权限也就是资源的权限，
 * 所以 Spring Security OAuth2 中提供了关于资源的注解 @EnableResourceServer，和 @EnableWebSecurity的作用类似;
 *
 * @author Eric Lu
 * @create 2019-11-20 17:21
 **/
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OauthResourceServerExtContextConfig extends ResourceServerConfigurerAdapter {

//    @Value("${security.oauth2.client.client-id}")
//    private String clientId;

//    @Value("${security.oauth2.client.client-secret}")
//    private String secret;

//    @Value("${security.oauth2.authorization.check-token-access}")
//    private String checkTokenEndpointUrl;

//    @Value("${security.oauth2.resource.jwt.key-value}")
//    private String jwtKey;

//    @Autowired
//    private RedisConnectionFactory redisConnectionFactory;

//    @Bean
//    public TokenStore redisTokenStore (){
//        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
////        tokenStore.setSerializationStrategy(new JacksonJsonSerializationStrategy());
//
//        return tokenStore;
//    }

    //不需要，ResourceServerTokenServicesConfiguration已经包含此配置
//    @Bean
//    public RemoteTokenServices tokenService() {
//        RemoteTokenServices tokenService = new RemoteTokenServices();
//        tokenService.setClientId(clientId);
//        tokenService.setClientSecret(secret);
//        tokenService.setCheckTokenEndpointUrl(checkTokenEndpointUrl);
//        return tokenService;
//    }

//    @Bean
//    public TokenStore jwtTokenStore() {
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }

//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
//
//        accessTokenConverter.setSigningKey(jwtKey);
//        accessTokenConverter.setVerifierKey(jwtKey);
//        return accessTokenConverter;
//    }

//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        //jwt token store
////        resources.tokenStore(jwtTokenStore());
//        //redis token store
//        resources.tokenServices(tokenService());
//
//    }

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
