package net.engining.sacl.online2;

import net.engining.gm.config.AsyncExtContextConfig;
import net.engining.gm.config.GeneralContextConfig;
import net.engining.gm.config.JPAContextConfig;
import net.engining.gm.config.SnowflakeSequenceIDContextConfig;
import net.engining.gm.config.Swagger2ContextConfig;
import net.engining.gm.config.WebContextConfig;
import net.engining.gm.config.WebMvcExtContextConfig;
import net.engining.gm.config.props.GmCommonProperties;
import net.engining.gm.config.security.ActuatorWebSecurityConfigurerAdapter;
import net.engining.pg.param.props.PgParamAndCacheProperties;
import net.engining.pg.props.CommonProperties;
import net.engining.pg.security.props.PgSecurityProperties;
import net.engining.sacl.config.PreferentiallyDependentContextConfig;
import net.engining.sacl.online2.config.ZeebeContextConfig;
import net.engining.smartstar.config.listener.AuthenticationSuccessEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.statemachine.data.jpa.JpaStateRepository;
import org.springframework.statemachine.data.redis.RedisStateRepository;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 这个类用来组装需要的配置，根据不同的项目组装需要的配置项 <br>
 * 使用@Import用来导入@Configuration注解的config类(也可以通过@SpringBootApplication指定scanBasePackages来扫描@Configuration) <br>
 * 使用@ImportResource用来加载传统的xml配置
 * 
 * @author Eric Lu
 *
 */
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(value = {
		CommonProperties.class,
		PgParamAndCacheProperties.class,
		GmCommonProperties.class,
		PgSecurityProperties.class
		})
@Import(value = {
		GeneralContextConfig.class,
		AsyncExtContextConfig.class,
		JPAContextConfig.class,
		Swagger2ContextConfig.class,
		WebContextConfig.class,
		WebMvcExtContextConfig.class,
		PreferentiallyDependentContextConfig.class,
		//微服务本地监控端点的安全控制
		ActuatorWebSecurityConfigurerAdapter.class,
		SnowflakeSequenceIDContextConfig.class,
		ZeebeContextConfig.class
		})
@EntityScan(
		basePackages = {
				"net.engining.pg.parameter.entity",
				"org.springframework.statemachine.data.jpa",
				"org.springframework.statemachine.data.redis",
		})
@EnableJpaRepositories(
		basePackageClasses = {
				JpaStateRepository.class,
		})
@EnableRedisRepositories(
		basePackageClasses = {
				RedisStateRepository.class,
		})
public class CombineConfiguration {
	/** logger */
	private static final Logger log = LoggerFactory.getLogger(CombineConfiguration.class);

//	@Bean
//	public Request.Options options(){
//		return new Request.Options(2000,1000);
//	}

	/**
	 * 全局统一的 Stream message Handler 异常处理
	 * @param message
	 */
	@StreamListener("errorChannel")
	public void error(Message<?> message) {
		ErrorMessage errorMessage = (ErrorMessage) message;
		log.warn("Handling ERROR: " + errorMessage.getPayload().getMessage());
	}

	@Bean
	public AuthenticationSuccessEventListener authenticationSuccessEventListener(){
		return new AuthenticationSuccessEventListener();
	}

}
