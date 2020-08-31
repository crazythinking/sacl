package net.engining.sacl.online2;

import feign.Request;
import net.engining.gm.config.GeneralContextConfig;
import net.engining.gm.config.JPAContextConfig;
import net.engining.gm.config.SnowflakeSequenceIDContextConfig;
import net.engining.gm.config.Swagger2ContextConfig;
import net.engining.gm.config.WebContextConfig;
import net.engining.gm.config.WebMvcExtContextConfig;
import net.engining.gm.config.props.GmCommonProperties;
import net.engining.pg.param.props.PgParamAndCacheProperties;
import net.engining.pg.props.CommonProperties;
import net.engining.sacl.config.Only4ActuatorWebSecurityExtContextConfig;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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
		SnowflakeSequenceIDContextConfig.class,
		})
@EntityScan(
		basePackages = {
				"net.engining.pg.parameter.entity",
		})
public class CombineConfiguration {

//	@Bean
//	public Request.Options options(){
//		return new Request.Options(2000,1000);
//	}
}
