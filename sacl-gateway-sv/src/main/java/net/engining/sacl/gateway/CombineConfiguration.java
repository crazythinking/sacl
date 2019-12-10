package net.engining.sacl.gateway;

import net.engining.gm.config.GeneralContextConfig;
import net.engining.gm.config.JPAContextConfig;
import net.engining.gm.config.SnowflakeSequenceIDContextConfig;
import net.engining.gm.config.ValidatorContextConfig;
import net.engining.gm.config.props.GmCommonProperties;
import net.engining.pg.param.props.PgParamAndCacheProperties;
import net.engining.pg.props.CommonProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;

@Configuration
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
		SnowflakeSequenceIDContextConfig.class
		})
public class CombineConfiguration {
	
	@Bean
	public ServerCodecConfigurer serverCodecConfigurer() {
		return new DefaultServerCodecConfigurer();
	}
	
}

