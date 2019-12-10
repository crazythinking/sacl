package net.engining.sacl.console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 微服务SpringBoot启动类;
 * 这里继承了SpringBootServletInitializer，是为了可以支持WAR包部署的方式；如果只考虑用jar方式启动内置web容器如：Tomcat，则不需要继承；
 * 
 * @author luxue
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class GrayConsoleApplication extends SpringBootServletInitializer{
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GrayConsoleApplication.class);
    }

    public static void main(String[] args) {
    	SpringApplication.run(GrayConsoleApplication.class, args);
    }
}
