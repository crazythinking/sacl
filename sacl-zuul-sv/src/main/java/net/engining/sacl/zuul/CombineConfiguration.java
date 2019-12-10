package net.engining.sacl.zuul;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import net.engining.gm.config.GeneralContextConfig;
import net.engining.gm.config.JPAContextConfig;
import net.engining.gm.config.SnowflakeSequenceIDContextConfig;
import net.engining.gm.config.Swagger2ContextConfig;
import net.engining.gm.config.ValidatorContextConfig;
import net.engining.gm.config.WebContextConfig;
import net.engining.gm.config.WebMvcExtContextConfig;
import net.engining.gm.config.props.GmCommonProperties;
import net.engining.pg.param.props.PgParamAndCacheProperties;
import net.engining.pg.props.CommonProperties;
import net.engining.sacl.zuul.config.OAuthSecurityExtContextConfig;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//打开swagger，访问/swagger-ui.html; 暂时加在顶层config上，解决swagger报错(Unable to infer base url)的坑，未找到正真原因，一度加在Swagger2ContextConfig是可行的，可能是与某个包冲突;
@EnableSwagger2
//显式的指定具体的Properties类，不通过扫描的方式，更清晰；
//另也不需要在@ConfigurationProperties注解的自定义Properties类上加@Component
@EnableConfigurationProperties(value = { 
		CommonProperties.class,
		PgParamAndCacheProperties.class,
		GmCommonProperties.class,
		})
//显式的指定具体的@Configuration类，不通过扫描的方式，更清晰；
@Import(value = {
		GeneralContextConfig.class,
		JPAContextConfig.class,
		ValidatorContextConfig.class,
		Swagger2ContextConfig.class,
		WebContextConfig.class,
		WebMvcExtContextConfig.class,
		OAuthSecurityExtContextConfig.class,
		SnowflakeSequenceIDContextConfig.class
		})
public class CombineConfiguration {
	
}

