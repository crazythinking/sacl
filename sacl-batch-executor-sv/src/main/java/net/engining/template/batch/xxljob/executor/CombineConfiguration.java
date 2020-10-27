package net.engining.template.batch.xxljob.executor;

import net.engining.gm.config.GeneralContextConfig;
import net.engining.gm.config.JPAContextConfig;
import net.engining.gm.config.Swagger2ContextConfig;
import net.engining.gm.config.WebContextConfig;
import net.engining.gm.config.WebMvcExtContextConfig;
import net.engining.gm.config.XxlJobConfig;
import net.engining.gm.config.props.XxlJobProperties;
import net.engining.pg.props.CommonProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
/**
 * 示例
 * 这个类用来组装需要的配置，根据不同的项目组装需要的配置项 <br>
 * 使用@Import用来导入@Configuration注解的config类(也可以通过@SpringBootApplication指定scanBasePackages来扫描@Configuration) <br>
 * 使用@ImportResource用来加载传统的xml配置
 * @author 作者
 * @version 版本
 * @since
 * @date 2019/8/14 10:20
 */
@Configuration
@EnableConfigurationProperties(value = {
        CommonProperties.class,
        XxlJobProperties.class,
})
@Import(value = {
        GeneralContextConfig.class,
        JPAContextConfig.class,
        Swagger2ContextConfig.class,
        WebContextConfig.class,
        WebMvcExtContextConfig.class,
        XxlJobConfig.class,
})
public class CombineConfiguration {

}
