package net.engining.sacl.online2;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
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
@EntityScan(
		basePackages = {
				"net.engining.pg.parameter.entity",
		})
public class CombineConfiguration {

}
