package net.engining.sacl.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author luxue
 *
 */
@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class FcGatewayApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(FcGatewayApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(FcGatewayApplication.class, args);
	}
}
