package net.engining.sacl.online;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 微服务SpringBoot启动类;
 * 这里继承了SpringBootServletInitializer，是为了可以支持WAR包部署的方式；如果只考虑用jar方式启动内置web容器如：Tomcat，则不需要继承；
 * 由于要使用CommonsMultipartResolver代替spring boot默认的StandardServletMultipartResolver所以需要排除MultipartAutoConfiguration；
 * 
 * @author luxue
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class OnlineApplication extends SpringBootServletInitializer{
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(OnlineApplication.class);
    }

    public static void main(String[] args) {
    	SpringApplication.run(OnlineApplication.class, args);
    }
}
