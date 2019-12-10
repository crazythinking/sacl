package net.engining.sacl.auth;

import net.engining.gm.config.GeneralContextConfig;
import net.engining.gm.config.JPAContextConfig;
import net.engining.gm.config.SnowflakeSequenceIDContextConfig;
import net.engining.gm.config.Swagger2ContextConfig;
import net.engining.gm.config.WebContextConfig;
import net.engining.gm.config.WebMvcExtContextConfig;
import net.engining.gm.config.props.GmCommonProperties;
import net.engining.pg.param.props.PgParamAndCacheProperties;
import net.engining.pg.props.CommonProperties;
import net.engining.sacl.auth.config.CommonTokenEnhancer;
import net.engining.sacl.auth.config.OauthServerExtContextConfig;
import net.engining.sacl.auth.config.SsoSecurityExtContextConfig;
import net.engining.sacl.auth.endpoint.CheckTokenEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 这个类用来组装需要的配置，根据不同的项目组装需要的配置项 <br>
 * 使用@Import用来导入@Configuration注解的config类(也可以通过@SpringBootApplication指定scanBasePackages来扫描@Configuration) <br>
 * 使用@ImportResource用来加载传统的xml配置
 *
 * @author Eric Lu
 */
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(value = {
        CommonProperties.class,
        PgParamAndCacheProperties.class,
        GmCommonProperties.class
})
@Import(value = {
        GeneralContextConfig.class,
        JPAContextConfig.class,
        Swagger2ContextConfig.class,
        WebContextConfig.class,
		WebMvcExtContextConfig.class,
        SnowflakeSequenceIDContextConfig.class,
		OauthServerExtContextConfig.class,
		SsoSecurityExtContextConfig.class
})
@ComponentScan(
        basePackages = {
				"net.engining.profile",
        })
@EntityScan(
		basePackages = {
				"net.engining.pg.parameter.entity",
				"net.engining.gm.entity",
				"net.engining.profile.entity",
		})
public class CombineConfiguration {

//	@Bean
//	public TokenStore jwtTokenStore() {
//		return new JwtTokenStore(jwtAccessTokenConverter());
//	}
//
//	/**
//	 * JwtAccessTokenConverter本身还是个TokenEnhancer
//	 *
//	 * @return
//	 */
//	@Bean
//	public JwtAccessTokenConverter jwtAccessTokenConverter() {
//		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
//		accessTokenConverter.setSigningKey("dev");
//		return accessTokenConverter;
//	}

	/**
	 * 覆盖原框架的checkTokenEndpoint
	 *
	 * @return
	 */
	@Bean
	@Primary
	public CheckTokenEndpoint checkTokenEndpoint() {
		return new CheckTokenEndpoint();
	}

	@Bean
	public CommonTokenEnhancer commonTokenEnhancer(){
		return new CommonTokenEnhancer();
	}

	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	@Bean
	public TokenStore redisTokenStore() {
		RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
		//TODO 默认是JdkSerialization，使用json的有坑，暂未解决
//		tokenStore.setSerializationStrategy(new JacksonJsonSerializationStrategy());

		return tokenStore;
	}

}
