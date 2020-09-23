package net.engining.sacl.online2;

import net.engining.sacl.online2.sao.Sao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.archaius.ArchaiusAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 微服务SpringBoot启动类;
 * 这里继承了SpringBootServletInitializer，是为了可以支持WAR包部署的方式；如果只考虑用jar方式启动内置web容器如：Tomcat，则不需要继承；
 * 由于要使用CommonsMultipartResolver代替spring boot默认的StandardServletMultipartResolver所以需要排除MultipartAutoConfiguration；
 *
 * @author luxue
 *
 */
@EnableDiscoveryClient
@EnableFeignClients(clients = {
        Sao.class
})
@EnableCircuitBreaker
@RemoteApplicationEventScan(basePackages = "net.engining.sacl.online2.bus")
@SpringBootApplication(exclude = {
        ArchaiusAutoConfiguration.class
})
public class OnlineApplication extends SpringBootServletInitializer{
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(OnlineApplication.class);
    }

    public static void main(String[] args) {
    	SpringApplication.run(OnlineApplication.class, args);
    }
}
