package net.engining.template.batch.xxljob.executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 微服务SpringBoot启动类-示例
 * @author 作者
 * @version 版本
 * @since
 * @date 2019/8/14 10:20
 */
@SpringBootApplication
public class XxlJobExecutorSvApplication extends SpringBootServletInitializer {
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(XxlJobExecutorSvApplication.class);
    }

    public static void main(String[] args) {
    	SpringApplication.run(XxlJobExecutorSvApplication.class, args);

    }
}
