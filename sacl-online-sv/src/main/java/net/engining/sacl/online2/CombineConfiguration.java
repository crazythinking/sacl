package net.engining.sacl.online2;

import net.engining.gm.config.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import net.engining.gm.config.props.GmCommonProperties;
import net.engining.pg.param.props.PgParamAndCacheProperties;
import net.engining.pg.props.CommonProperties;
import net.engining.sacl.config.Only4ActuatorWebSecurityExtContextConfig;
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
		})
@Import(value = {
		GeneralContextConfig.class,
		JPAContextConfig.class,
		Swagger2ContextConfig.class,
		WebContextConfig.class,
		WebMvcExtContextConfig.class,
		Only4ActuatorWebSecurityExtContextConfig.class,
		SnowflakeSequenceIDContextConfig.class
		})
public class CombineConfiguration {
	
	
}
