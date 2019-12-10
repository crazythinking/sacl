package net.engining.sacl.console;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import net.engining.gm.config.GeneralContextConfig;
import net.engining.gm.config.JPAContextConfig;
import net.engining.gm.config.SnowflakeSequenceIDContextConfig;
import net.engining.gm.config.Swagger2ContextConfig;
import net.engining.gm.config.ValidatorContextConfig;
import net.engining.gm.config.WebMvcExtContextConfig;
import net.engining.gm.config.props.GmCommonProperties;
import net.engining.pg.param.props.PgParamAndCacheProperties;
import net.engining.pg.props.CommonProperties;
import net.engining.pg.web.filter.Log4jMappedDiagnosticContextFilter;
import net.engining.sacl.console.config.WebSecurityExtContextConfig;
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
		WebMvcExtContextConfig.class,
		WebSecurityExtContextConfig.class,
		SnowflakeSequenceIDContextConfig.class
		})
public class CombineConfiguration {
	
	/**
	 * 由于不需要上传文件，所以不能import WebContextConfig; 
	 * TODO WebContextConfig 增加配置项用于判断是否需要注入上传文件的Bean
	 * @return
	 */
	@Bean
	public Log4jMappedDiagnosticContextFilter log4jMappedDiagnosticContextFilter(){
		return new Log4jMappedDiagnosticContextFilter();
	}
}
