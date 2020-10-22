package net.engining.sacl.online;

import net.engining.gm.config.GeneralContextConfig;
import net.engining.gm.config.JPAContextConfig;
import net.engining.gm.config.SnowflakeSequenceIDContextConfig;
import net.engining.gm.config.Swagger2ContextConfig;
import net.engining.gm.config.SyncSchedulingContextConfig;
import net.engining.gm.config.WebContextConfig;
import net.engining.gm.config.WebMvcExtContextConfig;
import net.engining.gm.config.props.GmCommonProperties;
import net.engining.gm.config.security.ActuatorWebSecurityConfigurerAdapter;
import net.engining.pg.param.props.PgParamAndCacheProperties;
import net.engining.pg.props.CommonProperties;
import net.engining.pg.security.props.PgSecurityProperties;
import net.engining.sacl.config.PreferentiallyDependentContextConfig;
import net.engining.smartstar.config.listener.AuthenticationSuccessEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ErrorMessage;
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
		JPAContextConfig.class,
		Swagger2ContextConfig.class,
		WebContextConfig.class,
		WebMvcExtContextConfig.class,
		PreferentiallyDependentContextConfig.class,
		//微服务本地监控端点的安全控制
		ActuatorWebSecurityConfigurerAdapter.class,
		SnowflakeSequenceIDContextConfig.class,
		SyncSchedulingContextConfig.class
		})
public class CombineConfiguration {

	/** logger */
	private static final Logger log = LoggerFactory.getLogger(CombineConfiguration.class);

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
